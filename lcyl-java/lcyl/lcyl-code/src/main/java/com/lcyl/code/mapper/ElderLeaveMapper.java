package com.lcyl.code.mapper;

import com.lcyl.code.domain.ElderLeave;
import com.lcyl.code.dto.ElderLeaveDto;
import com.lcyl.code.vo.ElderLeaveFormVo;
import com.lcyl.code.vo.ElderLeaveVo;
import com.lcyl.code.vo.ElderOptionVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 老人请假申请Mapper接口
 *
 * @author cch
 * @date 2026-03-20
 */
public interface ElderLeaveMapper {
    /**
     * 查询老人请假申请
     *
     * @param id 老人请假申请主键
     * @return 老人请假申请
     */
    public ElderLeave selectElderLeaveById(Long id);

    /**
     * 查询老人请假申请列表
     *
     * @param elderLeaveDto 老人请假申请
     * @return 老人请假申请集合
     */
    public List<ElderLeaveVo> selectElderLeaveList(ElderLeaveDto elderLeaveDto);

    /**
     * 新增老人请假申请
     *
     * @param elderLeave 老人请假申请
     * @return 结果
     */
    public int insertElderLeave(ElderLeave elderLeave);

    /**
     * 修改老人请假申请
     *
     * @param elderLeave 老人请假申请
     * @return 结果
     */
    public int updateElderLeave(ElderLeave elderLeave);

    /**
     * 删除老人请假申请
     *
     * @param id 老人请假申请主键
     * @return 结果
     */
    public int deleteElderLeaveById(Long id);

    /**
     * 批量删除老人请假申请
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteElderLeaveByIds(Long[] ids);

    /*-------------------------------------------------------------------*/

    /**
     * 根据老人id查询老人的基本信息
     *
     * @param elderId
     * @return
     */
    ElderLeaveFormVo selectLeaveFormInfoByElderId(@Param("elderId") Long elderId);

    /**
     * 按流程实例ID查请假单
     * @param processInstanceId
     * @return
     */
    ElderLeave selectByProcessInstanceId(@Param("processInstanceId") String processInstanceId);

    /**
     * 请假通过后回写流程状态
     * @param elderLeave
     * @return
     */
    int updateProcessInfo(ElderLeave elderLeave);

    /**
     * 定时更新状态到数据库
     */
    void updateLeaveToTimeout();

    /**
     * 查询所有老人的id和姓名
     * @return
     */
    List<ElderOptionVo> selectElderOptions();
}
