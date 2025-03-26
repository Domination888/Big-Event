package com.domination.service;

import com.domination.pojo.Article;
import com.domination.pojo.PageBean;

public interface ArticleService {
    //  新增文章
    void add(Article article);
    //  条件分页查询
    PageBean<Article> list(Integer pageNum, Integer pageSize, Integer categoryId, String state);

    void update(Article article);

    Article findById(Integer id);

    void delete(Integer id);
}
