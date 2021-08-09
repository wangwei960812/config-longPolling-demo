package com.ww.juc;

import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @author ：黑洞里的光
 * @date ：Created in 2021/8/9 9:53
 * @description：栅栏测试
 */
@Slf4j
public class BarrierTest {

    private CyclicBarrier barrier;

    @Test
    public void test1() throws InterruptedException {
        int num = 3;
        barrier = new CyclicBarrier(num, () -> {
            log.info("阶段工作已经全部完成，发送完成通知");
        });
        Thread worker1 = new Thread(new Worker(barrier, "工作人员1"));
        Thread worker2 = new Thread(new Worker(barrier, "工作人员2"));
        Thread worker3 = new Thread(new Worker(barrier, "工作人员3"));
        worker1.start();
        worker2.start();
        worker3.start();
        Thread.sleep(10000);
        if (barrier.isBroken()) {
            barrier.reset();
        }
    }

    class Worker implements Runnable {

        private CyclicBarrier barrier;
        private String name;

        public Worker(CyclicBarrier barrier, String name) {
            this.barrier = barrier;
            this.name = name;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("{}完成阶段一任务", name);
            try {
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            log.info("阶段一任务已经都完成，{}开始阶段二任务", name);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("{}完成阶段二任务", name);
            try {
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }
}
