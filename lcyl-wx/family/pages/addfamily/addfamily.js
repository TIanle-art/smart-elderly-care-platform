// family/pages/addfamily/addfamily.js
const app = getApp();
Page({

  data: {
    elderList: [],
    elderIndex: 0,         // 必须加
    selectedElderId: '',   // 选中的老人ID
    selectedIdCardNo: '', // 选中老人的真实身份证号（后端返回）
    familyName: '',       // 家人姓名
    idCard: '',           // 用户输入的身份证号
    relation: ''          // 称呼
  },

  // 获取老人列表
  getElderList() {
    wx.request({
      url: 'http://localhost:8080/wxLogin/getElderList',
      method: "GET",
      header: {
        'content-type': 'application/json',
        'authorization': app.globalData.token
      },
      success: (resp) => {
        if (resp.data.code == 200) {
          console.log(resp.data);
          this.setData({
            elderList: resp.data.data
          })
        }
      }
    })
  },

  // 选择老人
  onElderChange(e) {
    const index = e.detail.value;
    const item = this.data.elderList[index];

    this.setData({
      elderIndex: index,
      familyName: item.name,
      selectedElderId: item.id,          // 选中的ID
      selectedIdCardNo: item.idCardNo,   // 后端返回的真实身份证
      idCard: '' // 切换老人清空输入框
    });
  },

  // 输入身份证
  onIdCardInput(e) {
    this.setData({
      idCard: e.detail.value
    });
  },

  // 输入称呼
  onRelationInput(e) {
    this.setData({
      relation: e.detail.value
    });
  },

  confirmBind() {
    const {
      selectedElderId,
      familyName,
      idCard,
      relation,
      selectedIdCardNo
    } = this.data;

    // 1. 校验是否选择了老人
    if (!selectedElderId) {
      wx.showToast({ title: '请选择家人姓名', icon: 'none' });
      return;
    }

    // 2. 校验称呼
    if (!relation.trim()) {
      wx.showToast({ title: '请输入您对家人的称呼', icon: 'none' });
      return;
    }

    // 3. 校验身份证格式
    if (!idCard.trim() || !/^\d{17}[\dXx]$/.test(idCard)) {
      wx.showToast({ title: '请输入正确的18位身份证号', icon: 'none' });
      return;
    }

    // 4. 关键：和后端返回的真实身份证比对
    if (idCard !== selectedIdCardNo) {
      wx.showToast({
        title: '身份证号码错误',
        icon: 'error',
        duration: 2000
      });
      return;
    }

    wx.request({
      url: 'http://localhost:8080/wxLogin/addInfo',
      method: 'POST',
      header: {
        'authorization': app.globalData.token
      },
      data: {
        elderId: selectedElderId,
        relation: relation
      },
      success: (res) => {
        if (res.data.code === 200) {
          wx.showToast({
            title: '绑定成功',
            icon: 'success'
          });
          setTimeout(() => {
            wx.navigateBack();
          }, 1500);
        } else {
          wx.showToast({
            title: res.data.message || '绑定失败',
            icon: 'none'
          });
        }
      },
      fail: () => {
        wx.showToast({ title: '网络异常', icon: 'error' });
      }
    });
  },

  onLoad(options) {
    this.getElderList()
  },
})