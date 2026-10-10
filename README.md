# 智慧养老一体化平台（Smart Elderly Care Platform）

企业工程实践（1）课程项目：面向养老机构的资源调度一体化管理平台，覆盖 **床位资源、入住退住、护理任务、账单收费、服务订单、家属服务** 全业务流程。

项目基于 [RuoYi-Vue 3.9.1](https://gitee.com/y_project/RuoYi-Vue)（Spring Boot + Vue3 前后端分离快速开发框架）进行二次开发，包含 **Java 后端 + Vue3 管理前端 + 微信小程序** 三端完整源码。

## 项目结构

```
├── lcyl-java/lcyl/          # 后端：Spring Boot 多模块（Maven）
│   ├── lcyl-admin/          #   启动模块、Web 配置、系统管理接口
│   ├── lcyl-code/           #   业务模块：养老核心业务（老人/床位/入住/护理/账单/订单等）
│   ├── lcyl-common/         #   通用工具、常量、基础实体
│   ├── lcyl-framework/      #   安全、Token、拦截器、AOP 等框架能力
│   ├── lcyl-system/         #   系统管理（用户/角色/菜单/字典/部门/岗位）
│   ├── lcyl-quartz/         #   定时任务
│   ├── lcyl-generator/      #   代码生成器
│   ├── sql/                 #   数据库初始化脚本（ry_20250522.sql、quartz.sql）
│   └── doc/                 #   RuoYi 环境使用手册
├── lcyl-vue/lcyl/           # 前端：Vue3 + TypeScript + Vite + Element Plus 管理后台
└── lcyl-wx/                 # 微信小程序：家属端（原生小程序）
```

## 技术栈

| 端 | 技术 |
| --- | --- |
| 后端 | Java 8、Spring Boot 2.7、Spring Security、MyBatis + PageHelper、Druid、Redis、Activiti 工作流、Quartz、阿里云 OSS |
| 前端 | Vue 3.5、TypeScript、Vite 6、Element Plus、Pinia、Axios |
| 小程序 | 微信原生小程序（登录 / 服务预约 / 账单 / 合同 / 家属绑定） |

## 主要功能

- **资源管理**：楼层 → 房间 → 床位三级床位资源，房型与入住配置
- **出入院流程**：入住申请 / 审批 / 合同签订 / 到访登记，退住申请与退住完成（Activiti 审批流：请假、入驻）
- **护理管理**：护理级别、护理项目、护理计划与计划项、护理任务
- **收费中心**：账单、账单项、缴费记录、余额与充值、退款
- **服务商城**：服务项目、服务订单、预约、退款
- **老人档案**：老人信息、健康评估、能力评估、家属绑定、请假
- **小程序端**：微信一键登录、服务浏览与下单、我的账单 / 订单 / 合同、家属管理
- **系统管理**：用户、角色、菜单、部门、岗位、字典、参数、通知、日志、监控（RuoYi 体系）

## 快速开始

### 环境要求

- JDK 8+、Maven 3.6+、MySQL 5.7+、Redis 5+
- Node.js 16+（前端）、微信开发者工具（小程序）

### 1. 初始化数据库

```bash
mysql -u root -p
# 新建数据库 lcyl（utf8mb4），然后依次执行：
# lcyl-java/lcyl/sql/ry_20250522.sql
# lcyl-java/lcyl/sql/quartz.sql
```

### 2. 启动后端

```bash
cd lcyl-java/lcyl
mvn clean package -DskipTests
java -jar lcyl-admin/target/lcyl-admin.jar
# 或直接运行 com.lcyl.RuoYiApplication
```

启动前按需修改配置 `lcyl-admin/src/main/resources/application-druid.yml`（数据库账号密码）与 `application.yml`（Redis、阿里云 OSS、微信 AppId/Secret）。后端默认端口 `8080`。

### 3. 启动前端

```bash
cd lcyl-vue/lcyl
npm install
npm run dev
```

### 4. 运行小程序

用微信开发者工具导入 `lcyl-wx` 目录，在 `project.config.json` 中替换为你自己的小程序 AppID；后端微信登录所需 AppId/Secret 配置在 `lcyl-code/src/main/java/com/lcyl/code/constant/LoginConstant.java`。

## 配置说明（公开仓库已做脱敏）

以下配置在开源时已替换为占位符，部署前请填入自己的值：

- `application-druid.yml`：数据库密码、Druid 监控台密码
- `application.yml`：阿里云 OSS 的 accessKeyId / accessKeySecret、JWT secret
- `LoginConstant.java`：微信小程序 AppId / Secret
- `lcyl-wx/project.config.json`：小程序 AppID

## 开源许可

本项目基于 [RuoYi-Vue](https://gitee.com/y_project/RuoYi-Vue) 二次开发，遵循 MIT 许可证，详见 [LICENSE](./LICENSE)。
