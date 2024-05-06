package com.ashuo.scms.service;

import com.ashuo.scms.entity.Borrow;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface BorrowService extends IService<Borrow> {
//    public boolean returnEquipment(Borrow borrow) throws Exception;

    List selectByUserId(Integer userId);
}
