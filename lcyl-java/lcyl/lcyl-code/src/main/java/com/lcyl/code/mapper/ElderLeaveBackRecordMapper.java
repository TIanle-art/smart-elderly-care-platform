package com.lcyl.code.mapper;

import com.lcyl.code.domain.ElderLeaveBackRecord;

/**
* @author crystal
* @description 针对表【lc_elder_leave_back_record(老人请假销假/返院记录表)】的数据库操作Mapper
* @createDate 2026-03-20 19:26:39
* @Entity com.lcyl.code.domain.ElderLeaveBackRecord
*/
public interface ElderLeaveBackRecordMapper {

    int deleteByPrimaryKey(Long id);

    int insert(ElderLeaveBackRecord record);

    int insertSelective(ElderLeaveBackRecord record);

    ElderLeaveBackRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ElderLeaveBackRecord record);

    int updateByPrimaryKey(ElderLeaveBackRecord record);

}
