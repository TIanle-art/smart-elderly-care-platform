<template>
  <div class="dashboard-page">
    <!-- 第一行：数据概览 + 我的信息 -->
    <el-row :gutter="20" class="mb-4">
      <!-- 模块1：数据概览 -->
      <el-col :span="16">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span class="title">数据概览</span>
              <span class="update-time">数据更新：{{ nowDate }}</span>
            </div>
          </template>

          <div class="overview-row">
            <div class="overview-item">
              <div class="item-title">老人数量</div>
              <div class="chart-box" :ref="setChartRef"></div>
            </div>
            <div class="overview-item">
              <div class="item-title">床位数量</div>
              <div class="chart-box" :ref="setChartRef"></div>
            </div>
            <div class="overview-item">
              <div class="item-title">服务单数量</div>
              <div class="chart-box" :ref="setChartRef"></div>
            </div>
            <div class="overview-item">
              <div class="item-title">员工数量</div>
              <div class="chart-box" :ref="setChartRef"></div>
            </div>
            <div class="overview-item">
              <div class="item-title">收入金额</div>
              <div class="chart-box" :ref="setChartRef"></div>
            </div>
          </div>
        </el-card>
      </el-col>
      <!-- 模块2：我的信息 -->
      <el-col :span="8">
        <el-card shadow="hover" style="height: 265px">
          <template #header>
            <span class="title">我的信息</span>
          </template>
          <div class="info-card">
            <div class="info-top">
              <el-avatar :size="70" :src="userStore.avatar" />
              <p class="greeting">您好，{{ userStore.nickName }}，今天也是元气满满的一天！</p>
            </div>

            <div class="info-list">
              <div class="info-item">账号：{{ userStore.name }}</div>
              <div class="info-item">职位：{{ userStore.roleName || '无' }}</div>
              <div class="info-item">部门：{{ userStore.dept?.deptName || '无' }}</div>
              <div class="info-item">岗位：{{ userStore.postNames || '无' }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 第二行：数据统计 + 快捷方式 -->
    <el-row :gutter="20" class="mb-4">
     <!-- 模块1：数据统计 -->
      <el-col :span="16">
        <el-card shadow="hover">
          <template #header>
            <span class="title">数据统计</span>
            <div class="card-header">
              <div class="tabs">
                <span class="tab-item" :class="{ active: activeTab === 'income' }" @click="switchTab('income')">
                  收益情况
                </span>
                <span class="tab-item" :class="{ active: activeTab === 'checkin' }" @click="switchTab('checkin')">
                  入退情况
                </span>
                <span class="tab-item" :class="{ active: activeTab === 'service' }" @click="switchTab('service')">
                  服务情况
                </span>
              </div>

              <div class="time-filter">
                <el-radio-group v-model="statTimeRange" size="small" @change="refreshChart">
                  <el-radio-button label="today">今日</el-radio-button>
                  <el-radio-button label="week">本周</el-radio-button>
                  <el-radio-button label="month">本月</el-radio-button>
                </el-radio-group>

                <el-date-picker
                    v-model="statDateRange"
                    type="daterange"
                    size="small"
                    placeholder="选择日期"
                    value-format="YYYY-MM-DD"
                    @change="refreshChart"
                />
              </div>
            </div>
          </template>
          <!-- 图表 -->
          <div class="chart-box" ref="chartRef" style="width: 100%; height: 240px;"></div>
        </el-card>
      </el-col>

      <!-- 模块2：快捷方式 -->
      <el-col :span="8">
        <el-card shadow="hover">
          <template #header>
            <span class="title">快捷方式</span>
          </template>
          <div class="shortcut-grid">
            <div class="shortcut-item" @click="goToMyTo">
              <div class="icon-box">待办</div>
              <span class="icon-box-text">待办事项</span>
            </div>
            <div class="shortcut-item" @click="goToBillList">
              <div class="icon-box">入账</div>
              <span class="icon-box-text">入账列表</span>
            </div>
            <div class="shortcut-item" @click="goToOrderManage">
              <div class="icon-box">订单</div>
              <span class="icon-box-text">订单管理</span>
            </div>
            <div class="shortcut-item" @click="goToRefundManage">
              <div class="icon-box">退款</div>
              <span class="icon-box-text">退款管理</span>
            </div>
            <div class="shortcut-item" @click="goToCheckIn">
              <div class="icon-box">入住</div>
              <span class="icon-box-text">入住申请</span>
            </div>
            <div class="shortcut-item" @click="goToCheckoutApply">
              <div class="icon-box">退住</div>
              <span class="icon-box-text">退住申请</span>
            </div>
            <div class="shortcut-item" @click="goToLeaveApply">
              <div class="icon-box">请假</div>
              <span class="icon-box-text">请假申请</span>
            </div>
            <div class="shortcut-item" @click="goToVisitorArrival">
              <div class="icon-box">预约</div>
              <span class="icon-box-text">预约登记</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 第三行：待办事项 + 预约总览 -->
    <el-row :gutter="20" class="mb-4">
      <!-- 模块1：代办事项 -->
      <el-col :span="16">
        <el-card shadow="hover">
          <template #header>
            <span class="title">待办事项</span>
          </template>
          <div class="todo-list">
            <div v-for="(item, index) in todoList" :key="item.taskId" class="todo-item">
              <div class="todo-left">
                <span :class="['todo-type', getTypeClass(item.category)]">
              {{ item.category }}
            </span>
                <div class="todo-info">
                  <div class="todo-title">
                    {{ item.taskName }}
                  </div>
                  <div class="todo-meta">
                    申请时间：{{ formatDate(item.applyTime) }} | 状态：{{ item.flowStatus }}
                  </div>
                </div>
              </div>

              <!-- 审批按钮 -->
              <div class="todo-right">
                <el-button type="primary" @click="goToMyTo">审批</el-button>
              </div>
            </div>
            <div v-if="todoList.length === 0" style="text-align:center;color:#999;padding:20px;">
              暂无待办
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 预约总览 -->
      <el-col :span="8">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span class="title">预约总览</span>
              <el-button type="text" size="small" @click="goToday">回到今日</el-button>
            </div>
          </template>
          <div class="calendar-section">
            <div class="date-header">
              <span class="current-date">{{ displayDate }}</span>
            </div>
            <div class="calendar-header">
              <!-- 前一天按钮 -->
              <el-button size="small" @click="prevDay"> &lt; </el-button>
              <div class="calendar-days">
                <div
                    v-for="(item, idx) in weekList"
                    :key="idx"
                    class="day-item"
                    :class="{ active: item.hasReserve, selected: selectedDate === item.date }"
                    @click="selectDay(item.date)"
                >
                  <div class="weekday">{{ item.weekday }}</div>
                  <div class="daynum">{{ item.day }}</div>
                  <div v-if="item.hasReserve" class="reserve-dot"></div>
                </div>
              </div>
              <!-- 后一天按钮 -->
              <el-button size="small" @click="nextDay"> &gt; </el-button>
            </div>
            <div class="schedule-list" style="max-height:220px; overflow-y:auto;">
              <div v-for="item in reserveList" :key="item.id" class="schedule-item">
                <div class="time">{{ item.reservationTime }}</div>
                <div class="schedule-content">
                  <span class="tag" :class="item.type === '0' ? 'tag-visit' : 'tag-interview'">
                {{ item.type === '0' ? '参观' : '探访' }}
              </span>
                  <span>预约人：{{ item.name }} | 手机号：{{ item.phone }}</span>
                </div>
              </div>
              <div v-if="reserveList.length === 0" style="text-align:center;color:#999;padding:20px;">
                暂无预约
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 第四行：三个图表 -->
    <el-row :gutter="20" class="mb-4" style="margin-top: 24px;">
     <!-- 老人等级分布 -->
      <el-col :span="8">
        <el-card>
          <span class="title">老人等级分布</span>
          <div ref="chart1" class="chart-box" style="height:280px"/>
        </el-card>
      </el-col>
      <!-- 年龄性别统计 -->
      <el-col :span="8">
        <el-card>
          <span class="title">年龄性别统计</span>
          <div ref="chart2" class="chart-box" style="height:280px"/>
        </el-card>
      </el-col>
      <!-- 服务能力分析 -->
      <el-col :span="8">
        <el-card>
          <span class="title">服务能力分析</span>
          <div ref="chart3" class="chart-box" style="height:280px"/>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
<script setup>
import { ref, onMounted, nextTick, onUnmounted, watch } from 'vue'
import * as echarts from 'echarts'
import { ElMessage } from 'element-plus'
import { listArrival, getServiceStat, getCheckInOutStat, getNursingLevelStat, getAgeGenderStat, getServiceAbilityStat,getIncomeStat} from '@/api/index'
import { listReservation } from '@/api/code/reservation'
import { ROUTE_PATHS } from '@/router/route-constants'
import useUserStore from '@/store/modules/user'
import { useRouter } from 'vue-router'
import {getMyTodoList} from '@/api/system/myto.js'

// -------------------------代办事项-------------------------
const todoList = ref([])


// 加载待办列表
const loadTodoList = async () => {
  try {
    const res = await getMyTodoList()
    todoList.value = res.data
  } catch (err) {
    console.error("加载待办失败：", err)
    ElMessage.error("加载待办失败，请稍后重试")
  }
}

// 跳转请假我的待办列表
const goToLeave = () => {
  router.push('/code/leave-todo/index')
}

// 跳转退住申请列表
const goToCheckout = () => {
  router.push('/retreat/checkout')
}

 

// const goToMyTo = () => {
//   router.push('/system/my-to/index')
// }


// 存储所有图表DOM
const chartRefs = ref([])
let myCharts = []
const timeRange = ref('today')

// 收集多个ref的方法
const setChartRef = (el) => {
  if (el) {
    chartRefs.value.push(el)
  }
}

const getTypeClass = (category) => {
  if (category === '请假') return 'leave'
  if (category === '入住') return 'checkin'
  if (category === '退住') return 'retreat'
  return ''
}

// ---------------------快捷方式-------------------
const router = useRouter()
const userStore = useUserStore()

const goToCheckIn = () => router.push(ROUTE_PATHS.CHECKIN_APPLY)
const goToMyTo = () => router.push(ROUTE_PATHS.MY_TO)
const goToCheckoutApply = () => router.push(ROUTE_PATHS.CHECKOUT_APPLY)
const goToLeaveApply = () => router.push(ROUTE_PATHS.LEAVE_APPLY)
const goToVisitorArrival = () => router.push(ROUTE_PATHS.VISITOR_ARRIVAL)
const goToOrderManage = () => router.push(ROUTE_PATHS.ORDER_MANAGE)
const goToBillList=()=>router.push(ROUTE_PATHS.BILL_LIST)
const goToRefundManage=()=>router.push(ROUTE_PATHS.REFUND_MANAGE)


// ---------------------数据概览------------------
// const chartRefs = ref([])
// let myCharts = []
// const timeRange = ref('today')
const nowDate = ref('')

const dashboardData = ref({
  elderTotal: 0,
  elderLive: 0,
  elderLeave: 0,
  bedTotal: 0,
  bedUse: 0,
  bedSurplus: 0,
  staffTotal: 0,
  staffMan: 0,
  staffWoman: 0,
  serviceFee:0,
  monthFee:0,
  incomeTotal: 0
})

// const setChartRef = (el) => {
//   if (el) chartRefs.value.push(el)
// }

// 饼图配置
function getPieOption(total, dataList) {
  return {
    tooltip: { trigger: 'item', formatter: '{b}：{c}' },
    legend: { bottom: 0, itemWidth: 8, itemHeight: 8, textStyle: { fontSize: 11 } },
    series: [{
      type: 'pie', radius: ['60%', '80%'], center: ['50%', '40%'],
      label: { show: true, position: 'center', formatter: total + '', fontSize: 22, fontWeight: 'bold' },
      labelLine: { show: false },
      data: dataList
    }]
  }
}

// 加载数据
async function loadCountData() {
  try {
    const res = await listArrival({})
    dashboardData.value = { ...dashboardData.value, ...res.data }
    nowDate.value = new Date().toLocaleDateString('zh-CN')
  } catch (err) {
    console.error('获取统计数据失败', err)
  } finally {
    renderAllCharts()
  }
}

// 渲染图表
function renderAllCharts() {
  const d = dashboardData.value
  const chartData = [
    { total: d.elderTotal, data: [
        { name: '在院中', value: d.elderLive, itemStyle: { color: '#4D75FF' } },
        { name: '外出中', value: d.elderLeave, itemStyle: { color: '#36CFC9' } }
      ]},
    { total: d.bedTotal, data: [
        { name: '入住中', value: d.bedUse, itemStyle: { color: '#72CCBA' } },
        { name: '空闲中', value: d.bedSurplus, itemStyle: { color: '#B7E5DC' } }
      ]},
    { total: 24, data: [
        { name: '计划内', value: 10, itemStyle: { color: '#FF9C52' } },
        { name: '计划外', value: 14, itemStyle: { color: '#FFCF56' } }
      ]},
    { total: d.staffTotal, data: [
        { name: '男员工', value: d.staffMan, itemStyle: { color: '#9277FF' } },
        { name: '女员工', value: d.staffWoman, itemStyle: { color: '#C3BDFF' } }
      ]},
    { total: d.incomeTotal, data: [
        { name: '服务费', value: d.serviceFee, itemStyle: { color: '#F76599' } },
        { name: '月度费', value: d.monthFee, itemStyle: { color: '#FFA8C1' } }
      ]}
  ]

  // 循环生成图表
  chartRefs.value.forEach((el, index) => {
    if (!el) return
    const chart = echarts.init(el)
    chart.setOption(getPieOption(chartData[index].total, chartData[index].data))
    myCharts.push(chart)
  })
}


// ================== 数据统计图表 ==================
const isLoading = ref(false)
const activeTab = ref('checkin')
const statTimeRange = ref('today')
const statDateRange = ref([])
const chartRef = ref(null)
let mainChart = null

// 切换类型
const switchTab = (tab) => {
  activeTab.value = tab
  refreshChart()
}

async function refreshChart() {
  if (isLoading.value) return
  isLoading.value = true

  try {
    const params = {
      timeType: statTimeRange.value,
      startDate: statDateRange.value?.[0] || "",
      endDate: statDateRange.value?.[1] || ""
    }

    if (activeTab.value === 'checkin') {
      // 入退情况
      const res = await getCheckInOutStat(params)
      const { xData, checkInData, retreatData } = res.data

      mainChart.setOption({
        tooltip: { trigger: 'axis' },
        legend: { data: ['入住', '退住'], bottom: 10 },
        grid: { left: '3%', right: '4%', top: '15%', bottom: '15%', containLabel: true },
        xAxis: { type: 'category', data: xData },
        yAxis: { type: 'value',max: (value) => Math.ceil(value.max), },
        series: [
          { name: '入住', type: 'line', smooth: true, data: checkInData, itemStyle: { color: '#409EFF' } },
          { name: '退住', type: 'line', smooth: true, data: retreatData, itemStyle: { color: '#67C23A' } }
        ]
      }, true)
    } else if (activeTab.value === 'income') {
      // 收益情况
      try {
        const res = await getIncomeStat(params)
        const { xData, incomeData, refundData } = res.data

        mainChart.setOption({
          tooltip: { trigger: 'axis' },
          legend: { data: ['收入', '退款'], bottom: 10 },
          grid: { left: '3%', right: '4%', top: '15%', bottom: '15%', containLabel: true },
          xAxis: { type: 'category', data: xData },
          yAxis: { type: 'value',max: (value) => Math.ceil(value.max), },
          series: [
            {name: '收入', type: 'line', smooth: true, data: incomeData, itemStyle: { color: '#409EFF' }},
            {name: '退款', type: 'line', smooth: true, data: refundData, itemStyle: { color: '#F56C6C' }
            }
          ]
        }, true)
      } catch (e) {
        console.error('收益图表加载失败', e)
        mainChart.setOption({
          tooltip: { trigger: 'axis' },
          legend: { data: ['收入', '退款'] },
          xAxis: { data: [] },
          yAxis: {max: (value) => Math.ceil(value.max)},
          series: [{ name: '收入', data: [] }, { name: '退款', data: [] }]
        }, true)
      }
    } else if (activeTab.value === 'service') {
      // 服务情况
      try {
        const res = await getServiceStat({
          timeType: statTimeRange.value,
          startDate: statDateRange.value?.[0] || '',
          endDate: statDateRange.value?.[1] || ''
        })

        const { xData, serviceData } = res.data

        mainChart.setOption({
          tooltip: { trigger: 'axis' },
          legend: { data: ['服务次数'], bottom: 10 },
          grid: { left: '3%', right: '4%', top: '15%', bottom: '15%', containLabel: true },
          xAxis: { type: 'category', data: xData },
          yAxis: { type: 'value',max: (value) => Math.ceil(value.max) },
          series: [{name: '服务次数', type: 'line', smooth: true, data: serviceData, itemStyle: { color: '#409EFF' }, areaStyle: { color: 'rgba(64,158,255,0.1)' }
          }]
        }, true)
      } catch (error) {
        console.error('服务图表加载失败', error)
        mainChart.setOption({
          tooltip: { trigger: 'axis' },
          xAxis: { data: [] },
          yAxis: {max: (value) => Math.ceil(value.max)},
          series: [{ name: '服务次数', data: [] }]
        }, true)
      }
    }
  } catch (globalError) {
    console.error('图表刷新失败', globalError)
  } finally {
    // 关闭加载
    isLoading.value = false
  }
}

// ---------------页面加载执行--------------
// 最下方图表
const chart1 = ref()
const chart2 = ref()
const chart3 = ref()

onMounted(async () => {
  await nextTick()
  mainChart = echarts.init(chartRef.value)
  await refreshChart()
  const c1 = echarts.init(chart1.value)
  const c2 = echarts.init(chart2.value)
  const c3 = echarts.init(chart3.value)

  // 老人等级饼图
  const d1 = await getNursingLevelStat()
  c1.setOption({
    tooltip: { trigger: 'item', formatter: "{b}：{c}人" },
    legend: { orient: 'horizontal', bottom: 0, left: 'center', textStyle: { fontSize: 12 } },
    series: [{
      type: "pie",
      radius: ["30%", "55%"],
      center: ["50%", "40%"],
      data: d1.data,
      label: { formatter: "{b}\n{c}人", fontSize: 10 },
      itemStyle: { borderRadius: 6, borderColor: '#fff', borderWidth: 2 }
    }]
  })

  // 年龄性别柱状图
  const d2 = await getAgeGenderStat()
  c2.setOption({
    tooltip: {trigger: "axis", formatter: "{b}<br/>{a}：{c} 人", backgroundColor: 'rgba(255,255,255,0.9)', borderColor: '#eee'},
    legend: { data: ["男", "女"], bottom: 5 },
    grid: { left: "5%", right: "5%", top: "10%", bottom: "15%", containLabel: true },
    xAxis: {data: d2.data.xData, type: 'category', axisLabel: { fontSize: 12 }},
    yAxis: {type: "value", max: (value) => Math.ceil(value.max), axisLabel: { fontSize: 12 }},
    series: [
      {name: "男", type: "bar", data: d2.data.maleData, itemStyle: { color: "#409EFF" }, barWidth: '30%'},
      {name: "女", type: "bar", data: d2.data.femaleData, itemStyle: { color: "#67C23A" }, barWidth: '30%'}
    ]
  })

  // 服务能力雷达图
  const d3 = await getServiceAbilityStat()

  const serviceData = d3.data
      .filter(item => item.name && item.value != null)
      .map(item => ({
        name: String(item.name),
        value: Number(item.value) || 0
      }))

  const allValues = serviceData.map(item => item.value)
  const maxValue = Math.max(...allValues) || 10

  const indicator = serviceData.map(item => ({
    name: item.name,
    max: maxValue * 1.2
  }))

//生成图表数据
  const chartData = [serviceData.map(item => item.value)]

  c3.setOption({
    tooltip: {trigger: 'item',
      formatter: (params) => {
        return `${params.name}<br/>服务单数量：${params.value} 笔`
      },
      backgroundColor: 'rgba(255,255,255,0.95)',
      borderColor: '#eee',
      textStyle: { color: '#333', fontSize: 14 }
    },
    radar: {
      radius: '65%',
      indicator: indicator,
      axisName: { fontSize: 14, color: '#666' },
      splitArea: { areaStyle: { color: 'rgba(64,158,255,0.05)' } },
      splitLine: { lineStyle: { color: '#eee' } }
    },
    series: [{
      type: 'radar',
      data: chartData,
      areaStyle: { color: 'rgba(64,158,255,0.2)' },
      lineStyle: { color: '#409EFF', width: 3 },
      itemStyle: { color: '#409EFF' },
      symbol: 'circle',
      symbolSize: 10
    }]
  })

  // 监听窗口变化，自适应图表
  window.addEventListener('resize', () => {
    mainChart?.resize()
    c1?.resize()
    c2?.resize()
    c3?.resize()
  })
})

// ================== 日历 & 预约 ==================
const weekMap = ['日', '一', '二', '三', '四', '五', '六']
const selectedDate = ref('')
const currentDate = ref(new Date())
const weekList = ref([])
const reserveList = ref([])
const displayDate = ref('')

// 日期格式化
const formatDate = (date) => {
  const d = new Date(date)
  const y = d.getFullYear()
  const m = String(d.getMonth() + 1).padStart(2, '0')
  const dStr = String(d.getDate()).padStart(2, '0')
  return `${y}-${m}-${dStr}`
}
//时间格式化
const formatTime = (dateStr) => {
  const date = new Date(dateStr)
  const h = String(date.getHours()).padStart(2, '0')
  const m = String(date.getMinutes()).padStart(2, '0')
  return `${h}:${m}`
}

const initWeekList = (startDate) => {
  const list = []
  const now = new Date(startDate || currentDate.value)
  for (let i = 0; i < 7; i++) {
    const d = new Date(now)
    d.setDate(now.getDate() + i)
    const str = formatDate(d)
    list.push({ date: str, day: d.getDate(), weekday: weekMap[d.getDay()], hasReserve: false })
  }
  weekList.value = list
  if (!selectedDate.value) {
    selectedDate.value = list[0].date
    displayDate.value = list[0].date
  }
}
// 加载预约
const loadReserve = async () => {
  try {
    const res = await listReservation({})
    const allData = res.rows || []
    weekList.value.forEach(day => {
      day.hasReserve = allData.some(x => formatDate(x.appointmentTime) === day.date)
    })
    reserveList.value = allData
        .filter(x => formatDate(x.appointmentTime) === selectedDate.value)
        .map(item => ({ ...item, reservationTime: formatTime(item.appointmentTime) }))
  } catch (err) {
    console.error('加载预约失败', err)
  }
}

const prevDay = () => {
  const d = new Date(currentDate.value)
  d.setDate(d.getDate() - 1)
  currentDate.value = d
  initWeekList(d)
  selectedDate.value = weekList.value[0].date
  displayDate.value = selectedDate.value
  loadReserve()
}

const nextDay = () => {
  const d = new Date(currentDate.value)
  d.setDate(d.getDate() + 1)
  currentDate.value = d
  initWeekList(d)
  selectedDate.value = weekList.value[0].date
  displayDate.value = selectedDate.value
  loadReserve()
}

const goToday = () => {
  const today = new Date()
  currentDate.value = today
  initWeekList(today)
  selectedDate.value = formatDate(today)
  displayDate.value = selectedDate.value
  loadReserve()
}

const selectDay = (date) => {
  selectedDate.value = date
  displayDate.value = date
  loadReserve()
}

// ================== 生命周期 ==================
onMounted(async () => {
  await nextTick()
  const today = new Date()
  currentDate.value = today
  initWeekList(today)
  displayDate.value = formatDate(today)
  loadReserve()
  loadTodoList()
  loadCountData()
  window.addEventListener('resize', () => {
    myCharts.forEach(c => c.resize())
    mainChart?.resize()
  })
})

onUnmounted(() => {
  myCharts.forEach(c => c.dispose())
  mainChart?.dispose()
})
</script>
<style>
/* 数据统计标签 */
.tabs {
  display: flex;
  gap: 12px;
}
.tab-item {
  font-size: 12px;
  padding: 8px;
  color: #666;
  cursor: pointer;
}
.tab-item.active {
  background: #e8f3ff;
  color: #409eff;
  font-weight: 500;
}
.time-filter {
  display: flex;
  gap: 12px;
  align-items: center;
}
.chart-box {
  width: 100%;
  height: 160px;
}

/* 页面背景 */
.dashboard-page {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}
.mb-4 {
  margin-bottom: 20px;
}

/* 卡片标题 */
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.title {
  font-size: 18px;
  font-weight: bold;
}
.update-time {
  font-size: 13px;
  color: #909399;
}

/* 数据概览行 */
.overview-row {
  display: flex;
  justify-content: space-between;
  gap: 8px;
}
.overview-item {
  flex: 1;
  text-align: center;
}
.item-title {
  font-size: 14px;
  margin-bottom: 6px;
  color: #333;
}

/* 我的信息 */
.info-card {
  display: flex;
  flex-direction: column;
  justify-content: center;
  height: 100%;
  gap: 20px; }
.info-top {
  display: flex;
  align-items: center;
  gap: 16px;
}
.greeting {
  font-size: 16px;
  font-weight: 500;
  margin: 0;
}
.info-list {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px 20px;
  font-size: 14px;
  color: #666;
}
.info-item {
  white-space: nowrap;
}

/* 快捷方式 */
.shortcut-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 12px;
  padding: 8px 0;
}
.shortcut-item {
  width: 70px;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
  padding: 10px 8px;
  border-radius: 10px;
  background: #f8faff;
}
.shortcut-item:hover {
  background: #e8f3ff;
  transform: translateY(-2px);
}
.icon-box {
  width: 50px;
  height: 60px;
  border-radius: 8px;
  background: #f0f5ff;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #409eff;
  font-size: 12px;
}
.shortcut-item:hover .icon-box {
  background: #409eff;
  color: #fff;
}
.icon-box-text {
  font-size: 12px;
  font-weight: 500;
  color: #303133;
}

