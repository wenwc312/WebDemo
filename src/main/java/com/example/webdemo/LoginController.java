package com.example.webdemo;


import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {
    // 在application.properties寫佔位符，再注入值到controller
    @Value("${app.admin.username}") private String adminUsername;
    @Value("${app.admin.password}") private String adminPassword;

    // 顯示登入頁
    @GetMapping("/login")
    public String loginPage(){
        return "login"; // 對應 temp;ates/login.html
    }

    // 處理登入表單送出(帳密寫死在程式碼中)
    @PostMapping("/login")
    public String doLogin(@RequestParam String username,
                          @RequestParam String password,
                          HttpSession session,
                          Model model){
        // 這裡先用寫死帳密測試，之後可接資料庫
//        if("admin".equals(username) && "0000".equals(password)){
//            session.setAttribute("loginUser", username); //記住已登入
//            return "redirect:/welcome"; //跳轉!
//        }

        if(adminUsername.equals(username) && adminPassword.equals(password)){
            session.setAttribute("username", username);
            return "redirect:/welcome";
        }

        model.addAttribute("error",true);
        return "login"; //帳密錯誤，留在登入頁並顯示錯誤訊息
    }


    // 登入後的頁面
    @GetMapping("/welcome")
    public String welcomePage(HttpSession session, Model model){
        Object user = session.getAttribute("loginUser");
        if(user == null){
            return "redirect:/login"; //沒登入卻想直接訪問，導回登入頁
        }
        model.addAttribute("username",user);
        return "welcome"; // 對應templates/welcome.html
    }

    @GetMapping("/home")
    public String homePage(HttpSession session, Model model){
        Object user = session.getAttribute("loginUser");
        if(user == null){
            return "redirect:/login";
        }
        model.addAttribute("username",user);
        return "home";
    }

    // 登出
    @PostMapping("/logout")
    public String doLogout(HttpSession session){
        session.invalidate();
        return "redirect:/login";
    }
}
