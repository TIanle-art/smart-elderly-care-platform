package com.lcyl.system.domain;

import com.lcyl.common.annotation.Excel;
import com.lcyl.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 房位房型对象 lc_floor
 * 
 * @author ruoyi
 * @date 2026-03-21
 */
@Data
public class Floor extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 楼层名称 */
    @Excel(name = "楼层名称")
    private String name;

    /** 楼层编号 */
    private String code;



}
