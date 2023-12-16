package pl.dominikwawrzyn.schedule;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.dominikwawrzyn.employee.Employee;
import pl.dominikwawrzyn.employee.EmployeeRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/user/schedule")
public class UserScheduleController {
    private final EmployeeRepository employeeRepository;
    private final ScheduleRepository scheduleRepository;

    public UserScheduleController(EmployeeRepository employeeRepository, ScheduleRepository scheduleRepository) {
        this.employeeRepository = employeeRepository;
        this.scheduleRepository = scheduleRepository;
    }

    @GetMapping("/list")
    public String list(Model model) {
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(14);
        List<LocalDate> days = Stream.iterate(start, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(start, end) + 1)
                .collect(Collectors.toList());
        List<Employee> employees = employeeRepository.findAll();
        List<Schedule> schedules = scheduleRepository.findAll();

        model.addAttribute("days", days);
        model.addAttribute("employees", employees);
        model.addAttribute("schedules", schedules);
        return "user/userScheduleList";
    }
}
