// my/pages/contractDetails/contractDetails.js
Page({

  /**
   * 页面的初始数据
   */
  data: {

  },
  

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {
  
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onLoad(options) {
    console.log("接收的合同详情参数：", options)
  
    // 接收传来的 2 个参数
    const roomCode = options.roomCode       // 房间号
    const roomTypeName = options.roomTypeName // 房型
    const contractName = options.contractName //合同名称
    this.setData({
      roomCode: roomCode,
      roomTypeName: roomTypeName
    })
    wx.setNavigationBarTitle({
      title: contractName
    })
  },
  

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh() {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {

  }
})