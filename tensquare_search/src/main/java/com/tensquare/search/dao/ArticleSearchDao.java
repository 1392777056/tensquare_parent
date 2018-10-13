package com.tensquare.search.dao;

import com.tensquare.search.pojo.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Create with www.dezhe.com
 *
 * @Author 德哲
 * @Date 2018/10/13 13:31
 *
 * 文章搜索的持久层
 */
public interface ArticleSearchDao extends ElasticsearchRepository<Article,String>{

    /**
     * 使用关键字在文章的内容或者标题中模糊查询并分页
     * @param title
     * @param content
     * @param pageable
     * @return
     */
    Page<Article> findByTitleOrContentLike(String title, String content, Pageable pageable);

}
