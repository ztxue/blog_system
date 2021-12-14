package com.itheima.dao;

import com.github.pagehelper.PageInfo;
import com.itheima.model.domain.Article;
import com.itheima.model.domain.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {
    // 分页展示某个文章的评论
    @Select("SELECT * FROM t_comment WHERE article_id=#{aid} ORDER BY id DESC")
    List<Comment> selectCommentWithPage(Integer aid);

    // 评论和文章关联
    @Results({
            @Result(property = "author", column = "author"),
            @Result(property = "created", column = "created"),
            @Result(property = "content", column = "content"),
            @Result(property = "articleId",column = "article_Id"),
            @Result(property = "article", column = "article_Id", javaType = Article.class,
                    one = @One(select = "com.itheima.dao.ArticleMapper.selectArticleWithId")),

    })
    @Select("SELECT *  FROM t_comment")
    List<Comment> selectCommentWithArticle();

    // 后台查询最新几条评论
    @Select("SELECT * FROM t_comment ORDER BY id DESC")
    List<Comment> selectNewComment();

    // 发表评论
    @Insert("INSERT INTO t_comment (article_id,created,author,ip,content)" +
            " VALUES (#{articleId}, #{created},#{author},#{ip},#{content})")
    void pushComment(Comment comment);

    // 站点服务统计，统计评论数量
    @Select("SELECT COUNT(1) FROM t_comment")
    Integer countComment();

    // 通过文章id删除评论信息
    @Delete("DELETE FROM t_comment WHERE article_id=#{aid}")
    void deleteCommentWithId(Integer aid);
}

