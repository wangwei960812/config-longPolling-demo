package com.ww.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

/**
 * @author ：黑洞里的光
 * @date ：Created in 2021/7/29 17:17
 * @description：ip工具类
 */
public class IpUtil {

    private static final String[] LOCAL_HSOT = {"127.0.0.1","0.0.0.0"};

    public static List<String> getLocalIPList() {
        List<String> ipList = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            NetworkInterface networkInterface;
            Enumeration<InetAddress> inetAddresses;
            InetAddress inetAddress;
            String ip;
            while (networkInterfaces.hasMoreElements()) {
                networkInterface = networkInterfaces.nextElement();
                inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    inetAddress = inetAddresses.nextElement();
                    if (inetAddress instanceof Inet4Address) { // IPV4
                        ip = inetAddress.getHostAddress();
                        ipList.add(ip);
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return ipList;
    }

    public static String getLocalIP(){
        List<String> localIPList = getLocalIPList();
        localIPList.removeIf(ip->Arrays.asList(LOCAL_HSOT).contains(ip));
        return localIPList.get(0);
    }

    public static void main(String[] args) {
        System.out.println(IpUtil.getLocalIP());
    }
}
