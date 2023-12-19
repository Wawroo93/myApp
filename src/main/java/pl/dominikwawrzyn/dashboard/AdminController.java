package pl.dominikwawrzyn.dashboard;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.dominikwawrzyn.employee.Employee;
import pl.dominikwawrzyn.employee.EmployeeRepository;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final MessageRepository messageRepository;
    private final EmployeeRepository employeeRepository;

    public AdminController(MessageRepository messageRepository, EmployeeRepository employeeRepository) {
        this.messageRepository = messageRepository;
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<Message> messages = messageRepository.findAll();
        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        Employee currentEmployee = employeeRepository.findByEmail(currentUserName).orElse(null);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        List<String> formattedTimestamps = messages.stream()
                .map(message -> message.getTimestamp().format(formatter))
                .collect(Collectors.toList());

        model.addAttribute("currentEmployee", currentEmployee);
        model.addAttribute("messages", messages);
        model.addAttribute("timestamps", formattedTimestamps);
        return "admin/adminDashboard";
    }
}