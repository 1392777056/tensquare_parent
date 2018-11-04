package com.tensquare.friend.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 用户操作的调用接口
 */
@FeignClient("tensquare-user")
public interface UserClient {

    /**
     * 更新粉丝数
     * @param useId
     * @param fans
     */
    @RequestMapping(value = "/user/incfans/{userid}/{fans}",method = RequestMethod.POST)
    void incFanscount(@PathVariable("userid") String useId, @PathVariable("fans") int fans);

    /**
     * 更新关注数
     * @param useId
     * @param follow
     */
    @RequestMapping(value = "/user/incfollow/{userid}/{follow}",method = RequestMethod.POST)
    void incFollowconut(@PathVariable("userid") String useId,@PathVariable("follow") int follow);

}
