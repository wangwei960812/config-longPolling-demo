package com.ww.juc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author ：黑洞里的光
 * @date ：Created in 2021/8/2 16:22
 * @description：
 */
public class AtomicReferenceTest {


    public static void main(String[] args) {
        SpinLock lock = new SpinLock();
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        TicketThread ticketThread = new TicketThread(lock);
        for (int i = 0; i < 1000; i++) {
            executorService.execute(ticketThread);
        }
        executorService.shutdown();

    }

    static class SpinLock {
        private AtomicReference<Thread> atomicReference = new AtomicReference<>();

        public void lock() {
            Thread current = Thread.currentThread();
            while (!atomicReference.compareAndSet(null, current)) {
            }
        }

        public void unlock() {
            Thread current = Thread.currentThread();
            atomicReference.compareAndSet(current, null);
        }
    }

    static class TicketThread implements Runnable {

        private static int TICKET = 1000;

        private SpinLock lock;

        public TicketThread(SpinLock spinLock) {
            this.lock = spinLock;
        }

        @Override
        public void run() {
            while (TICKET > 0) {
                lock.lock();
                if (TICKET > 0) {
                    System.out.println(Thread.currentThread().getName() + " 卖出了第 " + TICKET + " 张票");
                    TICKET--;
                }
                lock.unlock();
            }
        }
    }
}
