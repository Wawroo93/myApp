package pl.dominikwawrzyn.employee;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserChangePassword {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public UserChangePassword(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/changePassword")
    public String showChangePasswordForm(Model model, Principal principal) {
        Employee employee = employeeRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("Niepoprawny email użytkownika: " + principal.getName()));
        model.addAttribute("employee", employee);
        return "user/userChangePassword";
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 Principal principal, RedirectAttributes redirectAttributes) {
        Employee employee = employeeRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("Niepoprawny email użytkownika: " + principal.getName()));

        if (!passwordEncoder.matches(oldPassword, employee.getPassword())) {
            redirectAttributes.addFlashAttribute("error", "Stare hasło jest niepoprawne");
            return "redirect:/user/changePassword";
        }

        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Nowe hasło i potwierdzenie hasła nie są takie same");
            return "redirect:/user/changePassword";
        }

        employee.setPassword(passwordEncoder.encode(newPassword));
        employeeRepository.save(employee);

        return "redirect:/user/dashboard";
    }
}
