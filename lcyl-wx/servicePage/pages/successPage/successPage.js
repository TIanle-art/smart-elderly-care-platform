Page({
  data: {
    orderId: "",
    serviceName: "",
    price: "",
    unit: "",
    imageUrl: "",
    familyName: "",
    serviceTime: "",
    remark: "",
    totalPrice: ""
  },

  onLoad(options) {
    this.setData({
      orderId: options.orderId,
      serviceName: decodeURIComponent(options.serviceName),
      price: options.price,
      unit: options.unit,
      imageUrl: decodeURIComponent(options.imageUrl),
      familyName: decodeURIComponent(options.familyName),
      serviceTime: decodeURIComponent(options.serviceTime),
      remark: decodeURIComponent(options.remark),
      totalPrice: options.totalPrice
    })
  },

  backToHome() {
    wx.reLaunch({ url: "/pages/home/home" })
  },

  viewOrder() {
    wx.navigateTo({
      url: "/my/pages/myOrder/myOrder"
    })
  }
})
