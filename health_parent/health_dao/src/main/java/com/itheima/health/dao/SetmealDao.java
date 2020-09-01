package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Description: No Description
 * User: Eric
 */
public interface SetmealDao {
    /**
     * 先添加套餐
     * @param setmeal
     */
    void add(Setmeal setmeal);

    /**
     * 添加套餐与检查组的关系
     * @param setmealId
     * @param checkgroupId
     */
    void addSetmealCheckGroup(@Param("setmealId") Integer setmealId, @Param("checkgroupId") Integer checkgroupId);

    /**
     * 条件 查询
     * @param queryString
     * @return
     */
    Page<Setmeal> findByCondition(String queryString);

    /**
     * 通过id查询
     * @param id
     * @return
     */
    Setmeal findById(int id);

    /**
     * 查询选中的检查组id集合
     * @param id
     * @return
     */
    List<Integer> findCheckgroupIdsBySetmealId(int id);

    /**
     * 更新套餐
     * @param setmeal
     */
    void update(Setmeal setmeal);

    /**
     * 删除套餐与检查组的关系
     * @param id
     */
    void deleteSetmealCheckGroup(Integer id);

    /**
     * 查询某个套餐的订单个数
     * @param id
     * @return
     */
    int findOrderCountBySetmealId(int id);

    /**
     * 通过id删除套餐
     * @param id
     */
    void deleteById(int id);

    /**
     * 查出数据库中的所有图片
     * @return
     */
    List<String> findImgs();

    /**
     * 查询所有
     * @return
     */
    List<Setmeal> findAll();

    /**
     * 查询套餐详情
     * @param id
     * @return
     */
    Setmeal findDetailById(int id);

    Setmeal findDetailById2(int id);

    List<CheckGroup> findCheckGroupListBySetmealId(int id);

    List<CheckItem> findCheckItemByCheckGroupId(Integer id);

    /**
     * 查询套餐预约数据
     * @return
     */
    List<Map<String, Object>> findSetmealCount();
}
