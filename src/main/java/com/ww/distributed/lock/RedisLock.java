package com.ww.distributed.lock;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.*;

/**
 * @author ：黑洞里的光
 * @date ：Created in 2021/7/19 15:34
 * @description：redis分布式锁
 */
@Slf4j
@Component
public class RedisLock {

    private StringRedisTemplate redisTemplate;

    private ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("XX-task-%d").build();

    private ScheduledExecutorService timeoutChecker = new ScheduledThreadPoolExecutor(1, threadFactory);

    private HashMap<String, Future> futures = new HashMap<>();

    static ThreadLocal<String> localThreadId = new ThreadLocal<>();

    @Value("${redis.lock.timeout:30000}")
    private long timeOut;

    @Value("${redis.lock.refreshTime:5000}")
    private long refreshTime;

    @Autowired
    public RedisLock(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    /**
     * 加锁
     *
     * @param lockKey 锁名
     * @return
     */
    public boolean lockWithTimeOut(String lockKey) {
        //加锁线程唯一标识符
        String threadId = UUID.randomUUID().toString().concat(String.valueOf(System.currentTimeMillis()));
        localThreadId.set(threadId);
        try {
            Boolean success = redisTemplate.opsForValue().setIfAbsent(lockKey, threadId, timeOut, TimeUnit.MILLISECONDS);
            if (!success) {
                log.info("线程：{} 对锁：{} 加锁失败", threadId, lockKey);
                return false;
            } else {
                //加锁成功后设置新建一个线程
                ScheduledFuture<?> schedule = timeoutChecker.scheduleWithFixedDelay(() -> {
                    if (threadId.equals(redisTemplate.opsForValue().get(lockKey))) {
                        Long expire = redisTemplate.getExpire(lockKey, TimeUnit.MILLISECONDS);
                        log.info("当前时间：{} 当前线程：{}还在持有锁：{}，锁的过期时间还有：{}ms，增加锁的过期时间", System.currentTimeMillis(), threadId, lockKey, expire);
                        expire = expire == null ? refreshTime : refreshTime + expire;
                        redisTemplate.expire(lockKey, expire, TimeUnit.MILLISECONDS);
                    } else {
                        log.info("当前线程：{}不再持有锁：{}，取消维活线程", threadId, lockKey);
                        Future future = futures.get(threadId);
                        future.cancel(true);
                    }
                }, 0, refreshTime, TimeUnit.MILLISECONDS);
                futures.put(threadId, schedule);
                log.info("线程：{} 对锁：{} 加锁成功", threadId, lockKey);
                return true;
            }
        } catch (Exception e) {
            log.info("线程：{} 对锁：{} 加锁异常：{}");
            return false;
        }
    }

    /**
     * 解锁
     *
     * @param lockKey
     * @return
     */
    public boolean unLockWithTimeOut(String lockKey) {
        String threadId = localThreadId.get();
        if (threadId.equals(redisTemplate.opsForValue().get(lockKey))) {
            Boolean success = redisTemplate.delete(lockKey);
            log.info("当前时间：{} 线程：{} 删除锁：{}", System.currentTimeMillis(), threadId, lockKey);
            return success;
        } else {
            log.info("线程：{} 没有持有锁：{}，无法删除", threadId, lockKey);
            return false;
        }
    }

    /**
     * 判断是否结束
     *
     * @return
     */
    public boolean finished() {
        for (Map.Entry<String, Future> stringFutureEntry : futures.entrySet()) {
            if (!stringFutureEntry.getValue().isCancelled()) {
                return false;
            }
        }
        return true;
    }

}
