package com.lcyl.code.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class Elderr {
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 图片
     */
    private String image;

    /**
     * 身份证号
     */
    private String idCardNo;

    /**
     * 年龄
     */
    private String age;

    /**
     * 性别
     */
    private String sex;
    /**
     * 状态（0：禁用，1:启用  2:请假 3:退住中 4入住中 5已退住）
     */
    private Integer status;

    /**
     * 手机号
     */
    private String phone;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建人id
     */
    private Long createBy;

    /**
     * 更新人id
     */
    private Long updateBy;

    /**
     * 备注
     */
    private String remark;

    /**
     * 床位编号
     */
    private String bedNumber;
    /**
     * 床位id
     */
    private Long bedId;

}
