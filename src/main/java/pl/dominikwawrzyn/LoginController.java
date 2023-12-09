package pl.dominikwawrzyn;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String loginPost() {
        // Tutaj powinna być logika autentykacji użytkownika

        // Po pomyślnej autentykacji przekieruj na stronę adminDashboard
        return "redirect:/admin/dashboard";
    }
}