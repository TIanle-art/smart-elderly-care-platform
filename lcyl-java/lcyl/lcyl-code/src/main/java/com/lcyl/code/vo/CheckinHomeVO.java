package com.lcyl.code.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class CheckinHomeVO {
    private Long applyId;          // 申请ID
    private String applyNo;        // 单据编号（RZ开头）
    private String elderlyName;    // 老人姓名
    private String idCard;         // 老人身份证号
    private String bedNo;          // 入住床位
    private Date checkinTime;      // 入住日期
    private String createBy;       // 创建人
    private Date createTime;       // 创建时间

    // 详情页扩展字段
    private String phone;          // 老人电话
    private String gender;         // 性别
    private Integer age;           // 年龄
    private BigDecimal careFee;    // 护理费
    private BigDecimal roomFee;    // 床位费
    private BigDecimal totalFee;   // 总费用
    private String contractNo;     // 合同编号
    private Date signTime;         // 签约时间
    private String payStatus;
}
