package pl.dominikwawrzyn.dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.dominikwawrzyn.employee.Employee;
import pl.dominikwawrzyn.employee.EmployeeRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/admin/comment")
public class CommentController {
    private final MessageRepository messageRepository;
    private final CommentRepository commentRepository;
    private final EmployeeRepository employeeRepository;

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    public CommentController(MessageRepository messageRepository, CommentRepository commentRepository, EmployeeRepository employeeRepository) {
        this.messageRepository = messageRepository;
        this.commentRepository = commentRepository;
        this.employeeRepository = employeeRepository;
    }

    @PostMapping("/addComment/{messageId}")
    public String addComment(@PathVariable Long messageId, @ModelAttribute Comment comment, @RequestParam Long authorId) {
        try {
            Message message = messageRepository.findById(messageId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Message Id:" + messageId));
            Employee author = employeeRepository.findById(authorId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Employee Id:" + authorId));
            comment.setMessage(message);
            comment.setAuthor(author);
            commentRepository.save(comment);
            logger.info("Comment added successfully");
        } catch (Exception e) {
            logger.error("Error occurred while adding comment", e);
        }
        return "redirect:/admin/dashboard";
    }
}
