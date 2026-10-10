<template>
  <div class="evaluate-container">
    <!-- 顶部步骤条 -->
    <div class="steps-header">
      <el-steps :active="1" finish-status="success" align-center>
        <el-step title="申请入住" />
        <el-step title="入住评估" />
        <el-step title="入住审批" />
        <el-step title="入住配置" />
        <el-step title="签约办理" />
      </el-steps>
    </div>

    <!-- 主内容区 -->
    <div class="main-wrapper">
      <div class="content-left">
        <el-card>
          <template #header>
            <div class="card-header">
              <span class="title">健康能力评估</span>
            </div>
          </template>

          <el-tabs v-model="activeTab" class="eval-tabs">
            <!-- Tab1: 健康评估 -->
            <el-tab-pane label="健康评估" name="health">
              <div class="section-block">
                <div class="section-title">疾病和评估</div>
                
                <!-- 疾病诊断 -->
                <el-form label-width="140px" class="eval-form" ref="healthFormRef" :rules="healthRules" :disabled="isReadOnly">
                  <el-form-item label="*疾病诊断" prop="diseases">
                    <el-checkbox-group v-model="healthForm.diseases">
                      <el-checkbox label="无疾病" />
                      <el-checkbox label="高血压I10-I15" />
                      <el-checkbox label="糖尿病I14" />
                      <el-checkbox label="冠心病I25" />
                      <el-checkbox label="慢性阻塞性肺疾病J44" />
                      <el-checkbox label="慢性胃炎K29" />
                      <el-checkbox label="脑卒中I63" />
                      <el-checkbox label="骨质疏松症M81.9-M82" />
                      <el-checkbox label="帕金森病G20" />
                      <el-checkbox label="慢性肾脏病-慢性肾衰竭N18" />
                      <el-checkbox label="白内障H25.0-H26" />
                      <el-checkbox label="痴呆（含阿尔茨海默病）F01-F03" />
                      <el-checkbox label="骨质增生性关节炎M17.0-M19" />
                      <el-checkbox label="恶性肿瘤" />
                      <el-checkbox label="抑郁症、焦虑症F32-F33" />
                      <el-checkbox label="肺癌（支气管/肺）" />
                    </el-checkbox-group>
                  </el-form-item>

                  <!-- 用药情况 -->
                  <el-form-item label="用药情况">
                    <div class="med-table-wrapper">
                      <el-table :data="healthForm.medications" border style="width: 100%">
                        <el-table-column label="药物名称" min-width="150">
                          <template #default="{ row }">
                            <el-input v-model="row.name" placeholder="请输入" clearable />
                          </template>
                        </el-table-column>
                        <el-table-column label="服药方法" min-width="120">
                          <template #default="{ row }">
                            <el-input v-model="row.method" placeholder="请输入" clearable />
                          </template>
                        </el-table-column>
                        <el-table-column label="用药剂量" min-width="120">
                          <template #default="{ row }">
                            <el-input v-model="row.dosage" placeholder="请输入" clearable />
                          </template>
                        </el-table-column>
                        <el-table-column label="操作" width="80" align="center" fixed="right">
                          <template #default="{ $index }">
                            <el-button v-if="$index === 0" type="primary" link @click="addMedication">
                              <el-icon><Plus /></el-icon>
                            </el-button>
                            <el-button v-else type="danger" link @click="removeMedication($index)">
                              <el-icon><Delete /></el-icon>
                            </el-button>
                          </template>
                        </el-table-column>
                      </el-table>
                    </div>
                  </el-form-item>

                  <!-- 过3次内跌倒事件 -->
                  <div class="section-title" style="margin-top: 40px;">过3次内跌倒事件</div>
                  
                  <el-form-item label="*跌倒" prop="fallStatus">
                    <el-radio-group v-model="healthForm.fallStatus">
                      <el-radio label="无">无</el-radio>
                      <el-radio label="发生过1次">发生过1次</el-radio>
                      <el-radio label="发生过2次">发生过2次</el-radio>
                      <el-radio label="发生过3次及以上">发生过3次及以上</el-radio>
                    </el-radio-group>
                  </el-form-item>

                  <el-form-item label="*走失" prop="lostStatus">
                    <el-radio-group v-model="healthForm.lostStatus">
                      <el-radio label="无">无</el-radio>
                      <el-radio label="发生过1次">发生过1次</el-radio>
                      <el-radio label="发生过2次">发生过2次</el-radio>
                      <el-radio label="发生过3次及以上">发生过3次及以上</el-radio>
                    </el-radio-group>
                  </el-form-item>

                  <el-form-item label="*噎食" prop="chokeStatus">
                    <el-radio-group v-model="healthForm.chokeStatus">
                      <el-radio label="无">无</el-radio>
                      <el-radio label="发生过1次">发生过1次</el-radio>
                      <el-radio label="发生过2次">发生过2次</el-radio>
                      <el-radio label="发生过3次及以上">发生过3次及以上</el-radio>
                    </el-radio-group>
                  </el-form-item>

                  <el-form-item label="*自杀" prop="suicideStatus">
                    <el-radio-group v-model="healthForm.suicideStatus">
                      <el-radio label="无">无</el-radio>
                      <el-radio label="发生过1次">发生过1次</el-radio>
                      <el-radio label="发生过2次">发生过2次</el-radio>
                      <el-radio label="发生过3次及以上">发生过3次及以上</el-radio>
                    </el-radio-group>
                  </el-form-item>

                  <!-- 身体健康评估 -->
                  <div class="section-title" style="margin-top: 40px;">身体健康评估</div>

                  <el-form-item label="*压力性损伤" prop="pressure">
                    <el-checkbox-group v-model="healthForm.pressure">
                      <el-checkbox label="无" />
                      <el-checkbox label="难以愈合的Ⅱ度压疮" />
                      <el-checkbox label="Ⅲ度、Ⅳ度压疮" />
                    </el-checkbox-group>
                  </el-form-item>

                  <el-form-item label="*特殊皮肤护理" prop="skinCare">
                    <el-checkbox-group v-model="healthForm.skinCare">
                      <el-checkbox label="无特殊护理需求" />
                      <el-checkbox label="难以愈合的创面或造口" />
                      <el-checkbox label="气管切开" />
                      <el-checkbox label="经皮肾造瘘" />
                      <el-checkbox label="经皮肝胆引流管" />
                      <el-checkbox label="造口（结肠、回肠等）" />
                      <el-checkbox label="鼻饲" />
                      <el-checkbox label="吸痰" />
                    </el-checkbox-group>
                  </el-form-item>

                  <el-form-item label="*体检报告">
                    <FileUpload
                      v-model="healthForm.checkResult"
                      :limit="1"
                      :file-size="10"
                      :file-type="['pdf']"
                      :is-show-tip="true"
                    />
                  </el-form-item>
                </el-form>
              </div>
            </el-tab-pane>

            <!-- Tab2: 能力评估 -->
            <el-tab-pane label="能力评估" name="ability">
              <div class="section-block">
                <div class="section-title">自理能力评估</div>
                
                <div class="ability-tip">
                  <el-alert type="info" :closable="false">
                    <template #default>
                      <div><strong>能力评估共4类：</strong></div>
                      <div style="margin-top: 8px; line-height: 1.8;">
                        <div><strong>① 自理能力分值：</strong>总分0-45分，包括进食、洗澡、修饰、穿衣等10项指标</div>
                        <div><strong>② 精神状态分值：</strong>总分30分</div>
                        <div><strong>③ 感知觉与沟通分值：</strong>总分10分</div>
                        <div><strong>④ 社会参与分值：</strong>总分10分</div>
                      </div>
                    </template>
                  </el-alert>
                </div>

                <el-form label-width="180px" class="ability-form" ref="abilityFormRef" :disabled="isReadOnly">
                  <!-- ① 自理能力评估 -->
                  <div class="ability-section">
                    <div class="ability-section-title">
                      <span class="number">①</span> 自理能力评估（请选择对应选项）
                    </div>
                    <div v-for="(item, index) in abilityQuestions" :key="index" class="question-item">
                      <div class="question-title">
                        <span class="q-num">*{{ index + 1 }}、{{ item.question }}</span>
                      </div>
                      <el-radio-group v-model="item.score" class="option-group">
                        <el-radio v-for="opt in item.options" :key="opt.value" :label="opt.value">
                          {{ opt.label }}
                        </el-radio>
                      </el-radio-group>
                    </div>
                  </div>

                  <!-- ② 精神状态评估 -->
                  <div class="ability-section">
                    <div class="ability-section-title">
                      <span class="number">②</span> 精神状态评估
                    </div>
                    <el-form-item label="*认知功能是否完好">
                      <el-checkbox-group v-model="mentalForm.cognitive">
                        <el-checkbox label="是（意识清，定向力、记忆力、理解力、计算力、判断力正常）" />
                        <el-checkbox label="否（意识不清或存在定向力、记忆力、理解力、计算力、判断力障碍之一）" />
                        <el-checkbox label="精神异常症状" />
                      </el-checkbox-group>
                    </el-form-item>
                    <el-form-item label="其他">
                      <el-input v-model="mentalForm.other" placeholder="请输入其他补充说明" maxlength="200" show-word-limit />
                    </el-form-item>
                  </div>

                  <!-- ③ 感知觉与沟通 -->
                  <div class="ability-section">
                    <div class="ability-section-title">
                      <span class="number">③</span> 感知觉与沟通
                    </div>
                    <el-form-item label="*视力">
                      <el-radio-group v-model="perceptionForm.vision">
                        <el-radio label="能看清正常大小的字，可分辨日常物品">能看清正常大小的字，可分辨日常物品</el-radio>
                        <el-radio label="视物模糊，需借助放大镜阅读">视物模糊，需借助放大镜阅读</el-radio>
                        <el-radio label="分辨不清日常物品">分辨不清日常物品</el-radio>
                      </el-radio-group>
                    </el-form-item>
                    <el-form-item label="*听力">
                      <el-radio-group v-model="perceptionForm.hearing">
                        <el-radio label="可正常交流">可正常交流</el-radio>
                        <el-radio label="仅能听到贴近耳边的声音">仅能听到贴近耳边的声音</el-radio>
                        <el-radio label="听不到周围环境声音">听不到周围环境声音</el-radio>
                      </el-radio-group>
                    </el-form-item>
                    <el-form-item label="*沟通表达">
                      <el-radio-group v-model="perceptionForm.expression">
                        <el-radio label="可正常表达">可正常表达</el-radio>
                        <el-radio label="表达困难，需借助手势或其他辅助工具">表达困难，需借助手势或其他辅助工具</el-radio>
                        <el-radio label="无法表达">无法表达</el-radio>
                      </el-radio-group>
                    </el-form-item>
                  </div>

                  <!-- ④ 社会参与 -->
                  <div class="ability-section">
                    <div class="ability-section-title">
                      <span class="number">④</span> 社会参与
                    </div>
                    <el-form-item label="*生活能力评估">
                      <el-radio-group v-model="socialForm.lifeAbility">
                        <el-radio label="能独立收看电视、阅读书报等">能独立收看电视、阅读书报等</el-radio>
                        <el-radio label="能独立或在协助下参与社区和集体活动">能独立或在协助下参与社区和集体活动</el-radio>
                        <el-radio label="不能参与社区和集体活动">不能参与社区和集体活动</el-radio>
                      </el-radio-group>
                    </el-form-item>
                  </div>
                </el-form>
              </div>
            </el-tab-pane>

            <!-- Tab3: 评估报告 -->
            <el-tab-pane label="评估报告" name="report">
              <div class="section-block">
                <div class="report-summary">
                  <el-descriptions title="评估结果汇总" :column="2" border size="default">
                    <el-descriptions-item label="自理能力总分">
                      <el-tag type="primary" size="large">{{ calculateAbilityScore() }} 分</el-tag>
                    </el-descriptions-item>
                    <el-descriptions-item label="能力等级">
                      <el-tag :type="getAbilityLevelType()" size="large">{{ getAbilityLevel() }}</el-tag>
                    </el-descriptions-item>
                    <el-descriptions-item label="精神状态">
                      {{ mentalForm.cognitive.length > 0 ? '已评估' : '未评估' }}
                    </el-descriptions-item>
                    <el-descriptions-item label="感知觉与沟通">
                      {{ perceptionForm.vision ? '已评估' : '未评估' }}
                    </el-descriptions-item>
                    <el-descriptions-item label="社会参与">
                      {{ socialForm.lifeAbility ? '已评估' : '未评估' }}
                    </el-descriptions-item>
                    <el-descriptions-item label="疾病诊断数量">
                      {{ healthForm.diseases.length }} 项
                    </el-descriptions-item>
                  </el-descriptions>

                  <el-divider />

                  <div class="nursing-level">
                    <h3>护理等级建议</h3>
                    <el-tag 
                      v-if="getNursingLevel() === '一级护理'" 
                      type="danger" 
                      size="large" 
                      effect="dark"
                    >
                      {{ getNursingLevel() }}
                    </el-tag>
                    <el-tag 
                      v-else-if="getNursingLevel() === '二级护理'" 
                      type="warning" 
                      size="large" 
                      effect="dark"
                    >
                      {{ getNursingLevel() }}
                    </el-tag>
                    <el-tag 
                      v-else-if="getNursingLevel() === '三级护理'" 
                      type="success" 
                      size="large" 
                      effect="dark"
                    >
                      {{ getNursingLevel() }}
                    </el-tag>
                    <el-tag v-else type="info" size="large" effect="dark">{{ getNursingLevel() }}</el-tag>
                  </div>

                  <el-divider />

                  <div class="report-detail">
                    <h3>详细评估报告</h3>
                    <el-input
                      v-model="reportContent"
                      type="textarea"
                      :rows="10"
                      placeholder="系统将根据评估结果自动生成报告，您也可以手动补充..."
                      readonly
                      class="report-textarea"
                    />
                  </div>
                </div>
              </div>
            </el-tab-pane>
          </el-tabs>

          <!-- 底部按钮 -->
          <div class="footer-actions">
            <el-button @click="handleCancel">返回</el-button>
            <template v-if="!isReadOnly">
              <el-button v-if="activeTab !== 'report'" type="primary" @click="handleNext">下一步</el-button>
              <el-button v-else type="primary" @click="handleSubmit" :loading="submitLoading">提交</el-button>
            </template>
          </div>
        </el-card>
      </div>

      <!-- 右侧操作记录 -->
      <div class="content-right">
        <el-card>
          <template #header>
            <span class="title">操作记录</span>
          </template>
          <el-timeline v-if="operationRecords.length > 0">
            <el-timeline-item
              v-for="(record, index) in operationRecords"
              :key="index"
              :timestamp="record.time"
              :type="record.type"
              placement="top"
            >
              <div class="record-title">{{ record.operator }} {{ record.action }}</div>
              <div class="record-desc">操作结果：{{ record.result }}</div>
            </el-timeline-item>
          </el-timeline>
          <el-empty v-else description="暂无操作记录" :image-size="100" />
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { Plus, Delete } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRoute, useRouter } from 'vue-router'
import { getCheckin, evaluateCheckin } from '@/api/system/checkin.js'
import { storeToRefs } from 'pinia'
import useUserStore from '@/store/modules/user.js'
import FileUpload from '@/components/FileUpload/index.vue'

