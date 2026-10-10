package com.lcyl.code.domain.dto;

import lombok.Data;

@Data
public class MonthBillDto {
    private String billMonth;
    private Long elderId;
    private Integer createBy;
    private Double depositAmount;
}
