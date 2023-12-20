package pl.dominikwawrzyn.dashboard;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.dominikwawrzyn.employee.Employee;
import pl.dominikwawrzyn.employee.EmployeeRepository;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserController {
    private final MessageRepository messageRepository;
    private final EmployeeRepository employeeRepository;
    private final CommentRepository commentRepository;

    public UserController(MessageRepository messageRepository, EmployeeRepository employeeRepository, CommentRepository commentRepository) {
        this.messageRepository = messageRepository;
        this.employeeRepository = employeeRepository;
        this.commentRepository = commentRepository;
    }

    @GetMapping("/dashboard")
    public String userDashboard(Model model) {
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
        return "user/userDashboard";
    }
    @PostMapping("/messages/updateReadStatus")
    public String updateReadStatus(@RequestParam Long messageId, @RequestParam(required = false) List<Long> readBy) {
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
        }
        return "redirect:/user/dashboard";
    }
    @PostMapping("/comment/addComment/{messageId}")
    public String addComment(@PathVariable Long messageId, @ModelAttribute Comment comment, @RequestParam Long authorId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid message Id:" + messageId));
        Employee author = employeeRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee Id:" + authorId));
        comment.setMessage(message);
        comment.setAuthor(author);
        commentRepository.save(comment);
        return "redirect:/user/dashboard";
    }
}