package ru.viklover.oopgame.api;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@CrossOrigin
public class MainController {

    @GetMapping
    public String router(HttpServletRequest request) {

        if (request.isUserInRole("user")) {
            return "redirect:/home";
        }

        return "redirect:/login";
    }
}