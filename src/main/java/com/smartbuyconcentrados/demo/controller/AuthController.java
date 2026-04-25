package com.smartbuyconcentrados.demo.controller;

import com.smartbuyconcentrados.demo.model.User;
import com.smartbuyconcentrados.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String erro,
                            @RequestParam(required = false) String logout,
                            Model model) {
        if (erro != null) model.addAttribute("erro", "E-mail ou senha incorretos.");
        if (logout != null) model.addAttribute("msg", "Você saiu com sucesso.");
        return "login";
    }

    @GetMapping("/cadastro")
    public String cadastroPage(Model model) {
        model.addAttribute("user", new User());
        return "cadastro";
    }

    @PostMapping("/cadastro")
    public String cadastrar(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        try {
            userService.cadastrar(user);
            redirectAttributes.addFlashAttribute("msg", "Cadastro realizado! Faça login.");
            return "redirect:/login";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/cadastro";
        }
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }
}