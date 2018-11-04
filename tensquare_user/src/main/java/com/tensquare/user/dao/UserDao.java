package com.tensquare.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.user.pojo.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface UserDao extends JpaRepository<User,String>,JpaSpecificationExecutor<User>{

    /**
     * 根据手机号去查询用户
     * @param Mobile
     * @return
     */
    User findByMobile(String Mobile);

    /**
     * 更新用户粉丝数
     * @param userId  id
     * @param fans   粉丝数量
     */
    @Query("update User u set u.fanscount = u.fanscount+?2 where id = ?1")
    @Modifying
    void incFanscount(String userId,int fans);

    /**
     * 更新用户关注数
     * @param userId
     * @param follow   关注的数量
     */
    @Query("update User u set u.followcount = u.followcount+?2 where id = ?1")
    @Modifying
    void incFollowconut(String userId,int follow);
}
