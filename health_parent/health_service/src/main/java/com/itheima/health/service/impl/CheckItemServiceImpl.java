package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.dao.CheckItemDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Description: No Description
 * User: Eric  jdk proxy com.sum.proxy
 * interfaceClass发布到zookeeper上的接口
 */
@Service(interfaceClass = CheckItemService.class)
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemDao checkItemDao;

    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }

    /**
     * 新增
     * @param checkItem
     */
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    /**
     * 分页条件查询
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult<CheckItem> findPage(QueryPageBean queryPageBean) {
        // 使用静态方法，pageheleper
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        // 有条件 实现模糊，拼接%
        if(!StringUtils.isEmpty(queryPageBean.getQueryString())){
            // 不为空，有查询条件
            // 拼接%
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
        // 紧接着的查询语句会被分页
        // Page 是pageHelper对象, 分页信息
        Page<CheckItem> page = checkItemDao.findByCondition(queryPageBean.getQueryString());
        // 包装到PageResult 再返回
        // 1. 解耦
        // 2. total是基本类型，丢失。
        // 3. page对象内容过多 total,rows
        return new PageResult<CheckItem>(page.getTotal(), page.getResult());
    }

    /**
     * 通过id删除
     * @param id
     */
    @Override
    public void deleteById(int id) throws HealthException {
        // 判断检查项是否被检查组使用了
        int count = checkItemDao.findCountByCheckItemId(id);
        if(count > 0){
            // 报错
            throw new HealthException("该检查项已经被使用了，不能删除!");
        }
        // 删除
        checkItemDao.deleteById(id);
    }

    /**
     * 通过id查询
     * @param id
     * @return
     */
    @Override
    public CheckItem findById(int id) {
        return checkItemDao.findById(id);
    }

    /**
     * 更新
     * @param checkItem
     */
    @Override
    public void update(CheckItem checkItem) {
        checkItemDao.update(checkItem);
    }
}
