<template>
    <div class="approval-detail-page">
        <!-- 顶部步骤条 -->
        <el-steps :active="1" finish-status="success" align-center class="steps-container">
            <el-step title="申请退住" />
            <el-step title="申请审批" />
            <el-step title="解除合同" />
            <el-step title="调整账单" />
            <el-step title="账单审批" />
            <el-step title="退住审批" />
            <el-step title="费用清算" />
        </el-steps>

        <!-- 主内容卡片 -->
        <el-card shadow="never">
            <el-row :gutter="20">
                <!-- 左侧：信息展示区 -->
                <el-col :span="16">
                    <!-- 基本信息 -->
                    <div class="info-block">
                        <h3 class="block-title">基本信息</h3>
                        <el-table :data="[{}]" :show-header="false"  style="width: 100%">
                            <el-table-column width="120">
                                <template #default="scope">
                                    <span class="label-text">单据编号</span>
                                </template>
                            </el-table-column>
                            <el-table-column>
                                <template #default="scope">
                                    {{ data.retreatCode }}
                                </template>
                            </el-table-column>
                        </el-table>

                        <el-table :data="[{}]" :show-header="false"  style="width: 100%; margin-top: -1px">
                            <el-table-column width="120">
                                <template #default="scope">
                                    <span class="label-text">老人姓名</span>
                                </template>
                            </el-table-column>
                            <el-table-column>
                                <template #default="scope">
                                    {{ data.oldManName }}
                                </template>
                            </el-table-column>
                        </el-table>

                        <el-table :data="[{}]" :show-header="false"  style="width: 100%; margin-top: -1px">
                            <el-table-column width="120">
                                <template #default="scope">
                                    <span class="label-text">老人身份证号</span>
                                </template>
                            </el-table-column>
                            <el-table-column>
                                <template #default="scope">
                                    {{ data.idCard }}
                                </template>
                            </el-table-column>
                        </el-table>

                        <el-table :data="[{}]" :show-header="false"  style="width: 100%; margin-top: -1px">
                            <el-table-column width="120">
                                <template #default="scope">
                                    <span class="label-text">联系方式</span>
                                </template>
                            </el-table-column>
                            <el-table-column>
                                <template #default="scope">
                                    {{ data.phone }}
                                </template>
                            </el-table-column>
                        </el-table>

                        <el-table :data="[{}]" :show-header="false"  style="width: 100%; margin-top: -1px">
                            <el-table-column width="120">
                                <template #default="scope">
                                    <span class="label-text">护理等级</span>
                                </template>
                            </el-table-column>
                            <el-table-column>
                                <template #default="scope">
                                    {{ data.nursingLevel }}
                                </template>
                            </el-table-column>
                        </el-table>

                        <el-table :data="[{}]" :show-header="false"  style="width: 100%; margin-top: -1px">
                            <el-table-column width="120">
                                <template #default="scope">
                                    <span class="label-text">入住床位</span>
                                </template>
                            </el-table-column>
                            <el-table-column>
                                <template #default="scope">
                                    {{ data.bedNo }}
                                </template>
                            </el-table-column>
                        </el-table>

                      <el-table :data="[{}]" :show-header="false" style="width: 100%; margin-top: -1px">
                        <el-table-column width="120">
                          <template #default="scope">
                            <span class="label-text">签约合同：</span>
                          </template>
                        </el-table-column>
                        <el-table-column>
                          <template #default="scope">
                            <el-link type="primary" @click="previewContract">
                              查看合同
                            </el-link>
                          </template>
                        </el-table-column>
                      </el-table>

                        <el-table :data="[{}]" :show-header="false"  style="width: 100%; margin-top: -1px">
                            <el-table-column width="120">
                                <template #default="scope">
                                    <span class="label-text">养老顾问</span>
                                </template>
                            </el-table-column>
                            <el-table-column>
                                <template #default="scope">
                                    {{ data.counselor }}
                                </template>
                            </el-table-column>
                        </el-table>

                        <el-table :data="[{}]" :show-header="false"  style="width: 100%; margin-top: -1px">
                            <el-table-column width="120">
                                <template #default="scope">
                                    <span class="label-text">护理员</span>
                                </template>
                            </el-table-column>
                            <el-table-column>
                                <template #default="scope">
                                    {{ data.nurse }}
                                </template>
                            </el-table-column>
                        </el-table>
                    </div>

                    <!-- 申请信息 -->
                    <div class="info-block" style="margin-top: 20px">
                        <h3 class="block-title">申请信息</h3>
                        <el-table :data="[{}]" :show-header="false"  style="width: 100%">
                            <el-table-column width="120">
                                <template #default="scope">
                                    <span class="label-text">退住日期</span>
                                </template>
                            </el-table-column>
                            <el-table-column>
                                <template #default="scope">
                                    {{ data.checkoutDate }}
                                </template>
                            </el-table-column>
                        </el-table>

                        <el-table :data="[{}]" :show-header="false"  style="width: 100%; margin-top: -1px">
                            <el-table-column width="120">
                                <template #default="scope">
                                    <span class="label-text">退住原因</span>
                                </template>
                            </el-table-column>
                            <el-table-column>
                                <template #default="scope">
                                    {{ data.reason }}
                                </template>
                            </el-table-column>
                        </el-table>

                        <el-table :data="[{}]" :show-header="false"  style="width: 100%; margin-top: -1px">
                            <el-table-column width="120">
                                <template #default="scope">
                                    <span class="label-text">备注</span>
                                </template>
                            </el-table-column>
                            <el-table-column>
                                <template #default="scope">
                                    {{ data.remark }}
                                </template>
                            </el-table-column>
                        </el-table>

                        <el-table :data="[{}]" :show-header="false"  style="width: 100%; margin-top: -1px">
                            <el-table-column width="120">
                                <template #default="scope">
                                    <span class="label-text">申请人</span>
                                </template>
                            </el-table-column>
                            <el-table-column>
                                <template #default="scope">
                                    {{ data.applicantName }}
                                </template>
                            </el-table-column>
                        </el-table>

                        <el-table :data="[{}]" :show-header="false"  style="width: 100%; margin-top: -1px">
                            <el-table-column width="120">
                                <template #default="scope">
                                    <span class="label-text">申请时间</span>
                                </template>
                            </el-table-column>
                            <el-table-column>
                                <template #default="scope">
                                    {{ data.applyTime }}
                                </template>
                            </el-table-column>
                        </el-table>
                    </div>
                </el-col>

                <!-- 右侧：审批操作区 -->
                <el-col :span="8">
                    <div class="approval-panel">
                        <!-- 审批结果 -->
                        <div class="form-item">
                            <span class="label">*审批结果</span>
                            <el-radio-group v-model="form.approvalResult" label="number">
                                <el-radio label=1>审批通过</el-radio>
                                <el-radio label=2>驳回</el-radio>
                            </el-radio-group>
                        </div>

                        <!-- 审批意见 -->
                        <div class="form-item">
                            <span class="label">*审批意见</span>
                            <el-input v-model="form.approvalOpinion" type="textarea" :rows="3" placeholder="请输入审批意见"
                                maxlength="200" show-word-limit />
                        </div>

                        <!-- 操作记录 -->
                        <div class="log-card">
                            <h4 class="log-title">操作记录</h4>
                            <div class="log-timeline">
                                <div class="log-item">
                                    <div class="dot done">
                                        <el-icon size="12">
                                            <Check />
                                        </el-icon>
                                    </div>
                                    <div class="log-content">
                                        <p>发起申请-申请退住</p>
                                        <span class="operator">{{ data.applicantName }}(已发起)</span>
                                        <span class="time">{{ data.applyTime }}</span>
                                    </div>
                                </div>
                                <div class="log-item">
                                    <div class="dot processing">
                                        <el-icon size="12">
                                            <Clock />
                                        </el-icon>
                                    </div>
                                    <div class="log-content">
                                        <p>{{data.role}}审批-申请审批</p>
                                        <span class="operator">{{ userUser.nickName }}(审批中)</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </el-col>
            </el-row>

            <!-- 底部按钮 -->
            <div class="btn-group">
                <el-button @click="handleBack">返回</el-button>
                <el-button type="primary" @click="handleSubmit">提交</el-button>
            </div>
        </el-card>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Check, Clock } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { useRoute } from 'vue-router';
