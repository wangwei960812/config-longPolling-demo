package com.ww.distributed.lock.redis;

import com.ww.distributed.lock.DealBusiness;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author ：黑洞里的光
 * @date ：Created in 2021/7/20 11:04
 * @description：
 */
@Slf4j
@Component
public class RedisLockUtils {

    private RedisLock redisLock;

    @Autowired
    public RedisLockUtils(RedisLock redisLock) {
        this.redisLock = redisLock;
    }

    public Object dealWithLock(String lockKey, DealBusiness dealBusiness, Object... params){
        Object resullt = null;
        try{
            //加锁
            if(redisLock.lockWithTimeOut(lockKey)){
                log.info("业务处理");
                resullt =  dealBusiness.deal(params);
            }
        }catch (Exception e){
            log.info("{}",e);
        }finally {
            //解锁
            redisLock.unLockWithTimeOut(lockKey);
        }
        return resullt;
    }
}
