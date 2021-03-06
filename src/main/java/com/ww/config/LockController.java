package com.ww.config;

import com.ww.distributed.lock.DealBusiness;
import com.ww.distributed.lock.redis.RedisLockUtils;
import com.ww.distributed.lock.zookeeper.ZookeeperLockUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：黑洞里的光
 * @date ：Created in 2021/7/20 10:57
 * @description：
 */
@Slf4j
@RestController
public class LockController {

    @Autowired
    private RedisLockUtils redisLockUtils;

    @Autowired
    private ZookeeperLockUtils zookeeperLockUtils;

    @RequestMapping("/redis/lock/deal")
    public Object redisLock(String lockKey) {
        return redisLockUtils.dealWithLock(lockKey, new DealBusiness() {
            @Override
            public Object deal(Object... params) {
                log.info("加锁业务测试");
                try {
                    Thread.sleep(8*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "success";
            }
        });
    }

    @RequestMapping("/zookeeper/lock/deal")
    public Object zookeeperLock(String lockKey) {
        return zookeeperLockUtils.dealWithLock(lockKey, new DealBusiness() {
            @Override
            public Object deal(Object... params) {
                log.info("加锁业务测试");
                try {
                    Thread.sleep(8*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "success";
            }
        });
    }
}
