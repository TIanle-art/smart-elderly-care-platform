const app = getApp()
Page({
  data: {
    statusBarHeight: 20,
    navBarHeight: 44,

    currentTab: null,

    tabs: [{
        label: '全部',
        value: null
      },
      {
        label: '待上门',
        value: 0
      },
      {
        label: '已完成',
        value: 1
      },
      {
        label: '已取消',
        value: 2
      },
      {
        label: '已过期',
        value: 3
      }
    ],

    list: [],

    filteredList: [],

    showModal: false,
    modalContent: '',
    currentCancelId: null,

    // 1: 取消3次后今日不可预约
    // 2: 今日已不可预约
    modalType: 1
  },

  processListData(list) {
    if (!list || list.length === 0) return [];

    return list.map((item) => {
      // 1. 格式化 ISO 时间
      const date = new Date(item.appointmentTime);
      const formattedTime = `${date.getFullYear()}.${(date.getMonth() + 1)
        .toString()
        .padStart(2, "0")}.${date.getDate().toString().padStart(2, "0")}`;
      const time = `${date.getHours()}:${date
        .getMinutes()
        .toString()
        .padStart(2, "0")}`;

      // 2. 类型 type=1 → 探访老人
      let typeText = "",
        typeClass = "";
      if (item.type === 1) {
        typeText = "探访老人";
        typeClass = "type-visit";
      }else{
        typeText="参观预约"
        typeClass = "type-visit"
      }

      // 3. 状态（你最新规则：0待上门 1已完成 2已取消 3已过期）
      let statusText = "",
        statusClass = "";
      switch (item.status) {
        case 0:
          statusText = "待上门";
          statusClass = "status-wait-service";
          break;
        case 1:
          statusText = "已完成";
          statusClass = "status-done";
          break;
        case 2:
          statusText = "已取消";
          statusClass = "status-cancel";
          break;
        case 3:
          statusText = "已过期";
          statusClass = "status-expire";
          break;
      }

      // 返回页面需要的所有字段
      return {
        ...item,
        formattedTime, 
        time,
        typeText,
        typeClass,
        statusText,
        statusClass,
        userName: item.name,
        targetLabel: item.greeting,
        targetName: item.olderName,
      };
    });
  },

getFilteredList() {
  const status = this.data.currentTab
    console.log(status);
    wx.request({
      url: 'http://localhost:8080/wxLogin/selectVisitInfo',
      method: 'POST',
      data: {
        status: status
      },
      header: {
        'content-type': 'application/json',
        'authorization': app.globalData.token
      },
      success: (res) => {
        console.log(res.data.data);
        if (res.data.code === 200) {
          const formattedList = this.processListData(res.data.data)
          this.setData({
            filteredList: formattedList // 直接使用后端返回的数据
          })
        }
      }
    })
  },

  onLoad() {
    this.getFilteredList()
  },

  goBack() {
    wx.navigateBack({
      delta: 1,
      fail: () => {
        wx.switchTab({
          url: '/pages/index/index'
        })
      }
    })
  },

  switchTab(e) {
    const tabValue = e.currentTarget.dataset.value;
  
    console.log("当前点击的tab值：", tabValue); // 先看这里有没有值
    this.setData({
      currentTab:tabValue
    })
    
    this.getFilteredList() // 重新请求后端数据
  },
 // 点击取消按钮
 showCancelConfirm(e) {
  const id = e.currentTarget.dataset.id;
  // 弹框确认
  wx.showModal({
    title: "确认取消",
    content: "确定要取消该预约吗？",
    success: (res) => {
      if (res.confirm) {
        this.doCancel(id);
      }
    }
  });
},

doCancel(id) {
  wx.request({
    url: `http://localhost.15:8080/wxLogin/deleteVisit/${id}`,
    method: "PUT",
    header: {
      'content-type': 'application/json',
      'authorization': app.globalData.token|| ''  // 这里是后端要的 token 字段
    },
    success: (res) => {
      wx.showToast({ title: "取消成功" });
      // 取消成功后刷新列表
      this.getFilteredList()
    }
  });
},

  handleCancel(e) {
    const {
      id
    } = e.currentTarget.dataset

    // 这里模拟两种不同提示
    // 你接后端后，可以根据接口返回值动态决定弹窗内容
    let modalType = 1
    if (id % 2 === 0) {
      modalType = 2
    }

    

    const modalContent =
      modalType === 1 ?
      '取消3次后，该账号今日不可进行预约，\n是否还要继续?' :
      '该账号今日已不可进行预约，\n是否还要继续?'

    this.setData({
      showModal: true,
      currentCancelId: id,
      modalType,
      modalContent
    })
  },

  closeModal() {
    this.setData({
      showModal: false,
      currentCancelId: null
    })
  },

  confirmCancel() {
    const {
      currentCancelId,
      list
    } = this.data

    const newList = list.map(item => {
      if (item.id === currentCancelId) {
        return {
          ...item,
          status: 'cancelled',
          statusText: '已取消',
          statusClass: 'status-cancelled',
          canCancel: false
        }
      }
      return item
    })

    this.setData({
      list: newList,
      showModal: false,
      currentCancelId: null
    })

    this.updateFilteredList()

    wx.showToast({
      title: '已取消预约',
      icon: 'success'
    })
  },

  preventTouchMove() {},
  noop() {}
})