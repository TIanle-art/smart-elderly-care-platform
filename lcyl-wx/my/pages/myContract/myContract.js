const app = getApp()

Page({
  data: {
    statusBarHeight: 20,
    navBarHeight: 44,
    contractList: [],
    showDownloadPopup: false,
    currentContract: {},
    showCopiedToast: false,
    loading: false,
    elderList: [],
  },

  onShow() {
    this.initNavBar()
    this.getMyElderList()
  },

  initNavBar() {
    try {
      const systemInfo = wx.getWindowInfo?.() || wx.getSystemInfoSync()
      const menuButtonInfo = wx.getMenuButtonBoundingClientRect?.()
      let statusBarHeight = systemInfo.statusBarHeight || 20
      let navBarHeight = 44
      if (menuButtonInfo) {
        const gap = menuButtonInfo.top - statusBarHeight
        navBarHeight = gap * 2 + menuButtonInfo.height
      }
      this.setData({ statusBarHeight, navBarHeight })
    } catch (err) {}
  },

  getMyElderList() {
    this.setData({ loading: true })
    wx.request({
      url: 'http://localhost:8080/wxLogin/getElderBedList',
      method: "POST",
      header: {
        'content-type': 'application/json',
        'authorization': app.globalData.token || ''
      },
      success: (resp) => {
        if (resp.data.code == 200) {
          let elderList = resp.data.data || []
          this.setData({ elderList })
          this.getAllContractForAllElders(elderList)
        }
      },
      fail: () => {
        this.setData({ loading: false })
      }
    })
  },

  // 获取老人头像（支持多人）
  getElderPhoto(elderId, index) {
    if (!elderId) return

    wx.request({
      url: `http://localhost:8080/wxLogin/bed/${elderId}`,
      method: 'GET',
      header: {
        'authorization': app.globalData.token || ''
      },
      success: (res) => {
        console.log(res.data.data);
        let photo = res.data.data.photo || ""
        let roomCode = res.data.data.roomCode || ""
        let roomTypeName = res.data.data.roomTypeName || ""
        this.setData({
          [`contractList[${index}].photo`]: photo,
          [`contractList[${index}].roomCode`]: roomCode,
          [`contractList[${index}].roomTypeName`]: roomTypeName
        })
        
      }
    })
  },

  getAllContractForAllElders(elderList) {
    wx.request({
      url: 'http://localhost:8080/wxLogin/contract/list?pageSize=1000',
      method: 'GET',
      header: {
        Authorization: app.globalData.token || ''
      },
      success: (res) => {
        if (res.data.code !== 200) return
  
        const allContracts = res.data.rows || []
        const myAllContracts = allContracts.filter(item => {
          for (let i = 0; i < elderList.length; i++) {
            if (item.elderId == elderList[i].id) return true
          }
          return false
        })

        // 🔥 前端根据 当前时间 自动判断状态
        const now = new Date()

        const list = myAllContracts.map(item => {
          const startTime = item.startTime ? new Date(item.startTime) : null
          const endTime = item.endTime ? new Date(item.endTime) : null

          let statusText = ""
          let statusClass = ""

          if (!startTime || !endTime) {
            statusText = "暂无期限"
            statusClass = "status-pending"
          } else if (now < startTime) {
            statusText = "未生效"
            statusClass = "status-pending"
          } else if (now > endTime) {
            statusText = "已过期"
            statusClass = "status-expired"
          } else {
            statusText = "已生效"
            statusClass = "status-active"
          }

          return {
            id: item.id,
            elderId: item.elderId,
            contractNo: item.contractNo,
            statusText: statusText,
            statusClass: statusClass,
            contractName: item.name,
            roomTypeName: item.roomTypeName,
            roomCode: item.roomCode,
            familyName: item.elderName,
            signDate: item.signDate ? item.signDate.split(' ')[0] : '',
            startTime: item.startTime ? item.startTime.split(' ')[0] : '',
            endTime: item.endTime ? item.endTime.split(' ')[0] : '',
            validPeriod: (item.startTime && item.endTime) 
              ? `${item.startTime.split(' ')[0]}~${item.endTime.split(' ')[0]}` 
              : '暂无',
            fileUrl: item.pdfUrl,
            cancelTime: item.cancelTime ? item.cancelTime.split(' ')[0] : ''
          }
        })
  
        this.setData({ contractList: list })

        list.forEach((item, index) => {
          if (item.elderId) {
            this.getElderPhoto(item.elderId, index)
          }
        })
      },
      complete: () => {
        this.setData({ loading: false })
      }
    })
  },

  handleView(e) {
    const item = e.currentTarget.dataset.item
  
    const roomTypeName = item.roomTypeName
    const roomCode = item.roomCode
    const contractName = item.contractName 
    wx.navigateTo({
      url: `/my/pages/contractDetails/contractDetails?roomTypeName=${roomTypeName}&roomCode=${roomCode}&contractName=${contractName}`,
    })
  },
  handleDownload(e) {
    const item = e.currentTarget.dataset.item
    if (!item.fileUrl) {
      wx.showToast({ title: '暂无文件', icon: 'none' })
      return
    }
    this.setData({
      showDownloadPopup: true,
      currentContract: item
    })
  },

  goBack() {
    wx.navigateBack({ delta: 1, fail: () => wx.switchTab({ url: '/pages/my/index' }) })
  },

  handleCopyLink() {
    const { fileUrl } = this.data.currentContract
    if (!fileUrl) {
      wx.showToast({ title: '链接无效', icon: 'none' })
      return
    }
    wx.setClipboardData({
      data: fileUrl,
      success: () => {
        this.setData({ showDownloadPopup: false })
        this.setData({ showCopiedToast: true })
        setTimeout(() => {
          this.setData({ showCopiedToast: false })
        }, 3000)
      },
      fail: () => {
        wx.showToast({ title: '复制失败', icon: 'error' })
      }
    })
  },

  closePopup() {
    this.setData({ showDownloadPopup: false })
  },

  preventTouchMove() {}
})