// 接收父组件传递的任务数据
const props = defineProps({
  taskData: {
    type: Object,
    default: () => ({})
  }
})

// 检查是否为只读模式
const route = useRoute()
const router = useRouter()

const isReadOnly = computed(() => {
  return route.query.readonly === 'true' || props.taskData.readonly === true;
});

// Tab 控制
const activeTab = ref('health')
const submitLoading = ref(false)

// 当前业务ID
const currentCheckInId = ref(null)
const currentCheckInData = ref(null)

// 表单引用
const healthFormRef = ref(null)
const abilityFormRef = ref(null)



const userStore = useUserStore()
const { user } = storeToRefs(userStore)


// 健康评估表单
const healthForm = reactive({
  diseases: [],
  medications: [{ name: '', method: '', dosage: '' }],
  fallStatus: '',
  lostStatus: '',
  chokeStatus: '',
  suicideStatus: '',
  pressure: [],
  skinCare: [],
  checkResult: ''
})

// 健康评估校验规则
const healthRules = {
  diseases: [{ required: true, message: '请至少选择一项疾病诊断', trigger: 'change', type: 'array', min: 1 }],
  fallStatus: [{ required: true, message: '请选择跌倒情况', trigger: 'change' }],
  lostStatus: [{ required: true, message: '请选择走失情况', trigger: 'change' }],
  chokeStatus: [{ required: true, message: '请选择噎食情况', trigger: 'change' }],
  suicideStatus: [{ required: true, message: '请选择自杀情况', trigger: 'change' }],
  pressure: [{ required: true, message: '请至少选择一项压力性损伤', trigger: 'change', type: 'array', min: 1 }],
  skinCare: [{ required: true, message: '请至少选择一项特殊皮肤护理', trigger: 'change', type: 'array', min: 1 }]
}

