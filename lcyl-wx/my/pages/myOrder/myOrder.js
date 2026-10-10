const app = getApp()

Page({
  data: {

    currentTab: 'all',
    tabs: [
      { label: '全部', value: 'all' },
      { label: '待支付', value: 'pendingPay' },
      { label: '待执行', value: 'pendingService' },
      { label: '已执行', value: 'executed' },
      { label: '已完成', value: 'completed' }
    ],
    orders: [],
    filteredOrders: []
  },

  refundReasonOptions: [
    '不需要此项服务了',
    '费用有点贵',
    '临时有事，不方便服务',
    '信息填写错误',
    '重复下单'
  ],

  onLoad(options) {
    this.setData({
      currentTab: options.tab || 'all'
    })
    this.getMyOrders()
  },

  onShow() {
    this.getMyOrders()
  },



  getMyOrders() {
    wx.request({
      url: 'http://172.16.20.15:8080/wxLogin/myOrders',
      method: 'GET',
      header: {
        'content-type': 'application/json',
        authorization: app.globalData.token || ''
      },
      success: (res) => {
        if (res.data.code === 200) {
          const orders = (res.data.data || []).map(item => this.mapOrderItem(item))
          this.setData({ orders }, () => this.filterOrders())
          return
        }
        wx.showToast({
          title: res.data.msg || '获取订单失败',
          icon: 'none'
        })
      },
      fail: () => {
        wx.showToast({
          title: '获取订单失败',
          icon: 'none'
        })
      }
    })
  },

  mapOrderItem(item) {
    const statusMeta = this.getOrderStatusMeta(item.orderStatus, item.tradeStatus)
    return {
      id: item.id,
      orderNo: item.orderNo,
      serviceName: item.projectName || '服务项目',
      serverName: item.elderName || '未关联老人',
      serviceTime: this.formatDateTime(item.expectedServiceTime),
      rawServiceTime: item.expectedServiceTime || '',
      price: this.formatMoney(item.orderAmount),
      status: statusMeta.status,
      statusText: statusMeta.text,
      image: item.projectImage || '/images/head.png'
    }
  },

  getOrderStatusMeta(orderStatus, tradeStatus) {
    if (tradeStatus === '2') {
      return { status: 'refundProcessing', text: '退款处理中' }
    }
    if (tradeStatus === '3') {
      return { status: 'refunded', text: '已退款' }
    }
    if (tradeStatus === '4') {
      return { status: 'refundFailed', text: '退款失败' }
    }
    if (orderStatus === '0') {
      return { status: 'pendingPay', text: '待支付' }
    }
    if (orderStatus === '1') {
      return { status: 'pendingService', text: '待执行' }
    }
    if (orderStatus === '2') {
      return { status: 'executed', text: '已执行' }
    }
    if (orderStatus === '3') {
      return { status: 'completed', text: '已完成' }
    }
    if (orderStatus === '4') {
      return { status: 'refunded', text: '已退款' }
    }
    if (orderStatus === '5') {
      return { status: 'closed', text: '已关闭' }
    }
    return { status: 'all', text: '未知状态' }
  },

  formatDateTime(value) {
    if (!value) {
      return '-'
    }
    return String(value).replace('T', ' ').slice(0, 16)
  },

  formatMoney(value) {
    return Number(value || 0).toFixed(2)
  },

  handleBack() {
    wx.navigateBack({ delta: 1 })
  },

  handleTabChange(e) {
    this.setData({
      currentTab: e.currentTarget.dataset.value
    }, () => this.filterOrders())
  },

  filterOrders() {
    const { currentTab, orders } = this.data
    const filteredOrders = currentTab === 'all'
      ? orders
      : orders.filter(item => item.status === currentTab)

    this.setData({ filteredOrders })
  },

  findOrder(id) {
    return this.data.orders.find(item => String(item.id) === String(id))
  },

  handleCancel(e) {
    const orderId = e.currentTarget.dataset.id
    wx.showModal({
      title: '取消订单',
      content: '确认取消这笔订单吗？',
      success: res => {
        if (!res.confirm) {
          return
        }
        this.cancelOrder(orderId)
      }
    })
  },

  handleViewDetail(e) {
    wx.navigateTo({
      url: `/my/pages/myOrderDetail/myOrderDetail?id=${e.currentTarget.dataset.id}`
    })
  },

  handlePay(e) {
    const order = this.findOrder(e.currentTarget.dataset.id)
    if (!order) {
      wx.showToast({ title: '订单不存在', icon: 'none' })
      return
    }
    this.goPay(order)
  },

  handleRefund(e) {
    const orderId = e.currentTarget.dataset.id
    wx.showActionSheet({
      itemList: this.refundReasonOptions,
      success: res => {
        const refundReason = this.refundReasonOptions[res.tapIndex]
        this.applyRefund(orderId, refundReason)
      }
    })
  },



  cancelOrder(orderId) {
    wx.showLoading({ title: '处理中...' })
    wx.request({
      url: `http://localhost:8080/wxLogin/cancelOrder/${orderId}`,
      method: 'POST',
      header: {
        'content-type': 'application/json',
        authorization: app.globalData.token || ''
      },
      success: res => {
        wx.hideLoading()
        if (res.data.code !== 200) {
          wx.showToast({ title: res.data.msg || '取消失败', icon: 'none' })
          return
        }
        wx.showToast({ title: '取消成功', icon: 'success' })
        this.getMyOrders()
      },
      fail: () => {
        wx.hideLoading()
        wx.showToast({ title: '取消失败，请稍后重试', icon: 'none' })
      }
    })
  },

  applyRefund(orderId, refundReason) {
    wx.showLoading({ title: '提交中...' })
    wx.request({
      url: 'http://localhost:8080/wxLogin/applyRefund',
      method: 'POST',
      header: {
        'content-type': 'application/json',
        authorization: app.globalData.token || ''
      },
      data: {
        orderId,
        refundReason
      },
      success: res => {
        wx.hideLoading()
        if (res.data.code !== 200) {
          wx.showToast({ title: res.data.msg || '申请退款失败', icon: 'none' })
          return
        }
        wx.showToast({ title: '退款申请已提交', icon: 'success' })
        this.getMyOrders()
      },
      fail: () => {
        wx.hideLoading()
        wx.showToast({ title: '申请退款失败，请稍后重试', icon: 'none' })
      }
    })
  },

  goPay(order) {
    wx.navigateTo({
      url: `/servicePage/pages/payPage/payPage?` +
        `orderId=${order.id}` +
        `&serviceName=${encodeURIComponent(order.serviceName || '')}` +
        `&price=${order.price}` +
        `&unit=` +
        `&imageUrl=${encodeURIComponent(order.image || '')}` +
        `&familyName=${encodeURIComponent(order.serverName || '')}` +
        `&serviceTime=${encodeURIComponent(order.rawServiceTime || order.serviceTime || '')}` +
        `&remark=` +
        `&totalPrice=${order.price}`
    })
  }
})
