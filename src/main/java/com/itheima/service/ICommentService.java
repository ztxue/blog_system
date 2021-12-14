package com.itheima.service;

import com.github.pagehelper.PageInfo;
import com.itheima.model.domain.Comment;

import java.util.List;

public interface ICommentService {
    // 获取文章下的评论
    PageInfo<Comment> getComments(Integer aid, int page, int count);

    // 多表查询
    List<Comment> selectCommentWithArticle();

    // 用户发表评论
    void pushComment(Comment comment);

    // 分页
    PageInfo<Comment> selectComment(int page, int count);
}

