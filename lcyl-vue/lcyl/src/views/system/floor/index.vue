<template>
  <div class="app-container">
    <!-- 楼层 + 操作按钮 区域 -->
    <div class="top-bar">
      <div class="floor-tabs-wrapper">
        <div
            class="floor-tab"
            :class="{ active: activeFloor === floor.id + '' }"
            v-for="floor in floorList"
            :key="floor.id"
            @click="handleFloorChange(null, floor.id + '')"
        >
          <span class="floor-name">{{ floor.name }}</span>
          <el-icon v-if="activeFloor === floor.id + ''" class="check-icon">
            <Check/>
          </el-icon>
          <el-icon
              v-if="activeFloor === floor.id + ''"
              class="edit-floor-icon"
              @click.stop="handleEditFloor(floor)"
          >
            <Edit/>
          </el-icon>
        </div>
      </div>
      <div class="operate-btns">
        <el-button type="primary" size="default" @click="handleAddFloor">新增楼层</el-button>
        <el-button type="primary" size="default" @click="handleAddRoom">新增房间</el-button>
      </div>
    </div>

    <!-- 状态图例 -->
    <div class="status-legend">
      <div class="legend-item">
        <img src="@/assets/bed/bed-free.png" class="legend-icon"/>
        <span>空闲</span>
      </div>
      <div class="legend-item">
        <img src="@/assets/bed/bed-used.png" class="legend-icon"/>
        <span>已入住</span>
      </div>
      <div class="legend-item">
        <img src="@/assets/bed/bed-leave.png" class="legend-icon"/>
        <span>请假中</span>
      </div>
    </div>

    <div class="room-wrapper" v-loading="loading">
      <div class="room-card" v-for="room in validRoomList" :key="room.id">
        <div class="room-header">
          <div class="room-info">
            <div class="room-title">房间号：{{ room.code }}</div>
            <div class="room-type">房间类型：{{ room.roomTypeName }}</div>
          </div>
          <div class="room-actions">
            <el-button link type="danger" size="small" @click="handleDeleteRoom(room.id)">删除</el-button>
            <el-button link type="primary" size="small" @click="handleEditRoom(room)">编辑</el-button>
            <el-button link type="info" size="small" @click="handleViewRoom(room)">查看</el-button>
            <el-button type="primary" size="small" @click="handleAddBed(room)">新增床位</el-button>
          </div>
        </div>

        <div class="bed-list">
          <div
              class="bed-item"
              v-for="bed in room.beds"
              :key="bed.bedId"
          >
            <img
                :src="bed.bedStatus === 0 ? bedFree : bed.bedStatus === 1 ? bedUsed : bedLeave"
                class="bed-img"
            />

            <div class="bed-info">
              <div class="bed-number">床位号：{{ bed.bedNumber }}</div>
              <div class="elder-name">{{ bed.bedStatus === 0 ? '空闲' : '老人姓名：' + (bed.name || '未填写') }}</div>
            </div>

            <div class="bed-actions" v-if="bed.bedId">
              <el-icon class="edit-btn" @click.stop="handleEditBed(bed)">
                <Edit/>
              </el-icon>
              <el-icon class="delete-btn" @click.stop="handleDeleteBed(bed)">
                <Delete/>
              </el-icon>
            </div>
          </div>

          <div class="no-bed-tip" v-if="room.beds.length === 0">
            当前房间没有安排床位
          </div>
        </div>
      </div>

      <div class="empty-tip" v-if="!loading && validRoomList.length === 0">
        <el-empty description="该楼层暂无房间数据"/>
      </div>
    </div>

    <!-- 楼层新增/编辑弹窗 -->
    <el-dialog v-model="floorDialogVisible" title="楼层管理" width="400px" @close="resetFloorForm">
      <el-form :model="floorForm" :rules="floorRules" ref="floorFormRef" label-width="80px">
        <el-form-item label="楼层名称" prop="name">
          <el-input v-model="floorForm.name" placeholder="请输入楼层名称（如：1楼）"/>
        </el-form-item>
        <el-form-item label="楼层编号" prop="code">
          <el-input v-model="floorForm.code" placeholder="请输入楼层编号（如：1）"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="floorDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitFloorForm">确定</el-button>
      </template>
    </el-dialog>

    <!-- 房间新增/编辑弹窗 -->
    <el-dialog v-model="roomDialogVisible" title="房间管理" width="400px" @close="resetRoomForm">
      <el-form :model="roomForm" :rules="roomRules" ref="roomFormRef" label-width="80px">
        <el-form-item label="房间编号" prop="code">
          <el-input v-model="roomForm.code" placeholder="请输入房间编号（如：101）"/>
        </el-form-item>
        <el-form-item label="房间类型" prop="roomTypeName">
          <el-input v-model="roomForm.roomTypeName" placeholder="请输入房间类型（如：单人间）"/>
        </el-form-item>
        <el-form-item label="所属楼层" prop="floorId">
          <el-select v-model="roomForm.floorId" placeholder="请选择所属楼层">
            <el-option
                v-for="floor in floorList"
                :key="floor.id"
                :label="floor.name"
                :value="floor.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="roomDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitRoomForm">确定</el-button>
      </template>
    </el-dialog>

    <!-- 床位新增弹窗 -->
    <el-dialog v-model="bedDialogVisible" title="新增床位" width="400px" @close="resetBedForm">
      <el-form :model="bedForm" :rules="bedRules" ref="bedFormRef" label-width="80px">
        <el-form-item label="床位编号" prop="bedNumber">
          <el-input v-model="bedForm.bedNumber" placeholder="请输入床位编号（如：1）"/>
        </el-form-item>
        <el-form-item label="老人姓名" v-if="bedForm.bedStatus != 0">
          <el-input v-model="bedForm.name" placeholder="请输入老人姓名"/>
        </el-form-item>
        <el-form-item label="所属房间" prop="roomId">
          <el-input v-model="bedForm.roomName" disabled placeholder="所属房间"/>
        </el-form-item>
        <el-form-item label="床位状态" prop="bedStatus">
          <el-select v-model="bedForm.bedStatus" placeholder="请选择床位状态">
            <el-option label="空闲" value="0"/>
            <el-option label="已入住" value="1"/>
            <el-option label="请假中" value="2"/>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="bedDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitBedForm">确定</el-button>
      </template>
    </el-dialog>

    <!-- 床位编辑弹窗 -->
    <el-dialog v-model="editBedDialogVisible" title="修改床位" width="420px" @close="resetEditBedForm">
      <el-form :model="editBedForm" ref="editBedFormRef" label-width="80px" :rules="bedRules">
        <el-form-item label="床位编号" prop="bedNumber">
          <el-input v-model="editBedForm.bedNumber" placeholder="请输入床位编号"/>
        </el-form-item>
        <el-form-item label="老人姓名" v-if="editBedForm.bedStatus != 0">
          <el-input v-model="editBedForm.name" placeholder="请输入老人姓名"/>
        </el-form-item>
        <el-form-item label="床位状态" prop="bedStatus">
          <el-select v-model="editBedForm.bedStatus" placeholder="请选择床位状态">
            <el-option label="空闲" value="0"/>
            <el-option label="已入住" value="1"/>
            <el-option label="请假中" value="2"/>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editBedDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEditBed">保存修改</el-button>
      </template>
    </el-dialog>

    <!-- 房间查看弹窗 -->
    <el-dialog v-model="viewRoomDialogVisible" title="房间详情" width="650px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="房间编号">{{ viewRoomData.code }}</el-descriptions-item>
        <el-descriptions-item label="房间类型">{{ viewRoomData.roomTypeName }}</el-descriptions-item>
        <el-descriptions-item label="床位总数">{{ viewRoomData.beds?.length || 0 }}</el-descriptions-item>
        <el-descriptions-item label="空闲床位" :span="2">
          {{ viewRoomData.beds?.filter(item => item.bedStatus === 0).length || 0 }}
        </el-descriptions-item>
      </el-descriptions>
      <div style="margin-top: 16px">
        <h4 style="margin-bottom: 8px">床位列表</h4>
        <el-table :data="viewRoomData.beds || []" border size="small">
          <el-table-column label="床位编号" prop="bedNumber"/>
          <el-table-column label="入住老人" prop="name"/>
          <el-table-column label="护理等级" prop="nursingLevelName" min-width="150"/>
          <el-table-column label="入住期限" min-width="220">
            <template #default="scope">
              <span>
                {{
                  scope.row.startTime && scope.row.endTime
                      ? formatDate(scope.row.startTime) + " ~ " + formatDate(scope.row.endTime)
                      : "无"
                }}
              </span>
            </template>
          </el-table-column>
          <el-table-column label="床位状态">
            <template #default="scope">
              <el-tag
                  :type="scope.row.bedStatus === 0 ? 'primary' : scope.row.bedStatus === 1 ? 'success' : 'danger'"
              >
                {{ scope.row.bedStatus === 0 ? '空闲' : scope.row.bedStatus === 1 ? '已入住' : '请假中' }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import {ref, onMounted, reactive, computed, watch} from "vue";
import {ElMessage, ElMessageBox} from "element-plus";
import {Edit, Delete, Check} from '@element-plus/icons-vue'
import {
  getFloorRoomBed,
  addFloor,
  updateFloor,
  delFloor,
  addRoom,
  updateRoom,
  delRoom,
  addBed,
  updateBed,
  deleteBed
} from "@/api/system/floor.js";
import {listFloor} from "@/api/system/floor.js";

import bedFree from '@/assets/bed/bed-free.png'
import bedUsed from '@/assets/bed/bed-used.png'
import bedLeave from '@/assets/bed/bed-leave.png'

const floorList = ref([]);
const activeFloor = ref("");
const loading = ref(false);
const roomList = ref([]);

const validRoomList = computed(() => {
  return roomList.value.map(room => {
    const validBeds = (room.beds || []).filter(bed => bed.bedId != null);
    return {...room, beds: validBeds};
  });
});

const floorFormRef = ref(null);
const roomFormRef = ref(null);
const bedFormRef = ref(null);
const editBedFormRef = ref(null);

const floorDialogVisible = ref(false);
const roomDialogVisible = ref(false);
const bedDialogVisible = ref(false);
const editBedDialogVisible = ref(false);
const viewRoomDialogVisible = ref(false);

const floorActionType = ref("add");
const roomActionType = ref("add");

const floorForm = reactive({id: "", name: "", code: ""});
const roomForm = reactive({id: "", code: "", roomTypeName: "", floorId: ""});
const bedForm = reactive({bedNumber: "", name: "", roomId: "", roomName: "", bedStatus: "0", code: ""});
const editBedForm = reactive({id: "", bedNumber: "", name: "", bedStatus: ""});
const viewRoomData = reactive({});

const floorRules = ref({
  name: [{required: true, message: "请输入楼层名称", trigger: "blur"}],
  code: [{required: true, message: "请输入楼层编号", trigger: "blur"}]
});
const roomRules = ref({
  code: [{required: true, message: "请输入房间编号", trigger: "blur"}],
  roomTypeName: [{required: true, message: "请输入房间类型", trigger: "blur"}],
  floorId: [{required: true, message: "请选择所属楼层", trigger: "change"}]
});
const bedRules = ref({
  bedNumber: [{required: true, message: "请输入床位编号", trigger: "blur"}],
  bedStatus: [{required: true, message: "请选择床位状态", trigger: "change"}]
});

const formatDate = (dateStr) => {
  if (!dateStr) return "";
  let d = new Date(dateStr);
  let y = d.getFullYear();
  let m = (d.getMonth() + 1).toString().padStart(2, "0");
  let day = d.getDate().toString().padStart(2, "0");
  return `${y}-${m}-${day}`;
};

const handleEditFloor = (floor) => {
  floorForm.id = floor.id;
  floorForm.name = floor.name;
  floorForm.code = floor.code;
  floorActionType.value = "edit";
  floorDialogVisible.value = true;
};

const handleEditBed = (bed) => {
  editBedForm.id = bed.bedId;
  editBedForm.bedNumber = bed.bedNumber;
  editBedForm.name = bed.name || "";
  editBedForm.bedStatus = bed.bedStatus + "";
  editBedDialogVisible.value = true;
};

watch(() => editBedForm.bedStatus, (newVal) => {
  if (newVal === '0') editBedForm.name = '';
});

const submitEditBed = async () => {
  try {
    await editBedFormRef.value.validate();
    const name = (editBedForm.name || '').trim();
    if (!name) editBedForm.bedStatus = '0';

    const params = {
      id: editBedForm.id,
      bedNumber: editBedForm.bedNumber,
      name: name,
      bedStatus: Number(editBedForm.bedStatus)
    };

    const res = await updateBed(params);
    if (res.code === 200) {
      ElMessage.success("修改成功");
      editBedDialogVisible.value = false;
      handleFloorChange();
    } else ElMessage.error("修改失败");
  } catch (error) {
    console.error(error);
  }
};

const resetEditBedForm = () => {
  editBedFormRef.value?.resetFields();
  editBedForm.id = editBedForm.bedNumber = editBedForm.name = editBedForm.bedStatus = "";
};

const handleDeleteBed = async (bed) => {
  try {
    await ElMessageBox.confirm(`确定删除床位【${bed.bedNumber}】吗？`, "提示");
    const res = await deleteBed(bed.bedId);
    if (res.code === 200) {
      ElMessage.success("删除成功");
      handleFloorChange();
    } else ElMessage.error("删除失败");
  } catch (error) {
    if (error !== "cancel") ElMessage.error("删除失败");
  }
};

const getFloorList = async () => {
  try {
    const res = await listFloor({});
    if (res.code === 200 && res.rows) {
      floorList.value = res.rows;
      if (floorList.value.length) {
        activeFloor.value = floorList.value[0].id + "";
        setTimeout(() => handleFloorChange(), 50);
      }
    }
  } catch (error) {
    ElMessage.error("获取楼层失败");
  }
};

const handleFloorChange = async (tab, name) => {
  const target = name || activeFloor.value;
  if (!target) return;
  activeFloor.value = target;
  loading.value = true;
  roomList.value = [];
  try {
    const res = await getFloorRoomBed(Number(target));

    // 房间号按数字降序排列
    let rooms = res.data?.[0]?.rooms || [];
    rooms = rooms.sort((a, b) => {
      const numA = parseInt(a.code) || 0;
      const numB = parseInt(b.code) || 0;
      return numA - numB;
    });

    roomList.value = rooms;
  } catch (error) {
    ElMessage.error("获取数据失败");
  } finally {
    loading.value = false;
  }
};

const resetFloorForm = () => {
  floorFormRef.value?.resetFields();
  floorForm.id = floorForm.name = floorForm.code = "";
  floorActionType.value = "add";
};
const handleAddFloor = () => {
  resetFloorForm();
  floorDialogVisible.value = true;
};
const submitFloorForm = async () => {
  try {
    await floorFormRef.value.validate();
    const res = floorActionType.value === "add" ? await addFloor(floorForm) : await updateFloor(floorForm);
    if (res.code === 200) {
      ElMessage.success("操作成功");
      floorDialogVisible.value = false;
      getFloorList();
    } else ElMessage.error("操作失败");
  } catch (e) {
    console.error(e);
  }
};

const resetRoomForm = () => {
  roomFormRef.value?.resetFields();
  roomForm.id = roomForm.code = roomForm.roomTypeName = "";
  roomForm.floorId = Number(activeFloor.value);
  roomActionType.value = "add";
};
const handleAddRoom = () => {
  resetRoomForm();
  roomDialogVisible.value = true;
};
const handleEditRoom = (room) => {
  resetRoomForm();
  roomActionType.value = "edit";
  roomForm.id = room.id;
  roomForm.code = room.code;
  roomForm.roomTypeName = room.roomTypeName;
  roomForm.floorId = room.floorId || Number(activeFloor.value);
  roomDialogVisible.value = true;
};
const submitRoomForm = async () => {
  try {
    await roomFormRef.value.validate();
    const res = roomActionType.value === "add" ? await addRoom(roomForm) : await updateRoom(roomForm);
    if (res.code === 200) {
      ElMessage.success("操作成功");
      roomDialogVisible.value = false;
      handleFloorChange();
    } else ElMessage.error("操作失败");
  } catch (e) {
    console.error(e);
  }
};
const handleDeleteRoom = async (id) => {
  try {
    await ElMessageBox.confirm("确定删除该房间？", "提示");
    const res = await delRoom(id);
    if (res.code === 200) {
      ElMessage.success("删除成功");
      handleFloorChange();
    } else ElMessage.error("删除失败");
  } catch (e) {
    if (e !== "cancel") ElMessage.error("删除失败");
  }
};

const handleViewRoom = (room) => {
  Object.assign(viewRoomData, room);
  viewRoomDialogVisible.value = true;
};
const resetBedForm = () => {
  bedFormRef.value?.resetFields();
  bedForm.bedNumber = bedForm.name = bedForm.roomId = bedForm.roomName = bedForm.code = "";
  bedForm.bedStatus = "0";
};
const handleAddBed = (room) => {
  resetBedForm();
  bedForm.roomId = room.id;
  bedForm.roomName = room.code + "（" + room.roomTypeName + "）";
  bedForm.code = room.code;
  bedDialogVisible.value = true;
};

// 加强版：床位上限校验（单人1、双人2、四人4）
const submitBedForm = async () => {
  try {
    await bedFormRef.value.validate();

    const roomId = bedForm.roomId;
    const room = roomList.value.find(r => r.id === roomId);
    if (!room) {
      ElMessage.error("房间不存在");
      return;
    }

    const current = room.beds?.length || 0;
    let max = 4;
    if (room.roomTypeName.includes("单人")) max = 1;
    else if (room.roomTypeName.includes("双人")) max = 2;
    else if (room.roomTypeName.includes("四人")) max = 4;

    if (current >= max) {
      ElMessage.error(`${room.roomTypeName} 最多只能放 ${max} 张床位，已达上限！`);
      return;
    }

    const bedData = {...bedForm, bedStatus: Number(bedForm.bedStatus), floorId: Number(activeFloor.value)};
    await addBed(bedData);
    ElMessage.success("新增床位成功");
    bedDialogVisible.value = false;
    handleFloorChange();
  } catch (error) {
    console.error(error);
    ElMessage.error("新增床位失败");
  }
};

onMounted(() => {
  getFloorList();
});
</script>

<style scoped>
.top-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.floor-tabs-wrapper {
  display: flex;
  gap: 8px;
  background: #f5f7fa;
  padding: 8px;
  border-radius: 8px;
}

.floor-tab {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 16px;
  background: #f5f7fa;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 16px;
}

.floor-tab.active {
  background: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.floor-tab:hover {
  background: #e4e7ed;
}

.check-icon {
  color: #00b42a;
  font-size: 16px;
}

.edit-floor-icon {
  color: #409eff;
  font-size: 16px;
  opacity: 0;
  transition: opacity 0.2s;
}

.floor-tab.active:hover .edit-floor-icon {
  opacity: 1;
}

.operate-btns {
  display: flex;
  gap: 12px;
}

.status-legend {
  display: flex;
  gap: 20px;
  margin-bottom: 16px;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 6px;
}

.legend-icon {
  width: 24px;
  height: 24px;
}

.room-wrapper {
  margin-top: 0;
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
  min-height: 200px;
}

.room-card {
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  padding: 12px;
  background: #f5f7fa;
}

.room-header {
  position: relative;
  margin-bottom: 12px;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.room-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.room-title {
  font-weight: 500;
  font-size: 14px;
}

.room-type {
  font-size: 13px;
  color: #909399;
}

.room-actions {
  display: flex;
  gap: 4px;
}

.bed-list {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 10px;
}

.bed-item {
  display: flex;
  flex-direction: row;
  align-items: center;
  padding: 8px;
  border-radius: 6px;
  cursor: pointer;
  background: #f5f7fa;
  position: relative;
  overflow: hidden;
}

.bed-img {
  width: 48px;
  height: 48px;
  margin-right: 8px;
  opacity: 0.85;
}

.bed-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
  font-size: 13px;
  line-height: 1.5;
}

.bed-number {
  font-weight: 500;
}

.elder-name {
  color: #666;
}

.bed-actions {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.45);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 20px;
  opacity: 0;
  transition: opacity 0.2s;
}

.bed-item:hover .bed-actions {
  opacity: 1;
}

.edit-btn, .delete-btn {
  color: #fff;
  cursor: pointer;
  font-size: 18px;
}

.no-bed-tip {
  grid-column: 1/-1;
  text-align: center;
  color: #909399;
  font-size: 13px;
  padding: 12px 0;
}

.empty-tip {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 60px 0;
}
</style>