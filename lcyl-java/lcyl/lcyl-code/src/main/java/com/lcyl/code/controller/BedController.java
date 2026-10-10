package com.lcyl.code.controller;

import com.lcyl.common.annotation.Log;
import com.lcyl.common.core.controller.BaseController;
import com.lcyl.common.core.domain.AjaxResult;
import com.lcyl.common.core.page.TableDataInfo;
import com.lcyl.common.enums.BusinessType;
import com.lcyl.common.utils.Result;
import com.lcyl.common.utils.poi.ExcelUtil;
import com.lcyl.system.domain.Bed;
import com.lcyl.system.domain.LcRoomType;
import com.lcyl.system.domain.Room;
import com.lcyl.system.domain.dto.FloorRoomBedDTO;
import com.lcyl.system.mapper.LcRoomMapper;
import com.lcyl.system.mapper.LcRoomTypeMapper;
import com.lcyl.system.service.BedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 床位表Controller
 *
 * @author tyf
 * @date 2026-03-23
 */
@RestController
@RequestMapping("/system/bed")
public class BedController extends BaseController
{
    @Autowired
    private BedService bedService;

    @GetMapping("/floor/{floorId}")
    public Result getFloorRoomBed(@PathVariable Long floorId) {
        List<FloorRoomBedDTO> data = bedService.getFloorRoomBedByFloorId(floorId);
        return Result.success(data);
    }

    /**
     * 查询床位表列表
     */
   @PreAuthorize("@ss.hasPermi('system:bed:list')")
//    @PreAuthorize("@anonymous()")
    @GetMapping("/list")
    public TableDataInfo list(Bed bed)
    {
        startPage();
        List<Bed> list = bedService.selectBedList(bed);
        return getDataTable(list);
    }

    /**
     * 导出床位表列表
     */
    @PreAuthorize("@ss.hasPermi('system:bed:export')")
    @Log(title = "床位表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Bed bed)
    {
        List<Bed> list = bedService.selectBedList(bed);
        ExcelUtil<Bed> util = new ExcelUtil<Bed>(Bed.class);
        util.exportExcel(response, list, "床位表数据");
    }

    /**
     * 获取床位表详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:bed:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(bedService.selectBedById(id));
    }

    /**
     * 新增床位表
     */
    @Autowired
    private LcRoomMapper roomMapper;
    @Autowired
    private LcRoomTypeMapper roomTypeMapper;
    @PreAuthorize("@ss.hasPermi('system:bed:add')")
    @Log(title = "床位表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Bed bed)
    {
            // 1. 调用你写的校验方法，判断床位数是否超限
            int checkResult = bedService.insertBed(bed);
            // 2. 校验不通过：返回错误提示
            if (checkResult == 1) {
                // 查询房间/房型信息，返回更友好的提示（可选，提升体验）
                Room room = roomMapper.selectLcRoomById(bed.getRoomId());
                LcRoomType roomType = roomTypeMapper.selectLcRoomTypeById(room.getRoomTypeId());
                return AjaxResult.error("新增失败！当前" + roomType.getName () +
                        "最大床位数为" + roomType.getBedCount() +
                        "，已达上限");
            }
            // 3. 校验通过：执行实际的新增操作
        try {
//            bedService.insertBed ( bed ); // 实际新增床位的方法
            return AjaxResult.success("新增床位成功");
        }catch (Exception e){
            return AjaxResult.success("新增床位失败");
        }

    }

    /**
     * 修改床位表
     */
    @PreAuthorize("@ss.hasPermi('system:bed:edit')")
    @Log(title = "床位表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Bed bed)
    {
        int result = bedService.updateBed(bed, bed.getName ());
        if (result == 0) {
            return AjaxResult.error("修改失败！没有该用户信息或床位不存在/参数错误");
        }
//            bedService.updateBed ( bed , bed.getName () );
            return AjaxResult.success("修改床位成功");
    }

    /**
     * 删除床位表
     */
    @PreAuthorize("@ss.hasPermi('system:bed:remove')")
    @Log(title = "床位表", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(bedService.deleteBedByIds(ids));
    }
}
