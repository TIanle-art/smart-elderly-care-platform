package com.lcyl.system.mapper;

import com.lcyl.system.domain.Elder;
import com.lcyl.system.domain.dto.AddInfo;
import com.lcyl.system.domain.vo.BedVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ElderMapper {

    // 查询
    public Elder selectElderById(Long id);

    // 列表
    public List<Elder> selectElderList(Elder elder);

    // 新增
    public int insertElder(Elder elder);

    // 修改
    public int updateElder(Elder elder);

    // ✅ 绑定床位
    void updateBedId(@Param("bedId") Long bedId, @Param("name") String elderName);
    // 删除
    public int deleteElderById(Long id);

    // 批量删除
    public int deleteElderByIds(Long[] ids);

    List<Elder> selectElderByMember(Long memberId);

    List<Elder> selectAllElderList();

    List<Long> selectElderInfo(Long memberId);

    Integer insertInfo(@Param("addInfo") AddInfo addInfo,  @Param("memberId") Long memberId);

    List<BedVO> selectElderBedList(Long memberId);

    Integer delElderInfoById(Long id);

    void updateStatus(@Param("elderId")Long elderId,@Param("status") int i);
}