package com.itheima.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.dao.CommentMapper;
import com.itheima.dao.StatisticMapper;
import com.itheima.model.domain.Article;
import com.itheima.model.domain.Comment;
import com.itheima.model.domain.Statistic;
import com.itheima.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CommentServiceImpl implements ICommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private StatisticMapper statisticMapper;

    // 根据文章id分页查询评论
    @Override
    public PageInfo<Comment> getComments(Integer aid, int page, int count) {
        PageHelper.startPage(page, count);
        List<Comment> commentList = commentMapper.selectCommentWithPage(aid);
        PageInfo<Comment> commentInfo = new PageInfo<>(commentList);
        return commentInfo;
    }
    // 多表查询
    @Override
    public List<Comment> selectCommentWithArticle() {
        return commentMapper.selectCommentWithArticle();
    }

    // 评论分页
    @Override
    public PageInfo<Comment> selectComment(int page, int count) {
        PageHelper.startPage(page, count);
        List<Comment> commentList = commentMapper.selectCommentWithArticle();
        PageInfo<Comment> commentInfo = new PageInfo<>(commentList);
        return commentInfo;

    }

    // 用户发表评论
    @Override
    public void pushComment(Comment comment) {
        commentMapper.pushComment(comment);
        // 更新文章评论数据量
        Statistic statistic = statisticMapper.selectStatisticWithArticleId(comment.getArticleId());
        statistic.setCommentsNum(statistic.getCommentsNum() + 1);
        statisticMapper.updateArticleCommentsWithId(statistic);
    }

}

