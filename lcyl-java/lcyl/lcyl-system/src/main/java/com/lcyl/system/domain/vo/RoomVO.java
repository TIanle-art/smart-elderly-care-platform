package com.lcyl.system.domain.vo;

import com.lcyl.system.domain.LcRoomType;
import lombok.Data;

@Data
public class RoomVO {
    // 房间基础字段（复用LcRoom的字段）
    private Long id;
    private String code;
    private Integer sort;
    private Long floorId;
    private Long roomTypeId;
    private String createBy;
    private String updateBy;
    private String remark;
    private String createTime;
    private String updateTime;
    private Double price;
    // 关联房型信息
    private LcRoomType roomType;
    
    // 床位统计
    private Integer bedTotal;  // 总床位
    private Integer bedUsed;   // 已使用床位
}