// 能力评估问题列表（完整10题）
const abilityQuestions = ref([
  {
    question: '进食：使用适当的器具将食物送入嘴中并咽下',
    score: null,
    options: [
      { label: 'A. 独立完成，无需他人帮助（0分）', value: 0 },
      { label: 'B. 需要部分帮助，如切割食物（1分）', value: 1 },
      { label: 'C. 需要他人协助喂食或管饲喂养（2分）', value: 2 },
      { label: 'D. 主要依赖他人协助，自己参与一半及以上（3分）', value: 3 },
      { label: 'E. 完全依赖他人帮助，自己无法进食（5分）', value: 5 }
    ]
  },
  {
    question: '洗澡：清洗全身或淋浴',
    score: null,
    options: [
      { label: 'A. 独立完成（0分）', value: 0 },
      { label: 'B. 需要部分帮助（1分）', value: 1 },
      { label: 'C. 在他人协助下完成洗浴或淋浴（2分）', value: 2 },
      { label: 'D. 主要依赖他人协助（3分）', value: 3 },
      { label: 'E. 完全依赖他人帮助（5分）', value: 5 }
    ]
  },
  {
    question: '修饰：梳头、刷牙、洗脸、剃须等',
    score: null,
    options: [
      { label: 'A. 独立完成（0分）', value: 0 },
      { label: 'B. 需要部分帮助（1分）', value: 1 },
      { label: 'C. 在他人协助下完成修饰或清洁（2分）', value: 2 },
      { label: 'D. 主要依赖他人协助（3分）', value: 3 },
      { label: 'E. 完全依赖他人帮助（5分）', value: 5 }
    ]
  },
  {
    question: '穿衣：穿脱衣服、系扣子、拉拉链等',
    score: null,
    options: [
      { label: 'A. 独立完成（0分）', value: 0 },
      { label: 'B. 需要部分帮助（1分）', value: 1 },
      { label: 'C. 需要他人协助穿脱衣服（2分）', value: 2 },
      { label: 'D. 主要依赖他人协助（3分）', value: 3 },
      { label: 'E. 完全依赖他人帮助（5分）', value: 5 }
    ]
  },
  {
    question: '大便控制：控制大便排出的能力',
    score: null,
    options: [
      { label: 'A. 能够控制（0分）', value: 0 },
      { label: 'B. 偶尔失禁，每周1次（1分）', value: 1 },
      { label: 'C. 经常失禁，每周2次以上（2分）', value: 2 },
      { label: 'D. 完全失禁（5分）', value: 5 }
    ]
  },
  {
    question: '小便控制：控制小便排出的能力',
    score: null,
    options: [
      { label: 'A. 能够控制（0分）', value: 0 },
      { label: 'B. 偶尔失禁，每周1次（1分）', value: 1 },
      { label: 'C. 经常失禁，每周2次以上（2分）', value: 2 },
      { label: 'D. 完全失禁（5分）', value: 5 }
    ]
  },
  {
    question: '如厕：使用便器或去卫生间，清洁自己',
    score: null,
    options: [
      { label: 'A. 独立完成（0分）', value: 0 },
      { label: 'B. 需要部分帮助（1分）', value: 1 },
      { label: 'C. 需要他人协助（2分）', value: 2 },
      { label: 'D. 主要依赖他人协助（3分）', value: 3 },
      { label: 'E. 完全依赖他人帮助（5分）', value: 5 }
    ]
  },
  {
    question: '床椅转移：在床和椅子之间转移',
    score: null,
    options: [
      { label: 'A. 独立完成（0分）', value: 0 },
      { label: 'B. 需要部分帮助或使用辅助器具（1分）', value: 1 },
      { label: 'C. 需要他人协助（2分）', value: 2 },
      { label: 'D. 需要他人较多协助（3分）', value: 3 },
      { label: 'E. 完全依赖他人帮助（5分）', value: 5 }
    ]
  },
  {
    question: '平地行走：在平地上行走（可使用辅助器具）',
    score: null,
    options: [
      { label: 'A. 独立完成（0分）', value: 0 },
      { label: 'B. 需要使用辅助器具（1分）', value: 1 },
      { label: 'C. 需要他人协助（2分）', value: 2 },
      { label: 'D. 需要他人较多协助（3分）', value: 3 },
      { label: 'E. 完全依赖他人帮助或使用轮椅（5分）', value: 5 }
    ]
  },
  {
    question: '上下楼梯：上下整段楼梯',
    score: null,
    options: [
      { label: 'A. 独立完成（0分）', value: 0 },
      { label: 'B. 需要使用辅助器具或扶手（1分）', value: 1 },
      { label: 'C. 需要他人协助（2分）', value: 2 },
      { label: 'D. 需要他人较多协助（3分）', value: 3 },
      { label: 'E. 无法上下楼梯（5分）', value: 5 }
    ]
  }
])

