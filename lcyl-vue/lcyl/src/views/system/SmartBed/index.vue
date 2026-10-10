<template>
  <div class="smart-bed-container">
    <!-- 楼层切换 Tab（紧凑样式） -->
    <div class="floor-tabs-wrapper">
      <div
          class="floor-tab"
          :class="{ active: activeFloor === item }"
          v-for="item in floorList"
          :key="item"
          @click="handleFloorChange(item)"
      >
        <span class="tab-text">{{ item }}</span>
        <!-- 报警红点：示例给1楼加报警点 -->
        <div v-if="item === '1楼'" class="alarm-dot"></div>
      </div>
    </div>

    <!-- 当前楼层房间内容 -->
    <div class="floor-content">
      <div
          class="room-card"
          v-for="room in currentFloorData"
          :key="room.roomCode"
      >
        <div class="room-header">
          <div class="room-title">
            <span>房间号：{{ room.roomCode }}</span>
            <div class="room-icon">🔔</div>
            <div class="room-icon">📹</div>
            <div class="room-icon">🔒</div>
          </div>
          <div class="room-info">
            <span>房门状态：{{ room.doorStatus }}</span>
            <span style="margin-left: 20px">温度：{{ room.temp }}℃</span>
            <span style="margin-left: 20px">湿度：{{ room.humidity }}%</span>
            <span style="margin-left: 20px">报警状态：{{ room.alarmStatus }}</span>
          </div>
        </div>

        <div class="bed-grid">
          <div
              class="bed-item"
              :class="{ 'bed-warning': bed.isWarning }"
              v-for="bed in room.beds"
              :key="bed.bedCode"
          >
            <div class="bed-header">
              <span>床位号：{{ bed.bedCode }}</span>
              <div class="bed-actions">📄 ⌚ ➡ ⚠️</div>
            </div>
            <div class="bed-elder">
              <span>老人姓名：{{ bed.elderName || '-' }}</span>
            </div>

            <!-- 空床位 -->
            <div v-if="!bed.elderName" class="bed-empty">
              当前床位没有安排老人
            </div>

            <!-- 已离床 -->
            <div v-else-if="bed.status === '已离床'" class="bed-data">
              <div class="bed-status-icon">🛏️ 已离床</div>
              <div class="bed-outbed">
                <div class="bed-row">👤 离床次数：{{ bed.outBedCount }}次</div>
                <div class="bed-row">⏰ 离床时间：{{ bed.outBedTime }}</div>
              </div>
            </div>

            <!-- 清醒/睡眠中 -->
            <div v-else class="bed-data">
              <div class="bed-status-icon">🛏️ {{ bed.status }}</div>
              <div class="bed-vitals">
                <div class="bed-row">❤️ 心率：{{ bed.heartRate }}次/分</div>
                <div class="bed-row">🫁 呼吸率：{{ bed.breathRate }}次/分</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

// 楼层列表
const floorList = ref(['1楼', '2楼', '3楼'])
// 当前激活楼层
const activeFloor = ref('1楼')

