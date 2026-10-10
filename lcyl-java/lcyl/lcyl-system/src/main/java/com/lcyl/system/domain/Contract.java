package com.lcyl.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lcyl.common.annotation.Excel;
import com.lcyl.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 合同表对象 contract
 *
 * @author tyf
 * @date 2026-03-26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contract extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 合同名称 */
    @Excel(name = "合同名称")
    private String name;

    /** 合同编号 */
    @Excel(name = "合同编号")
    private String contractNo;

    /** 协议地址 */
    @Excel(name = "合同PDF地址")
    private String pdfUrl;

    /** 丙方手机号 */
    @Excel(name = "丙方手机号")
    private String memberPhone;

    /** 老人id */
    @Excel(name = "老人ID")
    private Long elderId;

    /** 老人姓名 */
    @Excel(name = "老人姓名")
    private String elderName;

    /** 丙方名称 */
    @Excel(name = "丙方名称")
    private String memberName;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "合同开始时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "合同结束时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /** 状态，0：未生效，1：已生效，2：已过期, 3：已失效 */
    @Excel(name = "合同状态")
    private Integer status;

    /** 排序字段 */
    private Integer sort;

    /** 级别描述 */
    @Excel(name = "级别描述")
    private String levelDesc;

    /** 入住单号 */
    @Excel(name = "入住单号")
    private String checkInNo;

    /** 签约日期 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "签约日期", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date signDate;

    /** 解除提交人 */
    private String releaseSubmitter;

    /** 解除日期 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date releaseDate;

    /** 解除协议url */
    private String releasePdfUrl;

    // 父类 BaseEntity 已包含 createTime、updateTime、remark、createBy、updateBy
}