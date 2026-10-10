package com.lcyl.code.constant;

public class CheckinConstants {
    // Activiti流程定义Key（与BPMN文件的process id一致）
    public static final String CHECKIN_PROCESS_KEY = "checkinProcess";

    // 申请状态码（主表apply_status）
    public static final String APPLY_STATUS_INIT = "01";        // 初始状态（仅发起申请）
    public static final String APPLY_STATUS_BASIC_DONE = "02"; // 老人基础信息已填写
    public static final String APPLY_STATUS_FAMILY_DONE = "03";// 家属信息已填写
    public static final String APPLY_STATUS_FILE_DONE = "04";  // 资料已上传
    public static final String APPLY_STATUS_EVAL = "05";       // 评估中
    public static final String APPLY_STATUS_APPROVE = "06";    // 审批中
    public static final String APPLY_STATUS_CONFIG = "07";     // 入住配置中
    public static final String APPLY_STATUS_SIGN = "08";       // 签约中
    public static final String APPLY_STATUS_CHECKED = "09";    // 已入住
    public static final String APPLY_STATUS_REJECT = "99";     // 已驳回

    // 申请编号前缀
    public static final String APPLY_NO_PREFIX = "CK";
}
