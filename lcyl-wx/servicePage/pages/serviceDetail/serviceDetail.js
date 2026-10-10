const app = getApp()
Page({
  data: {
    id: "",
    detail: {},
    familyName: "",
    familyId: "",

    serviceDate: "",
    serviceTime: "",
    nowDate: "",
    endDate: "",

    showFamilyList: false,
    familyList: [],
    showEmptyFamilyModal: false // 新增
  },

  onLoad(options) {
    this.setData({
      id: options.id,
      nowDate: new Date().toISOString().slice(0, 10),
      endDate: "2030-12-31"
    })
    this.getDetail()
    this.getFamilyList()
  },

  onDateChange(e) {
    this.setData({ serviceDate: e.detail.value })
  },
  onTimeChange(e) {
    this.setData({ serviceTime: e.detail.value })
  },

  getDetail() {
    wx.request({
      url: "http://172.16.20.15:8080/wxLogin/item/list",
      header: { authorization: app.globalData.token || '' },
      success: (res) => {
        if (res.data.code === 200) {
          let list = res.data.rows || []
          let current = list.find(item => item.id == this.data.id)
          this.setData({ detail: current })
        }
      }
    })
  },

  getFamilyList() {
    wx.request({
      url: "http://172.16.20.15:8080/wxLogin/getElderBedList",
      method: "POST",
      header: {
        'content-type': 'application/json',
        'authorization': app.globalData.token
      },
      success: (resp) => {
        if (resp.data.code == 200) {
          this.setData({ familyList: resp.data.data || [] })
        }
      }
    })
  },

  showFamilyPicker() {
    this.setData({ showFamilyList: true })
  },
  hideFamilyPicker() {
    this.setData({ showFamilyList: false })
  },

  selectFamilyItem(e) {
    const id = e.currentTarget.dataset.id
    const name = e.currentTarget.dataset.name
    this.setData({
      familyId: id,
      familyName: name,
      showFamilyList: false
    })
  },

  // ================== 新增：点击前检查有无家人 ==================
  checkFamilyBeforeShow() {
    const { familyList } = this.data
    if (!familyList || familyList.length === 0) {
      this.setData({ showEmptyFamilyModal: true })
      return
    }
    this.setData({ showFamilyList: true })
  },

  // 关闭弹窗
  closeEmptyModal() {
    this.setData({ showEmptyFamilyModal: false })
  },

  // 去绑定家人
  goToBindFamily() {
    this.setData({ showEmptyFamilyModal: false })
    wx.navigateTo({
      url: '/family/pages/addfamily/addfamily'
    })
  },

  toBuy() {
    const { familyName, serviceDate, serviceTime, detail } = this.data
    if (!familyName) {
      wx.showToast({ title: '请选择服务家人', icon: 'none' })
      return
    }
    if (!serviceDate || !serviceTime) {
      wx.showToast({ title: '请选择日期时间', icon: 'none' })
      return
    }

    const fullTime = serviceDate + ' ' + serviceTime

    wx.navigateTo({
      url: `/servicePage/pages/orderConfirm/orderConfirm?serviceId=${detail.id}&serviceName=${detail.itemName}&price=${detail.price}&unit=${detail.unit}&familyId=${this.data.familyId}&familyName=${familyName}&serviceTime=${encodeURIComponent(fullTime)}&imageUrl=${detail.imageUrl}`
    })
  }
})