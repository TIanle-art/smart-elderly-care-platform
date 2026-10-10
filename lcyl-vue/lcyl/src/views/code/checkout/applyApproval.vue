<template>
  <div class="approval-page">
    <!-- 顶部步骤条 -->
    <el-steps :active="1" finish-status="success" align-center>
      <el-step title="申请退住" />
      <el-step title="申请审批" />
      <el-step title="解除合同" />
      <el-step title="调整账单" />
      <el-step title="账单审批" />
      <el-step title="退住审批" />
      <el-step title="费用清算" />
    </el-steps>

    <!-- 审批结果卡片 -->
    <el-card class="result-card" shadow="never">
      <div class="section-title">
        <span class="text">审批结果</span>
      </div>

      <!-- 审批中状态 -->
      <div class="approval-status">
        <el-icon class="status-icon" size="48">
          <WarningFilled />
        </el-icon>
        <h3 class="status-text">审批中</h3>
        <p class="status-tip">稍等，退住申请单正在审批中，若该申请单长时间未处理，请联系（审批角色）！</p>
      </div>

      <!-- 操作记录 -->
      <div class="operation-log">
        <h4 class="log-title">操作记录</h4>
        <div class="log-list">
          <div class="log-item">
            <div class="log-dot done">
              <el-icon><Check /></el-icon>
            </div>
            <div class="log-content">
              <p class="log-desc">发起申请-申请退住<br><span class="log-operator">顾廷烨（已发起）</span></p>
              <span class="log-time">2048-10-15 09:00:00</span>
            </div>
          </div>
          <div class="log-item">
            <div class="log-dot processing">
              <el-icon><Clock /></el-icon>
            </div>
            <div class="log-content">
              <p class="log-desc">（角色）审批-申请审批<br><span class="log-operator">盛明兰（审批中）</span></p>
            </div>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 底部按钮 -->
    <div class="btn-bottom">
      <el-button size="default" @click="handleBack">返回</el-button>
    </div>
  </div>
</template>

<script setup>
import { WarningFilled, Check, Clock } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// 返回上一页
const handleBack = () => {
  router.push('/inout/retreat/checkout')
}
</script>

<style scoped>
.approval-page {
  padding: 20px;
  background: #fff;
  min-height: 100vh;
}

/* 步骤条样式 */
.el-steps {
  margin-bottom: 20px;
}

/* 卡片样式 */
.result-card {
  border: 1px solid #e5e6eb;
  border-radius: 4px;
}

/* 模块标题 */
.section-title {
  display: flex;
  align-items: center;
  margin-bottom: 40px;
  font-size: 18px;
  font-weight: 500;
}
.section-title .badge {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  background-color: #e54d42;
  color: #fff;
  font-size: 12px;
  margin-right: 8px;
}
.section-title .text {
  color: #333;
}

/* 审批中状态 */
.approval-status {
  text-align: center;
  margin-bottom: 30px;
}
.status-icon {
  color: #e6a23c;
  margin-bottom: 16px;
}
.status-text {
  font-size: 20px;
  color: #333;
  margin: 0 0 12px;
}
.status-tip {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
}

/* 操作记录 */
.operation-log {
  max-width: 480px;
  margin: 0 auto;
  background: #f2f2f2;
  padding: 20px;
  border-radius: 4px;
}
.log-title {
  font-size: 14px;
  font-weight: 500;
  color: #333;
  margin: 0 0 20px;
}
.log-list {
  position: relative;
}
.log-list::before {
  content: '';
  position: absolute;
  left: 10px;
  top: 24px;
  bottom: 0;
  width: 2px;
  background-color: #ccc;
}
.log-item {
  display: flex;
  margin-bottom: 24px;
  position: relative;
}
.log-item:last-child {
  margin-bottom: 0;
}
.log-dot {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  z-index: 1;
}
.log-dot.done {
  background-color: #67c23a;
}
.log-dot.processing {
  background-color: #409eff;
}
.log-content {
  flex: 1;
  margin-left: 12px;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}
.log-desc {
  font-size: 14px;
  color: #333;
  line-height: 1.6;
  margin: 0;
}
.log-operator {
  font-size: 12px;
  color: #666;
}
.log-time {
  font-size: 12px;
  color: #999;
  white-space: nowrap;
}

/* 底部按钮 */
.btn-bottom {
  text-align: center;
  margin-top: 30px;
}
</style>