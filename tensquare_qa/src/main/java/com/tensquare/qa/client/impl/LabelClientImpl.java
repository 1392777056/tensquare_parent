package com.tensquare.qa.client.impl;

import com.tensquare.qa.client.LabelClient;
import entity.Result;
import entity.StatusCode;
import org.springframework.stereotype.Component;

/**
 * @auther 德哲
 * @date 2018/11/5 21:45.
 */
@Component
public class LabelClientImpl implements LabelClient {

    /**
     * 当无法从基础微服务获取数据时，执行方法
     * @param id
     * @return
     */
    @Override
    public Result findById(String id) {
        return new Result(false, StatusCode.ERROR,"熔断器执行了。。");
    }
}
