package com.lcyl.code.service;

import java.util.Date;
import java.util.List;
import com.lcyl.code.domain.CheckInConfigg;
import com.lcyl.code.domain.dto.CheckInFlowDTO;
import com.lcyl.code.vo.ConfigVo;

/**
 * 入住配置表Service接口
 *
 * @author ruoyi
 * @date
 */
public interface ICheckInConfigService
{
    /**
     * 查询入住配置表
     *
     * @param id 入住配置表主键
     * @return 入住配置表
     */
    public CheckInConfigg selectCheckInConfigById(Long id);

    /**
     * 查询入住配置表列表
     *
     * @param checkInConfig 入住配置表
     * @return 入住配置表集合
     */
    public List<CheckInConfigg> selectCheckInConfigList(CheckInConfigg checkInConfig);

    /**
     * 新增入住配置表
     *
     * @param checkInConfig 入住配置表
     * @return 结果
     */
    public int insertCheckInConfig(CheckInConfigg checkInConfig);

    /**
     * 修改入住配置表
     *
     * @param checkInConfig 入住配置表
     * @return 结果
     */
    public int updateCheckInConfig(CheckInConfigg checkInConfig);

    /**
     * 批量删除入住配置表
     *
     * @param ids 需要删除的入住配置表主键集合
     * @return 结果
     */
    public int deleteCheckInConfigByIds(Long[] ids);

    /**
     * 删除入住配置表信息
     *
     * @param id 入住配置表主键
     * @return 结果
     */
    public int deleteCheckInConfigById(Long id);

    /**
     * 提交入住配置：保存配置到入住配置表，并将老人关联到所选床位（占用床位）
     *
     * @param checkInConfig 入住配置（含 elderId、bedId、bedNo 及费用等）
     * @return 结果
     */
    public CheckInFlowDTO submitConfig(CheckInConfigg checkInConfig);
    //根据老人id查询入住配置
    ConfigVo selectCheckInConfigByElderId(Integer elderId, String billMonth, Date startTime, Date endTime);
}
