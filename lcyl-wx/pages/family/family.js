const app =getApp();
Page({
  data: {
    familyList: [
      
    ],
    showFamilyModal: false, // 控制弹窗显隐
    inputFamilyName: ''     // 弹窗输入内容
  },

  // 解绑操作
  handleUnbind(e) {
    const id = e.currentTarget.dataset.id;
    wx.showModal({
      title: "提示",
      content: "确定要解绑该家人吗？",
      success: (res) => {
        if (res.confirm) {
          // 执行解绑逻辑，如更新数据
         wx.request({
          url: `http://localhost:8080/wxLogin/deleteElder/${id}`,
          method:'DELETE',
          header: {
            'content-type': 'application/json',
            'authorization': app.globalData.token
          },
          success:(res) =>{
            if(res.data.code == 200){
              wx.showToast({
                title: '解绑成功',
                icon: 'success',
                duration: 2000
              });
              this.getElderList();
            }
          }
         })
        }
      }
    });
  },

  // 跳转健康数据
  goToHealth(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/health/health?id=${id}`
    });
  },

  // 跳转我的账单
  goToBill(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/family/pages/mybill/mybill?id=${id}`
    });
  },

  // 跳转绑定家人
  goToBind() {
    wx.navigateTo({
      url: "/family/pages/addfamily/addfamily"
    });
  },

  getElderList() {
    wx.request({
      url: 'http://localhost:8080/wxLogin/getElderBedList',
      method: "POST",
      header: {
        'content-type': 'application/json',
        'authorization': app.globalData.token
      },
      success: (resp) => {
        if (resp.data.code == 200) {
          this.setData({
            familyList: resp.data.data
          })
        }
      }
    })
  },
  
  onLoad(options) {
    this.getElderList()
  },
  onShow() {
    this.getElderList()
  },
});