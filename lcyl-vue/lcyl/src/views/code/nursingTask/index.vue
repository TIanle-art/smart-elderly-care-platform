<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" inline label-width="100px">
      <el-form-item label="老人姓名">
        <el-input v-model="queryParams.elderName" placeholder="请输入" clearable />
      </el-form-item>

      <el-form-item label="护理员姓名">
        <el-select v-model="queryParams.nurseName" placeholder="请选择" clearable style="width: 200px">
          <el-option
              v-for="nurse in nurseList"
              :key="nurse.id"
              :label="nurse.name"
              :value="nurse.name"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="护理项目">
        <el-select v-model="queryParams.projectName" placeholder="请选择" clearable style="width: 200px">
        </el-select>
      </el-form-item>

      <el-form-item label="期望服务时间">
        <el-date-picker
            v-model="timeRange"
            type="datetimerange"
            range-separator="~"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            value-format="YYYY-MM-DD HH:mm:ss"
            format="YYYY-MM-DD HH:mm:ss"
            style="width: 300px"
        />
      </el-form-item>

      <el-form-item>
        <el-button @click="resetQuery">重置</el-button>
        <el-button type="primary" @click="handleQuery">搜索</el-button>
      </el-form-item>
    </el-form>

    <!-- 状态标签 -->
    <el-tabs v-model="activeStatus" class="mb10">
      <el-tab-pane label="待执行" name="0" />
      <el-tab-pane label="已执行" name="1" />
      <el-tab-pane label="已取消" name="2" />
      <el-tab-pane label="已完成" name="3" />
    </el-tabs>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="dataList" border>
      <el-table-column label="序号" type="index" width="50" align="center" />
      <el-table-column label="老人姓名" prop="elderName" align="center" />
      <el-table-column label="床位号" prop="bedNumber" align="center" />
      <el-table-column label="护理项目名称" prop="projectName" align="center" width="100"/>
      <el-table-column label="项目类型" align="center" width="100">
        <template #default="scope">
          {{ scope.row.itemType === '0' ? '护理计划内' : '护理计划外' }}
        </template>
      </el-table-column>
      <el-table-column label="护理员姓名" prop="nurseName" align="center" />
      <el-table-column label="期望服务时间" prop="expectedServiceTime" width="180" align="center" />
      <el-table-column label="创建人" prop="applicantName" align="center" />
      <el-table-column label="创建时间" prop="createTime" width="180" align="center" />
      <el-table-column label="关联单据" prop="orderNo" align="center" width="200"/>
      <el-table-column label="操作" width="260" align="center">
        <template #default="scope">
          <el-button link type="primary" @click="handleDetail(scope.row)">查看</el-button>
          <el-button link type="danger" @click="openCancelModal(scope.row)" :disabled="scope.row.executeStatus !== '0'">取消</el-button>
          <el-button link type="success" @click="openExecuteModal(scope.row)" :disabled="scope.row.executeStatus !== '0'">执行</el-button>
          <el-button link type="primary" @click="openRescheduleModal(scope.row)" :disabled="scope.row.executeStatus !== '0'">改期</el-button>
          <el-button link type="success" @click="handleComplete(scope.row)" :disabled="scope.row.executeStatus !== '1'">完成</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
        v-show="total>0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="handleQuery"
    />

    <!-- 查看详情弹窗 -->
    <el-dialog v-model="detailVisible" title="护理任务详情" width="800px">
      <div v-loading="detailLoading">
        <div class="detail-group">
          <h3>基本信息</h3>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="老人姓名">{{ detailData.elderName }}</el-descriptions-item>
            <el-descriptions-item label="床位号">{{ detailData.bedNumber }}</el-descriptions-item>
            <el-descriptions-item label="护理项目">{{ detailData.projectName }}</el-descriptions-item>
            <el-descriptions-item label="项目类型">{{ detailData.itemType === '0' ? '计划内' : '计划外' }}</el-descriptions-item>
            <el-descriptions-item label="护理员">{{ detailData.nurseName }}</el-descriptions-item>
            <el-descriptions-item label="期望服务时间">{{ formatDateTime(detailData.expectedServiceTime) }}</el-descriptions-item>
            <el-descriptions-item label="创建人">{{ detailData.applicantName }}</el-descriptions-item>
            <el-descriptions-item label="创建时间">{{ formatDateTime(detailData.createTime) }}</el-descriptions-item>
            <el-descriptions-item label="关联单据">{{ detailData.orderNo }}</el-descriptions-item>
            <el-descriptions-item label="执行状态">
              <el-tag :type="
                detailData.executeStatus === '0' ? 'warning' :
                detailData.executeStatus === '1' ? 'success' :
                detailData.executeStatus === '3' ? 'success' : 'danger'
              ">
                {{
                  detailData.executeStatus === '0' ? '待执行' :
                      detailData.executeStatus === '1' ? '已执行' :
                          detailData.executeStatus === '3' ? '已完成' : '已取消'
                }}
              </el-tag>
            </el-descriptions-item>
          </el-descriptions>
        </div>

        <!-- 已执行 + 已完成 都显示执行记录 -->
        <div class="detail-group" v-if="detailData.executeStatus === '1' || detailData.executeStatus === '3'">
          <h3>执行记录</h3>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="执行人">{{ detailData.executorName }}</el-descriptions-item>
            <el-descriptions-item label="执行时间">{{ detailData.executeTime }}</el-descriptions-item>
            <el-descriptions-item label="执行图片">
              <el-image v-if="detailData.executeImage" :src="detailData.executeImage" style="width:100px;height:100px" />
            </el-descriptions-item>
            <el-descriptions-item label="执行记录">{{ detailData.executeRecord }}</el-descriptions-item>
          </el-descriptions>
        </div>

        <!-- 只有已取消 显示取消记录 -->
        <div class="detail-group" v-if="detailData.executeStatus === '2'">
          <h3>取消记录</h3>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="取消人">{{ detailData.executorName }}</el-descriptions-item>
            <el-descriptions-item label="取消时间">{{ detailData.cancelTime }}</el-descriptions-item>
            <el-descriptions-item label="取消原因" :span="2">{{ detailData.cancelReason }}</el-descriptions-item>
          </el-descriptions>
        </div>
      </div>
    </el-dialog>

    <!-- 取消弹窗 -->
    <el-dialog v-model="cancelVisible" title="取消任务" width="40%">
      <el-form ref="cancelFormRef" :model="cancelForm" label-width="80px" :rules="cancelRules">
        <el-form-item label="取消原因" prop="cancelReason">
          <el-input v-model="cancelForm.cancelReason" type="textarea" rows="3" placeholder="请输入取消原因" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="cancelVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmCancel">确定</el-button>
      </template>
    </el-dialog>

    <!-- 执行弹窗-->
    <el-dialog v-model="executeVisible" title="执行任务" width="500px">
      <el-form ref="executeFormRef" :model="executeForm" label-width="100px" :rules="executeRules">
        <el-form-item label="执行时间" prop="executeTime">
          <el-date-picker v-model="executeForm.executeTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" style="width:100%" />
        </el-form-item>

        <el-form-item label="执行图片" prop="executeImage">
          <el-upload
              class="avatar-uploader"
              action="http://172.16.20.242:8080/upload"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :before-upload="beforeAvatarUpload"
          >
            <el-image v-if="executeForm.executeImage" :src="executeForm.executeImage" fit="cover" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>

        <el-form-item label="执行记录" prop="executeRecord">
          <el-input v-model="executeForm.executeRecord" type="textarea" rows="3" placeholder="请输入执行记录" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="executeVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmExecute">确定</el-button>
      </template>
    </el-dialog>

    <!-- 改期弹窗 -->
    <el-dialog v-model="rescheduleVisible" title="任务改期" width="40%">
      <el-form ref="rescheduleFormRef" :model="rescheduleForm" label-width="120px" :rules="rescheduleRules">
        <el-form-item label="期望服务时间" prop="expectedServiceTime">
          <el-date-picker
              v-model="rescheduleForm.expectedServiceTime"
              type="datetime"
              value-format="YYYY-MM-DD HH:mm:ss"
              :disabled-date="disabledPastDate"
              style="width:100%"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="rescheduleVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmReschedule">确定改期</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts" name="NursingTask">
