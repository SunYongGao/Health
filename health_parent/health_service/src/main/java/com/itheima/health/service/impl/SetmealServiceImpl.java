package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.SetmealDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.QiNiuUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: No Description
 * User: Eric
 */
@Service(interfaceClass = SetmealService.class)
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;

    /**
     * 添加套餐
     * @param setmeal
     * @param checkgroupIds
     */
    @Override
    @Transactional
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        // 先添加套餐
        setmealDao.add(setmeal);
        // 套餐的id
        Integer setmealId = setmeal.getId();
        // 添加套餐与检查组的关系
        if(null != checkgroupIds){
            for (Integer checkgroupId : checkgroupIds) {
                setmealDao.addSetmealCheckGroup(setmealId, checkgroupId);
            }
        }
        // 生成页面
    }

    /**
     * 分页条件查询
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult<Setmeal> findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        if(!StringUtils.isEmpty(queryPageBean.getQueryString())){
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString()+ "%");
        }
        Page<Setmeal> page = setmealDao.findByCondition(queryPageBean.getQueryString());
        return new PageResult<>(page.getTotal(), page.getResult());
    }

    /**
     * 通过id查询
     * @param id
     * @return
     */
    @Override
    public Setmeal findById(int id) {
        return setmealDao.findById(id);
    }

    /**
     * 查询选中的检查组id集合
     * @param id
     * @return
     */
    @Override
    public List<Integer> findCheckgroupIdsBySetmealId(int id) {
        return setmealDao.findCheckgroupIdsBySetmealId(id);
    }

    /**
     * 修改套餐提交
     * @param setmeal
     * @param checkgroupIds
     */
    @Override
    @Transactional
    public void update(Setmeal setmeal, Integer[] checkgroupIds) {
        // 更新套餐
        setmealDao.update(setmeal);
        // 删除旧关系
        setmealDao.deleteSetmealCheckGroup(setmeal.getId());
        // 添加新关系
        if(null != checkgroupIds){
            for (Integer checkgroupId : checkgroupIds) {
                setmealDao.addSetmealCheckGroup(setmeal.getId(), checkgroupId);
            }
        }
    }

    /**
     * 通过id删除
     * @param id
     */
    @Override
    @Transactional
    public void deleteById(int id) throws HealthException {
        // 判断是否被订单使用
        int count = setmealDao.findOrderCountBySetmealId(id);
        // 使用了则报错
        if(count > 0){
            throw new HealthException("该套餐已经被使用了，不能删除");
        }
        // 未使用
        // 先删除套餐与检查组关系
        setmealDao.deleteSetmealCheckGroup(id);
        // 再删除套餐
        setmealDao.deleteById(id);
    }

    /**
     * 查出数据库中的所有图片
     * @return
     */
    @Override
    public List<String> findImgs() {
        return setmealDao.findImgs();
    }

    @Override
    public List<Setmeal> findAll() {
        List<Setmeal> setmealList = setmealDao.findAll();
        // 拼接图片的完整路径 $.each
        setmealList.forEach(setmeal -> {
            setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());
        });
        // 生成列表静态文件
        generateSetmealList(setmealList);
        // 套餐详情页面
        generateSetmealDetals(setmealList);
        return setmealList;
    }


    @Autowired
    private FreeMarkerConfigurer freemarkerConfig;

    @Value("${out_put_path}")
    private String out_put_path;

    /**
     * 生成 详情页面
     * @param setmealList
     */
    private void generateSetmealDetals(List<Setmeal> setmealList){
        for (Setmeal setmeal : setmealList) {
            // 实例检查组与检查项信息
            Setmeal setmealDetail = setmealDao.findDetailById(setmeal.getId());
            // 设置完整的图片
            setmealDetail.setImg(setmeal.getImg());
            // 数据模型
            Map<String,Object> dataMap = new HashMap<String,Object>();
            dataMap.put("setmeal", setmealDetail);
            String templateName = "mobile_setmeal_detail.ftl";
            String filename = String.format("%s/setmeal_%d.html",out_put_path,setmealDetail.getId());
            generateHtml(templateName,dataMap,filename);
        }
    }

    public static void main(String[] args) {
        System.out.println(String.format("%s/setmeal_%d.html","C:/sz98/health_parent/health_mobile/src/main/webapp/pages",12));
    }

    private void generateHtml(String templateName,Map<String,Object> dataMap,String filename){
        // 获取模板
        Configuration configuration = freemarkerConfig.getConfiguration();
        try {
            Template template = configuration.getTemplate(templateName);
            // utf-8 不能少了。少了就中文乱码
            BufferedWriter writer =  new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename),"utf-8"));
            // 填充数据到模板
            template.process(dataMap,writer);
            // 关闭流
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成套餐列表静态页面
     * @param setmealList
     */
    private void generateSetmealList(List<Setmeal> setmealList){
        Map<String,Object> dataMap = new HashMap<String,Object>();
        // key setmealList 与模板中的变量要一致
        dataMap.put("setmealList",setmealList);
        // 输出
        String setmealListFile = out_put_path + "/mobile_setmeal.html";

        generateHtml("mobile_setmeal.ftl", dataMap, setmealListFile);
    }

    /**
     * 查询套餐详情
     * @param id
     * @return
     */
    @Override
    public Setmeal findDetailById(int id) {
        return setmealDao.findDetailById(id);
    }

    /**
     * 查询套餐详情
     * @param id
     * @return
     */
    @Override
    public Setmeal findDetailById2(int id) {
        return setmealDao.findDetailById2(id);
    }

    /**
     * 查询套餐详情 方式三
     */
    @Override
    public Setmeal findDetailById3(int id) {
        // 查询套餐信息
        Setmeal setmeal = setmealDao.findById(id);
        // 查询套餐下的检查组
        List<CheckGroup> checkGroups = setmealDao.findCheckGroupListBySetmealId(id);
        if(null != checkGroups){
            for (CheckGroup checkGroup : checkGroups) {
                // 通过检查组id检查检查项列表
                List<CheckItem> checkItems = setmealDao.findCheckItemByCheckGroupId(checkGroup.getId());
                // 设置这个检查组下所拥有的检查项
                checkGroup.setCheckItems(checkItems);
            }
            //设置套餐下的所拥有的检查组
            setmeal.setCheckGroups(checkGroups);
        }
        return setmeal;
    }

    /**
     * 查询套餐预约数据
     * @return
     */
    @Override
    public List<Map<String, Object>> findSetmealCount() {
        return setmealDao.findSetmealCount();
    }
}
