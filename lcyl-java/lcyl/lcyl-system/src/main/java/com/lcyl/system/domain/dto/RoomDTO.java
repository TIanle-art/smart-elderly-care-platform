package com.lcyl.system.domain.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class RoomDTO {

    // 房间ID
    private Long id;

    // 房间编号
    private String code;

    // 所属楼层ID
    private Long floorId;

    // 房间价格
    private BigDecimal price;
    // 房型名称（四人间 / 双人间 / 单间）
    private String roomTypeName;
    private Long roomTypeId;
    // 该房间下的所有床位
    private List<BedDTO> bedList;
    private Date updateTime;
    private Date createTime;
}