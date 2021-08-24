package com.ww.juc;

import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author ：黑洞里的光
 * @date ：Created in 2021/8/10 19:07
 * @description：
 */
@Slf4j
public class TicketTest {

    class Ticket {

        private Integer count;

        public Integer getCount() {
            return count;
        }

        public Ticket(Integer count) {
            this.count = count;
            log.info("一共有{}张票", count);
        }

        private synchronized void saleTicket() throws InterruptedException {
            if (count > 0) {
                log.info("{}卖出一张票", Thread.currentThread().getName());
                count--;
                log.info("还剩下{}张票", count);
                Thread.sleep(1);
            }
        }
    }

    class LTicket {

        private Integer count;
        private final Lock lock = new ReentrantLock();

        public LTicket(Integer count) {
            this.count = count;
        }

        public Integer getCount() {
            return count;
        }

        public void saleTicket() {
            lock.lock();
            try {
                if (count > 0) {
                    log.info("{}卖出一张票", Thread.currentThread().getName());
                    count--;
                    log.info("还剩下{}张票", count);
                    Thread.sleep(2);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        }
    }

    @Test
    public void test1() throws InterruptedException {
        final Ticket ticket = new Ticket(30);
        Runnable sale = new Runnable() {
            @Override
            public void run() {
                while (ticket.getCount() > 0) {
                    try {
                        ticket.saleTicket();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        };
        //卖票测试
        Thread tSale1 = new Thread(sale);
        Thread tSale2 = new Thread(sale);
        Thread tSale3 = new Thread(sale);

        tSale1.start();
        tSale2.start();
        tSale3.start();
        Thread.sleep(10000);
    }

    @Test
    public void test2() throws InterruptedException {
        final LTicket ticket = new LTicket(100);
        Thread thread1 = new Thread(() -> {
            while (ticket.getCount() > 0) {
                ticket.saleTicket();
            }
        });
        Thread thread2 = new Thread(() -> {
            while (ticket.getCount() > 0) {
                ticket.saleTicket();
            }
        });
        Thread thread3 = new Thread(() -> {
            while (ticket.getCount() > 0) {
                ticket.saleTicket();
            }
        });
        thread1.start();
        thread2.start();
        thread3.start();
        Thread.sleep(10000);
    }
}
