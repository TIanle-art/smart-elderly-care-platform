package com.lcyl.code.mapper;

import java.util.Date;
import java.util.List;
import com.lcyl.code.domain.CheckInConfigg;
import com.lcyl.code.vo.ConfigVo;
import org.apache.ibatis.annotations.Param;

/**
 * 入住配置表Mapper接口
 *
 * @author ruoyi
 * @date 2026-02-01
 */
public interface CheckInConfiggMapper
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
     * 删除入住配置表
     *
     * @param id 入住配置表主键
     * @return 结果
     */
    public int deleteCheckInConfigById(Long id);

    /**
     * 批量删除入住配置表
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCheckInConfigByIds(Long[] ids);
    //根据老人id查询入住配置
    ConfigVo selectCheckInConfigByElderId(@Param("startTime") Date startTime,@Param("endTime") Date endTime, @Param("elderId") Integer elderId,
                                          @Param("billMonth") String billMonth);
    //查询入住配置表
    ConfigVo selectCheckInConfig(@Param("elderId") Integer elderId, @Param("billMonth") String billMonth);

    /**
     * 根据老人ID查询入住配置
     */
    CheckInConfigg selectById(@Param("elderId") Long elderId);

    /**
     * 更新入住配置状态（退住完成）
     */
    void updateCheckInConfigRetreat(CheckInConfigg checkInConfig);

    void delByElderId(@Param("elderId") Long elderId);

}
