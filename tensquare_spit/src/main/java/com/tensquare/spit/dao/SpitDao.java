package com.tensquare.spit.dao;

import com.tensquare.spit.pojo.Spit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Create with www.dezhe.com
 *
 * @Author 德哲
 * @Date 2018/10/5 19:08
 *
 * 吐槽的持久层接口
 */
public interface SpitDao extends MongoRepository<Spit,String> {

    /**
     * 根据吐槽的父ID去查询吐槽列表
     * @param parentid
     * @param pageable
     * @return
     */
    Page<Spit> findByParentid(String parentid, Pageable pageable);

}