/* 待办事项 */
.todo-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}
.todo-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
}
.todo-left {
  display: flex;
  align-items: center;
  flex: 1;
}
.todo-right {
  margin-left: 20px;
}
.todo-type {
  padding: 4px 10px;
  border-radius: 4px;
  color: #fff;
  font-size: 13px;
  margin-right: 12px;
}
.todo-type.leave {
  background-color: #f56c6c !important;
  font-weight: bold;
}
.todo-type.checkin {
  background-color: mediumseagreen !important;
  font-weight: bold;
}
.todo-type.retreat {
  background-color: cornflowerblue !important;
  font-weight: bold;
}
.todo-info {
  display: flex;
  flex-direction: column;
}
.todo-title {
  font-size: 15px;
  font-weight: 500;
}
.todo-meta {
  font-size: 12px;
  color: #999;
  margin-top: 4px;
}

/* 日历 & 预约总览 */
.date-header {
  margin-bottom: 12px;
  font-size: 16px;
  color: #666;
}
.current-date {
  font-weight: 500;
}
.calendar-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 12px;
}
.calendar-days {
  display: flex;
  gap: 4px;
}
.day-item {
  position: relative;
  width: 36px;
  height: 36px;
  border-radius: 50%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  cursor: pointer;
  background: #f5f7fa;
  transition: all 0.2s ease;
}
.day-item:hover {
  background: #e8f3ff;
}
.day-item.selected {
  background: #409eff;
  color: #fff;
}
.weekday {
  font-size: 12px;
  margin-bottom: 2px;
}
.daynum {
  font-size: 15px;
  font-weight: 500;
}

/* 有预约 → 底部小蓝点 */
.day-item.active::after {
  content: "";
  width: 6px;
  height: 6px;
  background: #409eff;
  border-radius: 50%;
  position: absolute;
  bottom: 4px;
}

/* 预约列表 */
.schedule-item {
  display: flex;
  gap: 12px;
  margin-bottom: 10px;
  align-items: center;
}
.time {
  min-width: 60px;
  font-size: 13px;
  color: #666;
}
.schedule-content {
  font-size: 16px;
  display: flex;
  align-items: center;
  gap: 8px;
  flex: 1;
}

/* 标签：参观橙色 / 探访蓝色 */
.tag {
  padding: 4px 8px;
  border-radius: 4px;
  color: #fff;
  font-size: 12px;
  white-space: nowrap;
}
.tag-visit {
  background: #f5a623;
}
.tag-interview {
  background: #409eff;
}
</style>