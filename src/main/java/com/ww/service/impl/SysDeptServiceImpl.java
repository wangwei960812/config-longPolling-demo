package com.ww.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ww.db.entities.SysDept;
import com.ww.db.entities.SysDeptExample;
import com.ww.db.mapper.SysDeptMapper;
import com.ww.service.SysDeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
@CacheConfig(cacheNames = "SysDept")
public class SysDeptServiceImpl implements SysDeptService {

    @Resource
    private SysDeptMapper sysDeptMapper;

    @Override
    public List<SysDept> selectByExample(SysDeptExample example) {
        return sysDeptMapper.selectByExample(example);
    }

    @Cacheable(key = "caches[0].name+T(String).valueOf(#deptId)",unless = "#result eq null")
    @Override
    public SysDept selectByDeptId(Integer deptId) {
        SysDept sysDept = sysDeptMapper.selectByPrimaryKey(deptId);
        log.info("selectByDeptId：{} result:{}",deptId, JSONObject.toJSONString(sysDept));
        return sysDept;
    }
}
