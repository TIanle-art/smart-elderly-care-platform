package com.lcyl.code.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;  // 改为 java.util.Date

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LcRetreatContract {
    private Long id;
    private Long retreatId;
    private String retreatNo;
    private String contractNo;
    private String contractUrl;
    private String contractName;
    private Date terminateDate;
    private Long createBy;
    private Date createTime;
    private Long updateBy;
    private Date updateTime;

    private String createName;    // 提交人姓名（非数据库字段）
}