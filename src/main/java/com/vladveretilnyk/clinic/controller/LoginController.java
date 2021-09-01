package com.vladveretilnyk.clinic.controller;

import com.vladveretilnyk.clinic.entity.Role;
import com.vladveretilnyk.clinic.entity.User;
import com.vladveretilnyk.clinic.handler.AuthenticationSuccessHandlerImpl;
import com.vladveretilnyk.clinic.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class LoginController {

    private final AuthenticationSuccessHandlerImpl successHandler = new AuthenticationSuccessHandlerImpl();

    @GetMapping
    public String getUserIndexPage() {
        return "redirect:" + successHandler.determineTargetUrl(SecurityContextHolder.getContext().getAuthentication());
    }

    @GetMapping("login")
    public String getLoginPage(@RequestParam(name = "error", required = false) String error,
                               @RequestParam(name = "logout", required = false) String logout,
                               Model model) {
        if (SecurityContextHolder.getContext().
                getAuthentication().getPrincipal().getClass().equals(User.class)) {
            return getUserIndexPage();
        }

        model.addAttribute("error", error != null);
        model.addAttribute("logout", logout != null);
        return "login";
    }
}
