package com.ashuo.scms.service.impl;

import com.ashuo.scms.entity.Borrow;
import com.ashuo.scms.mapper.BorrowMapper;
import com.ashuo.scms.service.BorrowService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BorrowServiceImpl extends ServiceImpl<BorrowMapper, Borrow> implements BorrowService {


    @Autowired
    BorrowMapper borrowMapper;

    @Override
    public List selectByUserId(Integer userId) {
        QueryWrapper<Borrow> wrapper=new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        List<Borrow> borrows=borrowMapper.selectList(wrapper);
        return borrows;
    }
}