import {completeTask, elderDetail, getCheckout} from "@/api/code/checkout.ts";
import useUserStore from "@/store/modules/user"


const router = useRouter()
const route = useRoute();
const taskId = route.query.taskId;
const bussinessId = route.query.businessId;
const userUser = useUserStore()

console.log('接收到的 taskId:', taskId);
// 1. 表单数据模型
const form = reactive({
    approvalResult: 1, // 默认选中审批通过
    approvalOpinion: ''
})

// 使用 reactive 声明响应式对象
const data = reactive({
  retreatCode: '',
  oldManName: '',
  idCard: '',
  phone: '',
  nursingLevel: '',
  bedNo: '',
  contractNo: '',
  counselor: '',
  nurse: '',
  checkoutDate: '',
  reason: '',
  remark: '',
  applicant: '',
  applyTime: '',
  applicantName:''
});

// 或者在模板中使用时，如果 data 是 ref，则需要 data.value.billNo


// 3. 生命周期钩子 - 实际项目中在这里调用接口获取数据
onMounted(() => {
  // fetchData() // 示例：调用 API 获取数据库数据
  getCheckout(bussinessId).then(resp => {
    console.log(resp.data);
    if (resp.data) {
      data.retreatCode = resp.data.retreatCode;
      data.oldManName = resp.data.name;
      data.idCard = resp.data.idCardNo;
      data.phone = resp.data.phone;
      data.nursingLevel = resp.data.nursingLevelName;
      data.bedNo = resp.data.bedNo;
      data.contractNo = resp.data.contractUrl;
      data.counselor = resp.data.nursingName;
      data.nurse = resp.data.counselor;
      data.checkoutDate = resp.data.checkOutTime;
      data.reason = resp.data.reason;
      data.remark = resp.data.remark;
      data.applicant = resp.data.applicatId;
      data.applicantName = resp.data.applicatName;
      data.applyTime = resp.data.createTime;
    } else {
      data.retreatCode = '';
      data.oldManName = '';
      data.idCard = '';
      data.phone = '';
      data.nursingLevel = '';
      data.bedNo = '';
      data.contractNo = '';
      data.counselor = '';
      data.nurse = '';
      data.checkoutDate = '';
      data.reason = '';
      data.remark = '';
      data. applicant = '';
      data.applyTime = '';
    }
  })
})

