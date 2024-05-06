package com.ashuo.scms.controller;

import com.ashuo.scms.common.lang.ServerResponse;
import com.ashuo.scms.entity.Athlete;
import com.ashuo.scms.entity.Item;
import com.ashuo.scms.service.AthleteService;
import com.ashuo.scms.service.ItemService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Api(tags = "分组分道接口")
@RestController
@Slf4j
@RequestMapping("/group")
public class fenzufendaoController {


    @Autowired
    ItemService itemService;
    @Autowired
    AthleteService athleteService;

    @ApiOperation("分组分道")
    @PutMapping("/editGroup")
//    @RequiresRoles(value = {"1"})
    public ServerResponse editItem() {

        List<Item> itemList = itemService.list();

        for (Item item : itemList) {
            int amount = item.getGroupAmount();
            QueryWrapper<Athlete> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("i_id", item.getItemId());
            queryWrapper.eq("shenhe",1);
            List<Athlete> athleteList = athleteService.list(queryWrapper);
            Collections.shuffle(athleteList);
            for (int i = 0; i < athleteList.size(); i++) {
                Athlete athlete = athleteList.get(i);
                    int groupId = i / amount + 1; // 计算分组号
                    int track = i % amount + 1; // 计算道号
                    athlete.setGroupId(groupId); // 设置分组号
                    athlete.setTrack(track); // 设置道号
                    athlete.setEditTime(LocalDateTime.now());
                    athleteService.updateById(athlete);

            }
        }
        return ServerResponse.createBySuccessMessage("分组成功");
    }


}
