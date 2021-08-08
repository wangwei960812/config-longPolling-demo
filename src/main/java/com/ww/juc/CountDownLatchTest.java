package com.ww.juc;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ：黑洞里的光
 * @date ：Created in 2021/8/2 13:22
 * @description：
 */
public class CountDownLatchTest {
    public static void main(String[] args) {
        final CountDownLatch latch = new CountDownLatch(2);
        System.out.println("主线程开始执行");
        ExecutorService es1 = Executors.newSingleThreadExecutor();
        es1.execute(() -> {
            try {
                Thread.sleep(3000);
                System.out.println("子线程：" + Thread.currentThread().getName() + "执行");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            latch.countDown();
        });
        es1.shutdown();
        ExecutorService es2 = Executors.newSingleThreadExecutor();
        es2.execute(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("子线程：" + Thread.currentThread().getName() + "执行");
            latch.countDown();
        });
        es2.shutdown();
        System.out.println("等待两个线程执行完毕");
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("两个子线程都执行完毕，继续执行主线程");
    }
}
