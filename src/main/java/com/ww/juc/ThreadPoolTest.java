package com.ww.juc;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.testng.annotations.Test;

import java.util.Random;
import java.util.concurrent.*;

/**
 * @author ：黑洞里的光
 * @date ：Created in 2021/8/6 10:49
 * @description：
 */

public class ThreadPoolTest {

    @Test
    public void test1() throws ExecutionException, InterruptedException {

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Future<Object> future = executorService.submit(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "123";
        });
        System.out.println(future.get());
    }

    @Test
    public void test2(){
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("XX-task-%d").build();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 0, 0, null, new DelayQueue(), threadFactory, new ThreadPoolExecutor.DiscardPolicy());
//        threadPoolExecutor.submit();
    }

    @Test
    public void test3() throws ExecutionException, InterruptedException {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);
        ScheduledFuture<?> future = scheduledExecutorService.scheduleAtFixedRate(() -> {
            System.out.println("周期性执行计划");
        }, 0,15, TimeUnit.SECONDS);
        get(future);
        scheduledExecutorService.shutdown();
    }

    void get(Future future) throws ExecutionException, InterruptedException {
        System.out.println("future");
        Object o = future.get();
        System.out.println(o);
        get(future);
    }

    class MyTask implements Callable,Delayed{
        @Override
        public int compareTo(Delayed o) {
            return 0;
        }

        @Override
        public Object call() throws Exception {
            return null;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return 0;
        }
    }

}
