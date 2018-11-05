package com.tensquare.qa.client;

import com.tensquare.qa.client.impl.LabelClientImpl;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 用户指向基础微服务的方法
 */
@FeignClient(value = "tensquare-base",fallback = LabelClientImpl.class)  // 此处不支持下划线
public interface LabelClient {

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @RequestMapping(value = "/label/{id}",method = RequestMethod.GET)
    public Result findById(@PathVariable("id") String id);
}
