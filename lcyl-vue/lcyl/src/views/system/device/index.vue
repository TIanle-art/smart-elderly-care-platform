<template>
  <div class="app-container">
    <!-- 顶部搜索栏：所属产品下拉 + 新增设备按钮 -->
    <el-form :model="queryParams" ref="queryRef" :inline="true" label-width="80px" class="search-bar">
      <el-form-item label="所属产品">
        <el-select
            v-model="queryParams.productName"
            placeholder="请选择所属产品"
            clearable
            style="width: 220px"
            @change="handleQuery"
        >
          <el-option label="智能烟感" value="智能烟感" />
          <el-option label="紧急呼叫器" value="紧急呼叫器" />
          <el-option label="智能床垫" value="智能床垫" />
          <el-option label="智能网关" value="智能网关" />
          <el-option label="门磁设备" value="门磁设备" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleAdd" class="add-btn">
          新增设备
        </el-button>
      </el-form-item>
    </el-form>

    <!-- 设备列表 -->
    <el-table v-loading="loading" :data="deviceList" border :header-cell-style="{background: '#F5F7FA'}">
      <el-table-column label="序号" align="center" width="60">
        <template #default="scope">
          {{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column label="设备名称" align="center" prop="deviceName" />
      <el-table-column label="备注名称" align="center" prop="remarkName" />
      <el-table-column label="所属产品" align="center" prop="productName" />
      <el-table-column label="接入位置" align="center" prop="accessLocation" />
      <el-table-column label="节点类型" align="center" prop="nodeType" />
      <el-table-column label="创建人" align="center" prop="creator" />
      <el-table-column label="创建时间" align="center" width="180">
        <template #default="scope">
          {{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="180">
        <template #default="scope">
          <el-button link type="danger" @click="handleDelete(scope.row)" v-hasPermi="['system:device:remove']">
            删除
          </el-button>
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" style="margin-left:10px" v-hasPermi="['system:device:edit']">
            编辑
          </el-button>
          <el-button link type="primary" @click="handleView(scope.row)" style="margin-left:10px">
            查看
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
        v-show="total>0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
    />

    <!-- 添加/修改/查看 对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="deviceRef" :model="form" :rules="rules" label-width="100px" size="default">
        <el-form-item label="设备名称" prop="deviceName">
          <el-input v-model="form.deviceName" placeholder="请输入设备名称" :readonly="isView" />
        </el-form-item>
        <el-form-item label="备注名称" prop="remarkName">
          <el-input v-model="form.remarkName" placeholder="请输入备注名称" :readonly="isView" />
        </el-form-item>
        <el-form-item label="所属产品" prop="productName">
          <el-select v-model="form.productName" placeholder="请选择所属产品" :disabled="isView" style="width:100%">
            <el-option label="智能烟感" value="智能烟感" />
            <el-option label="紧急呼叫器" value="紧急呼叫器" />
            <el-option label="智能床垫" value="智能床垫" />
            <el-option label="智能网关" value="智能网关" />
            <el-option label="门磁设备" value="门磁设备" />
          </el-select>
        </el-form-item>
        <el-form-item label="接入位置" prop="accessLocation">
          <el-input v-model="form.accessLocation" placeholder="请输入接入位置（如：101房间/1011床位）" :readonly="isView" />
        </el-form-item>
        <el-form-item label="节点类型" prop="nodeType">
          <el-input v-model="form.nodeType" placeholder="默认：设备" :readonly="true" />
        </el-form-item>
        <el-form-item label="创建人" prop="creator">
          <el-input v-model="form.creator" placeholder="请输入创建人" :readonly="isView" />
        </el-form-item>
        <el-form-item label="创建时间" v-if="isView">
          <span>{{ parseTime(form.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注" :readonly="isView" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button v-if="!isView" type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">返 回</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts" name="Device">
import type { Device, DeviceQueryParams } from "@/types/api/system/device"
import { listDevice, getDevice, delDevice, addDevice, updateDevice } from "@/api/system/device"

const { proxy } = getCurrentInstance() as any

const deviceList = ref<Device[]>([])
const open = ref<boolean>(false)
const loading = ref<boolean>(true)
const total = ref<number>(0)
const title = ref<string>("")
const isView = ref<boolean>(false)

const data = reactive({
  form: {} as Device,
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    productName: undefined,
  } as DeviceQueryParams,
  rules: {
    deviceName: [
      { required: true, message: "设备名称不能为空", trigger: "blur" }
    ],
    accessLocation: [
      { required: true, message: "接入位置不能为空", trigger: "blur" }
    ],
    nodeType: [
      { required: true, message: "节点类型不能为空", trigger: "change" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)

// 时间格式化工具（和你项目保持一致）
function parseTime(time: string, format: string) {
  return proxy.parseTime(time, format)
}

/** 查询设备列表 */
function getList() {
  loading.value = true
  listDevice(queryParams.value).then(response => {
    deviceList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

// 取消按钮
function cancel() {
  open.value = false
  reset()
  isView.value = false
}

// 表单重置
function reset() {
  form.value = {
    id: null,
    deviceName: null,
    remarkName: null,
    productId: null,
    productName: null,
    accessLocation: null,
    nodeType: "设备", // 默认值
    creator: null,
    createTime: null,
    updateTime: null,
    remark: null
  }
  proxy.resetForm("deviceRef")
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作（可选，你截图里没显示，我先注释） */
// function resetQuery() {
//   proxy.resetForm("queryRef")
//   handleQuery()
// }

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "新增设备"
  isView.value = false
}

/** 修改按钮操作 */
function handleUpdate(row: Device) {
  reset()
  const _id = row.id
  getDevice(_id).then(response => {
    form.value = response.data
    open.value = true
    title.value = "编辑设备"
    isView.value = false
  })
}

/** 查看按钮操作 */
function handleView(row: Device) {
  reset()
  const _id = row.id
  getDevice(_id).then(response => {
    form.value = response.data
    open.value = true
    title.value = "查看设备详情"
    isView.value = true
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["deviceRef"].validate((valid: boolean) => {
    if (valid) {
      if (form.value.id != null) {
        updateDevice(form.value).then(() => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addDevice(form.value).then(() => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}

/** 删除按钮操作 */
function handleDelete(row: Device) {
  const _id = row.id
  proxy.$modal.confirm('是否确认删除设备 "' + row.deviceName + '"？').then(function() {
    return delDevice(_id)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

getList()
</script>

<style scoped>
.search-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}
.add-btn {
  background-color: #0066FF;
  border: none;
  padding: 10px 20px;
}
</style>