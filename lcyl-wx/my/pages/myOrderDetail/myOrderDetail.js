const app = getApp()

Page({
  data: {
    loading: true,
    orderInfo: null,
    orderView: null,
    executionInfo: null,
    refundInfo: null,
    projectImage: '/images/head.png',
    uiState: {
      key: '',
      bannerTitle: '',
      bannerDesc: '',
      showRefundCard: false,
      showExecutionCard: false,
      showCancelButton: false,
      showPayButton: false,
      showRefundButton: false
    }
  },

  refundReasonOptions: [
    '不需要此项服务了',
    '费用有点贵',
    '临时有事，不方便服务',
    '信息填写错误',
    '重复下单'
  ],

  onLoad(options) {
    this.orderId = options.id
    this.getOrderDetail()
  },

  onShow() {
    if (this.orderId) {
      this.getOrderDetail()
    }
  },

  getOrderDetail() {
    wx.request({
      url: `http://localhost:8080/wxLogin/myOrderDetail/${this.orderId}`,
      method: 'GET',
      header: {
        'content-type': 'application/json',
        authorization: app.globalData.token || ''
      },
      success: (res) => {
        if (res.data.code === 200) {
          const detail = res.data.data || {}
          const orderInfo = detail.orderInfo || null
          const executionInfo = detail.executionInfo || null
          const refundInfo = detail.refundInfo || null

          this.setData({
            loading: false,
            orderInfo,
            orderView: this.buildOrderView(orderInfo, executionInfo, refundInfo),
            executionInfo,
            refundInfo,
            projectImage: detail.projectImage || '/images/head.png',
            uiState: this.buildUiState(orderInfo, executionInfo, refundInfo)
          })
          return
        }

        this.setData({ loading: false })
        wx.showToast({
          title: res.data.msg || '获取订单详情失败',
          icon: 'none'
        })
      },
      fail: () => {
        this.setData({ loading: false })
        wx.showToast({
          title: '获取订单详情失败',
          icon: 'none'
        })
      }
    })
  },

  buildOrderView(orderInfo, executionInfo, refundInfo) {
    if (!orderInfo) {
      return null
    }
    return {
      amountText: this.formatMoney(orderInfo.orderAmount),
      expectedServiceTimeText: this.formatDateTime(orderInfo.expectedServiceTime),
      createTimeText: this.formatDateTime(orderInfo.createTime),
      executeTimeText: this.formatDateTime(executionInfo && executionInfo.executeTime),
      refundApplyTimeText: this.formatDateTime(refundInfo && refundInfo.applyTime)
    }
  },

  buildUiState(orderInfo, executionInfo, refundInfo) {
    const stateKey = this.getStateKey(orderInfo, refundInfo)

    const configMap = {
      pendingPay: {
        bannerTitle: '订单待支付',
        bannerDesc: '请尽快完成支付，超时将取消订单',
        showRefundCard: false,
        showExecutionCard: false,
        showCancelButton: true,
        showPayButton: true,
        showRefundButton: false
      },
      pendingService: {
        bannerTitle: '订单待服务',
        bannerDesc: '工作人员将为您服务',
        showRefundCard: false,
        showExecutionCard: false,
        showCancelButton: false,
        showPayButton: false,
        showRefundButton: true
      },
      refundProcessing: {
        bannerTitle: '订单退款中',
        bannerDesc: '预计2个工作日退回原账户',
        showRefundCard: true,
        showExecutionCard: false,
        showCancelButton: false,
        showPayButton: false,
        showRefundButton: false
      },
      refundFailed: {
        bannerTitle: '退款失败',
        bannerDesc: (refundInfo && refundInfo.failReason) || '退款处理失败，请稍后重试',
        showRefundCard: true,
        showExecutionCard: false,
        showCancelButton: false,
        showPayButton: false,
        showRefundButton: true
      },
      refunded: {
        bannerTitle: '订单已退款',
        bannerDesc: '退款已退回原支付账户',
        showRefundCard: true,
        showExecutionCard: false,
        showCancelButton: false,
        showPayButton: false,
        showRefundButton: false
      },
      executed: {
        bannerTitle: '订单已执行',
        bannerDesc: '',
        showRefundCard: false,
        showExecutionCard: !!executionInfo,
        showCancelButton: false,
        showPayButton: false,
        showRefundButton: false
      },
      completed: {
        bannerTitle: '订单已完成',
        bannerDesc: '',
        showRefundCard: false,
        showExecutionCard: !!executionInfo,
        showCancelButton: false,
        showPayButton: false,
        showRefundButton: false
      },
      closed: {
        bannerTitle: '订单已关闭',
        bannerDesc: orderInfo && orderInfo.cancelReason ? orderInfo.cancelReason : '订单已关闭',
        showRefundCard: false,
        showExecutionCard: false,
        showCancelButton: false,
        showPayButton: false,
        showRefundButton: false
      }
    }

    return {
      key: stateKey,
      ...configMap[stateKey]
    }
  },

  getStateKey(orderInfo, refundInfo) {
    if (!orderInfo) {
      return 'closed'
    }

    if (refundInfo) {
      if (refundInfo.refundStatus === '0') {
        return 'refundProcessing'
      }
      if (refundInfo.refundStatus === '2') {
        return 'refundFailed'
      }
      if (refundInfo.refundStatus === '1') {
        return 'refunded'
      }
    }

    if (orderInfo.tradeStatus === '0' || orderInfo.orderStatus === '0') {
      return 'pendingPay'
    }
    if (orderInfo.orderStatus === '1') {
      return 'pendingService'
    }
    if (orderInfo.orderStatus === '2') {
      return 'executed'
    }
    if (orderInfo.orderStatus === '3') {
      return 'completed'
    }
    if (orderInfo.orderStatus === '5' || orderInfo.tradeStatus === '5') {
      return 'closed'
    }
    return 'pendingService'
  },

  formatDateTime(value) {
    if (!value) {
      return '-'
    }
    return String(value).replace('T', ' ').slice(0, 19)
  },

  formatMoney(value) {
    return Number(value || 0).toFixed(2)
  },

  previewImage(e) {
    const url = e.currentTarget.dataset.url
    if (!url) {
      return
    }
    wx.previewImage({
      urls: [url], 
      current: url
    })
  },

  handleCancelOrder() {
    wx.showModal({
      title: '取消订单',
      content: '确认取消这笔订单吗？',
      success: res => {
        if (!res.confirm) {
          return
        }
        wx.showLoading({ title: '处理中...' })
        wx.request({
          url: `http://localhost:8080/wxLogin/cancelOrder/${this.orderId}`,
          method: 'POST',
          header: {
            'content-type': 'application/json',
            authorization: app.globalData.token || ''
          },
          success: resp => {
            wx.hideLoading()
            if (resp.data.code !== 200) {
              wx.showToast({ title: resp.data.msg || '取消失败', icon: 'none' })
              return
            }
            wx.showToast({ title: '取消成功', icon: 'success' })
            this.getOrderDetail()
          },
          fail: () => {
            wx.hideLoading()
            wx.showToast({ title: '取消失败，请稍后重试', icon: 'none' })
          }
        })
      }
    })
  },

  handlePayOrder() {
    const { orderInfo, orderView, projectImage } = this.data
    if (!orderInfo || !orderView) {
      return
    }
    wx.navigateTo({
      url: `/servicePage/pages/payPage/payPage?` +
        `orderId=${orderInfo.id}` +
        `&serviceName=${encodeURIComponent(orderInfo.projectName || '')}` +
        `&price=${orderView.amountText}` +
        `&unit=` +
        `&imageUrl=${encodeURIComponent(projectImage || '')}` +
        `&familyName=${encodeURIComponent(orderInfo.elderName || '')}` +
        `&serviceTime=${encodeURIComponent(orderInfo.expectedServiceTime || '')}` +
        `&remark=${encodeURIComponent(orderInfo.remark || '')}` +
        `&totalPrice=${orderView.amountText}`
    })
  },

  handleRefund() {
    wx.showActionSheet({
      itemList: this.refundReasonOptions,
      success: res => {
        const refundReason = this.refundReasonOptions[res.tapIndex]
        wx.showLoading({ title: '提交中...' })
        wx.request({
          url: 'http://localhost:8080/wxLogin/applyRefund',
          method: 'POST',
          header: {
            'content-type': 'application/json',
            authorization: app.globalData.token || ''
          },
          data: {
            orderId: this.orderId,
            refundReason
          },
          success: resp => {
            wx.hideLoading()
            if (resp.data.code !== 200) {
              wx.showToast({ title: resp.data.msg || '申请退款失败', icon: 'none' })
              return
            }
            wx.showToast({ title: '退款申请已提交', icon: 'success' })
            this.getOrderDetail()
          },
          fail: () => {
            wx.hideLoading()
            wx.showToast({ title: '申请退款失败，请稍后重试', icon: 'none' })
          }
        })
      }
    })
  },

  handleBack() {
    wx.navigateBack({ delta: 1 })
  }
})
