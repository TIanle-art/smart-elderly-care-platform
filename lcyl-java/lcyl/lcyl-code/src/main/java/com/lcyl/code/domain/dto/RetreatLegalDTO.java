package com.lcyl.code.domain.dto;

import lombok.Data;

/**
 * 法务上传解除合同参数
 */
@Data
public class RetreatLegalDTO {
    private Long retreatId;
    private String contractNo; //合同编号
    private String contractUrl;   // 合同文件URL
    private String contractName;  // 合同名称
    private String terminateDate; // 解除日期（字符串，前端传入格式如 yyyy-MM-dd）
}
