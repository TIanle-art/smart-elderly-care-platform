const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    defaultAvatar: '/images/head.png',

    defaultName: '用户名',

    form: {
      name: '',
      avatar: '',
      gender: ''
    },

    baseUrl: 'http://localhost:8080',

  },

  goProfile() {
    wx.navigateTo({
      url: '/my/pages/myProfile/myProfile'
    })
  },

  logout() {
    console.log("点击");
    app.globalData.token = ''
    console.log("这是log的值", app.globalData.token);
    wx.reLaunch({
      url: "/pages/index/index"
    })
  },

  /*
  * 读取用户信息
  */
  loadUserInfo() {
    // 请求后端 / 从缓存读取用户信息
    wx.request({
      url: `${this.data.baseUrl}/wxLogin/get/profile`,
      method: 'GET',
      header: {
        'content-type': 'application/json',
        'authorization': app.globalData.token
      },
      success: (res) => {
        const result = res.data || {}
  
        if (result.code === 200) {
          const user = result.data || {}
  
          this.setData({
            'form.name': user.name || '',
            'form.avatar': user.avatar || '',
            'form.gender': user.gender || ''
          })
  
        } else {
          wx.showToast({
            title: result.msg || '获取失败',
            icon: 'none'
          })
        }
      },
      fail: (err) => {
        console.error('获取失败：', err)
        wx.showToast({
          title: '网络异常',
          icon: 'none'
        })
      }
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
  },
  
  onShow(){
    this.loadUserInfo()
  }
})