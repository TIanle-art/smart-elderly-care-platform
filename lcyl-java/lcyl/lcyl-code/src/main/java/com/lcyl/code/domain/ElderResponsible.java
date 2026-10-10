package com.lcyl.code.domain;

import com.lcyl.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.List;

/**
 * @ClassName ElderResponsible
 * @Description TODO
 * @Author hfy
 * @Date 2026-03-25 10:44
 * @Version 1.0
 */
@Data
public class ElderResponsible extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long elderId;//老人id
    private String elderName;//老人姓名
    private Long floorId;//楼层id
    private String floorName;//楼层名称
    private Long roomId;        // 房间ID
    private String roomCode;    // 房间号
    private Long bedId;         // 床位ID
    private String bedNumber;   // 床位号
    private String status;      // 老人状态
    private List<Long> nurseIds;// 护理员IDs
    private String nurseNames;
    private List<String> nurseNameList;
}
