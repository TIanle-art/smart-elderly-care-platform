<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="护理项目名称" prop="itemName">
        <el-input
            v-model="queryParams.itemName"
            placeholder="请输入"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
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
            icon="Plus"
            @click="handleAdd"
            v-hasPermi="['nursingItem:item:add']"
        >新增护理项目</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="itemList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" type="index" width="50" align="center" />
      <el-table-column label="护理图片" align="center" width="80">
        <template #default="scope">
          <image-preview :src="scope.row.imageUrl" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="护理项目名称" align="center" prop="itemName" />
      <el-table-column label="价格（元）" align="center" prop="price">
        <template #default="scope">
          {{ Number(scope.row.price).toFixed(2) }}
        </template>
      </el-table-column>
      <el-table-column label="单位" align="center" prop="unit" />
      <el-table-column label="排序" align="center" prop="sort" />
      <el-table-column label="创建人" align="center" prop="creatorName" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" width="80">
        <template #default="scope">
          <el-tag :type="scope.row.status === 0 ? 'success' : 'danger'">
            {{ scope.row.status === 0 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
        <template #default="scope">
          <el-button
              link
              type="danger"
              @click="handleDelete(scope.row)"
              v-hasPermi="['nursingItem:item:remove']"
              :disabled="scope.row.isReferenced"
              :title="scope.row.isReferenced ? '该项目已绑定护理计划，不可删除' : ''"
          >删除</el-button>
          <el-button
              link
              type="primary"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['nursingItem:item:edit']"
              :disabled="scope.row.isReferenced"
              :title="scope.row.isReferenced ? '该项目已绑定护理计划，不可编辑' : ''"
          >编辑</el-button>
          <el-button
              link
              :type="scope.row.status === 0 ? 'danger' : 'primary'"
              @click="handleChangeStatus(scope.row)"
              v-hasPermi="['nursingItem:item:edit']"
              :disabled="scope.row.isReferenced && scope.row.status === 0"
              :title="scope.row.isReferenced && scope.row.status === 0 ? '该项目已绑定护理计划，不可禁用' : ''"
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

    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="itemRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="护理项目名称" prop="itemName">
          <el-input v-model="form.itemName" placeholder="请输入护理项目名称" />
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input-number
              v-model="form.price"
              :min="0"
              :precision="2"
              placeholder="请输入"
          />
        </el-form-item>
        <el-form-item label="单位" prop="unit">
          <el-input v-model="form.unit" placeholder="请输入单位" />
        </el-form-item>
        <el-form-item label="排序号" prop="sort">
          <el-input-number
              v-model="form.sort"
              :min="1"
              :step="1"
              placeholder="请输入"
          />
        </el-form-item>

        <el-form-item label="护理图片" prop="imageUrl">
          <el-upload
              class="avatar-uploader"
              action="http://172.16.20.242:8080/upload"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :before-upload="beforeAvatarUpload"
          >
            <el-image v-if="form.imageUrl" :src="form.imageUrl" fit="cover" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>

        <el-form-item label="项目描述" prop="itemDesc">
          <el-input
              v-model="form.itemDesc"
              type="textarea"
              placeholder="请输入项目描述"
              maxlength="50"
              show-word-limit
              :rows="3"
              resize="none"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
                v-for="dict in sys_normal_disable"
                :key="dict.value"
                :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
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

<script setup lang="ts" name="NursingItem">
import type { NursingItem, ItemQueryParams } from "@/types/api/code/item"
import { listItem, getItem, delItem, addItem, updateItem, checkItemNameUnique } from "@/api/code/nursingItem"
import { checkItemIsReferenced } from "@/api/code/plan"
import { Plus } from "@element-plus/icons-vue";
import { ElMessage, UploadFile } from 'element-plus'

const { proxy } = getCurrentInstance()
const { sys_normal_disable } = proxy.useDict('sys_normal_disable')

const itemList = ref<NursingItemExt[]>([])
const open = ref<boolean>(false)
const loading = ref<boolean>(true)
const showSearch = ref<boolean>(true)
const ids = ref<number[]>([])
const single = ref<boolean>(true)
const multiple = ref<boolean>(true)
const total = ref<number>(0)
const title = ref<string>("")

const data = reactive({
  // 新增/编辑表单
  form: {} as NursingItem,
  // 查询参数
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    itemName: undefined,
    price: undefined,
    unit: undefined,
    sort: undefined,
    imageUrl: undefined,
    itemDesc: undefined,
    status: undefined,
    creatorId: undefined,
    creatorName: undefined,
  } as ItemQueryParams,
  rules: {
    itemName: [
      { required: true, message: "护理项目名称不能为空", trigger: "blur" },
      { max: 10, message: "名称不能超过10个字符", trigger: "blur" }
    ],
    price: [
      { required: true, message: "价格不能为空", trigger: "blur" }
    ],
    unit: [
      { required: true, message: "单位不能为空", trigger: "blur" },
      { max: 5, message: "单位不能超过5个字符", trigger: "blur" }
    ],
    sort: [
      { required: true, message: "排序号不能为空", trigger: "blur" }
    ],
    imageUrl: [
      { required: true, message: "护理图片不能为空", trigger: "change" }
    ],
    itemDesc: [
      { required: true, message: "项目描述不能为空", trigger: "blur" },
      { max: 50, message: "描述不能超过50个字符", trigger: "blur" }
    ],
    status: [
      { required: true, message: "状态不能为空", trigger: "change" }
    ]
  }
})

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
  form.value.imageUrl = response.data || response.url;
  ElMessage.success("上传成功");
};

