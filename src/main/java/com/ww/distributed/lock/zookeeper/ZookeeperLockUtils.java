package com.ww.distributed.lock.zookeeper;

import com.ww.distributed.lock.DealBusiness;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author ：黑洞里的光
 * @date ：Created in 2021/7/20 15:13
 * @description：
 */
@Slf4j
@Component
public class ZookeeperLockUtils {

    @Autowired(required = false)
    private CuratorFramework client;

    public Object dealWithLock(String lockKey, DealBusiness dealBusiness, Object... params){
        Object resullt = null;
        InterProcessMutex mutex = new InterProcessMutex(client, "/curator/lock/"+lockKey);
        try{
            //加锁，同步阻塞
            log.info("加锁");
            mutex.acquire();
            log.info("业务处理");
            resullt =  dealBusiness.deal(params);
        }catch (Exception e){
            log.info("{}",e);
        }finally {
            //解锁
            try {
                log.info("解锁");
                mutex.release();
            } catch (Exception e) {
                log.info("解锁失败");
                e.printStackTrace();
            }
        }
        return resullt;
    }


}
