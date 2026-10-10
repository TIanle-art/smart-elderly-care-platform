package com.lcyl.code.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 在住管理-请假管理-'请假管理页面'视图对象
 */
@Data
public class ElderLeaveVo {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 单据编号
     */
    private String orderNo;

    /**
     * 老人姓名（冗余）
     */
    private String elderName;

    /**
     * 老人身份证号
     */
    private String elderIdCard;

    /**
     * 请假开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date leaveStartTime;

    /**
     * 预计返回时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date plannedReturnTime;

    /**
     * 实际返回时间（冗余字段，便于列表查询）
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date actualReturnTime;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 是否已返回：0否，1是
     */
    private Integer isReturned;

    /**
     * 业务状态
     */
    private String status;

    /**
     * 真实的请假状态
     */
    private String realStatus;
}
