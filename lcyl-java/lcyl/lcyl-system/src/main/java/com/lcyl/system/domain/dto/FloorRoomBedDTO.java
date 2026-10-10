package com.lcyl.system.domain.dto;

import com.lcyl.system.domain.Elder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data // 自动生成所有 getter/setter
public class FloorRoomBedDTO {
    // 楼层字段
    private Long floorId;
    private String floorName;

    // 房间字段（和 SQL 别名对应）
    private Long id;          // 房间ID
    private String code;      // 房间号
    private String roomTypeName; // 房型名称

    // 床位字段（和 SQL 别名对应）
    private Long bedId;
    private String bedNumber;
    private Integer bedStatus;
    private String lname;     // 护理员姓名
    private String name;      // 老人姓名
    private Long elderId;


    // 嵌套结构
    private List<RoomDTO> rooms;  // 一个楼层多个房间


    @Data
    public static class RoomDTO {
        private Long id;
        private String code;
        private String roomTypeName;
        private List<BedDTO> beds; // 一个房间多个床位

        @Data
        public static class BedDTO {
            private Long bedId;
            private String bedNumber;
            private Integer bedStatus;
            private String lname;
            private String name;
            private Long elderId;
            private Elder elder;
            private String remark;
            private String nursingLevelName;
            private Date startTime;//开始时间
            private Date endTime;//结束时间
            private String photo;
        }
    }
}