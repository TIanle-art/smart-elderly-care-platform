package com.lcyl.system.domain.dto;

import com.lcyl.system.domain.CheckInConfig;
import com.lcyl.system.domain.Elder;
import com.lcyl.system.domain.NursingLevel;
import lombok.Data;

import java.util.Date;

/**
 * 床位扁平化展示DTO
 *
 * @author tyf
 * @date 2026-03-26
 */
@Data
public class FlatBedDTO {

    /** 楼层ID */
    private Long floorId;
    /** 楼层名称 */
    private String floorName;

    /** 房间ID */
    private Long roomId;
    /** 房间编号 */
    private String roomCode;
    /** 房型名称 */
    private String roomTypeName;
    /** 房型照片 */
    private String photo;
    /** 床位ID */
    private Long bedId;
    /** 床位编号 */
    private String bedNumber;
    /** 床位状态 0-未入住 1-已入住 2-入住申请中 */
    private Integer bedStatus;
    /** 床位排序号 */
    private Integer sort;

    /** 老人信息 */
    private String elderName;
    private Long elderId;
    private Elder elder;


    /** 备注 */
    private String remark;
    /** 护理等级ID */
    private Long nursingLevelId;
    /** 护理等级名称 */
    private String nursingLevelName;
    private NursingLevel nursingLevel;
    private CheckInConfig checkInConfig;
    private Date startTime;//开始时间
    private Date endTime;//结束时间

}