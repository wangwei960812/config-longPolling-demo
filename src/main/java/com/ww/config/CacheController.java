package com.ww.config;

import com.ww.db.entities.SysDept;
import com.ww.service.SysDeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
public class CacheController {

    @Resource(name = "sysDeptServiceImpl")
    private SysDeptService sysDeptService;

    @Resource(name = "sysDeptServiceImpl2")
    private SysDeptService sysDeptServiceImpl2;

    @RequestMapping("/query/sysDept/{deptId}")
    public SysDept selectByDeptId(@PathVariable Integer deptId) {
        log.info("请求参数deptId：{}", deptId);
        return sysDeptService.selectByDeptId(deptId);
    }

    @RequestMapping("/query/sysDept/2/{deptId}")
    public SysDept selectByDeptId2(@PathVariable Integer deptId) {
        log.info("请求参数deptId：{}", deptId);
        return sysDeptServiceImpl2.selectByDeptId(deptId);
    }
}
