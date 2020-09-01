package com.itheima.health.service;

import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

/**
 * Description: No Description
 * User: Eric
 */
public interface OrderSettingService {

    /**
     * 批量导入预约设置
     * @param orderSettings
     */
    void add(List<OrderSetting> orderSettings) throws HealthException;

    /**
     * 通过月份查询预约设置信息
     * @param month
     * @return
     */
    List<Map<String, Integer>> getOrderSettingByMonth(String month);

    /**
     * 基于日历的预约设置
     * @param orderSetting
     */
    void editNumberByDate(OrderSetting orderSetting) throws HealthException;
}
