package com.tensquare.friend.dao;

import com.tensquare.friend.pojo.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 交友的持久层接口
 */
public interface FriendDao extends JpaRepository<Friend,String> {

    /**
     * 根据用户id 和朋友id 查询交友表的记录数
     * @param userId
     * @param friendId
     * @return
     */
    @Query("select count(f) from Friend f where f.userId = ?1 and f.friendId = ?2")
    int selectCount(String userId,String friendId);

    /**
     * 根据用户id和交友id更新喜欢的字段
     * @param userId
     * @param friendId
     * @param isLike
     * @return
     */
    @Query("update Friend f set f.isLike = ?3 where f.userId = ?1 and f.friendId = ?2")
    @Modifying
    void updateIsLike(String userId,String friendId,String isLike);

    /**
     * 删除关注
     * @param userId
     * @param friendId
     * @return
     */
    @Query("delete from Friend f where f.userId = ?1 and f.friendId = ?2")
    @Modifying
    void deleteFriend(String userId,String friendId);

}
