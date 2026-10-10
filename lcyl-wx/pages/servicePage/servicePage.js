const app = getApp()
Page({
  data: {
    serviceList: [],
    originalList: [],
    searchKey: "",
    showOrderPopup: false,
    selectedService: null,
    familyName: "",
    familyId: "",
    serviceDate: "",
    serviceTime: "",
    showFamilyList: false,
    familyList: [],
    nowDate: "",
    showNoFamilyTip: false // 无家人弹窗控制变量
  },

  onLoad(options) {
    this.getServiceList()
    this.getFamilyList()
    this.setData({ nowDate: new Date().toISOString().slice(0, 10) })
  },

  onSearchInput(e) {
    this.setData({ searchKey: e.detail.value.trim() })
    this.doSearch()
  },

  doSearch() {
    const { originalList, searchKey } = this.data
    if (!searchKey) { 
      this.setData({ serviceList: originalList }); 
      return 
    }
    const filtered = originalList.filter(i => i.itemName.includes(searchKey))
    this.setData({ serviceList: filtered })
  },

  getServiceList() {
    wx.request({
      url: "http://172.16.20.15:8080/wxLogin/item/list",
      header: { authorization: app.globalData.token || '' },
      success: res => {
        if (res.data.code === 200) {
          this.setData({ serviceList: res.data.rows, originalList: res.data.rows })
        }
      }
    })
  },

  goServiceDetail(e) {
    wx.navigateTo({ url: '/servicePage/pages/serviceDetail/serviceDetail?id=' + e.currentTarget.dataset.id })
  },

  showOrderPopup(e) {
    const id = e.currentTarget.dataset.id
    const service = this.data.serviceList.find(i => i.id == id)
    this.setData({ 
      showOrderPopup: true, 
      selectedService: service, 
      familyName: "", 
      serviceDate: "", 
      serviceTime: "" 
    })
  },

  hideOrderPopup() { 
    this.setData({ showOrderPopup: false }) 
  },

  getFamilyList() {
    wx.request({
      url: "http://172.16.20.15:8080/wxLogin/getElderBedList",
      method: "POST",
      header: { authorization: app.globalData.token },
      success: res => { 
        if (res.data.code === 200) {
          this.setData({ familyList: res.data.data || [] })
        } else {
          this.setData({ familyList: [] })
        }
      },
      fail: () => {
        this.setData({ familyList: [] })
      }
    })
  },

  // 检查家人列表，控制弹窗显示
  checkFamilyAndShowPicker() {
    const { familyList } = this.data
    if (!familyList || familyList.length === 0) {
      // 无家人，弹出自定义弹窗
      this.setData({ showNoFamilyTip: true })
      return
    }
    // 有家人，正常打开选择器
    this.setData({ showFamilyList: true })
  },

  showFamilyPicker() { 
    this.setData({ showFamilyList: true }) 
  },

  hideFamilyPicker() { 
    this.setData({ showFamilyList: false }) 
  },

  selectFamilyItem(e) {
    this.setData({ 
      familyId: e.currentTarget.dataset.id, 
      familyName: e.currentTarget.dataset.name, 
      showFamilyList: false 
    })
  },

  onDateChange(e) { 
    this.setData({ serviceDate: e.detail.value }) 
  },

  onTimeChange(e) { 
    this.setData({ serviceTime: e.detail.value }) 
  },

  // 关闭无家人弹窗
  hideNoFamilyTip() {
    this.setData({ showNoFamilyTip: false })
  },

  // 跳转绑定家人页面（替换为你的实际路径）
  goToBindFamily() {
    this.setData({ showNoFamilyTip: false })
    wx.navigateTo({
      url: '/family/pages/addfamily/addfamily'
    })
  },

  submitOrder() {
    const { familyName, serviceDate, serviceTime, selectedService, familyId } = this.data
    
    if (!familyName || !serviceDate || !serviceTime) {
      wx.showToast({ title: '请完善信息', icon: 'none' })
      return
    }

    const fullTime = serviceDate + ' ' + serviceTime
    wx.navigateTo({
      url: `/servicePage/pages/orderConfirm/orderConfirm?serviceId=${selectedService.id}&serviceName=${selectedService.itemName}&price=${selectedService.price}&unit=${selectedService.unit}&familyId=${familyId}&familyName=${familyName}&serviceTime=${encodeURIComponent(fullTime)}&imageUrl=${selectedService.imageUrl}`
    })
  },

  onPullDownRefresh() {
    this.getServiceList()
    setTimeout(() => wx.stopPullDownRefresh(), 1000)
  }
})