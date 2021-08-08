package com.ww.juc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @author ：黑洞里的光
 * @date ：Created in 2021/8/2 16:52
 * @description：
 */
public class AtomicStampedReferenceTest {

    private final static String INIT_REF = "pool-1-thread-3";

    private final static AtomicStampedReference<String> asr = new AtomicStampedReference<>(INIT_REF, 0);

    public static void main(String[] args) throws InterruptedException {
        System.out.println("初始对象为：" + asr.getReference());
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 10; i++) {
            executorService.execute(new StampedReferenceThread());
        }
        executorService.shutdown();
        executorService.awaitTermination(3, TimeUnit.SECONDS);
    }

    static class StampedReferenceThread implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep((int) Math.abs((Math.random() * 100)));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //乐观锁？
            final int stamp = asr.getStamp();
            if (asr.compareAndSet(INIT_REF, Thread.currentThread().getName(), 0, stamp + 1)) {
                System.out.println(Thread.currentThread().getName() + " 修改了对象！");
                System.out.println("新的对象为：" + asr.getReference());
                System.out.println("after stamp " + asr.getStamp());
            }
        }
    }
}
