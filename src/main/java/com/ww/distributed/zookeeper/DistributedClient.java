package com.ww.distributed.zookeeper;

import com.ww.configuration.ZooKeeperProperties;
import com.ww.util.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author ：黑洞里的光
 * @date ：Created in 2021/7/29 16:58
 * @description：服务端
 */
@Slf4j
public class DistributedClient {

    private ZooKeeper client;

    /**
     * 配置
     */
    private ZooKeeperProperties zooKeeperProperties;

    /**
     * 服务列表
     */
    private volatile List<String> serverList;

    private volatile List<ZkNodeData> nodeDatas;

    class ZkNodeData {
        private String node;
        private String nodePath;
        private byte[] data;

        public String getNode() {
            return node;
        }

        public void setNode(String node) {
            this.node = node;
        }

        public String getNodePath() {
            return nodePath;
        }

        public void setNodePath(String nodePath) {
            this.nodePath = nodePath;
        }

        public byte[] getData() {
            return data;
        }

        public void setData(byte[] data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "ZkNodeData{" +
                    "node='" + node + '\'' +
                    ", nodePath='" + nodePath + '\'' +
                    ", data=" + Arrays.toString(data) +
                    '}';
        }
    }

    public DistributedClient(ZooKeeperProperties zooKeeperProperties) {
        this.zooKeeperProperties = zooKeeperProperties;
    }

    /**
     * 创建到zk的客户端连接
     *
     * @throws Exception
     */
    public void connect() throws Exception {
        client = new ZooKeeper(zooKeeperProperties.getConnectString(), zooKeeperProperties.getSessionTimeout(), new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                // 收到事件通知后的回调函数（应该是我们自己的事件处理逻辑）
                try {
                    //重新更新服务器列表，并且注册了监听
                    getServerList();
                } catch (Exception e) {
                    e.printStackTrace();
                    if(e instanceof KeeperException.SessionExpiredException){
                        try {
                            connect();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    /**
     * 获取服务器信息列表
     *
     * @throws Exception
     */
    public void getServerList() throws Exception {

        // 获取服务器子节点信息，并且对父节点进行监听
        List<String> children = client.getChildren(zooKeeperProperties.getParentNode(), true);

        // 先创建一个局部的list来存服务器信息
        List<String> servers = new ArrayList<>();
        List<ZkNodeData> zkNodeDatas = new ArrayList<>();
        for (String child : children) {
            // child只是子节点的节点名
            String path = zooKeeperProperties.getParentNode() + "/" + child;
            byte[] data = client.getData(path, false, null);
            servers.add(new String(data));
            //
            ZkNodeData zkNodeData = new ZkNodeData();
            zkNodeData.setNodePath(path);
            zkNodeData.setData(data);
            zkNodeData.setNode(child);
            zkNodeDatas.add(zkNodeData);
        }
        // 把servers赋值给成员变量serverList，已提供给各业务线程使用
        serverList = servers;
        nodeDatas = zkNodeDatas;
        //打印服务器列表
        log.info("serverList:{}", serverList);
        log.info("nodeDatas:{}", nodeDatas);
    }

    /**
     * 向zk集群注册服务器信息
     *
     * @throws Exception
     */
    public void registerServer() throws Exception {
        String create = client.create(zooKeeperProperties.getParentNode() + "/" + IpUtil.getLocalIP(), IpUtil.getLocalIP().getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        log.info("{} is online..,{}", IpUtil.getLocalIP(), create);
    }

}
