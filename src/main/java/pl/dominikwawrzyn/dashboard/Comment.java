package pl.dominikwawrzyn.dashboard;

import lombok.*;
import pl.dominikwawrzyn.employee.Employee;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne
    private Employee author;

    @ManyToOne
    private Message message;

    private LocalDateTime timestamp;

    @PrePersist
    public void prePersist() {
        timestamp = LocalDateTime.now();
    }
    public Date getTimestampAsDate() {
        return java.sql.Timestamp.valueOf(this.timestamp);
    }
}