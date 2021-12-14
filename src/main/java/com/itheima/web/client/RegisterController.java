package com.itheima.web.client;

import com.itheima.model.domain.User;
import com.itheima.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/User")
public class RegisterController {

    @Resource
    private UserService userService;

    //注册：
    @GetMapping("/register")
    public String register() {
        return "comm/register";
    }

    @GetMapping("/toRegister")
    public String toRegister(HttpServletRequest req) {

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String email = req.getParameter("email");

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setValid(1);

        userService.insert(user);
        System.out.println("注册成功！"+"\n用户名："+username+"\n密码："+password+"\nvalid："+ user.getValid());
        return "comm/login";
    }

}