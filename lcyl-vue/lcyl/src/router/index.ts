import { createWebHistory, createRouter } from 'vue-router'
/* Layout */
import Layout from '@/layout/index.vue'
import BillAdjustment from '@/views/code/checkout/billAdjustment.vue'

/**
 * Note: 路由配置项
 *
 * hidden: true                     // 当设置 true 的时候该路由不会再侧边栏出现 如401，login等页面，或者如一些编辑页面/edit/1
 * alwaysShow: true                 // 当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式--如组件页面
 *                                  // 只有一个时，会将那个子路由当做根路由显示在侧边栏--如引导页面
 *                                  // 若你想不管路由下面的 children 声明的个数都显示你的根路由
 *                                  // 你可以设置 alwaysShow: true，这样它就会忽略之前定义的规则，一直显示根路由
 * redirect: noRedirect             // 当设置 noRedirect 的时候该路由在面包屑导航中不可被点击
 * name:'router-name'               // 设定路由的名字，一定要填写不然使用<keep-alive>时会出现各种问题
 * query: '{"id": 1, "name": "ry"}' // 访问路由的默认传递参数
 * roles: ['admin', 'common']       // 访问路由的角色权限
 * permissions: ['a:a:a', 'b:b:b']  // 访问路由的菜单权限
 * meta : {
 noCache: true                   // 如果设置为true，则不会被 <keep-alive> 缓存(默认 false)
 title: 'title'                  // 设置该路由在侧边栏和面包屑中展示的名字
 icon: 'svg-name'                // 设置该路由的图标，对应路径src/assets/icons/svg
 breadcrumb: false               // 如果设置为false，则不会在breadcrumb面包屑中显示
 activeMenu: '/system/user'      // 当路由设置了该属性，则会高亮相对应的侧边栏。
 }
 */

