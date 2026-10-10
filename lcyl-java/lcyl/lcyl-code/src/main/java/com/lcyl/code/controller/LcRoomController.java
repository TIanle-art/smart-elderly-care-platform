package com.lcyl.code.controller;

import com.lcyl.common.annotation.Log;
import com.lcyl.common.core.controller.BaseController;
import com.lcyl.common.core.domain.AjaxResult;
import com.lcyl.common.core.page.TableDataInfo;
import com.lcyl.common.enums.BusinessType;
import com.lcyl.common.utils.poi.ExcelUtil;
import com.lcyl.system.domain.LcRoomType;
import com.lcyl.system.domain.Room;
import com.lcyl.system.domain.dto.RoomDTO;
import com.lcyl.system.service.ILcRoomService;
import com.lcyl.system.service.ILcRoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 房间表Controller
 * 
 * @author ruoyi
 * @date 2026-03-24
 */
@RestController
@RequestMapping("/system/room")
public class LcRoomController extends BaseController
{
    @Autowired
    private ILcRoomService lcRoomService;

    @Autowired
    private ILcRoomTypeService lcRoomTypeService;

    /**
     * 查询房间表列表
     */
    @PreAuthorize("@ss.hasPermi('system:room:list')")
    @GetMapping("/list")
    public TableDataInfo list(Room room)
    {
        startPage();
        List<Room> list = lcRoomService.selectLcRoomList( room );
        return getDataTable(list);
    }

    /**
     * 导出房间表列表
     */
    @PreAuthorize("@ss.hasPermi('system:room:export')")
    @Log(title = "房间表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Room room)
    {
        List<Room> list = lcRoomService.selectLcRoomList( room );
        ExcelUtil<Room> util = new ExcelUtil<Room>( Room.class);
        util.exportExcel(response, list, "房间表数据");
    }

    /**
     * 获取房间表详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:room:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(lcRoomService.selectLcRoomById(id));
    }

    /**
     * 新增房间表
     */
    @PreAuthorize("@ss.hasPermi('system:room:add')")
    @Log(title = "房间表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RoomDTO room)
    {
        // 1. 校验入参：房型名称不能为空
        if (room == null || room.getRoomTypeName() == null || room.getRoomTypeName().trim().isEmpty()) {
            return AjaxResult.error("房型名称不能为空");
        }

// 2. 构建房型查询条件（根据名称模糊查询，适配非唯一场景）
        LcRoomType lcRoomType = new LcRoomType();
        String roomTypeName = room.getRoomTypeName().trim();
        lcRoomType.setName(roomTypeName); // 去除首尾空格，避免查询误差

// 3. 调用service查询房型列表（模糊匹配名称）
        List<LcRoomType> roomTypeList = lcRoomTypeService.selectLcRoomTypeList(lcRoomType);

// 4. 处理查询结果：兼容多房型/单房型场景
        if (roomTypeList == null || roomTypeList.isEmpty()) {
            return AjaxResult.error("未找到名称包含【" + roomTypeName + "】的房型，无法新增房间");
        }

// 4.1 多房型场景：提示用户选择（而非默认取第一个，避免误赋值）
        if (roomTypeList.size() > 1) {
            // 拼接所有匹配的房型名称+ID，返回给前端让用户选择
            StringBuilder tipMsg = new StringBuilder();
            tipMsg.append("找到多个匹配的房型：");
            for (LcRoomType type : roomTypeList) {
                tipMsg.append("[ID:").append(type.getId()).append("，名称:").append(type.getName()).append("] ");
            }
            tipMsg.append("请明确指定要关联的房型ID");
            return AjaxResult.error(tipMsg.toString());
        }

// 4.2 单房型场景：直接赋值房型ID
        LcRoomType existRoomType = roomTypeList.get(0);
        room.setRoomTypeId(existRoomType.getId());

// 5. 执行房间新增操作
        int saveResult = lcRoomService.insertLcRoom(room);
        if (saveResult > 0) {
            return AjaxResult.success("新增房间成功，关联房型：" + existRoomType.getName() + "(ID:" + existRoomType.getId() + ")");
        } else {
            return AjaxResult.error("新增房间失败，请重试");
        }
    }

    /**
     * 修改房间表
     */
    @PreAuthorize("@ss.hasPermi('system:room:edit')")
    @Log(title = "房间表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RoomDTO room)
    {
        // 1. 校验入参：房型名称不能为空
        if (room == null || room.getRoomTypeName() == null || room.getRoomTypeName().trim().isEmpty()) {
            return AjaxResult.error("房型名称不能为空");
        }

        // 2. 构建房型查询条件
        LcRoomType lcRoomType = new LcRoomType();
        String roomTypeName = room.getRoomTypeName().trim();
        lcRoomType.setName(roomTypeName);

        // 3. 查询房型列表
        List<LcRoomType> roomTypeList = lcRoomTypeService.selectLcRoomTypeList(lcRoomType);

        // 4. 未找到房型
        if (roomTypeList == null || roomTypeList.isEmpty()) {
            return AjaxResult.error("未找到名称包含【" + roomTypeName + "】的房型，无法修改房间");
        }

        // 4.1 多个匹配房型，提示用户明确选择
        if (roomTypeList.size() > 1) {
            StringBuilder tipMsg = new StringBuilder();
            tipMsg.append("找到多个匹配的房型：");
            for (LcRoomType type : roomTypeList) {
                tipMsg.append("[ID:").append(type.getId()).append("，名称:").append(type.getName()).append("] ");
            }
            tipMsg.append("请明确指定要关联的房型ID");
            return AjaxResult.error(tipMsg.toString());
        }

        // 4.2 唯一匹配，赋值房型ID
        LcRoomType existRoomType = roomTypeList.get(0);
        room.setRoomTypeId(existRoomType.getId());

        // 5. 执行修改
        int updateResult = lcRoomService.updateLcRoom(room);
        if (updateResult > 0) {
            return AjaxResult.success("修改房间成功，关联房型：" + existRoomType.getName() + "(ID:" + existRoomType.getId() + ")");
        } else {
            return AjaxResult.error("修改房间失败，请重试");
        }
    }

    /**
     * 删除房间表
     */
    @PreAuthorize("@ss.hasPermi('system:room:remove')")
    @Log(title = "房间表", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(lcRoomService.deleteLcRoomByIds(ids));
    }
}
