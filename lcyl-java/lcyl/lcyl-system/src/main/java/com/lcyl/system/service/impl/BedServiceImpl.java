package com.lcyl.system.service.impl;

import com.lcyl.common.exception.ServiceException;
import com.lcyl.system.domain.Bed;
import com.lcyl.system.domain.Elder;
import com.lcyl.system.domain.LcRoomType;
import com.lcyl.system.domain.Room;
import com.lcyl.system.domain.dto.FlatBedDTO;
import com.lcyl.system.domain.dto.FloorRoomBedDTO;
import com.lcyl.system.mapper.BedMapper;
import com.lcyl.system.mapper.ElderMapper;
import com.lcyl.system.mapper.LcRoomMapper;
import com.lcyl.system.mapper.LcRoomTypeMapper;
import com.lcyl.system.service.BedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BedServiceImpl implements BedService {

    @Autowired
    private BedMapper bedMapper;

    @Autowired
    private LcRoomTypeMapper roomTypeMapper;

    @Autowired
    private LcRoomMapper roomMapper;

    @Autowired
    private ElderMapper elderMapper;

    /**
     * 根据楼层ID获取 楼层+房间+床位 树形结构
     */
    @Override
    public List<FloorRoomBedDTO> getFloorRoomBedByFloorId(Long floorId) {
        // 1. 查询数据库：多表联查 → 返回扁平化数据
        List<FlatBedDTO> flatList = bedMapper.selectFloorRoomBedByFloorId(floorId,null);
        // 2. 空值判断：没有数据直接返回空集合
        if (flatList == null || flatList.isEmpty()) {
            return new ArrayList<>();
        }
        // 3. 过滤：只保留 房间ID不为null 的有效数据
        flatList = flatList.stream()
                .filter(dto -> dto.getRoomId() != null)
                .collect(Collectors.toList());
        // 4. 再次判空
        if (flatList.isEmpty()) {
            return new ArrayList<>();
        }
        // 5. 创建 楼层DTO，赋值楼层信息
        FloorRoomBedDTO floorDTO = new FloorRoomBedDTO();
        floorDTO.setFloorId(flatList.get(0).getFloorId());
        floorDTO.setFloorName(flatList.get(0).getFloorName());
        // 6. 核心：按房间ID分组
        // 效果：Map<房间ID, 该房间下所有床位的扁平化数据>
        Map<Long, List<FlatBedDTO>> roomGroup = flatList.stream()
                .collect(Collectors.groupingBy(FlatBedDTO::getRoomId));

        List<FloorRoomBedDTO.RoomDTO> roomList = new ArrayList<>();
        // 遍历每一组（每一组 = 一个房间 + 它下面所有床位）
        for (Map.Entry<Long, List<FlatBedDTO>> entry : roomGroup.entrySet()) {
            // 1. 获取当前组的 key = 房间ID
            Long roomId = entry.getKey();
            // 2. 获取当前组的 value = 这个房间下的所有床位数据（扁平化列表）
            List<FlatBedDTO> roomBeds = entry.getValue();
            // 3. 从列表中获取第一个床位数据（作为房间信息）
            FlatBedDTO first = roomBeds.get(0);
            // 4. 创建房间DTO
            FloorRoomBedDTO.RoomDTO roomDTO = new FloorRoomBedDTO.RoomDTO();
            // 5. 给房间赋值：ID、房间号、房型名称
            roomDTO.setId(roomId);
            roomDTO.setCode(first.getRoomCode());
            roomDTO.setRoomTypeName(first.getRoomTypeName());
            // 封装床位列表
            List<FloorRoomBedDTO.RoomDTO.BedDTO> bedList = new ArrayList<>();
            for (FlatBedDTO b : roomBeds) {
                // 每遍历一个床位，就创建一个 BedDTO 对象
                FloorRoomBedDTO.RoomDTO.BedDTO bedDTO = new FloorRoomBedDTO.RoomDTO.BedDTO();
                // 把扁平化数据里的床位信息，赋值给 BedDTO
                bedDTO.setBedId(b.getBedId());
                bedDTO.setBedNumber(b.getBedNumber());
                bedDTO.setBedStatus(b.getBedStatus());
                bedDTO.setNursingLevelName(b.getNursingLevelName());
                bedDTO.setName(b.getElderName());
                bedDTO.setElderId(b.getElderId());
                bedDTO.setRemark(b.getRemark());
                bedDTO.setStartTime(b.getStartTime());
                bedDTO.setEndTime(b.getEndTime());
                // 把封装好的床位对象，放进床位列表
                bedList.add(bedDTO);
            }
            // 把房间列表设置到楼层中
            roomDTO.setBeds(bedList);
            roomList.add(roomDTO);
        }

        floorDTO.setRooms(roomList);
        // 返回结果（外层包一层List）
        List<FloorRoomBedDTO> result = new ArrayList<>();
        result.add(floorDTO);
        return result;
    }

    @Override
    public FlatBedDTO getFloorRoomBedByFloorId1(Long elderId) {
        if (elderId == null) {
            return null;
        }
        // 调用Mapper：根据老人ID查扁平化数据
        List<FlatBedDTO> list = bedMapper.selectFloorRoomBedByFloorId(null, elderId);
        if (list == null || list.isEmpty()) {
            return null;
        }
//        FlatBedDTO flatBedDTO = new FlatBedDTO ();
//        flatBedDTO.setRoomTypeName ( list.get(0).getRoomTypeName () );
//        flatBedDTO.setRoomCode ( list.get(0).getRoomCode () );
//        flatBedDTO.setPhoto ( list.get(0).getPhoto () );
        // 老人只可能住一个床位 → 直接返回第一条
        return list.get ( 0 );
    }
    /**
     * 查询床位表
     */
    @Override
    public Bed selectBedById(Long id) {
        return bedMapper.selectBedById(id);
    }

    /**
     * 查询床位表列表
     */
    @Override
    public List<Bed> selectBedList(Bed bed) {
        return bedMapper.selectBedList(bed);
    }

    /**
     * 新增床位表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertBed(Bed bed) {
        if (bed.getRoomId() == null) {
            throw new ServiceException("房间ID不能为空");
        }
        Room room = roomMapper.selectLcRoomById(bed.getRoomId());
        if (room == null) {
            throw new ServiceException("房间不存在");
        }

        LcRoomType roomType = roomTypeMapper.selectLcRoomTypeById(room.getRoomTypeId());
        if (roomType == null || roomType.getBedCount() == null) {
            throw new ServiceException("房间类型配置错误");
        }

        // 查询该房间已有多少床位
        Integer maxBedNum = roomType.getBedCount();
        Integer currentBedNum = bedMapper.countBedByRoomId(bed.getRoomId());
        // 判断：已满 → 返回1，未满 → 插入返回0
        if (currentBedNum >= maxBedNum) {
            return 1; // 已满
        }

        // 未满才插入
        bedMapper.insertBed(bed);
        return 0;
    }

    /**
     * 修改床位表（含老人关联）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateBed(Bed bed, String name) {

//        // 1. 根据床位号查询床位（修复参数）
//        Bed bedInfo = bedMapper.selectBedNumber(bed.getBedNumber());
//
//// 2. 非空判断
//        if (bedInfo == null) {
//            throw new RuntimeException("床位不存在");
//        }
//        Elder elder = new Elder ();
//        elder.setName ( name );
//        List<Elder> elders = elderMapper.selectElderList ( elder );
//        if (elders == null || elders.isEmpty()) {
//            return 0;
//        }
        // 1. 先判断床位号是否重复
        Bed existBed = bedMapper.selectBedNumber(bed.getBedNumber());
        // 如果查到重复，并且 不是当前这条数据 → 拒绝更新
        if (existBed != null && !existBed.getId().equals(bed.getId())) {
            throw new RuntimeException("床位号【" + bed.getBedNumber() + "】已存在，不能重复！");
        }
        // 2. 根据姓名查询老人
        Elder elder = new Elder ();
        elder.setName ( name );
        List<Elder> elders = elderMapper.selectElderList ( elder );
        if (elders == null || elders.isEmpty()) {
            return 0;
        }
        // 2. 更新老人的 bed_id（把老人绑定到该床位）
        elderMapper.updateBedId(bed.getId(), bed.getName());

        // 3. 更新床位信息
        return bedMapper.updateBed1(bed);
    }

    /**
     * 批量删除床位表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteBedByIds(Long[] ids) {
        return bedMapper.deleteBedByIds(ids);
    }

    /**
     * 删除床位表信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteBedById(Long id) {
        return bedMapper.deleteBedById(id);
    }
}