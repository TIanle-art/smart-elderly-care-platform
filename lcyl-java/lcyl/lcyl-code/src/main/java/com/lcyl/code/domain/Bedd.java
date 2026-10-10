

package com.lcyl.code.domain;

import com.lcyl.common.annotation.Excel;
import com.lcyl.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 床位对象 bed
 *
 * @author your_name
 * @date 2026-01-30
 */
public class Bedd extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 床位ID */
    private Long id;

    /** 床位编号 */
    @Excel(name = "床位编号")
    private String bedNumber;

    /** 排序号 */
    @Excel(name = "排序号")
    private Integer sort;

    /** 房间ID */
    @Excel(name = "房间ID")
    private Long roomId;

    /** 床位状态：0=未入住 1=已入住 */
    private Integer status;

    @Excel(name = "房间编号")
    private String roomCode;

    @Excel(name = "房间类型名称")
    private String roomTypeName;

    // --- 新增字段：关联老人信息 ---
    private String elderName;
    private String elderSex;
    private String elderAge;
    private String elderPhone;
    // --- 新增字段：关联老人状态 ---
    private Integer elderStatus;
    // --- 结束新增 ---
    
    /** 床位状态 (0:空闲, 1:占用) */
    private Integer bedStatus;

    // --- 新增字段：关联入住配置信息 ---
    private Long nursingLevelId;
    private Date checkInStartTime;
    private Date checkInEndTime;
    // --- 结束新增 ---

    /** 查询参数：按床位状态筛选，0=未入住（空闲） 1=已入住 */
    private Integer statusQuery;
    /** 查询参数：按楼层筛选（room.floor_id） */
    private Long floorId;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setBedNumber(String bedNumber) {
        this.bedNumber = bedNumber;
    }

    public String getBedNumber() {
        return bedNumber;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getSort() {
        return sort;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomTypeName(String roomTypeName) {
        this.roomTypeName = roomTypeName;
    }

    public String getRoomTypeName() {
        return roomTypeName;
    }

    // --- Getter 和 Setter：关联老人信息 ---
    public String getElderName() {
        return elderName;
    }

    public void setElderName(String elderName) {
        this.elderName = elderName;
    }

    public String getElderSex() {
        return elderSex;
    }

    public void setElderSex(String elderSex) {
        this.elderSex = elderSex;
    }

    public String getElderAge() {
        return elderAge;
    }

    public void setElderAge(String elderAge) {
        this.elderAge = elderAge;
    }

    public String getElderPhone() {
        return elderPhone;
    }

    public void setElderPhone(String elderPhone) {
        this.elderPhone = elderPhone;
    }
    // --- 结束 Getter 和 Setter ---

    // --- Getter 和 Setter：关联老人状态 ---
    public Integer getElderStatus() {
        return elderStatus;
    }

    public void setElderStatus(Integer elderStatus) {
        this.elderStatus = elderStatus;
    }
    // --- 结束 Getter 和 Setter ---
    
    // --- Getter 和 Setter：床位状态 ---
    public Integer getBedStatus() {
        return bedStatus;
    }

    public void setBedStatus(Integer bedStatus) {
        this.bedStatus = bedStatus;
    }
    // --- 结束 Getter 和 Setter ---

    // --- Getter 和 Setter：关联入住配置信息 ---
    public Long getNursingLevelId() {
        return nursingLevelId;
    }

    public void setNursingLevelId(Long nursingLevelId) {
        this.nursingLevelId = nursingLevelId;
    }

    public Date getCheckInStartTime() {
        return checkInStartTime;
    }

    public void setCheckInStartTime(Date checkInStartTime) {
        this.checkInStartTime = checkInStartTime;
    }

    public Date getCheckInEndTime() {
        return checkInEndTime;
    }

    public void setCheckInEndTime(Date checkInEndTime) {
        this.checkInEndTime = checkInEndTime;
    }
    // --- 结束 Getter 和 Setter ---

    public Integer getStatusQuery() {
        return statusQuery;
    }

    public void setStatusQuery(Integer statusQuery) {
        this.statusQuery = statusQuery;
    }

    public Long getFloorId() {
        return floorId;
    }

    public void setFloorId(Long floorId) {
        this.floorId = floorId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("bedNumber", getBedNumber())
                .append("sort", getSort())
                .append("roomId", getRoomId())
                .append("roomCode", getRoomCode())
                .append("roomTypeName", getRoomTypeName())
                // --- 添加关联信息 ---
                .append("elderName", getElderName())
                .append("elderSex", getElderSex())
                .append("elderAge", getElderAge())
                .append("elderPhone", getElderPhone())
                .append("elderStatus", getElderStatus()) // 添加这一行
                .append("bedStatus", getBedStatus()) // 添加床位状态
                .append("nursingLevelId", getNursingLevelId())
                .append("checkInStartTime", getCheckInStartTime())
                .append("checkInEndTime", getCheckInEndTime())
                // --- 结束添加 ---
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .append("createBy", getCreateBy())
                .append("updateBy", getUpdateBy())
                .append("remark", getRemark())
                .toString();
    }
}