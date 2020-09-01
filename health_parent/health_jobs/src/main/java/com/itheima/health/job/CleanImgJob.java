package com.itheima.health.job;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.QiNiuUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Description: No Description
 * User: Eric
 */
@Component("cleanImgJob")
public class CleanImgJob {

    /**
     * 订阅服务
     */
    @Reference
    private SetmealService setmealService;

    public void cleanImg(){
        // 查出7牛上的s所有图片
        List<String> imgIn7Niu = QiNiuUtils.listFile();
        // 查出数据库中的所有图片
        List<String> imgInDb = setmealService.findImgs();
        // 7牛的-数据库的 imgIn7Niu剩下的就是要删除的
        // imgIn7Niu.size(7) - 3 = imgIn7Niu.size(4)
        // imgInDb里的对象在 imgIn7Niu 能找到就把这删除
        imgIn7Niu.removeAll(imgInDb);
        // 删除7牛上的垃圾图片
        String[] strings = imgIn7Niu.toArray(new String[]{});
        QiNiuUtils.removeFiles(strings);
    }
}
