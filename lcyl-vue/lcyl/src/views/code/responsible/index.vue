<template>
  <div class="app-container">
    <!-- 楼层选项卡 -->
    <el-tabs v-model="activeFloor" type="border-card" class="mb-4">
      <el-tab-pane
          v-for="floor in floorList"
          :key="floor.id"
          :label="floor.name"
          :name="floor.id + ''"
      />
    </el-tabs>

    <!-- 房间 & 床位 -->
    <div v-for="room in roomList" :key="room.id" class="room-section">
      <div class="room-header">
        <span class="room-title">房间号：{{ room.code }}</span>
        <el-button type="primary" @click="handleBatchAssign(room)">批量设置护理员</el-button>
      </div>

      <div class="bed-grid">
        <div v-for="bed in room.beds" :key="bed.id" class="bed-card">
          <div class="bed-info">
            <p class="bed-number">床位号：{{ bed.bedNumber }}</p>
            <p class="elder-name">老人姓名：{{ bed.elderName }}</p>
          </div>

          <div class="nurse-tags">
            <span class="tag-label">护理员：</span>
            <el-tag
                v-for="(name, idx) in bed.nurseNames"
                :key="idx"
                class="nurse-tag"
                size="small"
            >{{ name }}</el-tag>
            <span v-if="!bed.nurseNames?.length" class="text-gray-500 text-sm">未安排</span>
          </div>

          <el-button type="default" size="small" @click="handleSingleAssign(bed)">
            设置护理员
          </el-button>
        </div>
      </div>
    </div>

    <!-- 设置护理员弹窗 -->
    <el-dialog v-model="assignDialogVisible" title="设置护理员" width="500px" append-to-body>
      <el-form ref="assignFormRef" :model="assignForm" label-width="120px">
        <el-form-item label="护理员姓名" prop="nurseIds" required>
          <el-select
              v-model="assignForm.nurseIds"
              multiple
              filterable
              placeholder="请选择护理员"
              style="width: 100%"
              @change="handleNurseSelectChange"
          >
            <el-option
                v-for="nurse in nurseList"
                :key="nurse.id"
                :label="nurse.name"
                :value="nurse.id"
            />
          </el-select>
          <div class="text-gray-500 text-xs mt-1">最多可选择4个护理员</div>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="assignDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAssignSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts" name="ResponsibleElder">
import { ref, computed, onMounted, reactive } from 'vue'
import { listMyElder , assignNurse, batchAssignNurse} from '@/api/code/responsible'
import { listNurse } from '@/api/code/nurse'
import type { NurseQueryParams } from "@/types/api/code/nurse"

const { proxy } = getCurrentInstance()

// 老人数据
const elderList = ref<any[]>([])
const activeFloor = ref('')
const loading = ref(true)

// 护理员列表
const nurseList = ref<any[]>([])
const nurseQuery = reactive<NurseQueryParams>({
  pageNum: 1,
  pageSize: 100,
  name: undefined,
  phone: undefined
})

// 弹窗数据
const assignDialogVisible = ref(false)
const assignForm = reactive({
  nurseIds: [] as number[]
})
const currentAssign = ref({
  type: 'single' as 'single' | 'batch',
  elderId: null as number | null,
  roomId: null as number | null
})

const floorList = computed(() => {
  const map = new Map()
  elderList.value.forEach(item => {
    if (!map.has(item.floorId)) {
      map.set(item.floorId, { id: item.floorId, name: item.floorName })
    }
  })
  // 按楼层ID升序排列
  return Array.from(map.values()).sort((a, b) => a.id - b.id)
})

// 房间+床位列表（按房间号升序）
const roomList = computed(() => {
  const current = elderList.value.filter(e => e.floorId + '' === activeFloor.value)
  const map = new Map()
  current.forEach(item => {
    if (!map.has(item.roomId)) {
      map.set(item.roomId, {
        id: item.roomId,
        code: item.roomCode,
        beds: []
      })
    }
    map.get(item.roomId).beds.push({
      id: item.bedId,
      bedNumber: item.bedNumber,
      elderName: item.elderName,
      elderId: item.elderId,
      nurseNames: item.nurseNames ? item.nurseNames.split(' ').filter(Boolean) : []
    })
  })
  return Array.from(map.values()).sort((a, b) => parseInt(a.code) - parseInt(b.code))
})

// 获取老人列表
async function getList() {
  loading.value = true
  try {
    const res = await listMyElder()
    elderList.value = res.data
    if (res.data.length) {
      activeFloor.value = res.data[0].floorId + ''
    }
  } catch (err) {
    proxy.$modal.msgError("获取老人列表失败")
  } finally {
    loading.value = false
  }
}

// 获取护理员列表
async function getNurseList() {
  try {
    const res = await listNurse(nurseQuery)
    nurseList.value = res.rows
  } catch (e) {
    console.error("获取护理员列表失败", e)
    proxy.$modal.msgWarning("护理员列表加载失败")
  }
}

function handleSingleAssign(bed: any) {
  currentAssign.value = { type: 'single', elderId: bed.elderId, roomId: null }
  const selectedIds = bed.nurseNames
      .map((name: string) => nurseList.value.find(n => n.name === name)?.id)
      .filter(Boolean) as number[]
  assignForm.nurseIds = selectedIds
  assignDialogVisible.value = true
}

// 批量设置
function handleBatchAssign(room: any) {
  currentAssign.value = { type: 'batch', elderId: null, roomId: room.id }
  assignForm.nurseIds = [] // 初始空选
  assignDialogVisible.value = true
}

// 限制最多选4个
function handleNurseSelectChange(ids: number[]) {
  if (ids.length > 4) {
    assignForm.nurseIds = ids.slice(0, 4)
    proxy.$modal.msgWarning("最多只能选择4个护理员")
  }
}

// 提交设置
async function handleAssignSubmit() {
  if (!assignForm.nurseIds.length) {
    return proxy.$modal.msgWarning("请至少选择1个护理员")
  }
  try {
    if (currentAssign.value.type === 'single' && currentAssign.value.elderId) {
      await assignNurse(currentAssign.value.elderId, assignForm.nurseIds)
      proxy.$modal.msgSuccess("设置成功")
    } else if (currentAssign.value.type === 'batch' && currentAssign.value.roomId) {
      await batchAssignNurse(currentAssign.value.roomId, assignForm.nurseIds)
      proxy.$modal.msgSuccess("批量设置成功")
    }
    assignDialogVisible.value = false
    getList()
  } catch (err) {
    proxy.$modal.msgError("操作失败")
  }
}

onMounted(() => {
  getList()
  getNurseList()
})
</script>

<style scoped>
.room-section {
  margin-bottom: 20px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
}
.room-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  border-bottom: 1px solid #e4e7ed;
}
.bed-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
  padding: 16px;
}
.bed-card {
  background: #f9f9f9;
  padding: 12px;
  border-radius: 6px;
  border: 1px solid #e4e7ed;
}
.nurse-tags {
  margin: 8px 0;
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
}
.nurse-tag {
  background: #fff;
  border: 1px solid #409eff;
  color: #409eff;
}
</style>