// 公共路由
export const constantRoutes = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: () => import('@/views/redirect/index.vue')
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/login.vue'),
    hidden: true
  },
  {
    path: '/register',
    component: () => import('@/views/register.vue'),
    hidden: true
  },
  {
    path: "/:pathMatch(.*)*",
    component: () => import('@/views/error/404.vue'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/error/401.vue'),
    hidden: true
  },
  {
    path: '',
    component: Layout,
    redirect: '/index',
    children: [
      {
        path: '/index',
        component: () => import('@/views/index.vue'),
        name: 'Index',
        meta: { title: '首页', icon: 'dashboard', affix: true }
      }
    ]
  },
  {
    path: '/checkin',
    component: Layout,
    hidden: true,
    permissions: [],  // 明确声明不需要权限检查
    children: [
      {
        path: '',
        component: () => import('@/views/checkin/index.vue'),
        name: 'CheckinApply',
        meta: { title: '发起入住申请', activeMenu: '/system/checkin', noCache: true }
      },
      {
        path: 'success',
        component: () => import('@/views/checkin/success.vue'),
        name: 'CheckinSuccess',
        meta: { title: '提交成功', activeMenu: '/system/checkin', noCache: true }
      },
      {
        path: 'processing',
        component: () => import('@/views/checkin/processing.vue'),
        name: 'CheckinProcessing',
        meta: { title: '审批中', activeMenu: '/system/checkin', noCache: true }
      },
      {
        path: 'detail',
        component: () => import('@/views/checkin/detail.vue'),
        name: 'CheckinDetail',
        meta: { title: '入住申请详情', activeMenu: '/system/checkin', noCache: true }
      }
    ]
  },
  {
    path: '/system/checkin',
    component: Layout,
    hidden: true,
    permissions: [],  // 明确声明不需要权限检查
    children: [
      {
        path: 'list',
        component: () => import('@/views/system/checkin/list.vue'),
        name: 'CheckinTaskList',
        meta: { title: '入住任务中心', activeMenu: '/system/checkin', noCache: true }
      }
    ]
  },
  {
    path: '/system/my-apply',
    component: Layout,
    hidden: true,
    permissions: [],
    children: [
      {
        path: 'index',
        component: () => import('@/views/system/myApply/index.vue'),
        name: 'MyApply',
        meta: { title: '我的申请', noCache: true }
      }
    ]
  },
  {
    path: '/system/my-to',
    component: Layout,
    hidden: true,
    permissions: [],
    children: [
      {
        path: 'index',
        component: () => import('@/views/system/myto/myto.vue'),
        name: 'MyTo',
        meta: { title: '我的待办', noCache: true, activeMenu: '/system/my-to' }
      }
    ]
  },
  {
    path: '/user',
    component: Layout,
    hidden: true,
    redirect: 'noredirect',
    children: [
      {
        path: 'profile/:activeTab?',
        component: () => import('@/views/system/user/profile/index.vue'),
        name: 'Profile',
        meta: { title: '个人中心', icon: 'user' }
      }
    ]
  },
  {
    path:'/code/checkout-apply',
    component: Layout,
    hidden: true,
    permissions: [], // 父路由不加权限
    children: [
      // 发起退住申请（用户自己填信息的页面）
      {
        path:'checkoutApply',
        component: () => import('@/views/code/checkout/checkoutApply.vue'),
        name: 'CheckoutApply',
        meta: { title: '发起退住申请', noCache: true }
      },
      // 护理组长审批
      {
        path:'approvalAdmin',
        component: () => import('@/views/code/checkout/applyApprovalAdmin.vue'),
        name: 'ApprovalAdmin',
        permissions: ['nurse_leader'], // 👈 直接用后端角色KEY
        meta: { title: '护理组长审批' }
      },
      // 法务解除合同
      {
        path:'contractTerm',
        component: () => import('@/views/code/checkout/contractTerm.vue'),
        name: 'ContractTerm',
        permissions: ['legal_staff'], //
        meta: { title: '法务合同审批' }
      },
      // 结算员调整账单
      {
        path:'billAdjustment',
        component: () => import('@/views/code/checkout/billAdjustment.vue'),
        name: 'BillAdjustment',
        permissions: ['settleman_staff'], //
        meta: { title: '账单调整' }
      },
      // 结算组长审批
      {
        path:'billApproval',
        component: () => import('@/views/code/checkout/billApproval.vue'),
        name: 'BillApproval',
        permissions: ['settleman_leader'], //
        meta: { title: '结算组长审批' }
      },
      // 副院长审批
      {
        path:'checkoutApproval',
        component: () => import('@/views/code/checkout/checkoutApproval.vue'),
        name: 'CheckoutApproval',
        permissions: ['vice_dean'], //
        meta: { title: '副院长审批' }
      },
      // 费用结清
      {
        path:'finalSettlement',
        component: () => import('@/views/code/checkout/finalSettlement.vue'),
        name: 'FinalSettlement',
        permissions: ['settleman_staff', 'settleman_leader'],
        meta: { title: '费用结清' }
      },
        //清算成功
      {
        path:'finishment',
        component: () => import('@/views/code/checkout/finishment.vue'),
        name: 'Finishment',
        permissions: [],
        meta: { title: '清算成功' }
      },
    ]
  }

]

