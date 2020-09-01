package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: No Description
 * User: Eric
 */
@Service(interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Override
    @Transactional
    public void add(List<OrderSetting> orderSettings) throws HealthException {
        // 遍历
        for (OrderSetting os : orderSettings) {
            // 通过日期查询预约设置信息
            OrderSetting osInDB = orderSettingDao.findByOrderDate(os.getOrderDate());
            // 存在
            if(null != osInDB) {
                //     判断已预约数量是否大于 将要更新的 可预约数量
                if(osInDB.getReservations() > os.getNumber()) {
                    //         大于 报错
                    throw new HealthException(os.getOrderDate() + "中：可预约数不能小于已预约数");
                }else {
                    //         小于要更新可预约数
                    orderSettingDao.updateNumber(os);
                }
            }else {
                // 不存在 添加预约设置
                orderSettingDao.add(os);
            }
        }

    }

    /**
     * 通过月份查询预约设置信息
     * @param month
     * @return
     */
    @Override
    public List<Map<String, Integer>> getOrderSettingByMonth(String month) {
        // 拼接开始日期
        String startDate = month + "-01";
        // 结束日期
        String endDate = month + "-31";
        // 调用dao查询
        Map<String,String> map = new HashMap<String,String>();
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        return orderSettingDao.getOrderSettingByMonth(map);
    }

    /**
     * 基于日历的预约设置
     * @param os
     */
    @Override
    public void editNumberByDate(OrderSetting os) throws HealthException {
        // 通过日期查询预约设置信息
        OrderSetting osInDB = orderSettingDao.findByOrderDate(os.getOrderDate());
        // 存在
        if(null != osInDB) {
            //     判断已预约数量是否大于 将要更新的 可预约数量
            if(osInDB.getReservations() > os.getNumber()) {
                //         大于 报错
                throw new HealthException(os.getOrderDate() + "中：可预约数不能小于已预约数");
            }else {
                //         小于要更新可预约数
                orderSettingDao.updateNumber(os);
            }
        }else {
            // 不存在 添加预约设置
            orderSettingDao.add(os);
        }
    }
}
