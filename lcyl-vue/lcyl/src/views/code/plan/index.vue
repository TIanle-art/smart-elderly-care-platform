<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="护理计划名称" prop="planName">
        <el-input
            v-model="queryParams.planName"
            placeholder="请输入"
            clearable
            @keyup.enter="handleQuery"
            maxlength="10"
            show-word-limit
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择" clearable>
          <el-option
              v-for="dict in sys_normal_disable"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
            type="primary"
            plain
            icon="Plus"
            @click="handleAdd"
            v-hasPermi="['nursingPlan:plan:add']"
        >新增护理计划</el-button>
      </el-col>

      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="planList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" prop="id" />
      <el-table-column label="护理计划名称" align="center" prop="planName" />
      <el-table-column label="创建人" align="center" prop="creatorName" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status">
        <template #default="scope">
          <el-tag :type="scope.row.status === 0 ? 'success' : 'danger'">
            {{ scope.row.status === 0 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button
              link
              type="info"
              @click="handleView(scope.row)"
              v-hasPermi="['nursingPlan:plan:view']"
          >查看</el-button>

          <el-button
              link
              type="primary"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['nursingPlan:plan:edit']"
              :disabled="scope.row.isBound === 1"
              :title="scope.row.isBound === 1 ? '该护理计划已绑定护理等级，不可编辑' : ''"
          >修改</el-button>

          <el-button
              link
              type="danger"
              @click="handleDelete(scope.row)"
              v-hasPermi="['nursingPlan:plan:remove']"
              :disabled="scope.row.isBound === 1"
              :title="scope.row.isBound === 1 ? '该护理计划已绑定护理等级，不可删除' : ''"
          >删除</el-button>

          <el-button
              link
              :type="scope.row.status === 0 ? 'danger' : 'primary'"
              @click="handleChangeStatus(scope.row)"
              v-hasPermi="['nursingPlan:plan:edit']"
              :disabled="scope.row.isBound === 1"
              :title="scope.row.isBound === 1 ? '该护理计划已绑定护理等级，不可切换状态' : ''"
          >
            {{ scope.row.status === 0 ? '禁用' : '启用' }}
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

    <!-- 新增/修改护理计划对话框 -->
    <el-dialog :title="title" v-model="open" width="800px" append-to-body>
      <el-form ref="planRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="护理计划名称" prop="planName">
          <el-input
              v-model="form.planName"
              placeholder="请输入"
              maxlength="10"
              show-word-limit
          />
        </el-form-item>

        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
                v-for="dict in sys_normal_disable"
                :key="dict.value"
                :label="parseInt(dict.value)"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-divider content-position="center">护理项目</el-divider>
        <!-- 子表格 -->
        <el-table :data="nursingPlanItemList" border style="width: 100%;">
          <el-table-column label="护理项目名称" width="180">
            <template #default="{ row, $index }">
              <el-select
                  v-model="row.itemName"
                  placeholder="请选择"
                  style="width: 100%;"
                  @change="handleItemNameChange"
              >
                <el-option
                    v-for="item in getAvailableItems($index)"
                    :key="item.id"
                    :label="item.itemName"
                    :value="item.itemName"
                />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="期望服务时间" width="130">
            <template #default="{ row }">
              <el-time-picker
                  v-model="row.expectedTime"
                  format="HH:mm"
                  value-format="HH:mm"
                  placeholder="选择时间"
                  default-value="08:00"
                  style="width: 100%;"
              />
            </template>
          </el-table-column>
          <el-table-column label="执行周期" width="120">
            <template #default="{ row }">
              <el-select v-model="row.executeCycle" placeholder="请选择" style="width: 100%;">
                <el-option label="每日" value="每日" />
                <el-option label="每周" value="每周" />
                <el-option label="每月" value="每月" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="执行频次（次）" width="130">
            <template #default="{ row }">
              <el-input-number
                  v-model="row.executeTimes"
                  :min="1"
                  :max="7"
                  placeholder="1-7"
                  default-value="1"
                  style="width: 100%;"
              />
            </template>
          </el-table-column>

          <el-table-column label="操作" align="center">
            <template #default="{ $index }">
              <el-button
                  type="danger"
                  size="small"
                  icon="Minus"
                  @click="removeItem($index)"
                  :disabled="nursingPlanItemList.length <= 1"
              ></el-button>
              <el-button
                  type="primary"
                  size="small"
                  icon="Plus"
                  @click="addItem()"
                  v-if="$index === nursingPlanItemList.length - 1 && getUnselectedCount() > 0"
              ></el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancel">取消</el-button>
          <el-button type="primary" @click="submitForm">确定</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 查看护理计划对话框 -->
    <el-dialog title="查看护理计划" v-model="viewOpen" width="800px" append-to-body>
      <el-form ref="viewPlanRef" :model="viewForm" label-width="100px" disabled>
        <el-form-item label="护理计划名称" prop="planName">
          <el-input v-model="viewForm.planName" maxlength="10" show-word-limit />
        </el-form-item>

        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="viewForm.status">
            <el-radio
                v-for="dict in sys_normal_disable"
                :key="dict.value"
                :label="parseInt(dict.value)"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-divider content-position="center">护理项目</el-divider>

        <el-table :data="viewNursingPlanItemList" border style="width: 100%;">
          <el-table-column label="护理项目名称" width="200" align="center">
            <template #default="{ row }">
              <span>{{ getNursingItemNameById(row.itemId) || '未匹配到项目' }}</span>
            </template>
          </el-table-column>
          <el-table-column label="期望服务时间" width="180" align="center">
            <template #default="{ row }">
              <span>{{ row.expectedTime || '08:00' }}</span>
            </template>
          </el-table-column>
          <el-table-column label="执行周期" width="180" align="center">
            <template #default="{ row }">
              <span>{{ row.executeCycle || '每日' }}</span>
            </template>
          </el-table-column>
          <el-table-column label="执行频次（次）" align="center">
            <template #default="{ row }">
              <span>{{ row.executeTimes || 1 }}</span>
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="viewOpen = false">关闭</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts" name="Config">
import type { NursingPlan, NursingPlanItem, PlanQueryParams } from "@/types/api/code/plan"
import { listPlan, getPlan, delPlan, addPlan, updatePlan, checkPlanNameUnique } from "@/api/code/plan"
import { listItem } from "@/api/code/nursingItem"

const { proxy } = getCurrentInstance()
const { sys_normal_disable } = proxy.useDict('sys_normal_disable')

const planList = ref<NursingPlan[]>([])
const allNursingItems = ref<any[]>([])
const nursingPlanItemList = ref<{
  itemName: string,
  expectedTime: string,
  executeCycle: string,
  executeTimes: number,
  itemId?: number
}[]>([])

const viewOpen = ref<boolean>(false)
const viewForm = ref<NursingPlan>({} as NursingPlan)
const viewNursingPlanItemList = ref<{
  itemName: string,
  expectedTime: string,
  executeCycle: string,
  executeTimes: number,
  itemId?: number
}[]>([])

const open = ref<boolean>(false)
const loading = ref<boolean>(true)
const showSearch = ref<boolean>(true)
const ids = ref<number[]>([])
const single = ref<boolean>(true)
const multiple = ref<boolean>(true)
const total = ref<number>(0)
const title = ref<string>("")

const data = reactive({
  form: {} as NursingPlan,
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    planName: undefined,
    status: undefined,
  } as PlanQueryParams,
  rules: {
    planName: [
      { required: true, message: "护理计划名称不能为空", trigger: "blur" },
      { max: 10, message: "护理计划名称不能超过10个字符", trigger: "blur" },
    ],
    status: [
      { required: true, message: "状态不能为空", trigger: "change" }
    ],
    creatorId: [{ required: false }],
    creatorName: [{ required: false }],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 加载所有可选护理项目*/
const loadAllNursingItems = async () => {
  try {
    const res = await listItem({ status: 0 })
    allNursingItems.value = res.rows
  } catch (error) {
    proxy.$modal.msgError("加载护理项目列表失败")
    console.error(error)
  }
}


/** 获取未选中的项目数量 */
const getUnselectedCount = () => {
  const selectedNames = nursingPlanItemList.value
      .map(item => item.itemName)
      .filter(Boolean)
  return allNursingItems.value.length - selectedNames.length
}

/** 新增一行护理项目 */
const addItem = () => {
  if (getUnselectedCount() > 0) {
    nursingPlanItemList.value.push({
      itemName: "",
      expectedTime: "08:00",
      executeCycle: "每日",
      executeTimes: 1
    })
  }
}

/** 删除指定行护理项目 */
const removeItem = (index: number) => {
  if (nursingPlanItemList.value.length > 1) {
    nursingPlanItemList.value.splice(index, 1)
  } else {
    proxy.$modal.msgWarning("至少保留一个护理项目")
  }
}

/** 切换项目名称时刷新 */
function handleItemNameChange() {
  nursingPlanItemList.value = [...nursingPlanItemList.value]
}

/** 启用/禁用状态切换 */
function handleChangeStatus(row: NursingPlan) {
  const text = row.status === 0 ? '禁用' : '启用';
  proxy.$modal.confirm(`确认要${text}护理计划"${row.planName}"吗？`).then(() => {
    return updatePlan({ ...row, status: row.status === 0 ? 1 : 0 });
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess(`${text}成功`);
  }).catch(() => {});
}

/** 查询护理计划列表 */
function getList() {
  loading.value = true
  listPlan(queryParams.value).then(response => {
    planList.value = response.rows
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
    planName: null,
    status: 0,
    creatorId: null,
    creatorName: null,
    createTime: null,
    updateTime: null
  }
  nursingPlanItemList.value = [{
    itemName: "",
    expectedTime: "08:00",
    executeCycle: "每日",
    executeTimes: 1
  }]
  proxy.resetForm("planRef")
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
function handleSelectionChange(selection: NursingPlan[]) {
  ids.value = selection.map(item => item.id)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
async function handleAdd() {
  reset()
  await loadAllNursingItems()
  open.value = true
  title.value = "添加护理计划"
}

/** 修改按钮操作 */
async function handleUpdate(row: NursingPlan) {
  reset()
  await loadAllNursingItems()
  const _id = row.id || ids.value[0]
  getPlan(_id).then(response => {
    form.value = response.data
    nursingPlanItemList.value = response.data?.nursingPlanItemList?.map((item: any) => {
      const matchedItem = allNursingItems.value.find(i => i.id === (item.itemId || item.item_id))
      return {
        itemName: matchedItem?.itemName || "",
        expectedTime: item.expected_duration || item.expectedTime || "08:00",
        executeCycle: item.executeCycle || item.execute_cycle || "每日",
        executeTimes: item.executeTimes || item.execute_times || 1,
        itemId: item.itemId || item.item_id
      }
    }) || [{
      itemName: "",
      expectedTime: "08:00",
      executeCycle: "每日",
      executeTimes: 1
    }]
    open.value = true
    title.value = "修改护理计划"
  }).catch(error => {
    proxy.$modal.msgError("加载护理计划详情失败")
    console.error("修改弹窗数据加载失败：", error)
  })
}

// 获取当前可选项目
const getAvailableItems = (currentIndex: number) => {
  const selectedNames = nursingPlanItemList.value
      .filter((_, index) => index !== currentIndex)
      .map(item => item.itemName)
      .filter(Boolean)
  return allNursingItems.value.filter(item => !selectedNames.includes(item.itemName))
}

//根据id查询项目名称
const getNursingItemNameById = (itemId: number | undefined) => {
  if (!itemId) return ''
  const matchedItem = allNursingItems.value.find(item => item.id === itemId)
  return matchedItem?.itemName || ''
}

/** 查看按钮操作*/
async function handleView(row: NursingPlan) {
  const _id = row.id
  await loadAllNursingItems()
  getPlan(_id).then(response => {
    viewForm.value = response.data
    viewNursingPlanItemList.value = response.data?.nursingPlanItemList?.map((item: any) => ({
      itemId: item.itemId || item.item_id,
      expectedTime: item.expected_duration || item.expectedTime || "08:00",
      executeCycle: item.executeCycle || item.execute_cycle || "每日",
      executeTimes: item.executeTimes || item.execute_times || 1
    })) || []
    viewOpen.value = true
  }).catch(error => {
    proxy.$modal.msgError("加载护理计划详情失败")
    console.error("查看弹窗数据加载失败：", error)
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["planRef"].validate(async (valid: boolean) => {
    if (valid) {
      const invalidItem = nursingPlanItemList.value.find(item => !item.itemName)
      if (invalidItem) {
        proxy.$modal.msgError("请选择所有护理项目名称")
        return
      }
      if (!form.value.id) {
        try {
          const res = await checkPlanNameUnique(form.value.planName as string, form.value.id)
          if (!res.data) {
            proxy.$modal.msgError("护理计划名称已存在，请更换名称")
            return
          }
        } catch (error) {
          proxy.$modal.msgError("名称校验失败，请稍后重试");
          return;
        }
      }
      form.value.nursingPlanItemList = nursingPlanItemList.value.map(item => {
        const matchedItem = allNursingItems.value.find(i => i.itemName === item.itemName)
        return {
          itemId: matchedItem?.id || item.itemId,
          expectedTime: item.expectedTime,
          executeCycle: item.executeCycle,
          executeTimes: item.executeTimes,
          itemName: item.itemName
        }
      })

      if (form.value.id != null) {
        updatePlan(form.value).then(() => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addPlan(form.value).then(() => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}

/** 删除按钮操作 */
function handleDelete(row: NursingPlan) {
  const _ids = row.id || ids.value
  proxy.$modal.confirm('是否确认删除护理计划编号为"' + _ids + '"的数据项？').then(function() {
    return delPlan(_ids)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}
getList()
</script>

<style scoped>
.mb8 {
  margin-bottom: 8px;
}
.dialog-footer {
  text-align: right;
}
.el-table {
  --el-table-row-hover-bg-color: #f5f7fa;
}
</style>