package com.lcyl.code.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;  // 改为 java.util.Date

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LcRetreatHistory {
    private Long id;
    private Long retreatId;
    private Integer fromNode;
    private Integer toNode;
    private Integer action;
    private Long operatorId;
    private String operatorName;
    private String opinion;
    private String attachment;
    private Date createTime;
}