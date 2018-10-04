package com.tensquare.article.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.article.pojo.Article;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ArticleDao extends JpaRepository<Article,String>,JpaSpecificationExecutor<Article>{

    /**
     * 文章点赞
     * @param articleId
     *
     * modifying 操作更新
     */
    @Modifying
    @Query(value = "UPDATE tb_article SET thumbup = CASE WHEN thumbup IS NULL THEN 1 ELSE thumbup + 1 END WHERE id = ?1",nativeQuery = true)
    void thumbup(String articleId);

    /**
     * 文章审核
     * @param articleId
     */
    @Modifying
    @Query(value = "UPDATE Article SET state = 1 WHERE id = ?1")
    void examine(String articleId);

}
