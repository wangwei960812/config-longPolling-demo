package com.ww.juc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ：黑洞里的光
 * @date ：Created in 2021/8/2 16:08
 * @description：
 */
public class AtomicIntegerTest {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        AtomicInteger count = new AtomicInteger(0);
        for (int i = 0; i < 10000; i++) {
            executorService.submit(() -> {
                System.out.println(Thread.currentThread().getName() + "-count:" + count.get());
                count.incrementAndGet();
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(30, TimeUnit.SECONDS);
        System.out.println("count:" + count.get());
    }
}
