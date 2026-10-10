const app = getApp()
Page({
  data: {
    tabs: [
      { name: '全部', tradeStatus: null },
      { name: '待支付', tradeStatus: 0 },
      { name: '已支付', tradeStatus: 1 }
    ],
    currentTab: 0,
    elderId: "",
    billList: []
  },

  onLoad(options) {
    // 接收上一页跳转传来的老人ID
    if (options.id) {
      this.setData({
        elderId: options.id
      })
      // 默认加载 全部
      this.getBillList(this.data.tabs[0].tradeStatus);
    }
  },

  // 切换标签
  switchTab(e) {
    const index = e.currentTarget.dataset.index;
    const tradeStatus = e.currentTarget.dataset.tradestatus;
    this.setData({ currentTab: index });
    this.getBillList(tradeStatus);
  },

  // POST 请求后端接口
  getBillList(tradeStatus) {
    const { elderId } = this.data;
    if (!elderId) return;

    wx.showLoading({ title: '加载中...' });

    let postData = { elderId };
    if (tradeStatus !== null) {
      postData.tradeStatus = tradeStatus;
    }

    wx.request({
      url: 'http://localhost:8080/wxLogin/list', // 替换成真实接口域名
      method: 'POST',
      header: {
        'content-type': 'application/json',
        'authorization': app.globalData.token
      },
      data: postData,
      success: (res) => {
        console.log(res.data);
        if (res.data.code === 200) {
          this.setData({
            billList: res.data.data || []
          })
        } else {
          wx.showToast({ title: res.data.msg || '获取失败', icon: 'none' });
        }
      },
      fail: () => {
        wx.showToast({ title: '网络请求失败', icon: 'error' });
      },
      complete: () => {
        wx.hideLoading();
      }
    })
  },

  // 查看明细
  goToBill(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/family/pages/elderOrder/elderOrder?id=${id}`
    });
  },

  // 去支付
  goPay(e) {
    const id = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/pay/pay?id=${id}`
    });
  }
})