// 精神状态评估
const mentalForm = reactive({
  cognitive: [],
  other: ''
})

// 感知觉与沟通
const perceptionForm = reactive({
  vision: '',
  hearing: '',
  expression: ''
})

// 社会参与
const socialForm = reactive({
  lifeAbility: ''
})

// 评估报告内容
const reportContent = ref('')

// 操作记录
const operationRecords = ref([])

// 添加药物
const addMedication = () => {
  healthForm.medications.push({ name: '', method: '', dosage: '' })
}

// 删除药物
const removeMedication = (index) => {
  healthForm.medications.splice(index, 1)
}

// 计算能力评估总分
const calculateAbilityScore = () => {
  return abilityQuestions.value.reduce((total, item) => {
    return total + (item.score || 0)
  }, 0)
}

// 获取能力等级
const getAbilityLevel = () => {
  const score = calculateAbilityScore()
  if (score <= 10) return '能力完好'
  if (score >= 11 && score <= 20) return '轻度失能'
  if (score >= 21 && score <= 30) return '中度失能'
  if (score >= 31 && score <= 40) return '中重度失能'
  if (score >= 41 && score <= 50) return '重度失能'
  return '未评估'
}

// 获取能力等级标签类型
const getAbilityLevelType = () => {
  const level = getAbilityLevel()
  if (level === '能力完好') return 'success'
  if (level === '轻度失能') return 'warning'
  if (level === '中度失能') return 'danger'
  if (level === '中重度失能') return 'danger'
  if (level === '重度失能') return 'danger'
  return 'info'
}

