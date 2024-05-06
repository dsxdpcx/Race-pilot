package com.ashuo.scms.service;

import com.ashuo.scms.entity.Athlete;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author AShuo
 * @since 2021-04-05
 */
public interface AthleteService extends IService<Athlete> {

    IPage<Athlete> getAthleteByCondition(Page<Athlete> page, Athlete athlete);
//    IPage<Athlete> getAthleteByItem(Page<Athlete> page, Athlete athlete);

    int addAthlete(Athlete athlete);

    int modifyAthlete(Athlete athlete);
    int modifyShenhe(Athlete athlete);

    int modifyGroup(Athlete athlete);

    int removeAthlete(int athleteId);


}