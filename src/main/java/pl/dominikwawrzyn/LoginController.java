package pl.dominikwawrzyn;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.dominikwawrzyn.employee.Employee;
import pl.dominikwawrzyn.employee.EmployeeRepository;

@Controller
public class LoginController {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginController(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String loginPost(@RequestParam("email") String email, @RequestParam("password") String password) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Niepoprawny email: " + email));

        String encodedPassword = passwordEncoder.encode(password);

        if (!passwordEncoder.matches(encodedPassword, employee.getPassword())) {
            throw new IllegalArgumentException("Niepoprawne hasło");
        }

        // Sprawdź rolę użytkownika
        boolean isAdmin = employee.isAdmin();

        if (isAdmin) {
            return "redirect:/admin/dashboard";
        } else {
            return "redirect:/user/dashboard";
        }
    }
}