// 是否引用
interface NursingItemExt extends NursingItem {
  isReferenced?: boolean
}

async function handleChangeStatus(row: NursingItemExt) {
  const text = row.status === 0 ? '禁用' : '启用';
  const _id = row.id;
  const _name = row.itemName;

  if (row.status === 0) {
    try {
      const res = await checkItemIsReferenced({ itemId: _id })
      if (res.data) {
        proxy.$modal.msgWarning(`【${_name}】已与护理计划进行绑定，禁用该项操作不可执行！`)
        return
      }
    } catch (error) {
      proxy.$modal.msgError("校验护理项目引用状态失败，请稍后重试！")
      console.error("校验引用状态失败：", error)
      return
    }
  }

  proxy.$modal.confirm(`确认要${text}护理项目"${_name}"吗？`).then(() => {
    return updateItem({ ...row, status: row.status === 0 ? 1 : 0 });
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess(`${text}成功`);
  }).catch((err) => {
    if (err?.message?.includes("foreign key") || err?.toString()?.includes("1451")) {
      proxy.$modal.msgWarning(`【${_name}】已与护理计划进行绑定，${text}该项操作不可执行！`)
    } else {
      proxy.$modal.msgError(`${text}失败，请联系管理员！`)
    }
    console.error(`${text}失败：`, err)
  });
}

const { queryParams, form, rules } = toRefs(data)

async function getList() {
  loading.value = true
  try {
    const response = await listItem(queryParams.value)

    const itemListWithRef = await Promise.all(
        response.rows.map(async (item: NursingItem) => {
          try {
            const res = await checkItemIsReferenced({ itemId: item.id })
            return { ...item, isReferenced: res.data }
          } catch (error) {
            console.error(`校验项目${item.itemName}引用状态失败：`, error)
            return { ...item, isReferenced: false }
          }
        })
    )
    itemList.value = itemListWithRef
    total.value = response.total
  } catch (error) {
    console.error("获取护理项目列表失败：", error)
    proxy.$modal.msgError("获取护理项目列表失败，请稍后重试！")
  } finally {
    loading.value = false
  }
}
// 取消
function cancel() {
  open.value = false
  reset()
}

function reset() {
  form.value = {
    id: null,
    itemName: null,
    price: null,
    unit: null,
    sort: null,
    imageUrl: null,
    itemDesc: null,
    status: null,
    creatorId: null,
    creatorName: null,
    createTime: null,
    updateTime: null
  }
  proxy.resetForm("itemRef")
}

function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

function resetQuery() {
  proxy.resetForm("queryRef")
  handleQuery()
}

// 批量选择
function handleSelectionChange(selection: NursingItemExt[]) {
  ids.value = selection.map(item => item.id)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

function handleAdd() {
  reset()
  open.value = true
  title.value = "添加护理项目"
}

async function handleUpdate(row: NursingItemExt) {
  const _id = row.id || ids.value[0]
  const _name = row.itemName || "该护理项目"

  try {
    const res = await checkItemIsReferenced({ itemId: _id })
    if (res.data) {
      proxy.$modal.msgWarning(`【${_name}】已与护理计划进行绑定，编辑该项操作不可执行！`)
      return
    }
  } catch (error) {
    proxy.$modal.msgError("校验护理项目引用状态失败，请稍后重试！")
    console.error("校验引用状态失败：", error)
    return
  }

  reset()
  getItem(_id).then(response => {
    form.value = {
      ...response.data,
      status: Number(response.data.status)
    }
    open.value = true
    title.value = "修改护理项目"
  })
}

function submitForm() {
  proxy.$refs["itemRef"].validate(async (valid: boolean) => {
    if (valid) {
      if (!form.value.id) {
        try {
          const {data: isUnique} = await checkItemNameUnique(form.value.itemName as string);
          if (!isUnique) {
            proxy.$modal.msgError("护理项目名称已存在，请更换名称");
            return;
          }
        } catch (error) {
          proxy.$modal.msgError("名称校验失败，请稍后重试");
          return;
        }
      }
      // 有id
      if (form.value.id != null) {
        updateItem(form.value).then(() => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        // 无id
        addItem(form.value).then(() => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}

async function handleDelete(row: NursingItemExt) {
  const _id = row.id || ids.value[0]
  const _name = row.itemName || "该护理项目"

  try {
    const res = await checkItemIsReferenced({ itemId: _id })
    if (res.data) {
      proxy.$modal.msgWarning(`【${_name}】已与护理计划进行绑定，删除该项操作不可执行！`)
      return
    }
  } catch (error) {
    proxy.$modal.msgError("校验护理项目引用状态失败，请稍后重试！")
    console.error("校验引用状态失败：", error)
    return
  }

  proxy.$modal.confirm(`是否确认删除护理项目【${_name}】？`).then(function() {
    return delItem(_id)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch((err) => {
    if (err?.message?.includes("foreign key") || err?.toString()?.includes("1451")) {
      proxy.$modal.msgWarning(`【${_name}】已与护理计划进行绑定，删除该项操作不可执行！`)
    } else {
      proxy.$modal.msgError("删除失败，请联系管理员！")
    }
    console.error("删除失败：", err)
  })
}

getList()
</script>

<style scoped>
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