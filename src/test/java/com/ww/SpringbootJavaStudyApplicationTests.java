package com.ww;

import com.ww.distributed.lock.RedisLock;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.concurrent.CountDownLatch;

@Slf4j
@SpringBootTest
@WebAppConfiguration
class SpringbootJavaStudyApplicationTests {

    @Autowired
    private RedisLock redisLock;

    @Test
    void contextLoads() {
    }

    @Test
    void redisLockTest() throws Exception{
        Thread t1 = new Thread(() -> {
            boolean success = redisLock.lockWithTimeOut("test");
            log.info("加锁结果：{}", success);
        });

        Thread t2 = new Thread(() -> {
            boolean success = redisLock.lockWithTimeOut("test");
            log.info("加锁结果：{}", success);
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        while (!redisLock.finished()){
            Thread.sleep(1000);
        }
    }

}
