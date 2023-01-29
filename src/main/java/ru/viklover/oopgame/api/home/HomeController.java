package ru.viklover.oopgame.api.home;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import ru.viklover.oopgame.security.user.UserDetailsImpl;

@Controller
@RequestMapping("/home")
@CrossOrigin
public class HomeController {

    @GetMapping
    public String homePage(HttpServletRequest request, Model model) {

        boolean isUser = request.isUserInRole("USER");

        model.addAttribute("is_logged", isUser);

        if (isUser) {

            var userPasswordAuthenticationToken =
                    (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            var userDetails = (UserDetailsImpl) userPasswordAuthenticationToken.getPrincipal();

            model.addAttribute("username", userDetails.getUsername());
        }

        return "home/index";
    }
}
