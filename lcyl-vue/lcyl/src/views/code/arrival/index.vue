<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="120px">
      <el-form-item label="来访人姓名" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入来访人姓名" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="来访人手机号" prop="phone">
        <el-input v-model="queryParams.phone" placeholder="请输入来访人手机号" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="来访时间" prop="visitTime">
        <el-date-picker clearable v-model="queryParams.visitTime" type="date" value-format="YYYY-MM-DD"
          placeholder="请选择来访时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <div class="type-filter-container">
      <div class="type-filter-tag">
        <el-tag v-for="item in typeFilterOptions" :key="item.value"
          :class="['filter-tag', { active: selectedType === item.value }]" @click="handleTypeFilter(item.value)">
          {{ item.label }}
        </el-tag>
      </div>
      <div class="button_add">
        <el-button type="primary" plain icon="Plus" @click="handleAdd" class="add-btn" v-hasPermi="['code:arrival:add']">
          新增
        </el-button>
      </div>
    </div>

    <el-table v-loading="loading" :data="arrivalList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" prop="id" />
      <el-table-column label="访问类别" align="center" prop="type">
        <template #default="scope">
          {{ getTypeText(scope.row.type) }}
        </template>
      </el-table-column>
      <el-table-column label="来访人姓名" align="center" prop="name" />
      <el-table-column label="来访人手机号" align="center" prop="phone" />
      <el-table-column label="老人姓名" align="center" prop="elderName" />
      <el-table-column label="来访时间" align="center" prop="visitTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.visitTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建人" align="center" prop="createPerson" />
      <el-table-column label="创建时间" align="center" prop="createTime">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改来访登记对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="arrivalRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="来访人姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入来访人姓名" />
        </el-form-item>
        <el-form-item label="访问类别" prop="type">
          <el-select v-model="form.type" placeholder="请选择访问类别" clearable>
            <el-option v-for="item in typeOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="来访人手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入来访人手机号" />
        </el-form-item>
        <el-form-item label="老人姓名" prop="elderName">
          <!-- <el-input v-model="form.elderName" placeholder="请输入老人姓名" /> -->
          <el-select v-model="form.elderName" placeholder="请选择老人姓名" clearable>
            <el-option v-for="item in olderList" :key="item.id" :label="item.name" :value="item.name" />
          </el-select>
        </el-form-item>
        <el-form-item label="来访时间" prop="visitTime">
          <el-date-picker clearable v-model="form.visitTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss"
            placeholder="请选择来访时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="创建人" prop="createPerson">
          <el-input v-model="form.createPerson" placeholder="请输入创建人" />
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
import type { Arrival, ArrivalQueryParams } from "@/types/api/code/arrival"
import { listArrival, getArrival, delArrival, addArrival, updateArrival, getTypeOptions ,getOlderList} from "@/api/code/arrival"
import { onMounted } from "vue"

const { proxy } = getCurrentInstance()

const arrivalList = ref<Arrival[]>([])
const open = ref<boolean>(false)
const loading = ref<boolean>(true)
const showSearch = ref<boolean>(true)
const ids = ref<number[]>([])
const single = ref<boolean>(true)
const multiple = ref<boolean>(true)
const total = ref<number>(0)
const title = ref<string>("")
  const olderList = ref<Arrival[]>([])

const data = reactive({
  form: {} as Arrival,
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    type: undefined,
    name: undefined,
    phone: undefined,
    elderName: undefined,
    visitTime: undefined,
    createPerson: undefined,
  } as ArrivalQueryParams,
  rules: {
  }
})

const { queryParams, form, rules } = toRefs(data)


const typeOptions = ref([
  { label: "参观预约", value: 0 },
  { label: "探访预约", value: 1 }
])

// 筛选选项（全部 + 参观预约 + 探访预约）
const typeFilterOptions = ref([
  { label: "全部", value: -1 }, // 全部用-1标识，区分0/1
  { label: "参观预约", value: 0 },
  { label: "探访预约", value: 1 }
])

// 当前选中的类别（默认全部）
const selectedType = ref(-1)

// 新增：数字转文字方法
const getTypeText = (type) => {
  const item = typeOptions.value.find(item => item.value == type)
  return item ? item.label : "未知"
}

/** 查询来访登记列表 */
function getList() {
  loading.value = true
  listArrival(queryParams.value).then(response => {
    arrivalList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

// 取消按钮
function cancel() {
  open.value = false
  reset()
}

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
      arrivalList.value = response.rows;
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

// 表单重置
function reset() {
  form.value = {
    id: null,
    type: null,
    name: null,
    phone: null,
    elderName: null,
    visitTime: null,
    createPerson: null,
    createTime: null
  }
  proxy.resetForm("arrivalRef")
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
function handleSelectionChange(selection: Arrival[]) {
  ids.value = selection.map(item => item.id)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加来访登记"
}

/** 修改按钮操作 */
function handleUpdate(row: Arrival) {
  reset()
  const _id = row.id || ids.value[0]
  getArrival(_id).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改来访登记"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["arrivalRef"].validate((valid: boolean) => {
    if (valid) {
      if (form.value.id != null) {
        updateArrival(form.value).then(() => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addArrival(form.value).then(() => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}

/** 删除按钮操作 */
function handleDelete(row: Arrival) {
  const _ids = row.id || ids.value
  proxy.$modal.confirm('是否确认删除来访登记编号为"' + _ids + '"的数据项？').then(function () {
    return delArrival(_ids)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => { })
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('code/arrival/export', {
    ...queryParams.value
  }, `arrival_${new Date().getTime()}.xlsx`)
}

function getOlder() {
  getOlderList(queryParams.value).then(response => {
    olderList.value = response.data
  })
 
}

onMounted(() => {
  getList()
  getOlder()
})

</script>

<style scoped lang="scss">
/* 标签栏容器，使用flex布局实现左右分布 */
.type-filter-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 2px;
  border-radius: 4px;
}

/* 左侧标签组样式 */
.type-filter-tag {
  display: flex;
  align-items: center;
  gap: 0;
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
  background: #f5f5f5;
  color: #1989fa;
  /* 选中文字蓝色 */
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

/* 去掉tag默认的hover样式 */
.filter-tag:hover:not(.active) {
  background: #eee;
}

.button_add {
  flex-shrink: 0;
}
</style>