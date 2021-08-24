package com.ww.limit.slider;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;

/**
 * @author ：黑洞里的光
 * @date ：Created in 2021/8/19 10:37
 * @description：窗口
 */
@Slf4j
@Data
public class Window {
    /**
     * 唯一标识符
     */
    private String name;

    /**
     * 滑动窗口
     */
    private LinkedList<Node> slots;

    /**
     * 时间间隔
     */
    private long intervalNanos;

    /**
     * 窗口大小
     */
    private long windowSize;

    /**
     * 流量限制
     */
    private long limit;


    /**
     * 节点
     */
    @Data
    class Node {
        /**
         * 起始时间
         */
        private long startTime;
        /**
         * 终止时间
         */
        private long endTime;
        /**
         * 时间段内计数
         */
        private long count;

    }

    /**
     * @param tokens
     * @return
     */
    public boolean tryAcquire(long tokens) {
        long now = System.nanoTime();
        //删除已经过时节点
        long earliestWindowsStartTime = now - intervalNanos * windowSize;
        while (!slots.isEmpty() && slots.getFirst().getEndTime() < earliestWindowsStartTime) {
            slots.removeFirst();
        }
        long count = 0;
        //当前窗口计数
        for (Node slot : slots) {
            count += slot.getCount();
        }
        //如果达到计数限制，返回false，表示获取失败
        if(count+tokens>limit){
            return false;
        }

        return false;
    }
}