// 获取护理等级
const getNursingLevel = () => {
  const score = calculateAbilityScore()
  if (score == 0) return '无需护理'
  if (score > 0 && score <= 20) return '三级护理'
  if (score >= 21 && score <= 30) return '二级护理'
  if (score >= 31 && score <= 40) return '一级护理'
  if (score >= 41 && score <= 50) return '特级护理'
  return '待评估'
}

// 下一步
const handleNext = async () => {
  if (activeTab.value === 'health') {
    // 验证健康评估必填项
    try {
      await healthFormRef.value.validate()
      activeTab.value = 'ability'
    } catch (error) {
      ElMessage.warning('请完成所有必填项')
      return
    }
  } else if (activeTab.value === 'ability') {
    // 验证能力评估必填项
    const unfinished = abilityQuestions.value.find(q => q.score === null)
    if (unfinished) {
      ElMessage.warning('请完成所有能力评估题目')
      return
    }
    
    if (mentalForm.cognitive.length === 0) {
      ElMessage.warning('请完成精神状态评估')
      return
    }
    
    if (!perceptionForm.vision || !perceptionForm.hearing || !perceptionForm.expression) {
      ElMessage.warning('请完成感知觉与沟通评估')
      return
    }
    
    if (!socialForm.lifeAbility) {
      ElMessage.warning('请完成社会参与评估')
      return
    }
    
    // 生成评估报告
    generateReport()
    activeTab.value = 'report'
  }
}

