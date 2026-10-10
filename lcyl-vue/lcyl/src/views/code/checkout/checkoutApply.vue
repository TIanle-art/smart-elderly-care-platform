<template>
  <div class="checkout-apply-wrapper">
    <!-- 顶部步骤条 -->
    <el-steps
        style="max-width: 100%"
        :space="200"
        :active="0"
        finish-status="success"
        align-center
    >
      <el-step title="申请退住" />
      <el-step title="申请审批" />
      <el-step title="解除合同" />
      <el-step title="调整账单" />
      <el-step title="账单审批" />
      <el-step title="退住审批" />
      <el-step title="钱款清算" />
    </el-steps>

    <!-- 表单卡片 -->
    <el-card class="form-card" shadow="hover">
      <div class="form-header">
        <span class="title-text">填写申请信息</span>
      </div>

      <el-form
          ref="formRef"
          :model="form"
          :rules="rules"
          label-width="120px"
          label-position="right"
          class="apply-form"
      >
        <!-- 基本信息模块 -->
        <div class="module-title">
          <span class="number">1</span>
          <span class="text">基本信息</span>
        </div>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="老人姓名" prop="elderId">
              <el-select
                  v-model="form.elderId"
                  filterable
                  clearable
                  placeholder="请选择老人"
                  style="width: 300px"
                  @change="handleElderChange"
              >
                <el-option
                    v-for="item in elderList"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="老人身份证号">
              <el-input v-model="form.idCard" disabled placeholder="选择老人后自动带出" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="联系方式">
              <el-input v-model="form.phone" disabled placeholder="选择老人后自动带出" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="护理等级">
              <el-input v-model="form.nurseLevel" disabled placeholder="由系统自动获取" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="入住床位">
              <el-input v-model="form.bedNo" disabled placeholder="由系统自动获取" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="签约合同">
              <el-input v-model="form.contractNo" disabled placeholder="由系统自动获取" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="养老顾问">
              <el-input v-model="form.counselor" disabled placeholder="由系统自动获取" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="护理员">
              <el-input v-model="form.nurse" disabled placeholder="由系统自动获取" />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 退住信息模块 -->
        <div class="module-title">
          <span class="number">2</span>
          <span class="text">退住信息</span>
        </div>

        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="申请退住日期" prop="checkoutDate">
              <el-date-picker
                  v-model="form.checkoutDate"
                  type="date"
                  placeholder="选择退住日期"
                  style="width: 300px"
                  value-format="YYYY-MM-DD"
              />
            </el-form-item>
          </el-col>


          <el-col :span="24">
            <el-form-item label="退住原因" prop="reason">
              <el-select
                  v-model="form.reason"
                  placeholder="请选择退住原因"
                  style="width: 300px"
              >
                <el-option label="老人个人原因" value="老人个人原因" />
                <el-option label="护理质量不高" value="护理质量不高" />
                <el-option label="服务不周" value="服务不周" />
                <el-option label="管理混乱" value="管理混乱" />
                <el-option label="费用高昂" value="费用高昂" />
                <el-option label="违规退住" value="违规退住" />
                <el-option label="其他" value="其他" />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="备注说明">
              <el-input
                  v-model="form.remark"
                  type="textarea"
                  rows="3"
                  placeholder="请输入备注信息"
                  style="width: 300px"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 底部按钮 -->
        <div class="btn-box">
          <el-button @click="onReset">重置</el-button>
          <el-button type="primary" @click="onSubmit">提交申请</el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { useRouter } from 'vue-router';
import { getAllElder } from '@/api/code/elder';
import { startApplication, elderDetail } from '@/api/code/checkout';
import type { Elder } from '@/types/api/code/elder';
import type { RetreatStartDTO } from '@/types/api/code/checkout';

const router = useRouter();
const formRef = ref();
const elderList = ref<Elder[]>([]);

// 表单数据
const form = reactive({
  elderId: null as number | null,
  oldName: '',
  idCard: '',
  phone: '',
  nurseLevel: '',
  bedNo: '',
  contractNo: '',
  counselor: '',
  nurse: '',
  checkoutDate: '',
  reason: '', // 下拉选择
  remark: ''
});

// 表单校验规则
const rules = reactive({
  elderId: [{ required: true, message: '请选择老人', trigger: 'change' }],
  checkoutDate: [{ required: true, message: '请选择退住日期', trigger: 'change' }],
  reason: [{ required: true, message: '请选择退住原因', trigger: 'change' }]
});

// 加载老人列表
const loadElderList = async () => {
  const res = await getAllElder();
  if (res.code === 200) {
    console.log('老人数据：', res.data);

    // 只筛选出 status = 1 的数据赋值给列表
    elderList.value = (res.data || []).filter(item => item.status == 1);
  } else {
    ElMessage.error('获取老人列表失败');
  }
};

// 选择老人
const handleElderChange = async (elderId: number) => {
  const elder = elderList.value.find(e => e.id === elderId);
  if (elder) {
    form.oldName = elder.name;
    form.idCard = elder.idCardNo || '';
    form.phone = elder.phone || '';
  } else {
    form.oldName = '';
    form.idCard = '';
    form.phone = '';
  }

  const resp = await elderDetail(elderId);
  if (resp.data) {
    form.bedNo = resp.data.bedNo;
    form.contractNo = resp.data.contract;
    form.counselor = resp.data.counselor;
    form.nurse = resp.data.nurseNames?.[0] || '';
    form.nurseLevel = resp.data.nursingLevel;
  } else {
    form.bedNo = '';
    form.contractNo = '';
    form.counselor = '';
    form.nurse = '';
    form.nurseLevel = '';
  }
};

// 提交
const onSubmit = async () => {
  await formRef.value?.validate();
  if (!form.elderId) {
    ElMessage.warning('请选择老人');
    return;
  }

  const dto: RetreatStartDTO = {
    elderId: form.elderId,
    checkOutTime: form.checkoutDate,
    reason: form.reason,
    remark: form.remark
  };

  try {
    const res = await startApplication(dto);
    if (res.code === 200) {
      ElMessage.success('提交成功！');
      router.push('/retreat/checkout');
    } else {
      ElMessage.error(res.msg || '提交失败');
    }
  } catch (error: any) {
    ElMessage.error(error?.message || '提交失败');
  }
};

// 重置
const onReset = () => {
  formRef.value?.resetFields();
  form.elderId = null;
  form.oldName = '';
  form.idCard = '';
  form.phone = '';
  form.nurseLevel = '';
  form.bedNo = '';
  form.contractNo = '';
  form.counselor = '';
  form.nurse = '';
  form.checkoutDate = '';
  form.reason = '';
  form.remark = '';
};

onMounted(() => {
  loadElderList();
});
</script>

<style scoped>
.checkout-apply-wrapper {
  padding: 20px;
  background: #f5f7fa;
  min-height: 100vh;
}

.form-card {
  border-radius: 8px;
  overflow: hidden;
  margin-top: 20px;
}

.form-header {
  padding: 15px 20px;
  border-bottom: 1px solid #eee;
  margin-bottom: 20px;
}

.title-text {
  font-size: 18px;
  font-weight: 500;
  color: #333;
}

.module-title {
  display: flex;
  align-items: center;
  margin: 25px 0 15px;
  font-size: 16px;
  font-weight: 500;
}

.module-title .number {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 22px;
  height: 22px;
  border-radius: 50%;
  background-color: #1677ff;
  color: #fff;
  font-size: 14px;
  margin-right: 8px;
}

.module-title .text {
  color: #333;
}

.btn-box {
  text-align: center;
  margin-top: 30px;
}
</style>