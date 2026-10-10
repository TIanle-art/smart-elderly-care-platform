package com.lcyl.code.vo;

import lombok.Data;

/**
 * 下拉展示老人姓名
 */
@Data
public class ElderOptionVo {

    /** 老人ID */
    private Long elderId;

    /** 老人姓名 */
    private String elderName;
}