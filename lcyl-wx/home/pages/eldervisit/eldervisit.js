// home/pages/customVisit/customVisit.js
const app = getApp();
Page({
  data: {
    today: '', // 今天日期（用于picker限制）
    name: '',
    phone: '',
    familyName: '',
    date: '', // 预约日期
    period: 'morning',
    selectedTime: '',
    selectedDateTime: '', // 最终拼接好的日期时间（传给后端）
    // 时间列表
    morningTimes: ['08:00', '08:30', '09:00', '09:30', '10:00', '10:30', '11:00', '11:30', '12:00'],
    afternoonTimes: ['12:30', '13:00', '13:30', '14:00', '14:30', '15:00', '15:30', '16:00', '16:30', '17:00', '17:30', '18:00'],
    timeDisabled: true, // 未选日期时，时间不可选
    disabledTimeList: [], // 禁用的时间列表
    elder: [],
    elderIndex: 0, // picker 索引
    selectedElderId: '', // 选中的 id
    selectedElderName: '', // 选中的名字
  },

  // 姓名输入
  onNameInput(e) {
    this.setData({
      name: e.detail.value
    });
  },

  // 手机号输入
  onPhoneInput(e) {
    this.setData({
      phone: e.detail.value
    });
  },

  // 家人姓名输入
  onElderChange(e) {
    const index = e.detail.value;
    const selectItem = this.data.elder[index];

    this.setData({
      elderIndex: index,
      selectedElderId: selectItem.id,
      selectedElderName: selectItem.name
    });
  },

  // 选择日期 → 开启时间选择 + 自动判断禁用已过时间
  onDateChange(e) {
    const date = e.detail.value;
    this.setData({
      date: date,
      timeDisabled: false, // 选了日期，时间启用
      selectedTime: '',
      period: 'morning',
      selectedDateTime: ''
    });
    this.checkTimeDisable(date); // 校验今天的时间是否已过
  },

  onFamilyNameInput(e) {
    this.setData({ familyName: e.detail.value });
  },

  // 切换上午/下午
  switchPeriod(e) {
    if (this.data.timeDisabled) return; // 未选日期不能切换
    this.setData({
      period: e.currentTarget.dataset.period,
      selectedTime: '',
      selectedDateTime: ''
    });
  },

  // 选择具体时间 + 拼接日期时间
  selectTime(e) {
    if (this.data.timeDisabled) return;

    const time = e.currentTarget.dataset.time;
    if (this.data.disabledTimeList.includes(time)) return;

    const {
      date
    } = this.data;
    // 👇 这里自动拼接 :00 秒
    const selectedDateTime = `${date} ${time}:00`;

    this.setData({
      selectedTime: time,
      selectedDateTime: selectedDateTime
    });
  },

  // 核心：根据当前时间，禁用今天已过的时间段
  checkTimeDisable(selectDate) {
    const now = new Date();
    const today = this.formatDate(now);
    const disabledList = [];

    // 如果选择的不是今天 → 不禁用任何时间
    if (selectDate !== today) {
      this.setData({
        disabledTimeList: []
      });
      return;
    }

    // 如果是今天 → 禁用已过的时间
    const nowHour = now.getHours();
    const nowMinute = now.getMinutes();
    const currentTimeMinutes = nowHour * 60 + nowMinute;

    // 上午时间判断
    const morning = this.data.morningTimes;
    morning.forEach(t => {
      const [h, m] = t.split(':').map(Number);
      const timeMinutes = h * 60 + m;
      if (timeMinutes <= currentTimeMinutes) {
        disabledList.push(t);
      }
    });

    // 下午时间判断
    const afternoon = this.data.afternoonTimes;
    afternoon.forEach(t => {
      const [h, m] = t.split(':').map(Number);
      const timeMinutes = h * 60 + m;
      if (timeMinutes <= currentTimeMinutes) {
        disabledList.push(t);
      }
    });

    console.log('禁用的时间列表:', disabledList);

    this.setData({
      disabledTimeList: disabledList
    });

    // 如果当前选中的时间被禁用，清空选中状态
    if (disabledList.includes(this.data.selectedTime)) {
      this.setData({
        selectedTime: '',
        selectedDateTime: ''
      });
      wx.showToast({
        title: '所选时间已过期，请重新选择',
        icon: 'none',
        duration: 2000
      });
    }
  },

  // 日期格式化：yyyy-MM-dd
  formatDate(date) {
    const y = date.getFullYear();
    const m = (date.getMonth() + 1).toString().padStart(2, '0');
    const d = date.getDate().toString().padStart(2, '0');
    return `${y}-${m}-${d}`;
  },

  // 提交预约
  submitBooking() {
    const {
      name,
      phone,
      selectedElderName,
      selectedDateTime
    } = this.data;


    // 手机号格式校验
    if (!/^1[3-9]\d{9}$/.test(phone)) {
      wx.showToast({
        title: '请输入正确的手机号',
        icon: 'none'
      });
      return;
    }

    // 提交给后端
    wx.request({
      url: 'http://localhost:8080/wxLogin/addvisitor',
      method: 'POST',
      header: {
        'content-type': 'application/json',
        'authorization': app.globalData.token || '' // 这里是后端要的 token 字段
      },
      data: {
        type: 1,
        name: name,
        phone: phone,
        olderName: selectedElderName,
        appointmentTime: selectedDateTime
      },
      success: (res) => {
        if (res.data.code === 200) {
          wx.showToast({
            title: '预约成功',
            icon: 'success',
            success: () => {
              setTimeout(() => {
                wx.navigateBack();
              }, 1500);
            }
          });
        } else {
          wx.showToast({
            title: res.data.message || '预约失败',
            icon: 'none'
          });
        }
      },
      fail: () => {
        wx.showToast({
          title: '网络请求失败',
          icon: 'none'
        });
      }
    });
  },


  getElder() {
    wx.request({
      url: "http://localhost:8080/wxLogin/getElder",
      method: 'GET',
      header: {
        'content-type': 'application/json',
        'authorization': app.globalData.token || '' // 这里是后端要的 token 字段
      },
      success: (res) => {
        if (res.data.code == 200) {
          if (res.data.data != null) {
            this.setData({
              elder: res.data.data
            })
          } else {
            wx.switchTab({
              url: '/pages/family/family',
            })
          }
        }
      }
    })

  },
  onLoad(options) {
    // 获取今天，设置picker最小日期为今天
    const today = this.formatDate(new Date());
    this.setData({
      today: today,
      date: today,
      timeDisabled: false
    });
    this.checkTimeDisable(today);
    this.getElder();
  },
  onReady() {},
  onShow() {},
  onHide() {},
  onUnload() {},
  onPullDownRefresh() {},
  onReachBottom() {},
  onShareAppMessage() {}
});