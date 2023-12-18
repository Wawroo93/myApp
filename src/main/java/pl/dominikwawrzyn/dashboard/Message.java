package pl.dominikwawrzyn.dashboard;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;
import pl.dominikwawrzyn.employee.Employee;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @CreationTimestamp
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime timestamp;

    private String author;

    private String receiver;

    @ManyToMany
    @JoinTable(
            name = "message_read_by_employee",
            joinColumns = @JoinColumn(name = "message_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id"))
    private List<Employee> readByEmployees = new ArrayList<>();

    @OneToMany(mappedBy = "message")
    private List<Comment> comments = new ArrayList<>();

}