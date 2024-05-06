package com.ashuo.scms.service.impl;

import com.ashuo.scms.common.consant.Consant;
import com.ashuo.scms.entity.Athlete;
import com.ashuo.scms.entity.Caipan;
import com.ashuo.scms.entity.Item;
import com.ashuo.scms.entity.Season;
import com.ashuo.scms.mapper.AthleteMapper;
import com.ashuo.scms.mapper.CaipanMapper;
import com.ashuo.scms.mapper.ItemMapper;
import com.ashuo.scms.service.CaipanService;
import com.ashuo.scms.service.SeasonService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CaipanServiceImpl extends ServiceImpl<CaipanMapper, Caipan> implements CaipanService {
    @Autowired
    CaipanMapper caipanMapper;

    @Autowired
    ItemMapper itemMapper;

    @Autowired
    SeasonService seasonService;




}
