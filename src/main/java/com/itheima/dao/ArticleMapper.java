package com.itheima.dao;

import com.itheima.model.domain.Article;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface ArticleMapper {
    // 根据id查询文章信息
    @Select("SELECT * FROM t_article WHERE id=#{id}")
    Article selectArticleWithId(@Param("id") Integer id);

    // 查询所有文章
    List<Article> getAll();

    // 发表文章，同时使用@Options注解获取自动生成的主键id
    @Insert("INSERT INTO t_article (title,created,modified,tags,categories," +
            " allow_comment, thumbnail, content)" +
            " VALUES (#{title},#{created}, #{modified}, #{tags}, #{categories}," +
            " #{allowComment}, #{thumbnail}, #{content})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    Integer publishArticle(Article article);

    // 文章分页查询
    @Select("SELECT * FROM t_article ORDER BY id DESC")
    List<Article> selectArticleWithPage();

    // 通过id删除文章
    @Delete("DELETE FROM t_article WHERE id=#{id}")
    void deleteArticleWithId(int id);

    // 站点服务统计，统计文章数量
    @Select("SELECT COUNT(1) FROM t_article")
    Integer countArticle();

    // 通过id更新文章
    Integer updateArticleWithId(Article article);

    // 通过文章内容关键字模糊查询文章
    @Select("select * from t_article where title like CONCAT('%',#{title},'%')")
    List<Article> findByLike(String title);
}