// 生成评估报告
const generateReport = () => {
  const score = calculateAbilityScore()
  const level = getAbilityLevel()
  const nursingLevel = getNursingLevel()
  
  reportContent.value = `
评估结果摘要
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

一、自理能力评估
  总分：${score} 分
  能力等级：${level}
  护理等级建议：${nursingLevel}

二、健康状况
  疾病诊断：${healthForm.diseases.join('、') || '无'}
  用药情况：${healthForm.medications.filter(m => m.name).length} 种药物
  跌倒风险：${healthForm.fallStatus}
  走失风险：${healthForm.lostStatus}
  噎食风险：${healthForm.chokeStatus}
  自杀风险：${healthForm.suicideStatus}
  压力性损伤：${healthForm.pressure.join('、') || '无'}
  特殊护理：${healthForm.skinCare.join('、') || '无'}

三、精神与感知
  认知功能：${mentalForm.cognitive.join('、') || '未评估'}
  ${mentalForm.other ? '其他说明：' + mentalForm.other : ''}
  视力：${perceptionForm.vision}
  听力：${perceptionForm.hearing}
  沟通表达：${perceptionForm.expression}

四、社会参与
  ${socialForm.lifeAbility}

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
综合建议
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

根据以上评估结果，建议为该老人提供【${nursingLevel}】服务。

${level === '重度失能' 
  ? '该老人为重度失能状态，需要24小时专人看护，重点关注日常生活全方位照护、安全防护、健康监测及心理疏导。' 
  : level === '中重度失能' 
  ? '该老人为中重度失能状态，需要较多的日常生活协助，建议提供24小时监护，加强健康管理和功能训练。' 
  : level === '中度失能' 
  ? '该老人为中度失能状态，需要提供日常生活协助和安全保障，建议定期健康检查，加强功能训练。' 
  : level === '轻度失能'
  ? '该老人为轻度失能状态，需要部分日常生活协助，建议进行康复训练，预防功能退化。'
  : level === '能力完好'
  ? '该老人能力完好，建议提供健康监测和预防性照护，鼓励参与社交活动。'
  : '该老人未评估，建议尽快完成评估。'
}

${healthForm.diseases.length > 5 ? '⚠️ 注意：该老人患有多种疾病，需加强健康管理和用药监督。' : ''}
${healthForm.fallStatus !== '无' ? '⚠️ 注意：该老人存在跌倒风险，需加强安全防护。' : ''}
${healthForm.lostStatus !== '无' ? '⚠️ 注意：该老人存在走失风险，需加强监护。' : ''}
  `.trim()
}

