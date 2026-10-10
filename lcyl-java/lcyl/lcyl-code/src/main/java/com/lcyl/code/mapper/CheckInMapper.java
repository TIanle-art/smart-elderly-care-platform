package com.lcyl.code.mapper;

import java.util.List;

import com.lcyl.common.core.domain.entity.SysUser;
import com.lcyl.code.domain.CheckIn;
//import com.lcyl.code.domain.CheckInDetailDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 入住办理Mapper接口
 *
 * @author ruoyi
 * @date 2026-01-28
 */
@Mapper
public interface CheckInMapper
{
    /**
     * 查询入住办理
     *
     * @param id 入住办理主键
     * @return 入住办理
     */
    public CheckIn selectCheckInById(Long id);

    /**
     * 查询入住办理列表
     *
     * @param checkIn 入住办理
     * @return 入住办理集合
     */
    public List<CheckIn> selectCheckInList(CheckIn checkIn);

    List<CheckIn> selectMyCheckInList(CheckIn checkIn);

    /**
     * 新增入住办理
     *
     * @param checkIn 入住办理
     * @return 结果
     */
    public int insertCheckIn(CheckIn checkIn);

    /**
     * 修改入住办理
     *
     * @param checkIn 入住办理
     * @return 结果
     */
    public int updateCheckIn(CheckIn checkIn);

    /**
     * 删除入住办理
     *
     * @param id 入住办理主键
     * @return 结果
     */
    public int deleteCheckInById(Long id);

    /**
     * 批量删除入住办理
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCheckInByIds(Long[] ids);

    //查护士组长
//    public SysUser selectNurseBoss(Long id);

    //根据流程实例id查询入住办理
    public CheckIn selectCheckInByProcessId(@Param("processId") String processId);

    /**
     * 根据身份证号查询未结束的入住申请
     */
    CheckIn selectActiveByIdCard(@Param("idCard") String idCard);

    /**
     * 根据部门编号查询用户昵称列表（用于流程候选人）
     */
    List<String> selectByDeptNo(@Param("deptNo") Long deptNo);

    /**
     * 根据部门名称查询用户昵称列表（用于签约办理候选人，不依赖部门ID）
     */
    List<String> selectNickNamesByDeptName(@Param("deptName") String deptName);

    List<String> selectNickNamesByDeptNameLike(@Param("deptNameKeyword") String deptNameKeyword);

    List<String> selectUserNamesByDeptName(@Param("deptName") String deptName);

    List<String> selectUserNamesByDeptNameLike(@Param("deptNameKeyword") String deptNameKeyword);

    List<String> selectUserNamesByRoleNameLike(@Param("roleNameKeyword") String roleNameKeyword);

    List<String> selectNickNamesByRoleNameLike(@Param("roleNameKeyword") String roleNameKeyword);

    //查院长nikeNmae
    String selectDean(String roleName);

    //根据id查询办理信息
    String  selectCheckInDetailById(Long id);

    public String selectName(String roleName);

    String getlevelName(Long elderId);

    CheckIn selectCheckInByElderId(Long elderId);
}