// 动态路由，基于用户权限动态去加载
export const dynamicRoutes = [
  {
    path: '/system/user-auth',
    component: Layout,
    hidden: true,
    permissions: ['system:user:edit'],
    children: [
      {
        path: 'role/:userId(\\d+)',
        component: () => import('@/views/system/user/authRole.vue'),
        name: 'AuthRole',
        meta: { title: '分配角色', activeMenu: '/system/user' }
      }
    ]
  },
  {
    path: '/system/role-auth',
    component: Layout,
    hidden: true,
    permissions: ['system:role:edit'],
    children: [
      {
        path: 'user/:roleId(\\d+)',
        component: () => import('@/views/system/role/authUser.vue'),
        name: 'AuthUser',
        meta: { title: '分配用户', activeMenu: '/system/role' }
      }
    ]
  },
  {
    path: '/system/dict-data',
    component: Layout,
    hidden: true,
    permissions: ['system:dict:list'],
    children: [
      {
        path: 'index/:dictId(\\d+)',
        component: () => import('@/views/system/dict/data.vue'),
        name: 'Data',
        meta: { title: '字典数据', activeMenu: '/system/dict' }
      }
    ]
  },
  {
    path: '/monitor/job-log',
    component: Layout,
    hidden: true,
    permissions: ['monitor:job:list'],
    children: [
      {
        path: 'index/:jobId(\\d+)',
        component: () => import('@/views/monitor/job/log.vue'),
        name: 'JobLog',
        meta: { title: '调度日志', activeMenu: '/monitor/job' }
      }
    ]
  },
  {
    path: '/tool/gen-edit',
    component: Layout,
    hidden: true,
    permissions: ['tool:gen:edit'],
    children: [
      {
        path: 'index/:tableId(\\d+)',
        component: () => import('@/views/tool/gen/editTable.vue'),
        name: 'GenEdit',
        meta: { title: '修改生成配置', activeMenu: '/tool/gen' }
      }
    ]
  },
  {
    path: '/code/orders-detail',
    component: Layout,
    hidden: true,
    permissions: ['code:orders:query'],
    children: [
      {
        path: 'index/:id(\\d+)',
        component: () => import('@/views/code/orders/detail.vue'),
        name: 'OrderDetail',
        meta: { title: '订单详情', activeMenu: '/code/orders' }
      }
    ]
  },
  {
    path: '/code/leave-todo',
    component: Layout,
    hidden: true,
    permissions: ['code:leave:approve'],
    children: [
  {
    path: 'index',
    component: () => import('@/views/code/leave/todo.vue'),
    name: 'LeaveTodo',
    meta: {title: '待我审批', activeMenu: '/code/leave'}
  }
  ]},
  {
    path: '/code/leave-resubmit',
    component: Layout,
    hidden: true,
    permissions: ['code:leave:resubmit'],
    children: [
      {
        path: 'index',
        component: () => import('@/views/code/leave/resubmit.vue'),
        name: 'LeaveResubmit',
        meta: { title: '驳回修改', activeMenu: '/code/leave' }
      }
    ]
  },
  {
    path: '/code/leave-detail',
    component: Layout,
    hidden: true,
    permissions: ['code:leave:list'],
    children: [
      {
        path: 'create',
        component: () => import('@/views/code/leave/detail.vue'),
        name: 'LeaveCreate',
        meta: { title: '发起请假申请', activeMenu: '/code/leave' }
      },
      {
        path: 'view/:id(\\d+)',
        component: () => import('@/views/code/leave/detail.vue'),
        name: 'LeaveView',
        meta: { title: '请假详情', activeMenu: '/code/leave' }
      },
      {
        path: 'approve/:id(\\d+)',
        component: () => import('@/views/code/leave/detail.vue'),
        name: 'LeaveApprove',
        meta: { title: '请假审批', activeMenu: '/code/leave' }
      }]
    },

  {
    path: '/code/checkout/todo',
    component: Layout,
    hidden: true,
    permissions: ['code:checkout:todo'],
    children: [
      {
        path: 'index',
        component: () => import('@/views/code/checkout/index.vue'),
        name: 'CheckoutTodo',
        meta: { title: '退住待办', activeMenu: '/code/checkout', noCache: true }
      }
    ]
  },
  {
    path: '/code/bill-detail',
    component: Layout,
    hidden: true,
    permissions: ['code:bill:query'],
    children: [
      {
        path: 'index/:id(\\d+)',
        component: () => import('@/views/code/bill/detail.vue'),
        name: 'BillDetail',
        meta: { title: '账单详情', activeMenu: '/code/bill' }
      }
    ]
  }]

  
const router = createRouter({
  history: createWebHistory(),
  routes: constantRoutes,
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition
    }
    return { top: 0 }
  },
})

export default router