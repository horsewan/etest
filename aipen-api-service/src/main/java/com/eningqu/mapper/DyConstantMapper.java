package com.eningqu.mapper;

import com.eningqu.common.base.mapper.CrudDao;
import com.eningqu.domain.api.DyConstant;

import java.util.List;

public interface DyConstantMapper extends CrudDao<DyConstant> {


    DyConstant selectByPrimaryKey(Long id);

    List <DyConstant> selectByCode(String code);

    List<DyConstant> selectAll();

    void deleteByPrimaryKey(Long id);

    void insertDyConstant (DyConstant dyConstant);

    void updateByPrimaryKey (DyConstant dyConstant);


}
