// pages/home/home.js
const app =getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    list: [
      // {
      //   id: 1,
      //   imageUrl: '/images/arrows.png',
      //   title: '标题1',
      //   description: '描述信息1'
      // },
      // {
      //   id: 2,
      //   imageUrl: '/images/arrows.png',
      //   title: '标题2',
      //   description: '描述信息2'
      // },
      // {
      //   id: 3,
      //   imageUrl: '/images/arrows.png',
      //   title: '标题3',
      //   description: '描述信息3'
      // }
    ]
  },


 getBedType(){
  wx.request({
    url:'http://localhost:8080/wxLogin/getRoomType',
    method:'GET',
    header:{
      'content-type': 'application/json',
      'authorization': app.globalData.token|| ''  // 这里是后端要的 token 字段
    },
    success:(res)=>{
        console.log(res.data);
        if(res.data.code ==200){
          this.setData({
            list:res.data.data
          })
        }
    }
    
  })
},
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    this.getBedType();
  },
  goRoomDetail(e) {
    const id = e.currentTarget.dataset.id
    wx.navigateTo({
      url: '/home/pages/roomDetail/roomDetail?id=' + id
    })
  },
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {

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