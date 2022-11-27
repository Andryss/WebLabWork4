package web.controllers;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import web.entities.Request;
import web.services.UserService;

@Controller
@RequestMapping("/form")
public class FormController {

    private final UserService userService;

    @Autowired
    public FormController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getForm(Model model, HttpSession session) {
        model.addAttribute("request", new Request());
        model.addAttribute("history", userService.getUserHistory(session.getId()));
        return "form";
    }

    @PostMapping
    public String sendForm(Model model, HttpSession session,
                           @ModelAttribute("request") @Valid Request request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("history", userService.getUserHistory(session.getId()));
            return "form";
        }
        userService.addUserRequest(session.getId(), request);
        return "redirect:/form";
    }

}
