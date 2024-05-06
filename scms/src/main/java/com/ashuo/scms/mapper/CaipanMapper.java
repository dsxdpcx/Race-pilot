package com.ashuo.scms.mapper;
import com.ashuo.scms.entity.Caipan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public interface CaipanMapper extends BaseMapper<Caipan> {

    int insertCaipan(Caipan caipan);

    //按条件查询Caipan列表
    IPage<Caipan> queryCaipanByCaipanCondition(Page<Caipan> page, Caipan caipan);

    int updateCaipan(Caipan caipan);

    int deleteCaipan(int caipanId);
}
