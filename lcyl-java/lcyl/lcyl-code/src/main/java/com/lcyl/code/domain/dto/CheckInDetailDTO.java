package com.lcyl.code.domain.dto;

import lombok.Data;

import java.util.Date;
@Data
public class CheckInDetailDTO {
    private Long checkInId;

    /* ========== 二、流程态（前端最关心） ========== */
    private Long flowStatus;          // 当前流程阶段（0~5）
    private String currentTaskKey;    // 当前节点 key（BPMN 中 userTask id）
    private String currentTaskName;   // 当前节点名称
    private String taskId;            // 当前用户可操作的 taskId（⚠️ 关键）

    /* ========== 三、权限 / 页面控制 ========== */
    private Boolean canEdit;          // 是否可编辑表单
    private Boolean canApprove;       // 是否显示审批按钮
    private Boolean canConfig;        // 是否显示配置按钮
    private Boolean canSign;          // 是否显示签约按钮

    /* ========== 四、业务展示数据 ========== */
    private String title;             // 例如：张三的入住申请
    private String elderName;
    private String checkInCode;
    private Long status;              // 审批状态：0待审批 / 2通过 / 3拒绝 / 4驳回

    /* ========== 五、原始业务 JSON ========== */
    private String otherApplyInfo;    // basicForm 等
    private String reviewInfo;        // 家属 / 审核资料

    /* ========== 六、流程辅助信息（可选但推荐） ========== */
    private String processId;
    private Date createTime;
    private Date updateTime;

    /** 是否可以处理 */
    private Boolean canHandle;
}
