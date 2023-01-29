package ru.viklover.oopgame.api.home;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
@CrossOrigin
public class HomeController {

    @GetMapping
    public String homePage(HttpServletRequest request) {

        if (request.isUserInRole("user")) {
            System.out.println("has user role");
        }

        System.out.println("anonymous user");

        return "home/index";
    }
}
