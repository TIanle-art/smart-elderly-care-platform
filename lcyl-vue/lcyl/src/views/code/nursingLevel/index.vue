<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="护理等级名称" prop="levelName">
        <el-input
            v-model="queryParams.levelName"
            placeholder="请输入"
            clearable
            @keyup.enter="handleQuery"
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
            v-hasPermi="['nursingLevel:nursingLevel:add']"
        >新增护理等级</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="nursingLevelList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" prop="id" width="50" />
      <el-table-column label="护理等级名称" align="center" prop="levelName" />
      <el-table-column label="执行护理计划" align="center" prop="planName" />
      <el-table-column label="护理费用（元/月）" align="center" prop="monthlyFee" >
      <template #default="scope">
        {{ Number(scope.row.monthlyFee).toFixed(2) }}
      </template>
      </el-table-column>
      <el-table-column
          label="等级说明"
          align="center"
          prop="levelDesc"
          width="120"
          show-overflow-tooltip
      />
      <el-table-column label="创建人" align="center" prop="creatorName" width="100px"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" width="80px">
        <template #default="scope">
          <el-tag :type="scope.row.status === 0 ? 'success' : 'danger'">
            {{ scope.row.status === 0 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">

          <el-button
              link type="primary"
              :disabled="scope.row.referenced"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['nursingLevel:nursingLevel:edit']"
          >修改</el-button>

          <el-button
              link type="primary"
              :disabled="scope.row.referenced"
              @click="handleDelete(scope.row)"
              v-hasPermi="['nursingLevel:nursingLevel:remove']"
          >删除</el-button>

          <el-button
              link
              :type="scope.row.status === 0 ? 'danger' : 'primary'"
              :disabled="scope.row.referenced"
              @click="handleChangeStatus(scope.row)"
              v-hasPermi="['nursingPlan:plan:edit']"
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

    <!-- 添加或修改护理等级对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="levelRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="护理等级名称" prop="levelName">
          <el-input
              v-model="form.levelName"
              placeholder="请输入"
              maxlength="10"
              show-word-limit
          />
        </el-form-item>

        <el-form-item label="执行护理计划" prop="planId">
          <el-select v-model="form.planId" placeholder="请选择执行护理计划" clearable>
            <el-option v-if="planList.length === 0" label="暂无启用的护理计划" value="" disabled />
            <el-option
                v-for="plan in planList"
                :key="plan.id"
                :label="plan.planName"
                :value="plan.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="护理费用" prop="monthlyFee">
          <el-input-number
              v-model="form.monthlyFee"
              :min="0"
              :precision="2"
              placeholder="请输入每月护理费用"
          />
        </el-form-item>

        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="0">启用</el-radio>
            <el-radio :label="1">禁用</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="等级说明" prop="levelDesc">
          <el-input
              v-model="form.levelDesc"
              type="textarea"
              placeholder="请输入等级说明"
              maxlength="50"
              show-word-limit
              :rows="3"
              resize="none"
          />
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
import type { NursingLevel, NursingLevelQueryParams } from "@/types/api/code/nursingLevel"
import { listNursingLevel, getNursingLevel, delNursingLevel, addNursingLevel, updateNursingLevel,checkLevelNameUnique,checkNursingLevelIsReferenced} from "@/api/code/nursingLevel"
import { listPlan } from "@/api/code/plan";

const { proxy } = getCurrentInstance()
const { sys_normal_disable } = proxy.useDict('sys_normal_disable')

const nursingLevelList = ref<NursingLevel[]>([])
const open = ref<boolean>(false)
const loading = ref<boolean>(true)
const showSearch = ref<boolean>(true)
const ids = ref<number[]>([])
const single = ref<boolean>(true)
const multiple = ref<boolean>(true)
const total = ref<number>(0)
const title = ref<string>("")

const planList = ref([]);

async function loadPlanList() {
  try {
    const res = await listPlan({ status: 0 });
    planList.value = res.rows;
    console.log("加载的护理计划：", res.rows);
  } catch (e) {
    console.error("加载护理计划失败：", e);
    proxy.$modal.msgError("加载护理计划失败，请稍后重试！");
  }
}

const data = reactive({
  form: {
    id: null,
    levelName: null,
    planId: null,
    monthlyFee: 0,
    levelDesc: null,
    status: 0,
    creatorId: null,
    creatorName: null,
    createTime: null,
    updateTime: null
  } as NursingLevel,
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    levelName: undefined,
    status: undefined,
  } as NursingLevelQueryParams,
  rules: {
    levelName: [
      { required: true, message: "护理等级名称不能为空", trigger: "blur" },
      { max: 10, message: "护理等级名称不能超过10个字符", trigger: "blur" }
    ],
    planId: [
      { required: true, message: "请选择执行护理计划", trigger: "change" }
    ],
    monthlyFee: [
      { required: true, message: "每月护理费用不能为空", trigger: "change" },
      { type: 'number', min: 0, message: "每月护理费用不能小于0", trigger: "change" }
    ],
    status: [
      { required: true, message: "请选择状态", trigger: "change" }
    ],
    levelDesc: [
      { required: true, message: "等级说明不能为空", trigger: "blur" },
      { max: 50, message: "等级说明不能超过50个字符", trigger: "blur" }
    ]
  }
})

const { queryParams, form, rules } = toRefs(data)


/** 查询护理等级列表 */
async function getList() {
  loading.value = true
  listNursingLevel(queryParams.value).then(async response => {
    nursingLevelList.value = response.rows
    total.value = response.total
    loading.value = false

    for (let item of nursingLevelList.value) {
      try {
        const res = await checkNursingLevelIsReferenced({ nursingLevelId: item.id })
        item.referenced = res.data
      } catch (e) {
        item.referenced = false
      }
    }
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
    levelName: null,
    planId: null,
    monthlyFee: 0,
    levelDesc: null,
    status: 0,
    creatorId: null,
    creatorName: null,
    createTime: null,
    updateTime: null
  }
  proxy.resetForm("levelRef")
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
function handleSelectionChange(selection: NursingLevel[]) {
  ids.value = selection.map(item => item.id)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加护理等级"
  loadPlanList();
}

/** 修改按钮操作 */
function handleUpdate(row: NursingLevel) {
  reset()
  const _id = row.id || ids.value[0]
  getNursingLevel(_id).then(response => {
    form.value = {
      ...response.data,
      monthlyFee: Number(response.data.monthlyFee) || 0
    }
    open.value = true
    title.value = "修改护理等级"
    loadPlanList();
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["levelRef"].validate(async (valid: boolean) => {
    if (valid) {
      try {
        const { data: isUnique } = await checkLevelNameUnique(
            form.value.levelName as string,
            form.value.id
        );
        if (!isUnique) {
          proxy.$modal.msgError("护理等级名称已存在，请更换名称");
          return;
        }
      } catch (error) {
        proxy.$modal.msgError("名称校验失败，请稍后重试");
        return;
      }

      if (form.value.id != null) {
        try {
          const { data: isReferenced } = await checkNursingLevelIsReferenced({
            nursingLevelId: form.value.id
          });
          if (isReferenced) {
            proxy.$modal.msgError("该护理等级已被使用，无法修改");
            return;
          }
        } catch (e) {
          proxy.$modal.msgError("校验引用关系失败");
          return;
        }
      }

      if (form.value.id != null) {
        updateNursingLevel(form.value).then(() => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addNursingLevel(form.value).then(() => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}
/** 删除按钮操作 */
function handleDelete(row: NursingLevel) {
  const _ids = row.id || ids.value

  checkNursingLevelIsReferenced({ nursingLevelId: _ids }).then(res => {
    if (res.data) {
      proxy.$modal.msgError("该护理等级已被使用，无法删除");
    } else {
      proxy.$modal.confirm('是否确认删除护理等级编号为"' + _ids + '"的数据项？').then(function() {
        return delNursingLevel(_ids)
      }).then(() => {
        getList()
        proxy.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    }
  })
}

// 切换启用/禁用状态
function handleChangeStatus(row: NursingLevel) {

  checkNursingLevelIsReferenced({ nursingLevelId: row.id }).then(res => {
    if (res.data) {
      proxy.$modal.msgError("该护理等级已被使用，无法禁用");
      return;
    }

    const newStatus = row.status === 0 ? 1 : 0;
    updateNursingLevel({ ...row, status: newStatus }).then(() => {
      proxy.$modal.msgSuccess(newStatus === 0 ? "启用成功" : "禁用成功")
      getList()
    })
  })
}
/** 导出按钮操作 */
function handleExport() {
  proxy.download('nursingLevel/nursingLevel/export', {
    ...queryParams.value
  }, `nursingLevel_${new Date().getTime()}.xlsx`)
}

getList()
</script>