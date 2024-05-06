package com.ashuo.scms.controller;

import cn.hutool.crypto.SecureUtil;
import com.ashuo.scms.common.consant.Consant;
import com.ashuo.scms.common.lang.ServerResponse;
import com.ashuo.scms.entity.*;
import com.ashuo.scms.service.BorrowService;
import com.ashuo.scms.service.EquipmentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
@Api(tags = "器材管理接口")
@RestController
@Slf4j
@RequestMapping("/equipment")
public class EquipmentController {
    @Autowired
    EquipmentService equipmentService;
    @Autowired
    BorrowService borrowService;


    @ApiOperation("添加器材")
    @PostMapping("/addequipment")
//    @RequiresAuthentication
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse addEquipment(@RequestBody Equipment equipment) {

        if (equipment == null) {
            return ServerResponse.createByErrorCodeMessage(400, "添加失败，项目信息为空");
        }
        //通过seasonId、parentId和性别判断是否已存在相同Item
        Equipment tempEquipment = new Equipment();
        tempEquipment.setEqId(equipment.getEqId());
        tempEquipment.setEqName(equipment.getEqName());
        tempEquipment.setEqModel(equipment.getEqModel());
        tempEquipment.setEqPlace(equipment.getEqPlace());
        tempEquipment.setEqSum(equipment.getEqSum());
        tempEquipment.setEqBuytime(LocalDateTime.now());

        boolean effNum = false;
        try {
            effNum = equipmentService.save(equipment);
        } catch (Exception e) {
            return ServerResponse.createByErrorCodeMessage(400, "添加失败");
        }
        if (effNum == false) {
            return ServerResponse.createByErrorCodeMessage(400, "添加失败");
        }
        return ServerResponse.createBySuccessMessage("添加成功");
    }


    @ApiOperation("查询所有器材")
    @GetMapping("/queryequipment")
//    @RequiresAuthentication
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse<List<Equipment>> findAll() {
        List<Equipment> equipmentList = equipmentService.list();
        return ServerResponse.createBySuccess(equipmentList);

    }

    @ApiOperation("查询所有器材")
    @GetMapping("/queryone")
//    @RequiresAuthentication
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse<Equipment> findOne(Integer id) {
        Equipment equipment = equipmentService.getById(id);
        return ServerResponse.createBySuccess(equipment);

    }


    /**
     * 器材借还记录
     *
     * @param eId
     * @return
     */
    @ApiOperation("查询借还记录")
    @GetMapping("/queryborrow")
    @RequiresAuthentication
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse<List<Borrow>> findDetailsByeId(Integer eId) {
        QueryWrapper<Borrow> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("e_id", eId);
        List<Borrow> borrowList = borrowService.list(queryWrapper);
        return ServerResponse.createBySuccess(borrowList);
    }



    /**
     * 器材信息修改
     *
     * @param equipment
     * @return
     */
    @ApiOperation("修改器材信息")
    @PutMapping("/updateequipment")
//    @RequiresAuthentication
    @Transactional(rollbackFor = Exception.class)
    public String update(@RequestBody Equipment equipment) {
        System.out.println(equipment);
        try {
            equipmentService.updateById(equipment);
        } catch (Exception e) {
            return "修改失败!";
        }
        return "200";
    }



    /**
     * 删除器材
     *
     * @param eqId
     * @return
     */
    @ApiOperation("删除器材")
    @DeleteMapping("/removeequipment")
//    @RequiresAuthentication
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse<Object> deleteByIds(Integer eqId) {
        equipmentService.removeById(eqId);
        return ServerResponse.createBySuccessMessage("删除成功");
    }



}
