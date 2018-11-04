package com.tensquare.friend.controller;

import com.tensquare.friend.service.FriendService;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auther 德哲
 * @date 2018/10/31 20:53.
 */
@RestController
@RequestMapping("/friend")
public class FriendController {

    @Autowired
    private FriendService friendService;

    /**
     * 添加好友
     * @param friendId
     * @return
     *
     * 1是粉   2是黑
     */
    @RequestMapping(value = "/like/{friendid}/{type}",method = RequestMethod.PUT)
    public Result addFriend(@PathVariable("friendid") String friendId, @PathVariable("type") String type, HttpServletRequest request) {
        // 1.获取请求域中的登录信息
        Claims clamis = (Claims) request.getAttribute("user_claims");
        if (clamis == null) {
            return new Result(false, StatusCode.ACCESSERROR,"没有权限");
        }
        if ("1".equals(type)) {
            // 加粉
            int res = friendService.addFriend(clamis.getId(), friendId);
            if (res == 0) {
                return new Result(false,StatusCode.REPERROR,"请不要重复操作");
            }
        } else {
            // 拉黑
            int res = friendService.addNoFriend(clamis.getId(),friendId);
            if (res == 0){
                return new Result(false,StatusCode.REPERROR,"请不要重复操作");
            }
        }
        return new Result(true,StatusCode.OK,"操作成功");
    }

    /**
     * 删除好友
     * @param friendId
     * @return
     *
     */
    @RequestMapping(value = "/{friendid}",method = RequestMethod.DELETE)
    public Result deleteFriend(@PathVariable("friendid") String friendId, HttpServletRequest request) {
        // 1.获取请求域中的登录信息
        Claims clamis = (Claims) request.getAttribute("user_claims");
        if (clamis == null) {
            return new Result(false, StatusCode.ACCESSERROR, "没有权限");
        }
        friendService.deleteFriend(clamis.getId(), friendId);
        return new Result(true,StatusCode.OK,"操作成功");
    }

}
