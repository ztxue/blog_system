package com.itheima;


import com.itheima.model.domain.Article;
import com.itheima.model.domain.Comment;
import com.itheima.model.domain.User;
import com.itheima.service.IArticleService;
import com.itheima.service.ICommentService;
import com.itheima.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogSystemApplicationTests {

    @Resource
    UserServiceImpl userServiceimpl;
    @Resource
    ICommentService commentService;
    @Resource
    IArticleService articleService;

    @Test
    public void contextLoads() {
    }

    @Test
    public void getComment() {
        List<Comment> comments = commentService.selectCommentWithArticle();
        for (Comment commentlist : comments) {
            System.out.println(commentlist);
        }
    }

    @Test
    public void getCA() {
        List<Comment> selectCommentWitharticleList = commentService.selectCommentWithArticle();
        for (Comment comment : selectCommentWitharticleList) {
            System.out.println(comment);
        }
    }

    @Test
    public void getUser() {
        List<User> userList = userServiceimpl.queryAll();
        for (User user : userList) {
            System.out.println(user);
        }
    }

    @Test
    public void article() {
        System.out.println(articleService.selectArticleWithId(15));;

    }
}
