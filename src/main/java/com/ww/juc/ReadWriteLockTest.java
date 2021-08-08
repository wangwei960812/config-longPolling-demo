package com.ww.juc;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author ：黑洞里的光
 * @date ：Created in 2021/8/4 17:15
 * @description：读写锁测试
 */
public class ReadWriteLockTest {
    public static void main(String[] args) throws InterruptedException {
        final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        Thread thread_read = new Thread(() -> {
            lock.writeLock().lock();
            System.out.println("Thread read");
            lock.writeLock().unlock();
        });

        lock.writeLock().lock();
        lock.writeLock().lock();
        thread_read.start();
        Thread.sleep(1000);
        System.out.println("realse one once");
        lock.writeLock().unlock();
        lock.writeLock().unlock();
    }
}
