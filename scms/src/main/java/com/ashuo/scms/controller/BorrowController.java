package com.ashuo.scms.controller;

import com.ashuo.scms.common.lang.ServerResponse;
import com.ashuo.scms.entity.Borrow;
import com.ashuo.scms.entity.Equipment;
import com.ashuo.scms.entity.Item;
import com.ashuo.scms.entity.Season;
import com.ashuo.scms.service.BorrowService;
import com.ashuo.scms.service.EquipmentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "器材借用接口")
@RestController
@Slf4j
@RequestMapping("/borrow")
public class BorrowController {

    @Autowired
    BorrowService borrowService;
    @Autowired
    EquipmentService equipmentService;


    @ApiOperation("归还器材")
    @PutMapping("/returnequipment")
//    @RequiresAuthentication
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse returnEquipment(@RequestBody Borrow borrow) {
        if (borrow == null || borrow.getBoId() == null) {
            return ServerResponse.createByErrorCodeMessage(400, "归还失败，项目信息为空");
        }
        if(borrow.getBoState()==1)
            return ServerResponse.createByErrorCodeMessage(400, "已归还，不能重复归还");
        borrow.setBoState(1);
        borrow.setBoEndtime(LocalDateTime.now());
        boolean effNum = false;
        try {
            effNum = borrowService.updateById(borrow);
        } catch (Exception e) {
            return ServerResponse.createByErrorCodeMessage(400, "修改失败");
        }
        if (effNum == false) {
            return ServerResponse.createByErrorCodeMessage(400, "修改失败");
        }
        return ServerResponse.createBySuccessMessage("修改成功");
    }

    @ApiOperation("查看借用记录")
    @GetMapping("/queryborrow")
//    @RequiresAuthentication
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse queryBorrow(Integer userId) {

        QueryWrapper<Borrow> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);

        List<Borrow> borrowList = borrowService.list(queryWrapper);
        return ServerResponse.createBySuccess(borrowList);
    }


    @ApiOperation("借出器材")
    @PostMapping("/borrow")
//    @RequiresAuthentication
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse insert(@RequestBody Borrow borrow) {
        borrow.setBoStarttime(LocalDateTime.now());
        borrow.setBoState(0);
        try {
            borrowService.save(borrow);
        } catch (Exception e) {
            return ServerResponse.createByErrorCodeMessage(400, "新增失败");
        }
        return ServerResponse.createBySuccessMessage("新增成功");
    }


    /**
     * 删除借还记录
     *
     * @param id
     * @return
     */
    @ApiOperation("删除借还记录")
    @DeleteMapping("/removeborrow")
    @RequiresAuthentication
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse<Object> deleteByIds(Integer id) {
        borrowService.removeById(id);
        return ServerResponse.createBySuccessMessage("删除成功");
    }
}
