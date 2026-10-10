package com.lcyl.code.service.impl;

import com.lcyl.code.mapper.IndexMapper;
import com.lcyl.code.service.IndexService;
import com.lcyl.code.utils.DateRangeUtil;
import com.lcyl.common.core.domain.AjaxResult;
import com.lcyl.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.lcyl.code.utils.DateRangeUtil.getDateRange;

/**
 * @ClassName IndexServiceImpl
 * @Description TODO
 * @Author GuiGui
 * @Date 2026-03-31 15:04
 * @Version 1.0
 */
@Service
public class IndexServiceImpl implements IndexService {

    @Autowired
    private IndexMapper indexMapper;

//数据概览
    @Override
    public Map<String, Object> count() {
        Map<String, Object> map = new HashMap<>();
        //请假中
        Integer leaveCount = indexMapper.countLeave();
        //总人数
        Integer elderCount = indexMapper.countElder();
        //在住
        Integer liveCount = elderCount - leaveCount;
        //床位总数
        Integer bedCount = indexMapper.countBed();
        //床位在使用
        Integer bedUseCount = indexMapper.countBedUse();
        //床位剩余
        Integer bedSurplusCount = indexMapper.countNoLive();
        //员工数量
        Integer personCount = indexMapper.countPerson();
        //男数量
        Integer manCount = indexMapper.countMan();
        //女数量
        Integer womanCount = personCount - manCount;
        // 服务费
        Integer serviceFee = indexMapper.countServiceFee();
        //月度费
        Integer monthFee = indexMapper.countMonthFee();


        // 封装返回
        map.put("elderTotal", elderCount);
        map.put("elderLive", liveCount);
        map.put("elderLeave", leaveCount);

        map.put("bedTotal", bedCount);
        map.put("bedUse", bedUseCount);
        map.put("bedSurplus", bedSurplusCount);

        map.put("staffTotal", personCount);
        map.put("staffMan", manCount);
        map.put("staffWoman", womanCount);

        map.put("serviceFee", serviceFee);
        map.put("monthFee", monthFee);
        map.put("incomeTotal", serviceFee + monthFee);

        return map;
    }



//数据统计：入住+退住图表
    @Override
    public AjaxResult getCheckInOutStat(Map<String, String> params) {

        String timeType = params.get("timeType");
        String startDate = params.get("startDate");
        String endDate = params.get("endDate");

        Date[] range;

        try {
            if (StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {
                range = DateRangeUtil.parseDateRange(startDate, endDate);
            } else {
                range = getDateRange(timeType);
            }
        } catch (Exception e) {
            range = getDateRange("week");
        }

        Date start = range[0];
        Date end = range[1];

        // 查询数据
        List<Map<String, Object>> inList = indexMapper.selectCheckInGroupByDay(start, end);
        List<Map<String, Object>> outList = indexMapper.selectRetreatGroupByDay(start, end);

        Map<String, Integer> inMap = inList.stream()
                .collect(Collectors.toMap(
                        m -> m.get("date").toString(),
                        m -> Integer.parseInt(m.get("cnt").toString())
                ));

        Map<String, Integer> outMap = outList.stream()
                .collect(Collectors.toMap(
                        m -> m.get("date").toString(),
                        m -> Integer.parseInt(m.get("cnt").toString())
                ));

        // 生成 X 轴日期
        List<String> xData = getDayList(start, end);
        List<Integer> checkInData = new ArrayList<>();
        List<Integer> retreatData = new ArrayList<>();

        for (String day : xData) {
            checkInData.add(inMap.getOrDefault(day, 0));
            retreatData.add(outMap.getOrDefault(day, 0));
        }

        // 封装返回
        Map<String, Object> result = new HashMap<>();
        result.put("xData", xData);//x洲日期
        result.put("checkInData", checkInData);//入住人数
        result.put("retreatData", retreatData);//出院人数
        return AjaxResult.success(result);
    }

    // 生成日期：生成开始~结束之间的所有连续日期（MM-dd格式）
    private List<String> getDayList(Date start, Date end) {
        List<String> list = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(start);

        while (!cal.getTime().after(end)) {
            list.add(sdf.format(cal.getTime()));
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        return list;
    }



//收益统计图表：收入+退款
    @Override
    public AjaxResult getIncomeStat(Map<String, String> params) {
        String timeType = params.get("timeType");
        String startDate = params.get("startDate");
        String endDate = params.get("endDate");

        Date[] range;

        try {
            if (StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {
                range = DateRangeUtil.parseDateRange(startDate, endDate);
            } else {
                range = getDateRange(timeType);
            }
        } catch (Exception e) {
            range = getDateRange("week");
        }

        Date start = range[0];
        Date end = range[1];

        // 查询数据
        List<Map<String, Object>> incomeList = indexMapper.selectIncomeByDateRange(start, end);
        List<Map<String, Object>> refundList = indexMapper.selectRefundByDateRange(start, end);

        // 构造日期列表
        List<String> xData = getDayList(start, end);

        Map<String, Double> incomeMap = incomeList.stream()
                .collect(Collectors.toMap(
                        m -> m.get("day").toString(),
                        m -> Double.parseDouble(m.get("amount").toString())
                ));

        Map<String, Double> refundMap = refundList.stream()
                .collect(Collectors.toMap(
                        m -> m.get("day").toString(),
                        m -> Double.parseDouble(m.get("amount").toString())
                ));

        List<Double> incomeData = new ArrayList<>();
        List<Double> refundData = new ArrayList<>();

        for (String day : xData) {
            incomeData.add(incomeMap.getOrDefault(day, 0D));
            refundData.add(refundMap.getOrDefault(day, 0D));
        }

        Map<String, Object> result = new HashMap<>();
        result.put("xData", xData);
        result.put("incomeData", incomeData);
        result.put("refundData", refundData);

        return AjaxResult.success(result);
    }


//服务单统计
    @Override
    public AjaxResult getServiceStat(Map<String, String> params) {
        String timeType = params.get("timeType");
        String startDate = params.get("startDate");
        String endDate = params.get("endDate");

        Date[] range;

        if (StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date start = sdf.parse(startDate);
                Date end = sdf.parse(endDate);

                Calendar cal = Calendar.getInstance();
                cal.setTime(end);
                cal.set(Calendar.HOUR_OF_DAY, 23);
                cal.set(Calendar.MINUTE, 59);
                cal.set(Calendar.SECOND, 59);
                end = cal.getTime();

                range = new Date[]{start, end};
            } catch (Exception e) {
                range = getDateRange("week");
            }
        } else {
            range = getDateRange(timeType);
        }

        Date start = range[0];
        Date end = range[1];

        // 查询服务单数据
        List<Map<String, Object>> serviceList = indexMapper.countServiceOrderByExecuteTime(
                new SimpleDateFormat("yyyy-MM-dd").format(start),
                new SimpleDateFormat("yyyy-MM-dd").format(end)
        );

        List<String> xData = getDayList(start, end);

        Map<String, Integer> serviceMap = serviceList.stream()
                .collect(Collectors.toMap(
                        m -> m.get("day").toString(),
                        m -> Integer.parseInt(m.get("cnt").toString())
                ));

        List<Integer> serviceData = new ArrayList<>();
        for (String day : xData) {
            serviceData.add(serviceMap.getOrDefault(day, 0));
        }

        Map<String, Object> result = new HashMap<>();
        result.put("xData", xData);
        result.put("serviceData", serviceData);
        return AjaxResult.success(result);
    }


    // 老人等级分布
    @Override
    public List<Map<String, Object>> getNursingLevelStat() {
        return indexMapper.selectNursingLevelStat();
    }

    // 年龄性别分布
    @Override
    public Map<String, Object> getAgeGenderStat() {
        List<Map<String, Object>> list = indexMapper.selectAgeGenderStat();
        Map<String, Map<String, Integer>> rangeMap = new LinkedHashMap<>();

        String[] ranges = {"60岁以下", "60-70岁", "71-80岁", "81-90岁", "90岁以上"};

        for (String r : ranges) {
            rangeMap.put(r, new HashMap() {{
                //0男1女
                put("0", 0);
                put("1", 0);
            }});
        }

        for (Map<String, Object> m : list) {
            String range = (String) m.get("ageRange");
            String sex = (String) m.get("sex");
            Integer cnt = Integer.valueOf(m.get("cnt").toString());
            rangeMap.get(range).put(sex, cnt);
        }

        List<String> xData = new ArrayList<>(Arrays.asList(ranges));
        List<Integer> male = new ArrayList<>();
        List<Integer> female = new ArrayList<>();
        //按年龄顺序取出男、女人数
        for (String r : ranges) {
            male.add(rangeMap.get(r).get("0"));
            female.add(rangeMap.get(r).get("1"));
        }

        Map<String, Object> res = new HashMap<>();
        res.put("xData", xData);
        res.put("maleData", male);
        res.put("femaleData", female);
        return res;
    }

    // 服务能力统计
    @Override
    public List<Map<String, Object>> getServiceAbilityStat() {
        return indexMapper.selectServiceAbilityStat();
    }
}
