package com.lcyl.code.vo;

import lombok.Data;

/**
 * 填写请假单页面老人基本信息
 */
@Data
public class ElderLeaveFormVo {

    /** 老人ID */
    private Long elderId;

    /** 老人姓名 */
    private String elderName;

    /** 老人身份证号 */
    private String elderIdCard;

    /** 联系方式 */
    private String phone;

    /** 护理等级 */
    private String nursingLevel;

    /** 入住床位 */
    private String bedNo;

    /** 护理员 */
    private String nurseNames;
}