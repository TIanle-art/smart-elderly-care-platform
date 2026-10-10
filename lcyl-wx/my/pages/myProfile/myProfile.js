const app = getApp()
Page({
  data: {
    statusBarHeight: 20,
    navBarHeight: 44,

    // 后端基础地址：这里必须改成你自己的可访问地址
    baseUrl: 'http://localhost:8080',

    defaultAvatar: '/images/head.png',

    uploading: false,
    saving: false,

    form: {
      name: '',
      avatar: '',
      gender: ''
    }
  },

  onLoad() {
    this.initNavBar()
    this.loadUserInfo()
  },

  // 初始化导航栏高度
  initNavBar() {
    const systemInfo = wx.getWindowInfo ? wx.getWindowInfo() : wx.getSystemInfoSync()
    const menuButtonInfo = wx.getMenuButtonBoundingClientRect()

    const statusBarHeight = systemInfo.statusBarHeight || 20
    const navBarHeight =
      (menuButtonInfo.top - statusBarHeight) * 2 + menuButtonInfo.height

    this.setData({
      statusBarHeight,
      navBarHeight
    })
  },

  // 加载当前用户资料
  loadUserInfo() {
    // const token = wx.getStorageSync('token')
  
    // if (!token) {
    //   wx.showToast({
    //     title: '请先登录',
    //     icon: 'none'
    //   })
    //   return
    // }
  
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

  goBack() {
    const pages = getCurrentPages()
    console.log('当前页面栈长度：', pages.length)
  
    if (pages.length > 1) {
      wx.navigateBack({
        delta: 1
      })
    } else {
      wx.switchTab({
        url: '/pages/my/my'
      })
    }
  },

  // 输入昵称
  onNameInput(e) {
    this.setData({
      'form.name': e.detail.value
    })
  },

  // 选择头像并上传
  chooseAvatar() {
    if (this.data.uploading) {
      return
    }

    wx.chooseMedia({
      count: 1,
      mediaType: ['image'],
      sourceType: ['album', 'camera'],
      success: (res) => {
        const tempFilePath = res.tempFiles[0].tempFilePath

        this.uploadAvatar(tempFilePath)
      }
    })
  },

  // 上传头像到后端，再由后端传 OSS
  uploadAvatar(filePath) {
    this.setData({
      uploading: true
    })

    wx.showLoading({
      title: '上传中...',
      mask: true
    })

    wx.uploadFile({
      url: `${this.data.baseUrl}/upload`,
      filePath: filePath,
      name: 'file',
      success: (uploadRes) => {
        let result = {}

        try {
          result = JSON.parse(uploadRes.data)
        } catch (e) {
          wx.showToast({
            title: '上传返回格式错误',
            icon: 'none'
          })
          return
        }

        if (result.code === 200) {
          this.setData({
            'form.avatar': result.data
          })

          wx.showToast({
            title: '头像上传成功',
            icon: 'success'
          })
        } else {
          wx.showToast({
            title: result.msg || '上传失败',
            icon: 'none'
          })
        }
      },
      fail: (err) => {
        console.error('上传失败：', err)
        wx.showToast({
          title: '上传失败',
          icon: 'none'
        })
      },
      complete: () => {
        this.setData({
          uploading: false
        })
        wx.hideLoading()
      }
    })
  },

  // 选择性别
  showGenderOptions() {
    wx.showActionSheet({
      itemList: ['男', '女'],
      success: (res) => {
        const genderList = ['男', '女']
        this.setData({
          'form.gender': genderList[res.tapIndex]
        })
      }
    })
  },

  // 保存资料
  handleSave() {
    if (this.data.saving) {
      return
    }
  
    const { name, avatar, gender } = this.data.form
  
    if (!name || !name.trim()) {
      wx.showToast({
        title: '请输入昵称',
        icon: 'none'
      })
      return
    }
  
    if (!gender) {
      wx.showToast({
        title: '请选择性别',
        icon: 'none'
      })
      return
    }

    this.setData({
      saving: true
    })
  
    wx.showLoading({
      title: '保存中...',
      mask: true
    })
  
    wx.request({
      url: `${this.data.baseUrl}/wxLogin/profile`,
      method: 'PUT',
      data: {
        name: name.trim(),
        avatar,
        gender
      },
      header: {
        'content-type': 'application/json',
        'authorization': app.globalData.token
      },
      success: (res) => {
        const result = res.data || {}
  
        if (result.code === 200) {
          wx.setStorageSync('userInfo', {
            name: name.trim(),
            avatar,
            gender
          })
  
          wx.showToast({
            title: result.msg || '保存成功',
            icon: 'success'
          })
  
          setTimeout(() => {
            wx.navigateBack({
              delta: 1
            })
          }, 1200)
        } else {
          wx.showToast({
            title: result.msg || '保存失败',
            icon: 'none'
          })
        }
      },
      fail: (err) => {
        console.error('保存失败：', err)
        wx.showToast({
          title: '网络异常',
          icon: 'none'
        })
      },
      complete: () => {
        this.setData({
          saving: false
        })
        wx.hideLoading()
      }
    })
  }
})