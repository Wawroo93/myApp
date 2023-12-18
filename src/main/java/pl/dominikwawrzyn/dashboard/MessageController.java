package pl.dominikwawrzyn.dashboard;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.dominikwawrzyn.employee.Employee;
import pl.dominikwawrzyn.employee.EmployeeRepository;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/messages")
public class MessageController {
    private final MessageRepository messageRepository;
    private final EmployeeRepository employeeRepository;

    public MessageController(MessageRepository messageRepository, EmployeeRepository employeeRepository) {
        this.messageRepository = messageRepository;
        this.employeeRepository = employeeRepository;
    }
    @GetMapping("/add")
    public String showAddMessageForm(Model model) {
        model.addAttribute("message", new Message());
        return "admin/messages/adminMessageAdd";
    }
    @PostMapping("/add")
    public String addMessage(@ModelAttribute Message message, RedirectAttributes redirectAttributes) {
        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        Employee currentEmployee = employeeRepository.findByEmail(currentUserName).orElse(null);
        if (currentEmployee != null) {
            String fullName = currentEmployee.getFirstName() + " " + currentEmployee.getLastName();
            message.setAuthor(fullName);
            messageRepository.save(message);
            redirectAttributes.addFlashAttribute("success", "Wiadomość została dodana pomyślnie.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Nie można znaleźć aktualnie zalogowanego użytkownika.");
        }
        return "redirect:/admin/dashboard";
    }
    @PostMapping("/updateReadStatus")
    public String updateReadStatus(@RequestParam Long messageId, @RequestParam(required = false) List<Long> readBy, RedirectAttributes redirectAttributes) {
        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        Employee currentEmployee = employeeRepository.findByEmail(currentUserName).orElse(null);
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        if (optionalMessage.isPresent() && currentEmployee != null) {
            Message message = optionalMessage.get();
            if (readBy != null && readBy.contains(currentEmployee.getId())) {
                if (!message.getReadByEmployees().contains(currentEmployee)) {
                    message.getReadByEmployees().add(currentEmployee);
                }
            } else {
                message.getReadByEmployees().remove(currentEmployee);
            }
            messageRepository.save(message);
            redirectAttributes.addFlashAttribute("success", "Status wiadomości został zaktualizowany.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Nie można znaleźć wiadomości o podanym ID lub aktualnie zalogowanego użytkownika.");
        }
        return "redirect:/admin/dashboard";
    }
}
