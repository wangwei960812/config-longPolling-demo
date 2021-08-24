package com.ww.juc;

import lombok.extern.slf4j.Slf4j;

import java.util.PriorityQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author ：黑洞里的光
 * @date ：Created in 2021/8/11 14:15
 * @description：
 */
public class ConditionTest2 {

}

/**
 * 资源类
 */
@Slf4j
class Share {
    private volatile boolean flag = true;
    private final Lock lock1 = new ReentrantLock();
    private final Condition queue1Full = lock1.newCondition();
    private final Lock lock2 = new ReentrantLock();
    private final Condition queue2Full = lock2.newCondition();
    private PriorityQueue<Integer> queue1 = new PriorityQueue<>();
    private PriorityQueue<Integer> queue2 = new PriorityQueue<>();
    private Integer capacity1;
    private Integer capacity2;


    public Share(int capacity1, int capacity2) {
        this.capacity1 = capacity1;
        this.capacity2 = capacity2;
    }

    public int queue1Size() {
        return queue1.size();
    }

    public int queue2Size() {
        return queue2.size();
    }

    public void produceQueue1() {
        while (flag) {
            lock1.lock();
            try {
                while (queue1.size() < capacity1) {
                    //向队列中插入一个元素
                    queue1.offer(1);
                    log.info("{}向队列1中插入一个元素，队列剩余空间：{}", Thread.currentThread().getName(), capacity1 - queue1.size());
                }

                while (queue1.size() == capacity1) {
                    try {
                        log.info("队列1已经满了，等待空闲时间");
                        queue1Full.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        flag = false;
                    }
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } finally {
                lock1.unlock();
            }
        }
    }

}
