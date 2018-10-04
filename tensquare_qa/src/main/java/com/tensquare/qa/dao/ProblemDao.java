package com.tensquare.qa.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.qa.pojo.Problem;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ProblemDao extends JpaRepository<Problem,String>,JpaSpecificationExecutor<Problem>{

    /**
     * 根据labelid 查询出 最新回答的问题列表
     * @param labelid
     * @return
     */
    @Query(value = "SELECT * FROM tb_problem WHERE id IN (SELECT problemid FROM  tb_pl WHERE labelid = 1) AND reply > 0 ORDER BY replytime DESC",nativeQuery = true)
    Page<Problem> newList(String labelid, Pageable pageable);

    /**
     * 根据标签id ，查询出 热门回答的问题列表
     * @param labelid
     * @param pageable
     * @return
     */
    @Query(value = "select p from Problem p where p.id in (select problemid from Pl where labelid = ?1) order by p.reply desc")
    Page<Problem> hotList(String labelid, Pageable pageable);

    /**
     * 根据标签id，查询出 等待回答的问题列表
     * @param labelid
     * @param pageable
     * @return
     */
    @Query(value = "select p from Problem p where p.id in (select problemid from Pl where labelid = ?1) and p.reply = 0 order by p.createtime desc")
    Page<Problem> waitList(String labelid,Pageable pageable);
}
