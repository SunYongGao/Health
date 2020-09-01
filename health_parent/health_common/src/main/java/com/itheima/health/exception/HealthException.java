package com.itheima.health.exception;

/**
 * Description: 自定义异常使用
 * 1. 区分是系统的还是业务异常
 * 2. 给用户友好的提示
 * 3. 终止已知不符合业务逻辑的继续执行
 * User: Eric
 */
public class HealthException extends RuntimeException {
    public HealthException(String message){
        super(message);
    }
}
