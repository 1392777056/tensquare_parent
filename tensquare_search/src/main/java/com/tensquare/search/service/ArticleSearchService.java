package com.tensquare.search.service;

import com.tensquare.search.dao.ArticleSearchDao;
import com.tensquare.search.pojo.Article;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import utils.IdWorker;

/**
 * Create with www.dezhe.com
 *
 * @Author 德哲
 * @Date 2018/10/13 13:35
 */
@Service
public class ArticleSearchService {

    @Autowired
    private ArticleSearchDao articleSearchDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 保存方法
     * @param article
     */
    public void save(Article article) {
        article.setId(String.valueOf(idWorker.nextId()));
        articleSearchDao.save(article);
    }

    public Page<Article> findByKeywords(String keywords,int page,int size) {
        // 创建分页对象
        PageRequest pageRequest = PageRequest.of(page-1, size);
        // 查询并返回
        return articleSearchDao.findByTitleOrContentLike(keywords, keywords, pageRequest);
    }

}
