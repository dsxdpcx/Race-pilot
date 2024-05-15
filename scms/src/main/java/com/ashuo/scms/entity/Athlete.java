package com.ashuo.scms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Athlete implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "athlete_id", type = IdType.AUTO)
    private Integer athleteId;
    @TableField(exist = false)
    private User user;
    private Integer uId;

    private Integer iId;
    private String userIds;

    @TableField(exist = false)
    private Item item;

    private Integer scoreStatus;

    private Integer shenhe;

    private Integer groupId;

    private Integer track;

    @JsonIgnore
    @JsonFormat(pattern = "yyyy-MM-ddTHH:mm:ss", timezone = "GMT+8")
    private LocalDateTime signTime;
    @JsonIgnore
    @JsonFormat(pattern = "yyyy-MM-ddTHH:mm:ss", timezone = "GMT+8")
    private LocalDateTime shenheTime;
    @JsonIgnore
    @JsonFormat(pattern = "yyyy-MM-ddTHH:mm:ss", timezone = "GMT+8")
    private LocalDateTime editTime;

}