// 提交评估
const handleSubmit = () => {
  ElMessageBox.confirm('确认提交评估报告并进入审批流程？提交后将无法修改。', '确认提交', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      submitLoading.value = true
      
      // 构造 evaluation 数据（包含健康评估 + 能力评估 + 评估报告）
      const evaluationData = {
        healthForm: healthForm,
        abilityQuestions: abilityQuestions.value,
        mentalForm: mentalForm,
        perceptionForm: perceptionForm,
        socialForm: socialForm,
        reportContent: reportContent.value
      }
      
      console.log('准备提交评估数据:', evaluationData)
      
      // 转换为 JSON 字符串
      const evaluationJson = JSON.stringify(evaluationData)
      
      // 调用后端接口完成评估任务，传递 evaluation 参数
      const res = await evaluateCheckin(currentCheckInId.value, { evaluation: evaluationJson })
      
      console.log('评估接口返回:', res)
      
      if (res.code === 200) {
        ElMessage.success('评估报告已提交，进入审批流程')
        
        // 延迟跳转，让用户看到成功提示
        setTimeout(() => {
          // 跳转到审批中页面
          router.push({
            path: '/checkin/processing',
            query: {
              checkInId: currentCheckInId.value
            }
          })
        }, 1500)
      } else {
        ElMessage.error(res.msg || '提交失败')
      }
    } catch (error) {
      console.error('提交评估失败:', error)
      ElMessage.error('提交失败：' + (error.message || '未知错误'))
    } finally {
      submitLoading.value = false
    }
  }).catch(() => {
    console.log('用户取消提交')
  })
}

// 返回
const handleCancel = () => {
  ElMessageBox.confirm('确认返回？未保存的数据将丢失。', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    router.back()
  }).catch(() => {})
}

// 加载任务数据
onMounted(async () => {
  console.log('评估页面收到的任务数据:', props.taskData)
  
  // 获取 businessId
  const businessId = props.taskData.checkInId || route.query.businessId || route.query.checkInId
  
  if (!businessId) {
    ElMessage.error('缺少业务ID，无法加载数据')
    return
  }
  
  currentCheckInId.value = businessId
  
  try {
    const res = await getCheckin(businessId)
    if (res.code === 200 && res.data) {
      currentCheckInData.value = res.data
      console.log('加载入住申请数据成功:', res.data)
      
      // 构造操作记录（动态数据）
      operationRecords.value = [
        {
          operator: res.data.applicat || '申请人',
          action: '申请入住',
          time: res.data.createTime || new Date().toLocaleString('zh-CN', { 
            year: 'numeric', 
            month: '2-digit', 
            day: '2-digit', 
            hour: '2-digit', 
            minute: '2-digit', 
            second: '2-digit' 
          }),
          result: '已发起',
          type: 'success'
        }
      ]
      
      // 如果已经有评估记录，可以再添加一条
      if (res.data.flowStatus >= 1) {
        operationRecords.value.push({
          operator: userStore.nickName,
          action: '入住评估',
          time: res.data.updateTime || new Date().toLocaleString('zh-CN'),
          result: '正在处理',
          type: 'primary'
        })
      }
      
      // 如果已有评估数据，则解析并填充表单
      if (res.data.evaluation) {
        try {
          const evalData = JSON.parse(res.data.evaluation);
          populateFormData(evalData);
        } catch (parseError) {
          console.error('解析评估数据失败:', parseError);
        }
      }
      
      ElMessage.success('任务数据加载成功')
    } else {
      ElMessage.error(res.msg || '加载数据失败')
    }
  } catch (error) {
    console.error('加载任务数据失败:', error)
    ElMessage.error('加载任务数据失败：' + (error.message || '未知错误'))
  }
})  // 这是onMounted函数的结束

// 填充表单数据（用于只读模式显示已保存的数据）
function populateFormData(evalData) {
  if (evalData.healthForm) {
    Object.assign(healthForm, evalData.healthForm);
  }
  
  if (evalData.abilityQuestions) {
    // 用已保存的数据替换当前的abilityQuestions
    abilityQuestions.value = evalData.abilityQuestions.map((savedQuestion, index) => {
      // 获取原始问题结构
      const originalQuestion = abilityQuestions.value[index];
      if (originalQuestion) {
        // 保留原始问题文本和选项，但使用已保存的分数
        return {
          ...originalQuestion,
          score: savedQuestion.score
        };
      } else {
        // 如果原始问题不存在，返回已保存的问题
        return savedQuestion;
      }
    });
  }
  
  if (evalData.mentalForm) {
    Object.assign(mentalForm, evalData.mentalForm);
  }
  
  if (evalData.perceptionForm) {
    Object.assign(perceptionForm, evalData.perceptionForm);
  }
  
  if (evalData.socialForm) {
    Object.assign(socialForm, evalData.socialForm);
  }
  
  if (evalData.reportContent) {
    reportContent.value = evalData.reportContent;
  }
}


