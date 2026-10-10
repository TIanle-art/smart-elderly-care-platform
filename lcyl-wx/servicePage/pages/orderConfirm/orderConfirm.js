Page({
  data: {
    detail: {},
    familyId: "",
    familyName: "",
    serviceTime: "",
    remark: "",
    orderId: ""
  },

  onLoad(options) {
    this.setData({
      detail: {
        id: options.serviceId,
        itemName: options.serviceName,
        price: options.price,
        unit: options.unit,
        imageUrl: options.imageUrl
      },
      familyId: options.familyId,
      familyName: options.familyName,
      serviceTime: decodeURIComponent(options.serviceTime)
    })
  },

  onRemarkInput(e) {
    this.setData({ remark: e.detail.value })
  },

  submitOrder() {
    const data = this.data
    if (!data.familyId || !data.detail.id || !data.serviceTime) {
      wx.showToast({ title: "请完善下单信息", icon: "none" })
      return
    }

    wx.showLoading({ title: "提交中..." })
    wx.request({
      url: "http://172.16.20.15:8080/wxLogin/createOrder",
      method: "POST",
      header: {
        "content-type": "application/json",
        authorization: getApp().globalData.token || ""
      },
      data: {
        elderId: Number(data.familyId),
        projectId: Number(data.detail.id),
        expectedServiceTime: data.serviceTime,
        remark: data.remark
      },
      success: res => {
        wx.hideLoading()
        if (res.data.code !== 200 || !res.data.data) {
          wx.showToast({ title: res.data.msg || "下单失败", icon: "none" })
          return
        }

        const orderInfo = res.data.data
        this.setData({ orderId: orderInfo.orderId })

        wx.showToast({
          title: "订单提交成功",
          icon: "success",
          duration: 1200
        })

        setTimeout(() => {
          wx.navigateTo({
            url: `/servicePage/pages/payPage/payPage?orderId=${orderInfo.orderId}` +
              `&serviceName=${encodeURIComponent(data.detail.itemName)}` +
              `&price=${data.detail.price}` +
              `&unit=${data.detail.unit}` +
              `&imageUrl=${encodeURIComponent(data.detail.imageUrl || "")}` +
              `&familyName=${encodeURIComponent(data.familyName)}` +
              `&serviceTime=${encodeURIComponent(data.serviceTime)}` +
              `&remark=${encodeURIComponent(data.remark || "")}` +
              `&totalPrice=${data.detail.price}`
          })
        }, 1200)
      },
      fail: () => {
        wx.hideLoading()
        wx.showToast({ title: "网络异常，请稍后重试", icon: "none" })
      }
    })
  }
})
