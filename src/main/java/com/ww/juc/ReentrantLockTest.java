package com.ww.juc;

import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author ：黑洞里的光
 * @date ：Created in 2021/8/17 16:10
 * @description：
 */
@Slf4j
public class ReentrantLockTest {

    private List<Thread> threads;

    @BeforeTest
    public void init(){
        threads = new ArrayList<>();
    }

    @Test
    public void test1() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ReentrantLock lock = new ReentrantLock();
        Thread thread1 = new Thread(new MyRunnable(lock));
        threads.add(thread1);
        Thread thread2 = new Thread(new MyRunnable(lock));
        threads.add(thread2);
        thread1.start();
        thread2.start();
        Thread.sleep(10);
        for (int i = 0; i < 3; i++) {
            threads.forEach(thread->log.info("{} status:{}",thread.getName(),thread.getState()));
        }
        countDownLatch.await();
    }

    @Test
    public void test2() throws InterruptedException {
        log.info("time:{}",System.currentTimeMillis());
        LockSupport.parkNanos(1000000000);
        log.info("time:{}",System.currentTimeMillis());
        Thread.sleep(100000);
        log.info("time:{}",System.currentTimeMillis());
    }

    @Test
    public void test(){
        log.info("time:{}",System.currentTimeMillis());
        Thread main = Thread.currentThread();
        new Thread(()->{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LockSupport.unpark(main);
        }).start();
        LockSupport.park(main);
        log.info("time:{}",System.currentTimeMillis());
    }

}

@Slf4j
class MyRunnable implements Runnable{

    private Lock lock;

    public MyRunnable(Lock lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        log.info("{} status:{}",Thread.currentThread().getName(),Thread.currentThread().getState());
        try {
            lock.lock();
            log.info("加锁成功");
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
            log.info("解锁成功");
        }
    }
}
