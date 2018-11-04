package com.tensquare.friend.service;

import com.tensquare.friend.dao.FriendDao;
import com.tensquare.friend.dao.NoFriendDao;
import com.tensquare.friend.pojo.Friend;
import com.tensquare.friend.pojo.NoFriend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @auther 德哲
 * @date 2018/10/31 20:36.
 */
@Service
public class FriendService {

    @Autowired
    private FriendDao friendDao;

    @Autowired
    private NoFriendDao noFriendDao;

    /**
     * 删除好友
     * @param userid
     * @param friendid
     */
    @Transactional
    public void deleteFriend(String userid,String friendid) {
        friendDao.deleteFriend(userid,friendid);
        friendDao.updateIsLike(friendid,userid,"0");
    }

    /**
     * 添加好友
     * @param userId
     * @param friendId
     * @return
     */
    @Transactional
    public int addFriend(String userId,String friendId) {
        // 1.判断当前用户是否已经关注过friendId
        int isFriend = friendDao.selectCount(userId,friendId);
        if (isFriend>0) {
            return 0;
        }
        // 2.没关注过就要创建Friend对象
        Friend friend = new Friend();
        friend.setUserId(userId);
        friend.setFriendId(friendId);
        friend.setIsLike("0");
        // 3. 保存
        friendDao.save(friend);
        // 4.把friendId看成userId，反之再把userId看成friendId
        int inverseFriend = friendDao.selectCount(friendId,userId);
        if (inverseFriend > 0) {
             // 已经互相关注了 就更新两个数据
            friendDao.updateIsLike(userId,friendId,"1");
            friendDao.updateIsLike(friendId,userId,"1");
        }
        return 1;
    }

    /*public void addFriend(String userId,String friendId) {
        // 1.判断当前用户是否已经关注过friendId
        int isFriend = friendDao.selectCount(userId,friendId);
        if (isFriend>0) {
            throw new RuntimeException("请不要重复操作");
        }
        // 2.没关注过就要创建Friend对象
        Friend friend = new Friend();
        friend.setUserId(userId);
        friend.setFriendId(friendId);
        friend.setIsLike("0");
        // 3. 保存
        friendDao.save(friend);
        // 4.把friendId看成userId，反之再把userId看成friendId
        int inverseFriend = friendDao.selectCount(friendId,userId);
        if (inverseFriend > 0) {
            // 已经互相关注了 就更新两个数据
            friendDao.updateIsLike(userId,friendId,"1");
            friendDao.updateIsLike(friendId,userId,"1");
        }

    }*/

    /**
     * 拉黑
     * @param userId
     * @param friendId
     * @return
     */
    @Transactional
    public int addNoFriend(String userId,String friendId) {
        int noFriendCount = noFriendDao.selectNoFriendCount(userId,friendId);
        if (noFriendCount > 0) {
            return 0;
        }
        NoFriend noFriend = new NoFriend();
        noFriend.setUserId(userId);
        noFriend.setFriendId(friendId);
        noFriendDao.save(noFriend);
        return 1;
    }
}
