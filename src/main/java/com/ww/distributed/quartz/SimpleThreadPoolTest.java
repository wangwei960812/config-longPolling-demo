package com.ww.distributed.quartz;

import org.quartz.SchedulerConfigException;
import org.quartz.simpl.SimpleThreadPool;

import java.util.concurrent.CountDownLatch;

/**
 * @author ：黑洞里的光
 * @date ：Created in 2021/8/2 9:49
 * @description：quartz简单线程池测试
 */
public class SimpleThreadPoolTest {

    public static void main(String[] args) throws InterruptedException, SchedulerConfigException {
        final CountDownLatch countDownLatch = new CountDownLatch(10);
        SimpleThreadPool simpleThreadPool = new SimpleThreadPool(3, Thread.NORM_PRIORITY);
        simpleThreadPool.initialize();
        for (int i = 0; i < 12; i++) {
            simpleThreadPool.runInThread(() -> {
                System.out.println(Thread.currentThread().getName()+">>>>>>>>>blockForAvailableThreads>>>>>>>>>>>>>>>"+simpleThreadPool.blockForAvailableThreads());
                countDownLatch.countDown();
                System.out.println(Thread.currentThread().getName()+">>>>>>>>>print>>>>>>>>>"+countDownLatch.getCount());
            });
            System.out.println("blockForAvailableThreads>>>>>end>>>>>>>"+simpleThreadPool.blockForAvailableThreads());
        }
        simpleThreadPool.shutdown();
        countDownLatch.await();
        System.out.println("等待线程执行完毕");
    }
}
