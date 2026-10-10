/**
 * API 类型统一导出
 */
export * from "./common";

// 登录模块
export * from "./login";
export * from "./menu";

// System 模块
export * from "./system/user";
export * from "./system/role";
export * from "./system/menu";
export * from "./system/dept";
export * from "./system/post";
export * from "./system/dict";
export * from "./system/config";
export * from "./system/notice";
export * from "./system/floor";
export * from "./system/contract";
export * from "./system/SmartBed";
export * from "./system/device";
// monitor 模块
export * from "./monitor/cache";
export * from "./monitor/job";
export * from "./monitor/jobLog";
export * from "./monitor/logininfor";
export * from "./monitor/operlog";
export * from "./monitor/online";
export * from "./system/type";
export * from "./code/leave";
export * from "./code/arrival";
// 代码生成模块
export * from "./tool/gen";

// nurse 模块
export * from "./code/nurse";
// nursingItem 模块
export * from "./code/item";
// nursingPlan 模块
export * from "./code/plan";
// planItem 模块
export * from "./code/planItem";
// nursingLevel 模块
export * from "./code/nursingLevel";

export * from "./code/responsible";
// 订单详情模块
export * from "./code/refund";
export * from "./code/orders";


//退住模块
export * from "./code/checkout";


// 账单列表模块
export * from "./code/bill";
// 账单支付记录模块
export * from "./code/payment";
// 账单明细模块
export * from "./code/item";
// 账户余额模块
export * from "./code/balance";
// 预缴款充值记录模块
export * from "./code/recharge";

