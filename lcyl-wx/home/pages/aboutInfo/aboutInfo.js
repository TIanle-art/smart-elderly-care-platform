Page({
  data: {
    statusBarHeight: 20,
    navBarHeight: 44,

    showPhoneModal: false,
    phoneNumber: '0371-000-0000',

    showViewer: false,
    currentImage: '',
    currentImageIndex: 0,

    imageList: [
      '/images/pic-01.png',
      '/images/pic-02.png',
      '/images/pic-03.png'
    ]
  },

  onLoad() {
    const systemInfo = wx.getWindowInfo ? wx.getWindowInfo() : wx.getSystemInfoSync()
    const menuButton = wx.getMenuButtonBoundingClientRect ?
      wx.getMenuButtonBoundingClientRect() :
      null

    let statusBarHeight = systemInfo.statusBarHeight || 20
    let navBarHeight = 44

    if (menuButton) {
      navBarHeight = (menuButton.top - statusBarHeight) * 2 + menuButton.height
    }

    this.setData({
      statusBarHeight,
      navBarHeight
    })
  },

  goBack() {
    const pages = getCurrentPages()
  
    if (pages.length > 1) {
      wx.navigateBack({
        delta: 1
      })
    } else {
      wx.switchTab({
        url: '/pages/home/home'
      })
    }
  },

  openImageViewer(e) {
    const currentImage = e.currentTarget.dataset.current
    const index = this.data.imageList.findIndex(item => item === currentImage)

    this.setData({
      showViewer: true,
      currentImage,
      currentImageIndex: index < 0 ? 0 : index
    })
  },

  closeImageViewer() {
    this.setData({
      showViewer: false,
      currentImage: '',
      currentImageIndex: 0
    })
  },

  showPhonePopup() {
    this.setData({
      showPhoneModal: true
    })
  },

  hidePhonePopup() {
    this.setData({
      showPhoneModal: false
    })
  },

  stopPropagation() {},

  makePhoneCall() {
    wx.makePhoneCall({
      phoneNumber: this.data.phoneNumber,
      success: () => {
        this.hidePhonePopup()
      },
      fail: () => {
        wx.showToast({
          title: '已取消拨号',
          icon: 'none'
        })
      }
    })
  }
})