</script>

<style scoped lang="scss">
.evaluate-container {
  min-height: 100vh;
  background: #f5f7fa;
  padding-bottom: 20px;
}

.steps-header {
  background: #fff;
  padding: 30px 40px;
  margin-bottom: 20px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
}

.main-wrapper {
  display: flex;
  gap: 20px;
  padding: 0 20px;
  max-width: 1600px;
  margin: 0 auto;
}

.content-left {
  flex: 1;
  min-width: 0;
}

.content-right {
  width: 320px;
  flex-shrink: 0;
}

.card-header {
  .title {
    font-size: 16px;
    font-weight: 600;
    color: #303133;
    border-left: 4px solid #409eff;
    padding-left: 10px;
  }
}

.eval-tabs {
  :deep(.el-tabs__nav-wrap) {
    padding: 0 20px;
  }
}

.section-block {
  padding: 20px;
  min-height: 400px;
}

.section-title {
  font-size: 15px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 25px;
  padding-left: 12px;
  border-left: 4px solid #409eff;
}

.eval-form {
  :deep(.el-form-item) {
    margin-bottom: 28px;
  }
  
  :deep(.el-checkbox-group) {
    display: flex;
    flex-direction: column;
    gap: 12px;
  }
  
  :deep(.el-checkbox) {
    margin-right: 0;
  }
  
  :deep(.el-radio-group) {
    display: flex;
    flex-direction: column;
    gap: 12px;
  }
  
  :deep(.el-radio) {
    margin-right: 0;
  }
}

.med-table-wrapper {
  width: 100%;
  
  :deep(.el-table) {
    font-size: 13px;
  }
}

.ability-tip {
  margin-bottom: 30px;
}

.ability-form {
  :deep(.el-form-item) {
    margin-bottom: 22px;
  }
}

.ability-section {
  margin-bottom: 40px;
  padding: 25px;
  background: #fafafa;
  border-radius: 6px;
  border: 1px solid #e4e7ed;
}

.ability-section-title {
  font-size: 15px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 25px;
  display: flex;
  align-items: center;
  
  .number {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    width: 26px;
    height: 26px;
    background: #409eff;
    color: #fff;
    border-radius: 50%;
    font-size: 14px;
    margin-right: 12px;
  }
}

.question-item {
  margin-bottom: 28px;
  padding-bottom: 22px;
  border-bottom: 1px dashed #e4e7ed;
  
  &:last-child {
    border-bottom: none;
    margin-bottom: 0;
    padding-bottom: 0;
  }
}

.question-title {
  margin-bottom: 14px;
  
  .q-num {
    font-weight: 500;
    color: #606266;
    font-size: 14px;
  }
}

.option-group {
  display: flex;
  flex-direction: column;
  gap: 12px;
  padding-left: 24px;
  
  :deep(.el-radio) {
    margin-right: 0;
    
    .el-radio__label {
      color: #606266;
      font-size: 13px;
      line-height: 1.6;
    }
  }
}

.report-summary {
  padding: 20px;
}

.nursing-level {
  text-align: center;
  padding: 35px 0;
  
  h3 {
    margin-bottom: 20px;
    color: #303133;
    font-size: 16px;
  }
  
  .el-tag {
    font-size: 20px;
    padding: 14px 40px;
    font-weight: 600;
  }
}

.report-detail {
  h3 {
    margin-bottom: 15px;
    color: #303133;
    font-size: 16px;
  }
  
  .report-textarea {
    :deep(textarea) {
      font-family: 'Consolas', 'Monaco', 'Courier New', monospace;
      line-height: 1.8;
      color: #303133;
    }
  }
}

.footer-actions {
  text-align: center;
  padding: 35px 0 15px;
  border-top: 1px solid #ebeef5;
  margin-top: 25px;
  
  .el-button {
    min-width: 110px;
    height: 40px;
  }
}

.record-title {
  font-weight: 500;
  color: #303133;
  margin-bottom: 5px;
  font-size: 14px;
}

.record-desc {
  font-size: 13px;
  color: #909399;
}

:deep(.el-card__header) {
  padding: 16px 20px;
  background: #fafafa;
  border-bottom: 1px solid #ebeef5;
}

:deep(.el-descriptions) {
  margin-top: 20px;
}

:deep(.el-timeline) {
  padding: 10px 0;
}

</style>