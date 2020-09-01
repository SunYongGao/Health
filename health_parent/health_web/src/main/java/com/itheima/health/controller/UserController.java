package com.itheima.health.controller;

import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;

/**
 * Description: No Description
 * User: Eric
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("getLoginUsername")
    public Result getLoginUsername(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,username);
    }

    public static void main(String[] args) {
        Calendar car = Calendar.getInstance();

        car.add(Calendar.YEAR,-1);
        for(int i=0; i < 12; i++){
            car.add(Calendar.MONTH,1);
            System.out.println(car.getTime());
        }
    }

    @RequestMapping("/loginSuccess")
    public Result loginSuccess(){
        return new Result(true, "登陆成功");
    }

    @RequestMapping("/loginFail")
    public Result loginFail(){
        return new Result(false, "登陆失败");
    }
}
