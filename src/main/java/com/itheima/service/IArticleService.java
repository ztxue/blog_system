package com.itheima.service;

import com.github.pagehelper.PageInfo;
import com.itheima.model.domain.Article;

import java.util.List;


public interface IArticleService {
    // 分页查询文章列表
    PageInfo<Article> selectArticleWithPage(Integer page, Integer count);

    // 统计前10的热度文章信息
    List<Article> getHeatArticles();

    // 根据文章id查询单个文章详情
    Article selectArticleWithId(Integer id);

    // 发布文章
    void publish(Article article);

    // 根据主键更新文章
    void updateArticleWithId(Article article);

    // 根据主键删除文章
    void deleteArticleWithId(int id);

    // 模糊查询
    List<Article> findByLike(String title);
}