// 4. 方法区
// 预览合同（示例）
const previewContract = () => {
  const url = data.contractNo; // URL: 'https://yjy-slwl-oss.oss-cn-hangzhou.aliyuncs.com/f9f4bd37-60a2-4bcb-bb68-f0caa74c06ef.pdf'

  if (url) {
    // 在新标签页打开 PDF
    window.open(url, '_blank');
  } else {
    ElMessage.warning('合同文件不存在');
  }
}

// 返回
const handleBack = () => {
    router.back()
}


// 提交审批
const handleSubmit = () => {
  ElMessageBox.confirm('确认提交该审批申请？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    // 这里写接口请求逻辑
    console.log('提交的数据：', form)
    const dto = {
      taskId:taskId,
      action:form.approvalResult,
      opinion:form.approvalOpinion,
      businessData:{
        updateBy:userUser.nickName
      }
    }
    console.log(dto)
    completeTask(dto).then(resp=>{
      console.log(resp.data)
      router.push({
        path: '/retreat/checkout',
      });
    })
    ElMessage.success('审批提交成功！')
  }).catch(() => {
    ElMessage.info('已取消提交')
  })
}

</script>

<style scoped>
.approval-detail-page {
    padding: 20px;
    background: #f5f7fa;
    min-height: 100vh;
}

.steps-container {
    margin-bottom: 20px;
}

/* 信息卡片通用样式 */
.info-section {
    margin-bottom: 20px;
    --el-descriptions-label-bg-color: #f5f7fa;
}

/* 右侧审批面板 */
.approval-panel {
    background: #fff;
    padding: 0;
}

.form-item {
    margin-bottom: 20px;
}

.form-item .label {
    display: block;
    margin-bottom: 8px;
    font-weight: 500;
}

/* 操作记录样式 */
.log-card {
    background: #f2f3f5;
    padding: 16px;
    border-radius: 4px;
    margin-top: 20px;
}

.log-title {
    font-size: 14px;
    font-weight: 600;
    margin-bottom: 16px;
}

.log-timeline {
    position: relative;
    padding-left: 20px;
}

.log-timeline::before {
    content: '';
    position: absolute;
    left: 8px;
    top: 0;
    bottom: 0;
    width: 2px;
    background: #e5e6eb;
}

.log-item {
    position: relative;
    margin-bottom: 16px;
    display: flex;
    align-items: flex-start;
}

.dot {
    position: absolute;
    left: -20px;
    top: 4px;
    width: 16px;
    height: 16px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #fff;
    z-index: 1;
}

.dot.done {
    background: #67c23a;
}

.dot.processing {
    background: #409eff;
}

.log-content {
    flex: 1;
}

.log-content p {
    margin: 0 0 4px 0;
    font-size: 14px;
}

.operator {
    font-size: 12px;
    color: #666;
    margin-right: 10px;
}

.time {
    font-size: 12px;
    color: #999;
}

/* 底部按钮 */
.btn-group {
    text-align: center;
    margin-top: 30px;
}
</style>