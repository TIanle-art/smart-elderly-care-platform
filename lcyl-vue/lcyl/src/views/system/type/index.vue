<template>
  <div class="app-container">
    <!-- 搜索表单 -->
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="房型类型">
        <el-input
            v-model="queryParams.name"
            placeholder="请输入房型类型"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态">
        <!-- 关键1：清空时绑定值为null，且添加@change事件处理 -->
        <el-select
            v-model="queryParams.status"
            placeholder="全部状态"
            clearable
            style="width:120px"
            @change="handleStatusChange"
        >
          <el-option label="启用" :value="1" />
          <el-option label="禁用" :value="2" />
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
            v-hasPermi="['system:type:add']"
        >新增</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="typeList">
      <el-table-column label="房型ID" align="center" prop="id" />
      <el-table-column label="房型照片" align="center" width="100">
        <template #default="scope">
          <div @click.stop="previewImage(scope.row.photo)">
            <el-image
                :src="scope.row.photo"
                fit="cover"
                style="width:40px;height:40px;border-radius:4px;cursor:pointer"
            />
          </div>
        </template>
      </el-table-column>
      <el-table-column label="房型类型" align="center" prop="name" />
      <el-table-column label="床位数量" align="center" prop="bedCount" />
      <el-table-column label="房型价格" align="center" prop="price" />
      <el-table-column label="房型介绍" align="center" prop="introduction" min-width="180" />
      <el-table-column label="状态" align="center" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
            {{ scope.row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="200">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:type:edit']">修改</el-button>
          <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['system:type:remove']">删除</el-button>
          <el-button link type="primary" @click="handleSwitchStatus(scope.row)">
            {{ scope.row.status === 1 ? '禁用' : '启用' }}
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

    <!-- 弹窗 -->
    <el-dialog :title="title" v-model="open" width="550px" append-to-body>
      <el-form ref="typeRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="房型照片">
          <el-upload
              class="avatar-uploader"
              action="http://localhost:8080/upload"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :before-upload="beforeAvatarUpload"
          >
            <el-image v-if="form.photo" :src="form.photo" fit="cover" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>

        <el-form-item label="房型类型" prop="name">
          <el-input v-model="form.name" placeholder="请输入房型类型" />
        </el-form-item>
        <el-form-item label="床位数量" prop="bedCount">
          <el-input v-model="form.bedCount" placeholder="请输入床位数量" />
        </el-form-item>
        <el-form-item label="房型价格" prop="price">
          <el-input v-model="form.price" placeholder="请输入房型价格" />
        </el-form-item>
        <el-form-item label="房型介绍" prop="introduction">
          <el-input v-model="form.introduction" type="textarea" :rows="3" placeholder="请输入介绍" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="2">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 图片预览 -->
    <el-dialog v-model="previewVisible" title="图片预览" width="500px">
      <el-image :src="previewUrl" style="width:100%;height:auto" fit="contain" />
    </el-dialog>
  </div>
</template>

<script setup lang="ts" name="RoomType">
import { Plus } from "@element-plus/icons-vue";
import {ElMessage, UploadFile} from 'element-plus'
import type { LcRoomType, TypeQueryParams } from "@/types/api/system/type";
import { listType, getType, delType, addType, updateType } from "@/api/system/type";

const { proxy } = getCurrentInstance()!;

const typeList = ref<LcRoomType[]>([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const total = ref(0);
const title = ref("");

// 预览
const previewVisible = ref(false);
const previewUrl = ref("");

const data = reactive({
  form: {} as LcRoomType,
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    name: undefined,
    status: undefined, // 初始值为undefined
  } as TypeQueryParams,
  rules: {
    name: [{ required: true, message: "房型名称不能为空", trigger: "blur" }],
    bedCount: [{ required: true, message: "床位数量不能为空", trigger: "blur" }],
    price: [{ required: true, message: "房型价格不能为空", trigger: "blur" }],
    status: [{ required: true, message: "请选择状态", trigger: "change" }],
  },
});

const { queryParams, form, rules } = toRefs(data);

// 关键2：状态选择变化时处理 - 清空则删除status属性
const handleStatusChange = (value: number | null) => {
  if (value === null) {
    // 清空选择时，删除status参数（避免传递undefined/null）
    delete queryParams.value.status;
  } else {
    queryParams.value.status = value;
  }
};

// 图片预览
function previewImage(url: string) {
  if (!url) return;
  previewUrl.value = url;
  previewVisible.value = true;
}

// 上传成功
const handleAvatarSuccess = (response: any, uploadFile: UploadFile) => {
  form.value.photo = response.data || response.url;
  ElMessage.success("上传成功");
};

// 上传前校验
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

// 关键3：查询前处理参数 - 过滤空值
function getList() {
  loading.value = true;
  // 深拷贝并过滤空参数（避免传递name/status为undefined/null）
  const params = { ...queryParams.value };
  Object.keys(params).forEach(key => {
    if (params[key] === undefined || params[key] === null || params[key] === '') {
      delete params[key];
    }
  });
  listType(params).then((res) => {
    typeList.value = res.rows;
    total.value = res.total;
    loading.value = false;
  });
}

// 取消
function cancel() {
  open.value = false;
  reset();
}

// 重置表单
function reset() {
  form.value = {
    id: null,
    photo: null,
    name: null,
    bedCount: null,
    price: null,
    introduction: null,
    status: 1,
    remark: null,
    createBy: null,
    updateBy: null,
    createTime: null,
    updateTime: null,
  };
  proxy.resetForm("typeRef");
}

// 搜索
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

// 重置
function resetQuery() {
  proxy.resetForm("queryRef");
  // 关键4：重置时主动删除status参数
  delete queryParams.value.status;
  queryParams.value.name = undefined;
  handleQuery();
}

// 新增
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加房型";
}

// 修改
function handleUpdate(row: LcRoomType) {
  reset();
  const id = row.id;
  getType(id).then((res) => {
    form.value = res.data;
    open.value = true;
    title.value = "修改房型";
  });
}

// 提交
function submitForm() {
  proxy.$refs["typeRef"].validate((valid: boolean) => {
    if (!valid) return;
    if (form.value.id) {
      updateType(form.value).then(() => {
        proxy.$modal.msgSuccess("修改成功");
        open.value = false;
        getList();
      });
    } else {
      addType(form.value).then(() => {
        proxy.$modal.msgSuccess("新增成功");
        open.value = false;
        getList();
      });
    }
  });
}

// 删除
function handleDelete(row: LcRoomType) {
  const id = row.id;
  proxy.$modal.confirm("确定删除选中房型？").then(() => {
    return delType(id);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  });
}

// 导出
function handleExport() {
  // 导出时也过滤空参数
  const params = { ...queryParams.value };
  Object.keys(params).forEach(key => {
    if (params[key] === undefined || params[key] === null || params[key] === '') {
      delete params[key];
    }
  });
  proxy.download("system/type/export", params, `type_${new Date().getTime()}.xlsx`);
}

// 状态切换 1启用 2禁用
function handleSwitchStatus(row: LcRoomType) {
  const newStatus = row.status === 1 ? 2 : 1;
  const text = newStatus === 1 ? "启用" : "禁用";
  proxy.$modal.confirm(`确认${text}【${row.name}】？`).then(() => {
    const data = { id: row.id, status: newStatus };
    return updateType(data);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess(`${text}成功`);
  });
}

getList();
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