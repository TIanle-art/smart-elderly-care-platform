const app = getApp();

Page({
  data: {
    id: "",
    loading: false,
    billInfo: {},
    billItemList: [],
    paymentRecord: null,
    billView: {}
  },

  onLoad(options) {
    this.setData({
      id: options.id || ""
    });
    this.getBillDetail();
  },

  onShow() {
    if (this.data.id) {
      this.getBillDetail();
    }
  },

  getBillDetail() {
    if (!this.data.id) return;
    this.setData({ loading: true });
    wx.request({
      url: `http://localhost:8080/wxLogin/myBillDetail/${this.data.id}`,
      method: "GET",
      header: {
        "content-type": "application/json",
        Authorization: app.globalData.token || ""
      },
      success: (res) => {
        if (res.data && res.data.code === 200 && res.data.data) {
          const detail = res.data.data;
          const billInfo = detail.billInfo || {};
          this.setData({
            billInfo,
            billItemList: detail.billItemList || [],
            paymentRecord: detail.paymentRecord || null,
            billView: this.buildBillView(billInfo)
          });
          return;
        }
        wx.showToast({ title: "获取账单详情失败", icon: "none" });
      },
      fail: () => {
        wx.showToast({ title: "网络异常，请稍后重试", icon: "none" });
      },
      complete: () => {
        this.setData({ loading: false });
      }
    });
  },

  buildBillView(billInfo) {
    const tradeStatus = billInfo.tradeStatus == null ? "" : String(billInfo.tradeStatus);
    let statusText = "已关闭";
    if (tradeStatus === "0") {
      statusText = "待支付";
    } else if (tradeStatus === "1") {
      statusText = "已支付";
    }

    return {
      title: billInfo.billType === "2" ? "费用账单" : "月度账单",
      statusText,
      billAmountText: this.formatMoney(billInfo.billAmount),
      payableAmountText: this.formatMoney(billInfo.payableAmount),
      createTimeText: this.formatDateTime(billInfo.createTime),
      payDeadlineText: this.formatDateTime(billInfo.payDeadline),
      billMonthText: billInfo.billMonth || "-",
      elderNameText: billInfo.elderName || "-",
      elderIdCardText: billInfo.elderIdCard || "-",
      bedNoText: billInfo.bedNo || "-"
    };
  },

  handlePay() {
    const { billInfo, billView } = this.data;
    if (!billInfo || !billInfo.id || String(billInfo.tradeStatus) !== "0") {
      wx.showToast({ title: "当前账单无需支付", icon: "none" });
      return;
    }

    wx.navigateTo({
      url:
        `/servicePage/pages/payPage/payPage?sourceType=bill` +
        `&billId=${billInfo.id}` +
        `&serviceName=${encodeURIComponent(billView.title || "账单支付")}` +
        `&price=${billInfo.payableAmount || 0}` +
        `&totalPrice=${billInfo.payableAmount || 0}` +
        `&familyName=${encodeURIComponent(billInfo.elderName || "")}` +
        `&serviceTime=${encodeURIComponent(billView.payDeadlineText || "")}`
    });
  },

  handleBack() {
    wx.navigateBack({ delta: 1 });
  },

  formatDateTime(value) {
    if (!value) return "-";
    const date = new Date(value);
    if (Number.isNaN(date.getTime())) {
      return String(value).replace("T", " ");
    }
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const day = String(date.getDate()).padStart(2, "0");
    const hour = String(date.getHours()).padStart(2, "0");
    const minute = String(date.getMinutes()).padStart(2, "0");
    return `${year}-${month}-${day} ${hour}:${minute}`;
  },

  formatMoney(value) {
    const num = Number(value || 0);
    return num.toFixed(2);
  }
});
