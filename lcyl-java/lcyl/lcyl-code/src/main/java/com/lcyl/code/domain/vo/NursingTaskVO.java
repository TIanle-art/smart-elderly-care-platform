package com.lcyl.code.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName NursingTaskVO
 * @Description TODO
 * @Author hfy
 * @Date 2026-03-26 16:07
 * @Version 1.0
 */
@Data
public class NursingTaskVO {
    private Long id;
    private String elderName;      // 老人姓名
    private String bedNumber;      // 床位号（来自lc_elder）
    private String projectName;    // 护理项目
    private String itemType;       // 0计划内 1计划外
    private String applicantName;  // 创建人
    private String nurseName;       //护理员姓名
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expectedServiceTime; // 期望服务时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;       // 创建时间
    private String orderNo;        // 订单号
    private String executeStatus;  // 0待执行 1已执行 2已取消
}
