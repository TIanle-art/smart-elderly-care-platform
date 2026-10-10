package com.lcyl.system.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 老人信息表
 *
 * @author tyf
 * @date 2026-03-26
 */
@Data
public class Elder {

    /** 老人ID */
    private Long id;

    /** 老人姓名 */
    private String name;

    /** 老人照片 */
    private String image;

    /** 身份证号 */
    private String idCardNo;

    /** 欠费金额（元）【数据库age字段错误映射】 */
    private BigDecimal age;

    /** 性别 */
    private String sex;

    /** 状态（0：禁用，1:启用 2:请假 3:退住中 4:入住中 5:已退住） */
    private Integer status;

    /** 手机号 */
    private String phone;

    /** 创建时间 */
    private Date createTime;

    /** 更新时间 */
    private Date updateTime;

    /** 创建人ID */
    private Long createBy;

    /** 更新人ID */
    private Long updateBy;

    /** 备注 */
    private String remark;

    /** 床位编号 */
    private String bedNumber;

    /** 床位ID */
    private Long bedId;

}