import { ref, reactive, onMounted, watch } from 'vue'
import { listTask, getTaskDetail, cancelTask, executeTask, rescheduleTask, completedTask } from "@/api/code/nursingTask"
import { listNurse } from "@/api/code/nurse"
import { ElMessage, FormInstance, UploadFile } from 'element-plus'
import { Plus } from "@element-plus/icons-vue";
import dayjs from 'dayjs'

const formatDateTime = (dateStr: string) => {
  if (!dateStr) return '--'
  return dayjs(dateStr).format('YYYY-MM-DD HH:mm:ss')
}
// 禁止选择过去时间
const disabledPastDate = (time: Date) => {
  return time.getTime() < Date.now() - 24 * 60 * 60 * 1000;
};

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  elderName: '',
  nurseName: '',
  projectName: '',
  beginTime: '',
  endTime: '',
  executeStatus: '0'
})
const loading = ref(false)
const dataList = ref([])
const total = ref(0)
const timeRange = ref([])
const activeStatus = ref('0')
const nurseList = ref<any>([])

// 详情弹窗
const detailVisible = ref(false)
const detailLoading = ref(false)
const detailData = reactive({
  id: '',
  elderName: '',
  bedNumber: '',
  projectName: '',
  itemType: '',
  nurseName: '',
  expectedServiceTime: '',
  applicantName: '',
  createTime: '',
  orderNo: '',
  executeStatus: '',
  executorName: '',
  executeTime: '',
  executeImage: '',
  executeRecord: '',
  cancelTime: '',
  cancelReason: ''
})
//取消
const cancelVisible = ref(false)
const cancelFormRef = ref<FormInstance>()
const cancelForm = reactive({ id: null, cancelReason: '' })
const cancelRules = { cancelReason: [{ required: true, message: '请输入取消原因', trigger: 'blur' }] }
// 执行
const executeVisible = ref(false)
const executeFormRef = ref<FormInstance>()
const executeForm = reactive({
  id: null,
  executeTime: new Date().toISOString().slice(0, 19).replace('T', ' '),
  executeImage: '',
  executeRecord: ''
})
const executeRules = {
  executeTime: [{ required: true, trigger: 'change' }],
  executeImage: [{ required: true, trigger: 'change' }],
  executeRecord: [{ required: true, trigger: 'blur' }]
}
//改期
const rescheduleVisible = ref(false)
const rescheduleFormRef = ref<FormInstance>()
const rescheduleForm = reactive({ id: null, expectedServiceTime: '' })
const rescheduleRules = { expectedServiceTime: [{ required: true, trigger: 'change' }] }