// 楼层数据（1楼为你现有数据，2/3楼为模拟数据）
const floorData = ref({
  '1楼': [
    {
      roomCode: '101',
      doorStatus: '开启',
      temp: '26',
      humidity: '40',
      alarmStatus: '正常',
      beds: [
        { bedCode: '1011', elderName: '高', status: '清醒中', heartRate: 80, breathRate: 20, isWarning: false },
        { bedCode: '1012', elderName: '高启盛', status: '已离床', outBedCount: 6, outBedTime: '20:00:00', isWarning: false },
        { bedCode: '1013', elderName: '高启兰', status: '睡眠中', heartRate: 80, breathRate: 20, isWarning: false },
        { bedCode: '1014', elderName: '', status: '', isWarning: false },
        { bedCode: '1015', elderName: '', status: '', isWarning: false },
        { bedCode: '1016', elderName: '安欣', status: '睡眠中', heartRate: 80, breathRate: 20, isWarning: false }
      ]
    },
    {
      roomCode: '102',
      doorStatus: '开启',
      temp: '26',
      humidity: '40',
      alarmStatus: '正常',
      beds: [
        { bedCode: '1021', elderName: '高启强', status: '清醒中', heartRate: 0, breathRate: 0, isWarning: true },
        { bedCode: '1022', elderName: '高启盛', status: '已离床', outBedCount: 6, outBedTime: '20:00:00', isWarning: false },
        { bedCode: '1023', elderName: '高启兰', status: '睡眠中', heartRate: 80, breathRate: 20, isWarning: false }
      ]
    }
  ],
  '2楼': [
    {
      roomCode: '201',
      doorStatus: '开启',
      temp: '25',
      humidity: '42',
      alarmStatus: '正常',
      beds: [
        { bedCode: '2011', elderName: '陈康伯', status: '睡眠中', heartRate: 78, breathRate: 19, isWarning: false },
        { bedCode: '2012', elderName: '', status: '', isWarning: false },
        { bedCode: '2013', elderName: '', status: '', isWarning: false }
      ]
    },
    {
      roomCode: '205',
      doorStatus: '关闭',
      temp: '25',
      humidity: '42',
      alarmStatus: '正常',
      beds: [
        { bedCode: '2051', elderName: '', status: '', isWarning: false },
        { bedCode: '2052', elderName: '李姐', status: '清醒中', heartRate: 75, breathRate: 18, isWarning: false },
        { bedCode: '2053', elderName: '王哥', status: '已离床', outBedCount: 3, outBedTime: '19:30:00', isWarning: false }
      ]
    }
  ],
  '3楼': [
    {
      roomCode: '301',
      doorStatus: '开启',
      temp: '24',
      humidity: '45',
      alarmStatus: '正常',
      beds: [
        { bedCode: '3011', elderName: '张阿姨', status: '睡眠中', heartRate: 72, breathRate: 18, isWarning: false },
        { bedCode: '3012', elderName: '刘叔', status: '清醒中', heartRate: 76, breathRate: 19, isWarning: false },
        { bedCode: '3013', elderName: '', status: '', isWarning: false }
      ]
    },
    {
      roomCode: '302',
      doorStatus: '关闭',
      temp: '24',
      humidity: '45',
      alarmStatus: '正常',
      beds: [
        { bedCode: '3021', elderName: '赵奶奶', status: '已离床', outBedCount: 4, outBedTime: '21:00:00', isWarning: false },
        { bedCode: '3022', elderName: '钱爷爷', status: '睡眠中', heartRate: 70, breathRate: 17, isWarning: false },
        { bedCode: '3023', elderName: '', status: '', isWarning: false }
      ]
    }
  ]
})

// 计算当前楼层的房间数据
const currentFloorData = computed(() => {
  return floorData.value[activeFloor.value] || []
})

// 切换楼层
const handleFloorChange = (floor) => {
  activeFloor.value = floor
}
</script>

<style scoped>
.smart-bed-container {
  padding: 16px;
  background: #fff;
}

/* 紧凑版楼层Tab样式（与截图对齐） */
.floor-tabs-wrapper {
  display: flex;
  background: #f5f5f5;
  border-radius: 6px;
  padding: 2px;
  margin-bottom: 20px;
  width: fit-content; /* 自适应内容宽度，不占满整行 */
}
.floor-tab {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 8px 24px; /* 缩小内边距，让Tab更紧凑 */
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px; /* 缩小字号 */
  font-weight: 500;
  color: #606266;
  transition: all 0.2s ease;
  min-width: 80px;
}
.floor-tab.active {
  background: #ffffff;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
  color: #409eff;
}
.alarm-dot {
  position: absolute;
  top: 6px;
  right: 12px;
  width: 8px;
  height: 8px;
  background: #f5222d;
  border-radius: 50%;
}

.floor-content {
  margin-top: 16px;
}
.room-card {
  margin-bottom: 24px;
  padding: 16px;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  background: #fafafa;
}
.room-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}
.room-title {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 16px;
  font-weight: 500;
}
.room-icon {
  color: #409eff;
  cursor: pointer;
}
.room-info {
  font-size: 14px;
  color: #606266;
}
.bed-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
}
.bed-item {
  padding: 16px;
  border-radius: 8px;
  background: #f5f7fa;
  transition: all 0.3s;
}
.bed-warning {
  background: #fef0f0;
  border: 1px solid #fde2e2;
}
.bed-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
  font-size: 14px;
}
.bed-actions {
  display: flex;
  gap: 8px;
  color: #409eff;
}
.bed-elder {
  margin-bottom: 12px;
  font-size: 14px;
  color: #606266;
}
.bed-empty {
  padding: 24px 0;
  text-align: center;
  color: #c0c4cc;
}
.bed-data {
  display: flex;
  align-items: center;
  gap: 16px;
}
.bed-status-icon {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
  color: #409eff;
  font-size: 14px;
}
.bed-row {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
  font-size: 14px;
  color: #606266;
}
</style>