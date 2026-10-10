package com.lcyl.code.controller;

import com.lcyl.code.domain.LcRetreatBill;
import com.lcyl.code.domain.LcRetreatContract;
import com.lcyl.code.domain.LcRetreatHistory;
import com.lcyl.code.domain.Retreat;
import com.lcyl.code.domain.dto.RetreatStartDTO;
import com.lcyl.code.domain.dto.RetreatTaskCompletedDTO;
import com.lcyl.code.mapper.LcRetreatBillMapper;
import com.lcyl.code.mapper.LcRetreatContractMapper;
import com.lcyl.code.mapper.LcRetreatHistoryMapper;
import com.lcyl.code.service.IRetreatService;
import com.lcyl.common.annotation.Log;
import com.lcyl.common.core.controller.BaseController;
import com.lcyl.common.core.domain.AjaxResult;
import com.lcyl.common.core.page.TableDataInfo;
import com.lcyl.common.enums.BusinessType;
import com.lcyl.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * 退住Controller
 *
 * @author ruoyi
 * @date 2026-03-24
 */
@RestController
@RequestMapping("/code/checkout")
public class RetreatController extends BaseController
{

    @Autowired
    private IRetreatService retreatService;

    @Autowired
    private LcRetreatContractMapper retreatContractMapper;

    /**
     * 发起退住申请（启动工作流）
     */
    @PreAuthorize("@ss.hasRole('nurse')")
    @PostMapping("/start")
    public AjaxResult start(@RequestBody RetreatStartDTO dto) {
        Retreat retreat = retreatService.startApplication(dto);
        return success(retreat);
    }

    /**
     * 获取当前用户的待办任务列表
     */
    @GetMapping("/todo-list")
    public AjaxResult todoList() {
        List<Retreat> list = retreatService.getTodoList();
        return success(list);
    }

    @PostMapping("/complete-task")
    public AjaxResult completeTask(@RequestBody RetreatTaskCompletedDTO dto) {
        String string = retreatService.completeTask(dto);
        return success(string);
    }

    /**
     * 查询退住列表
     */
    @PreAuthorize("@ss.hasPermi('code:checkout:list')")
    @GetMapping("/list")
    public TableDataInfo list(Retreat retreat)
    {
        startPage();
        List<Retreat> list = retreatService.selectRetreatList(retreat);
        return getDataTable(list);
    }

    /**
     * 导出退住列表
     */
    @PreAuthorize("@ss.hasPermi('code:checkout:export')")
    @Log(title = "退住", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Retreat retreat)
    {
        List<Retreat> list = retreatService.selectRetreatList(retreat);
        ExcelUtil<Retreat> util = new ExcelUtil<Retreat>(Retreat.class);
        util.exportExcel(response, list, "退住数据");
    }

    /**
     * 获取退住详细信息
     */
    @PreAuthorize("@ss.hasPermi('code:checkout:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(retreatService.selectRetreatById(id));
    }

    /**
     * 新增退住
     */
    @PreAuthorize("@ss.hasPermi('code:checkout:add')")
    @Log(title = "退住", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Retreat retreat)
    {
        return toAjax(retreatService.insertRetreat(retreat));
    }

    /**
     * 修改退住
     */
    @PreAuthorize("@ss.hasPermi('code:checkout:edit')")
    @Log(title = "退住", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Retreat retreat)
    {
        return toAjax(retreatService.updateRetreat(retreat));
    }

    /**
     * 删除退住
     */
    @PreAuthorize("@ss.hasPermi('code:checkout:remove')")
    @Log(title = "退住", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(retreatService.deleteRetreatByIds(ids));
    }

    @GetMapping("/contract/{id}")
    public AjaxResult selectRetreatContract(@PathVariable("id") Long id){
        LcRetreatContract contract = retreatContractMapper.selectContractByRetreatId(id);
        return AjaxResult.success(contract);
    }

    @PostMapping("/upload/image")
    public AjaxResult uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return AjaxResult.error("文件不能为空");
            }

            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return AjaxResult.error("仅支持图片");
            }

            String fileName = UUID.randomUUID() + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String path = System.getProperty("user.dir") + "/upload/";
            File dir = new File(path);
            if (!dir.exists()) dir.mkdirs();

            File dest = new File(dir, fileName);
            file.transferTo(dest);

            String url = "/upload/" + fileName;
            return AjaxResult.success(url);
        } catch (Exception e) {
            return AjaxResult.error("上传失败：" + e.getMessage());
        }
    }

    @Autowired
    private LcRetreatBillMapper retreatBillMapper;
    @GetMapping("/retreatBill/{retreatId}")
    public AjaxResult getRetreatBill(@PathVariable("retreatId") Long retreatId) {
        LcRetreatBill bill = retreatBillMapper.selectLatestByRetreatId(retreatId);
        return AjaxResult.success(bill);
    }

    @Autowired
    private LcRetreatHistoryMapper retreatHistoryMapper;
    @GetMapping("/history/{retreatId}")
    public AjaxResult getRetreatHistory(@PathVariable("retreatId") Long retreatId) {
        List<LcRetreatHistory> historyList = retreatHistoryMapper.selectByRetreatId(retreatId);
        return AjaxResult.success(historyList);
    }
}