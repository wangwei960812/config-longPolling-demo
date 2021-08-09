package com.ww.service;

import com.ww.db.entities.SysDept;
import com.ww.db.entities.SysDeptExample;

import java.util.List;

public interface SysDeptService {

    List<SysDept> selectByExample(SysDeptExample example);

    SysDept selectByDeptId(Integer deptId);
}
