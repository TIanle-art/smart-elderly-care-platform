const app = getApp();

Page({
  data: {
    statusTabs: [
      { key: "all", label: "全部" },
      { key: "pending", label: "待支付" },
      { key: "paid", label: "已支付" }
    ],
    elderTabs: [{ key: "all", label: "全部" }],
    activeStatus: "all",
    activeElder: "all",
    billList: [],
    filteredBills: [],
    loading: false
  },

  onShow() {
    this.loadElderTabs();
    this.getMyBills();
  },

  loadElderTabs() {
    wx.request({
      url: "http://localhost.15:8080/wxLogin/getElderBedList",
      method: "POST",
      header: {
        "content-type": "application/json",
        Authorization: app.globalData.token || ""
      },
      success: (res) => {
        const elderList =
          (res.data && Array.isArray(res.data.rows) && res.data.rows) ||
          (res.data && Array.isArray(res.data.data) && res.data.data) ||
          [];

        const elderTabs = [{ key: "all", label: "全部" }].concat(
          elderList
            .filter((item) => item && item.id)
            .map((item) => ({
              key: String(item.id),
              label: item.name || "未知老人"
            }))
        );

        this.setData({ elderTabs }, () => {
          this.filterBills();
        });
      },
      fail: () => {
        this.setData({
          elderTabs: [{ key: "all", label: "全部" }]
        });
      }
    });
  },

  getMyBills() {
    this.setData({ loading: true });
    wx.request({
      url: "http://localhost:8080/wxLogin/myBills",
      method: "GET",
      header: {
        "content-type": "application/json",
        Authorization: app.globalData.token || ""
      },
      success: (res) => {
        if (res.data && res.data.code === 200 && Array.isArray(res.data.data)) {
          const billList = res.data.data.map((item) => this.mapBillItem(item));
          this.setData({ billList }, () => {
            this.filterBills();
          });
          return;
        }
        wx.showToast({ title: "获取账单失败", icon: "none" });
      },
      fail: () => {
        wx.showToast({ title: "网络异常，请稍后重试", icon: "none" });
      },
      complete: () => {
        this.setData({ loading: false });
      }
    });
  },

  mapBillItem(item) {
    const tradeStatus = item.tradeStatus == null ? "" : String(item.tradeStatus);
    let status = "closed";
    let statusText = "已关闭";
    if (tradeStatus === "0") {
      status = "pending";
      statusText = "待支付";
    } else if (tradeStatus === "1") {
      status = "paid";
      statusText = "已支付";
    }

    return {
      id: item.id,
      elderId: item.elderId != null ? String(item.elderId) : "",
      elderName: item.elderName || "-",
      orderNo: item.billNo || "-",
      title: item.billType === "2" ? "费用账单" : "月度账单",
      billMonth: item.billMonth || "-",
      createTime: this.formatDateTime(item.createTime),
      deadlineTime: this.formatDateTime(item.payDeadline),
      amount: this.formatMoney(item.payableAmount),
      amountValue: Number(item.payableAmount || 0),
      status,
      statusText
    };
  },

  filterBills() {
    const { billList, activeStatus, activeElder } = this.data;
    const filteredBills = billList.filter((item) => {
      const matchStatus = activeStatus === "all" || item.status === activeStatus;
      const matchElder = activeElder === "all" || item.elderId === activeElder;
      return matchStatus && matchElder;
    });
    this.setData({ filteredBills });
  },

  switchStatusTab(e) {
    const key = e.currentTarget.dataset.key;
    this.setData({ activeStatus: key }, () => {
      this.filterBills();
    });
  },

  switchElderTab(e) {
    const key = e.currentTarget.dataset.key;
    this.setData({ activeElder: key }, () => {
      this.filterBills();
    });
  },

  handleViewDetail(e) {
    const { id } = e.currentTarget.dataset;
    wx.navigateTo({
      url: `/my/pages/myBillDetail/myBillDetail?id=${id}`
    });
  },

  handlePay(e) {
    const { id } = e.currentTarget.dataset;
    const currentBill = this.data.billList.find((item) => item.id === id);
    if (!currentBill) {
      wx.showToast({ title: "账单信息不存在", icon: "none" });
      return;
    }

    wx.navigateTo({
      url:
        `/servicePage/pages/payPage/payPage?sourceType=bill` +
        `&billId=${currentBill.id}` +
        `&serviceName=${encodeURIComponent(currentBill.title)}` +
        `&price=${currentBill.amountValue}` +
        `&totalPrice=${currentBill.amountValue}` +
        `&familyName=${encodeURIComponent(currentBill.elderName)}` +
        `&serviceTime=${encodeURIComponent(currentBill.deadlineTime)}`
    });
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
    return Number(value || 0).toFixed(2);
  }
});
