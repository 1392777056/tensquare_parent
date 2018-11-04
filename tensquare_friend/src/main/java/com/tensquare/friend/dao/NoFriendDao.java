package com.tensquare.friend.dao;

import com.tensquare.friend.pojo.NoFriend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NoFriendDao extends JpaRepository<NoFriend,String> {

    /**
     * 查询拉黑列表
     * @param userId
     * @param friendId
     * @return
     */
    @Query("select count(nf) from NoFriend nf where nf.userId = ?1 and nf.friendId = ?2")
    int selectNoFriendCount(String userId,String friendId);

}
