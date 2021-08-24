package com.ww.juc;

import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

import java.util.PriorityQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class ConditionTest {

    private Lock lock = new ReentrantLock();

    @Test
    public void test1() throws InterruptedException {
        Condition condition = lock.newCondition();
        Thread cosumer = new Thread(new Consumer(lock, condition));
        Thread producer = new Thread(new Producer(lock, condition));
        cosumer.start();
        producer.start();
        Thread.sleep(2000);
    }

    @Test
    public void test2() throws InterruptedException {
        int queueSize = 10;
        PriorityQueue<Integer> queue = new PriorityQueue<>(queueSize);
        Condition notFull = lock.newCondition();
        Condition notEmpty = lock.newCondition();
        Thread consumer1 = new Thread(new Consumer2(lock, notFull, notEmpty, queue));
        Thread consumer2 = new Thread(new Consumer2(lock, notFull, notEmpty, queue));
        Thread producer2 = new Thread(new Producer2(lock, notFull, notEmpty, queue));
        consumer1.start();
        consumer2.start();
        producer2.start();
        Thread.sleep(10000);
        consumer1.interrupt();
        consumer2.interrupt();
        producer2.interrupt();
    }


    class Consumer implements Runnable {

        private Lock lock;
        private Condition condition;

        public Consumer(Lock lock, Condition condition) {
            this.lock = lock;
            this.condition = condition;
        }

        @Override
        public void run() {

            try {
                lock.lock();
                log.info("我在等一个信号：{}", Thread.currentThread().getName());
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                log.info("拿到一个信号：{}", Thread.currentThread().getName());
                lock.unlock();
            }
        }
    }

    class Producer implements Runnable {

        private Lock lock;
        private Condition condition;

        public Producer(Lock lock, Condition condition) {
            this.lock = lock;
            this.condition = condition;
        }

        @Override
        public void run() {
            try {
                lock.lock();
                log.info("我拿到锁：{}", Thread.currentThread().getName());
                condition.signalAll();
                log.info("我发出一个信号：{}", Thread.currentThread().getName());
            } finally {
                lock.unlock();
            }

        }
    }

    class Consumer2 implements Runnable {
        private volatile boolean flag = true;
        private Lock lock;
        private Condition notFull;
        private Condition notEmpty;
        private PriorityQueue<Integer> queue;

        public Consumer2(Lock lock, Condition notFull, Condition notEmpty, PriorityQueue<Integer> queue) {
            this.lock = lock;
            this.notFull = notFull;
            this.notEmpty = notEmpty;
            this.queue = queue;
        }

        @Override
        public void run() {
            while (flag) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                lock.lock();
                while (queue.isEmpty()) {
                    try {
                        log.info("队列空，等待数据");
                        notEmpty.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        flag = false;
                    }
                }
                //每次取走队首的一个元素
                queue.poll();
                //通知生产者进行生产
                notFull.signalAll();
                log.info("{}从队列中取走一个元素，队列剩余{}个元素", Thread.currentThread().getName(), queue.size());
                while(!queue.isEmpty()){
                    //每次取走队首的一个元素
                    queue.poll();
                    log.info("{}从队列中取走一个元素，队列剩余{}个元素", Thread.currentThread().getName(), queue.size());
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    flag = false;
                }
                lock.unlock();
            }
        }
    }

    class Producer2 implements Runnable {

        private int queueSize = 10;
        private volatile boolean flag = true;
        private Lock lock;
        private Condition notFull;
        private Condition notEmpty;
        private PriorityQueue<Integer> queue;

        public Producer2(Lock lock, Condition notFull, Condition notEmpty, PriorityQueue<Integer> queue) {
            this.lock = lock;
            this.notFull = notFull;
            this.notEmpty = notEmpty;
            this.queue = queue;
        }

        @Override
        public void run() {
            while (flag) {
                lock.lock();
                try {
//                    while (queue.size() == queueSize) {
//                        try {
//                            log.info("队列已经满了，等待空闲时间");
//                            notFull.await();
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                            flag = false;
//                        }
//                    }
//                    //向队列中插入一个元素
//                    queue.offer(1);
//                    notEmpty.signalAll();
//                    log.info("{}向队列中插入一个元素，队列剩余空间：{}", Thread.currentThread().getName(), queue.size());
                    while (queue.size() < queueSize) {
                        //向队列中插入一个元素
                        queue.offer(1);
                        log.info("{}向队列中插入一个元素，队列剩余空间：{}", Thread.currentThread().getName(), queue.size());
                    }
                    while (queue.size() == queueSize) {
                        try {
                            log.info("队列已经满了，等待空闲时间");
                            notFull.await();
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
                    lock.unlock();
                }
            }

        }
    }

}
