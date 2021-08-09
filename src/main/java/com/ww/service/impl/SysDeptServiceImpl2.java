package com.ww.service.impl;

import com.ww.db.entities.SysDept;
import com.ww.db.entities.SysDeptExample;
import com.ww.db.mapper.SysDeptMapper;
import com.ww.service.SysDeptService;
import com.ww.util.RedisCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service("sysDeptServiceImpl2")
public class SysDeptServiceImpl2 implements SysDeptService {

    @Resource
    private SysDeptMapper sysDeptMapper;

    @Autowired
    private RedisCacheUtil redisCacheUtil;

    @Value("${timeOut:30000}")
    private long timeOut;

    @Override
    public List<SysDept> selectByExample(SysDeptExample example) {
        return null;
    }

    @Override
    public SysDept selectByDeptId(Integer deptId) {
        final String key = "deptId:" + deptId;
        SysDept sysDept = (SysDept) redisCacheUtil.getValueOfObject(key);
        String keySign = key + "_sign";
        String valueSign = redisCacheUtil.getValue(keySign);
        //防止第一次查询的时候返回的为空
        if (sysDept == null) {
            //防止缓存穿透
            if (redisCacheUtil.exists(key)) {
                return null;
            }
            sysDept = sysDeptMapper.selectByPrimaryKey(deptId);
            redisCacheUtil.set(key, sysDept);
            redisCacheUtil.set(keySign, "1", timeOut * (new Random().nextInt(10) + 1));
            return sysDept;
        }
        if (valueSign != null) {
            return sysDept;
        } else {
            //设置标记的失效时间
            Long expireTime = timeOut * (new Random().nextInt(10) + 1);
            log.info("expireTime:{}", expireTime);
            redisCacheUtil.set(keySign, "1", expireTime);
            //异步处理缓存更新，应对高并发情况，会产生脏读的情况
            Executors.newScheduledThreadPool(1).schedule(() -> {
                log.info("异步执行操作");
                SysDept sysDept1 = sysDeptMapper.selectByPrimaryKey(deptId);
                redisCacheUtil.set(key, sysDept1);

            }, 1, TimeUnit.SECONDS);
        }
        return sysDept;
    }
}
