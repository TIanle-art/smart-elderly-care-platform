// index.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {},

  // 一键登录：获取手机号 + wx.login
  getPhoneNumber(e) {
    // 1. 先判断用户是否允许授权
    if (!e.detail.code) {
      wx.showToast({
        title: '已取消授权',
        icon: 'none'
      })
      return;
    }

    // 2. 获取手机号专用 code（必须用这个）
    let phoneCode = e.detail.code;

    // 3. 执行 wx.login 获取登录 code
    wx.login({
      success: (res) => {
        let loginCode = res.code;

        console.log("登录code：", loginCode);
        console.log("手机号code：", phoneCode);

        // 4. 两个 code 一起传给后端
        wx.request({
          url: "http://localhost:8080/wxLogin/phoneLogin",
          method: "POST",
          data: {
            code: loginCode, // 登录用
            phoneCode: phoneCode // 拿手机号
          },
          header: {
            'content-type': 'application/json'
          },
          success: (res) => {
            console.log(res.data)
            console.log("登录成功：", res.data.data.token);
            app.globalData.token = res.data.data.token
            app.globalData.userName = res.data.data.name
            wx.switchTab({
              url:"/pages/home/home"
            })
            wx.showToast({
              title: '登录成功'
            });
          },
          fail: (err) => {
            console.log("请求失败：", err);
          }
        });
      },
      fail: (err) => {
        console.log("wx.login 失败：", err);
      }
    });
  },

  getCount() {
    wx.request({
      url: 'http://localhost:8080/wxLogin/cancelled-count',
      method: 'GET',
      header: {
        'content-type': 'application/json', // 默认值
        'authorization': app.globalData.token
      },
      success: (resp) => {
        console.log(resp)
      }
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})