package pl.dominikwawrzyn.schedule;

import javax.persistence.*;
import lombok.*;
import pl.dominikwawrzyn.employee.Employee;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "schedules")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate day;

    private String startTime;

    private String endTime;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

}