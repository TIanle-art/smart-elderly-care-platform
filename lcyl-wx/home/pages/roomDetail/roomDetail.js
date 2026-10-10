const app = getApp()
Page({
  data: {
    roomInfo: {}
  },

  onLoad(options) {
    const id = options.id
    // 打印一下接收的id
    console.log("房型ID：", id)
    this.getRoomDetail(id)
  },

  // 获取房型详情（带 token 访问！）
  getRoomDetail(id) {
    // ✅ 打印当前全局 token
    // console.log("当前携带的token：", app.globalData.token)

    wx.request({
      url: 'http://localhost:8080/wxLogin/roomType/' + id,
      method: 'GET',
      header: {
        'content-type': 'application/json',
        'authorization': app.globalData.token || ''
      },
      success: (res) => {
        console.log("房型详情返回：", res.data)
        if (res.data.code === 200) {
          this.setData({
            roomInfo: res.data.data
          })
             // ✅ 只加这一行：把导航栏标题改成 双人间/单人间等
    wx.setNavigationBarTitle({
      title: res.data.data.name
    })
        }
      },
      fail(err) {
        console.log("请求失败", err)
      }
    })
  }
})