<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="120px">
      <el-form-item label="预约人姓名" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入预约人姓名" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="预约人电话" prop="phone">
        <el-input v-model="queryParams.phone" placeholder="请输入预约人电话" clearable @keyup.enter="handleQuery" />
      </el-form-item>

      <el-form-item label="预约状态" prop="status">
        <!-- 下拉选择框 -->
        <el-select v-model="queryParams.status" placeholder="请选择预约状态" clearable @keyup.enter="handleQuery">
          <!-- 循环渲染下拉选项 -->
          <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <br>
      <el-form-item label="预约时间" prop="appointmentTime">
        <el-date-picker clearable v-model="queryParams.appointmentTime" type="date" value-format="YYYY-MM-DD HH:mm:ss"
          placeholder="请选择预约时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <hr>
    <div class="type-filter-tag">
      <el-tag v-for="item in typeFilterOptions" :key="item.value"
        :class="['filter-tag', { active: selectedType === item.value }]" @click="handleTypeFilter(item.value)">
        {{ item.label }}
      </el-tag>
    </div>


    <el-table v-loading="loading" :data="reservationList" @selection-change="handleSelectionChange">
      <el-table-column label="序号" align="center" prop="id" />
      <el-table-column label="访问类别" align="center" prop="type">
        <template #default="scope">
          {{ getTypeText(scope.row.type) }}
        </template>
      </el-table-column>
      <el-table-column label="预约人名" align="center" prop="name" />
      <el-table-column label="预约人电话" align="center" prop="phone" />
      <el-table-column label="老人姓名" align="center" prop="olderName" />
      <el-table-column label="预约时间" align="center" prop="appointmentTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.appointmentTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>


      <el-table-column label="创建人" align="center" prop="createPeople" />
      <el-table-column label="状态" align="center" prop="status">
        <template #default="scope">
          <!-- 使用 el-tag 自动显示不同颜色和文字 -->
          <el-tag :type="getStatusTagType(scope.row.status)" disable-transitions>
            {{ getStatusText(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="openVisitDialog(scope.row)"
            v-hasPermi="['code:reservation:visit']" :disabled="[1, 2].includes(scope.row.status)">
            到院
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog title="确认到院时间" v-model="visitDialogVisible" width="400px" :close-on-click-modal="false">
      <el-form ref="visitFormRef" :model="visitForm" label-width="80px">
        <!-- 来访时间选择器（必填） -->
        <el-form-item label="来访时间" prop="visitTime" label-width="70px">
          <el-date-picker v-model="visitForm.visitTime" type="datetime" placeholder="选择来访时间" style="width: 100%;"
            format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss" :disabled-date="disabledPastDate" />
        </el-form-item>
      </el-form>

      <!-- 弹窗底部按钮 -->
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="visitDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmVisit">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改预约来访对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="reservationRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="预约人名" prop="name">
          <el-input v-model="form.name" placeholder="请输入预约人名" />
        </el-form-item>
        <el-form-item label="预约人电话" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入预约人电话" />
        </el-form-item>
        <el-form-item label="老人姓名" prop="olderName">
          <el-input v-model="form.olderName" placeholder="请输入老人姓名" />
        </el-form-item>
        <!-- 访问类别 -->
        <!-- <el-form-item label="访问类别" prop="type">
          <el-select v-model="form.type" placeholder="请选择访问类别" clearable>
            <el-option v-for="item in typeOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item> -->

        <el-form-item label="访问类别">
          <span>{{ getTypeText(form.type) }}</span>
        </el-form-item>

        <el-form-item label="预约时间" prop="appointmentTime">
          <el-date-picker clearable v-model="form.appointmentTime" type="date" value-format="YYYY-MM-DD HH:mm:ss"
            placeholder="请选择预约时间">
          </el-date-picker>
        </el-form-item>

        <!-- 新增：预约状态 下拉框 -->
        <el-form-item label="预约状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择预约状态" clearable>
            <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>

        <el-form-item label="创建人" prop="createPeople">
          <el-input v-model="form.createPeople" placeholder="请输入创建人" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts" name="Config">
import type { LcReservation, ReservationQueryParams } from "@/types/api/code/reservation"
import { listReservation, getReservation, delReservation, addReservation, updateReservation, getTypeOptions,updateArrivalTime } from "@/api/code/reservation"
import { el } from "element-plus/es/locales.mjs"
import { ElMessage } from 'element-plus';

const { proxy } = getCurrentInstance()

const reservationList = ref<LcReservation[]>([])
const open = ref<boolean>(false)
const loading = ref<boolean>(true)
const showSearch = ref<boolean>(true)
const ids = ref<number[]>([])
const single = ref<boolean>(true)
const multiple = ref<boolean>(true)
const total = ref<number>(0)
const title = ref<string>("")

const data = reactive({
  form: {} as LcReservation,
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    type: undefined,
    name: undefined,
    phone: undefined,
    olderName: undefined,
    appointmentTime: undefined,
    createPeople: undefined,
    status: undefined
  } as ReservationQueryParams,
  rules: {
  }
})

// 筛选选项（全部 + 参观预约 + 探访预约）
const typeFilterOptions = ref([
  { label: "全部", value: -1 }, // 全部用-1标识，区分0/1
  { label: "参观预约", value: 0 },
  { label: "探访预约", value: 1 }
])

// 当前选中的类别（默认全部）
const selectedType = ref(-1)

// 点击筛选逻辑
const handleTypeFilter = async (value) => {
  selectedType.value = value; // 更新选中状态
  loading.value = true;

  try {
    let response;
    // 分两种情况：全部 / 按类别筛选
    if (value === -1) {
      // 选「全部」：调用原列表接口（不带type筛选）
      getList();
    } else {
      // 选「参观/探访」：调用后端typeOptions接口（传路径参数value）
      response = await getTypeOptions(value);
      reservationList.value = response.rows;
    }
    // 更新列表数据
    // console.log(response)
    
  } catch (error) {
    console.error('筛选失败：', error);
    ElMessage.error('筛选访问类别失败，请重试');
  } finally {
    loading.value = false;
  }
}

// 确认到院时间
// 确认到访（提交时间）
const confirmVisit = async () => {
  // 表单校验
  if (!visitForm.visitTime) {
    ElMessage.warning('请选择来访时间！');
    return;
  }
  console.log(visitForm.visitTime)

  try {
    // 调用接口提交到访时间（替换为你的实际接口）
    // const res = await updateVisitTime({
    //   id: currentRow.value.id,  // 预约记录ID
    //   visitTime: visitForm.visitTime  // 选中的来访时间
    // });

    updateArrivalTime({
      id: currentRow.value.id,  // 预约记录ID
      visitTime: visitForm.visitTime  // 选中的来访时间
    }).then(res => {
      if (res.code === 200) {
        ElMessage.success('确认到院时间成功！');
        // 刷新数据
        getList();
      } else {
        ElMessage.error(res.msg || '确认到院时间失败，请重试！');
        console.error('提交失败：', res)
      }
    })


    // 关闭弹窗
    visitDialogVisible.value = false;
    // 刷新表格数据（调用你的表格刷新方法）
    // getReservationList();

  } catch (error) {
    ElMessage.error('确认到院时间失败，请重试！');
    console.error('提交失败：', error);
  }
};

const typeOptions = ref([
  { label: "参观预约", value: 0 },
  { label: "探访预约", value: 1 }
])

// 新增：数字转文字方法
const getTypeText = (type) => {
  const item = typeOptions.value.find(item => item.value == type)
  return item ? item.label : "未知"
}

const statusOptions = ref([
  { label: "待上门", value: 0 },
  { label: "已完成", value: 1 },
  { label: "已取消", value: 2 },
  { label: "已过期", value: 3 }
])

// 状态文字映射
const getStatusText = (status) => {
  const map = {
    0: "待上门",
    1: "已完成",
    2: "已取消",
    3: "已过期"
  };
  return map[status] || "未知";
};

// 状态颜色映射（使用 el-tag 内置类型）
const getStatusTagType = (status) => {
  const map = {
    0: "",       // 默认灰色
    1: "success",// 绿色
    2: "info",   // 浅灰色
    3: "warning" // 橙色
  };
  return map[status] || "";
};

const { queryParams, form, rules } = toRefs(data)

// 弹窗显隐状态
const visitDialogVisible = ref(false);
// 当前选中的行数据
const currentRow = ref(null);
// 到访表单数据
const visitForm = reactive({
  visitTime: ''  // 来访时间
});
// 表单校验ref
const visitFormRef = ref(null);

// 打开到访弹窗
const openVisitDialog = (row) => {
  // 记录当前行数据（用于后续接口传参，比如id）
  currentRow.value = row;
  // 重置表单
  visitForm.visitTime = '';
  // 打开弹窗
  visitDialogVisible.value = true;
};

// 可选：禁用过去的时间（只能选当前及未来时间）
const disabledPastDate = (time) => {
  return time.getTime() < Date.now() - 8.64e7; // 8.64e7 = 24*60*60*1000，禁用昨天及之前
};

/** 查询预约来访列表 */
function getList() {
  loading.value = true
  listReservation(queryParams.value).then(response => {
    reservationList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

// 取消按钮
function cancel() {
  open.value = false
  reset()
}

// 表单重置
function reset() {
  form.value = {
    id: null,
    type: null,
    name: null,
    phone: null,
    olderName: null,
    appointmentTime: null,
    createPeople: null,
    createTime: null,
    status: null
  }
  proxy.resetForm("reservationRef")
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef")
  handleQuery()
}

// 多选框选中数据
function handleSelectionChange(selection: LcReservation[]) {
  ids.value = selection.map(item => item.id)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加预约来访"
}

/** 修改按钮操作 */
function handleUpdate(row: LcReservation) {
  reset()
  const _id = row.id || ids.value[0]
  getReservation(_id).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改预约来访"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["reservationRef"].validate((valid: boolean) => {
    if (valid) {
      if (form.value.id != null) {
        updateReservation(form.value).then(() => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addReservation(form.value).then(() => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}

/** 删除按钮操作 */
function handleDelete(row: LcReservation) {
  const _ids = row.id || ids.value
  proxy.$modal.confirm('是否确认删除预约来访编号为"' + _ids + '"的数据项？').then(function () {
    return delReservation(_ids)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => { })
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('code/reservation/export', {
    ...queryParams.value
  }, `reservation_${new Date().getTime()}.xlsx`)
}

// // 转化过期状态
// function checkStatus(){
//   loading.value = true
//   checkTimeStatus().then(() => {
//     getList()
//     proxy.$modal.msgSuccess("转化成功")
//   })
// }

getList()
</script>

<style scoped>
/* 标签栏样式，和示例一致 */
.type-filter-tag {
  display: flex;
  align-items: center;
  gap: 0;
  /* 去掉标签间距，贴合显示 */
  background: #f5f5f5;
  /* 背景浅灰 */
  padding: 2px;
  border-radius: 4px;
}

.filter-tag {
  border: none;
  background: transparent;
  padding: 6px 20px;
  cursor: pointer;
  border-radius: 4px;
  font-size: 14px;
  color: #333;
  transition: all 0.2s;
}

/* 选中状态样式 */
.filter-tag.active {
  background: #fff;
  color: #1989fa;
  /* 选中文字蓝色 */
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

/* 去掉tag默认的hover样式 */
.filter-tag:hover:not(.active) {
  background: #eee;
}
</style>
