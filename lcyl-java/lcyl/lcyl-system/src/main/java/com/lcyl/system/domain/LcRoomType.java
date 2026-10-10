package com.lcyl.system.domain;

import com.lcyl.common.annotation.Excel;
import com.lcyl.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 房型设置对象 lc_room_type
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LcRoomType extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 房型ID */
    private Long id;

    /** 房型照片 */
    @Excel(name = "房型照片")
    private String photo;

    /** 房型名称 */
    @Excel(name = "房型名称")
    private String name;

    /** 床位数量 */
    private Integer bedCount;

    /** 房型价格 */
    @Excel(name = "房型价格")
    private BigDecimal price;

    /** 房型介绍 */
    @Excel(name = "房型介绍")
    private String introduction;

    /** 房型状态 */
    @Excel(name = "房型状态")
    private int status;


}
