package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Description: No Description
 * User: Eric
 */
public interface CheckGroupDao {

    /**
     * 添加检查组
     *
     * @param checkGroup
     */
    void add(CheckGroup checkGroup);

    /**
     * 添加检查组与检查项的关系
     *
     * @param checkGroupId
     * @param checkitemId
     */
    void addCheckGroupCheckItem(@Param("checkGroupId") Integer checkGroupId, @Param("checkitemId") Integer checkitemId);

    /**
     * 条件查询
     *
     * @param queryString
     * @return
     */
    Page<CheckGroup> findByCondition(String queryString);

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    CheckGroup findById(int id);

    /**
     * 查询选中的检查项id
     *
     * @param id
     * @return
     */
    List<Integer> findCheckItemIdsByCheckGroupId(int id);

    /**
     * 更新检查组信息
     *
     * @param checkGroup
     */
    void update(CheckGroup checkGroup);

    /**
     * 删除旧关系
     *
     * @param id
     */
    void deleteCheckGroupCheckItem(Integer id);

    /**
     * 判断检查组是否被套餐使用了
     *
     * @param id
     * @return
     */
    int findSetmealCountByCheckGroupId(int id);

    /**
     * 删除检查组
     *
     * @param id
     */
    void deleteById(int id);

    /**
     * 查询所有的检查组
     *
     * @return
     */
    List<CheckGroup> findAll();
}
