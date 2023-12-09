package pl.dominikwawrzyn.schedule;

import jakarta.persistence.*;
import lombok.*;
import pl.dominikwawrzyn.employee.Employee;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

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

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    private LocalTime startShift;

    private LocalTime endShift;

    private LocalDate date;

    @Transient
    private Duration hoursWorked;
    @PostLoad
    private void calculateHoursWorked() {
        this.hoursWorked = Duration.between(startShift, endShift);
    }
}