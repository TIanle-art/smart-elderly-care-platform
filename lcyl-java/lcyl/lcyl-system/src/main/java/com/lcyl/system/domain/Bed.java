package com.lcyl.system.domain;

import com.lcyl.common.annotation.Excel;
import com.lcyl.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 床位实体
 *
 * @author tyf
 * @date 2026-03-26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bed extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 床位ID */
    private Long id;

    /** 床位编号 */
    @Excel(name = "床位编号")
    private String bedNumber;

    private String name;

    /**
     * 床位状态
     * 0：未入住
     * 1：已入住
     * 2：入住申请中
     */
    @Excel(name = "床位状态")
    private Integer bedStatus;

    /** 床位号（排序） */
    @Excel(name = "床位号")
    private Integer sort;

    /** 房间ID */
    @Excel(name = "房间ID")
    private Long roomId;

    /** 备注 */
    @Excel(name = "备注")
    private String remark;

    private String createBy;

    private String code;//所属房间


}