// 图片上传
const beforeAvatarUpload = (rawFile: UploadFile) => {
  const isImage = rawFile.type.includes("image/");
  if (!isImage) {
    ElMessage.error("只能上传图片！");
    return false;
  }
  if (rawFile.size / 1024 / 1024 > 2) {
    ElMessage.error("图片大小不能超过 2MB!");
    return false;
  }
  return true;
};

const handleAvatarSuccess = (response: any, uploadFile: UploadFile) => {
  executeForm.executeImage = response.data || response.url;
  ElMessage.success("上传成功");
};

// 切换状态标签自动查询
watch(activeStatus, (val) => {
  queryParams.executeStatus = val
  handleQuery()
})

const handleQuery = async () => {
  queryParams.beginTime = timeRange.value?.[0] || ''
  queryParams.endTime = timeRange.value?.[1] || ''
  loading.value = true
  try {
    const res = await listTask(queryParams)
    dataList.value = res.rows
    total.value = res.total
  } finally {
    loading.value = false
  }
}

const resetQuery = () => {
  queryParams.elderName = ''
  queryParams.nurseName = ''
  queryParams.projectName = ''
  timeRange.value = []
  handleQuery()
}

const getNurseList = async () => {
  try {
    const res = await listNurse({})
    nurseList.value = res.rows
  } catch (e) {
    ElMessage.warning("获取护理员列表失败")
  }
}

const handleDetail = async (row) => {
  detailVisible.value = true
  detailLoading.value = true
  try {
    const res = await getTaskDetail(row.id)
    Object.assign(detailData, res.data)
  } catch (e) {
    ElMessage.error('加载详情失败')
  } finally {
    detailLoading.value = false
  }
}

const openCancelModal = (row) => {
  cancelForm.id = row.id
  cancelForm.cancelReason = ''
  cancelVisible.value = true
}

const confirmCancel = async () => {
  await cancelFormRef.value?.validate()
  try {
    await cancelTask(cancelForm.id, cancelForm.cancelReason)
    ElMessage.success('取消成功')
    cancelVisible.value = false
    handleQuery()
  } catch (e) {
    ElMessage.error('取消失败')
  }
}

const openExecuteModal = (row) => {
  executeForm.id = row.id
  executeForm.executeTime = new Date().toISOString().slice(0, 19).replace('T', ' ')
  executeForm.executeImage = ''
  executeForm.executeRecord = ''
  executeVisible.value = true
}

const confirmExecute = async () => {
  await executeFormRef.value?.validate()
  try {
    await executeTask(executeForm.id, executeForm)
    ElMessage.success('执行成功')
    executeVisible.value = false
    handleQuery()
  } catch (e) {
    ElMessage.error('执行失败')
  }
}

const openRescheduleModal = (row) => {
  rescheduleForm.id = row.id
  rescheduleForm.expectedServiceTime = row.expectedServiceTime
  rescheduleVisible.value = true
}

const confirmReschedule = async () => {
  await rescheduleFormRef.value?.validate()
  try {
    await rescheduleTask(rescheduleForm.id, rescheduleForm)
    ElMessage.success('改期成功')
    rescheduleVisible.value = false
    handleQuery()
  } catch (e) {
    ElMessage.error('改期失败')
  }
}
// 完成任务
const handleComplete = async (row) => {
  try {
    await completedTask(row.id)
    ElMessage.success('任务已完成')
    handleQuery()
  } catch (e) {
    ElMessage.error('操作失败')
  }
}

onMounted(() => {
  getNurseList()
  handleQuery()
})
</script>

<style scoped>
.mb10 {
  margin-bottom: 10px;
}

.detail-group {
  margin-bottom: 20px;
}

.detail-group h3 {
  margin-bottom: 10px;
  font-size: 14px;
  color: #333;
}
.avatar-uploader {
  width: 88px;
  height: 88px;
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar {
  width: 88px;
  height: 88px;
  display: block;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 88px;
  height: 88px;
  line-height: 88px;
  text-align: center;
}
</style>