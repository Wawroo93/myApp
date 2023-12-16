package pl.dominikwawrzyn.schedule;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import pl.dominikwawrzyn.employee.Employee;
import pl.dominikwawrzyn.employee.EmployeeRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Controller
@RequestMapping("/admin/schedule")
public class ScheduleController {
    private final ScheduleRepository scheduleRepository;
    private final EmployeeRepository employeeRepository;

    public ScheduleController(ScheduleRepository scheduleRepository, EmployeeRepository employeeRepository) {

        this.scheduleRepository = scheduleRepository;
        this.employeeRepository = employeeRepository;
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

//        for (Employee employee : employees) {
//            for (LocalDate day : days) {
//                if (schedules.stream().noneMatch(s -> s.getDay().equals(day) && s.getEmployee().equals(employee))) {
//                    Schedule schedule = new Schedule();
//                    schedule.setDay(day);
//                    schedule.setEmployee(employee);
//                    schedule.setStartTime("");
//                    schedule.setEndTime("");
//                    schedules.add(schedule);
//                    scheduleRepository.save(schedule);
//                }
//            }
//        }
        model.addAttribute("days", days);
        model.addAttribute("employees", employees);
        model.addAttribute("schedules", schedules);
        return "admin/schedule/adminScheduleList";
    }
//    @GetMapping("/listThisMonth")
//    public String listThisMonth(Model model) {
//        LocalDate start = LocalDate.now().withDayOfMonth(1);
//        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
//        List<LocalDate> days = Stream.iterate(start, date -> date.plusDays(1))
//                .limit(ChronoUnit.DAYS.between(start, end) + 1)
//                .collect(Collectors.toList());
//        List<Employee> employees = employeeRepository.findAll();
//        List<Schedule> schedules = scheduleRepository.findAll();
//
//        for (Employee employee : employees) {
//            for (LocalDate day : days) {
//                if (schedules.stream().noneMatch(s -> s.getDay().equals(day) && s.getEmployee().equals(employee))) {
//                    Schedule schedule = new Schedule();
//                    schedule.setDay(day);
//                    schedule.setEmployee(employee);
//                    schedule.setStartTime("");
//                    schedule.setEndTime("");
//                    schedules.add(schedule);
//                    scheduleRepository.save(schedule);
//                }
//            }
//        }
//        model.addAttribute("days", days);
//        model.addAttribute("employees", employees);
//        model.addAttribute("schedules", schedules);
//        return "admin/schedule/adminScheduleThisMonth";
//    }

    @GetMapping("/edit")
    public String showEditForm(Model model) {
        List<Employee> employees = employeeRepository.findAll();
        model.addAttribute("employees", employees);
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(14);
        List<LocalDate> days = Stream.iterate(start, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(start, end) + 1)
                .collect(Collectors.toList());
        model.addAttribute("days", days);

        List<Schedule> schedules = scheduleRepository.findAll();

        for (Employee employee : employees) {
            for (LocalDate day : days) {
                if (schedules.stream().noneMatch(s -> s.getDay().equals(day) && s.getEmployee().equals(employee))) {
                    Schedule schedule = new Schedule();
                    schedule.setDay(day);
                    schedule.setEmployee(employee);
                    schedule.setStartTime("");
                    schedule.setEndTime("");
                    schedules.add(schedule);
                    scheduleRepository.save(schedule);
                }
            }
        }

        model.addAttribute("schedules", schedules);
        ScheduleForm scheduleForm = new ScheduleForm();
        scheduleForm.setSchedules(schedules);
        model.addAttribute("scheduleForm", scheduleForm);
        return "admin/schedule/adminScheduleEdit";
    }
//    @PostMapping("/save")
//    public String saveSchedule(@ModelAttribute List<Schedule> schedules) {
//        for (Schedule schedule : schedules) {
//            if (scheduleRepository.existsById(schedule.getId())) {
//                Schedule existingSchedule = scheduleRepository.findById(schedule.getId())
//                        .orElseThrow(() -> new IllegalArgumentException("Invalid schedule Id:" + schedule.getId()));
//                existingSchedule.setStartTime(schedule.getStartTime());
//                existingSchedule.setEndTime(schedule.getEndTime());
//                scheduleRepository.saveAndFlush(existingSchedule);
//            }
//        }
//        return "redirect:/admin/schedule/list";
//    }
//@PostMapping("/save")
//public String saveSchedule(@ModelAttribute("scheduleForm") ScheduleForm scheduleForm) {
//    LocalDate start = LocalDate.now();
//    LocalDate end = start.plusDays(14);
//    for (Schedule schedule : scheduleForm.getSchedules()) {
//        if (scheduleRepository.existsById(schedule.getId())) {
//            Schedule existingSchedule = scheduleRepository.findById(schedule.getId())
//                    .orElseThrow(() -> new IllegalArgumentException("Invalid schedule Id:" + schedule.getId()));
//            if (!schedule.getDay().isBefore(start) && !schedule.getDay().isAfter(end)) {
//                // Aktualizuj harmonogram, jeśli jest w zakresie od dzisiaj do 14 dni do przodu
//                existingSchedule.setStartTime(schedule.getStartTime());
//                existingSchedule.setEndTime(schedule.getEndTime());
//            }
//            scheduleRepository.saveAndFlush(existingSchedule);
//        } else {
//            // Zapisz nowy harmonogram do bazy danych, jeśli nie istnieje
//            scheduleRepository.save(schedule);
//        }
//    }
//    return "redirect:/admin/schedule/list";
//}
@PostMapping("/save")
public String saveSchedule(@ModelAttribute("scheduleForm") ScheduleForm scheduleForm) {
    for (Schedule schedule : scheduleForm.getSchedules()) {
        if (schedule.getId() != null && scheduleRepository.existsById(schedule.getId())) {
            Schedule existingSchedule = scheduleRepository.findById(schedule.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid schedule Id:" + schedule.getId()));
            existingSchedule.setStartTime(schedule.getStartTime());
            existingSchedule.setEndTime(schedule.getEndTime());
            scheduleRepository.saveAndFlush(existingSchedule);
        }
    }
    return "redirect:/admin/schedule/list";
}

//    @PostMapping("/save")
//    public String saveSchedule(@ModelAttribute("scheduleForm") ScheduleForm scheduleForm) {
//        for (Schedule schedule : scheduleForm.getSchedules()) {
//            if (scheduleRepository.existsById(schedule.getId())) {
//                Schedule existingSchedule = scheduleRepository.findById(schedule.getId())
//                        .orElseThrow(() -> new IllegalArgumentException("Invalid schedule Id:" + schedule.getId()));
//                existingSchedule.setStartTime(schedule.getStartTime());
//                existingSchedule.setEndTime(schedule.getEndTime());
//                scheduleRepository.saveAndFlush(existingSchedule);
//            }
//        }
//        return "redirect:/admin/schedule/list";
//    }

//    @GetMapping("/editThisMonth")
//    public String editThisMonth(Model model) {
//        LocalDate start = LocalDate.now().withDayOfMonth(1);
//        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
//        List<LocalDate> days = Stream.iterate(start, date -> date.plusDays(1))
//                .limit(ChronoUnit.DAYS.between(start, end) + 1)
//                .collect(Collectors.toList());
//        List<Employee> employees = employeeRepository.findAll();
//        List<Schedule> schedules = scheduleRepository.findAll();
//
//        model.addAttribute("days", days);
//        model.addAttribute("employees", employees);
//        model.addAttribute("schedules", schedules);
//        ScheduleForm scheduleForm = new ScheduleForm();
//        scheduleForm.setSchedules(schedules);
//        model.addAttribute("scheduleForm", scheduleForm);
//        return "admin/schedule/adminScheduleEditThisMonth";
//    }

//    @PostMapping("/saveThisMonth")
//    public String saveThisMonth(@ModelAttribute("scheduleForm") ScheduleForm scheduleForm) {
//        for (Schedule schedule : scheduleForm.getSchedules()) {
//            if (scheduleRepository.existsById(schedule.getId())) {
//                Schedule existingSchedule = scheduleRepository.findById(schedule.getId())
//                        .orElseThrow(() -> new IllegalArgumentException("Invalid schedule Id:" + schedule.getId()));
//                existingSchedule.setStartTime(schedule.getStartTime());
//                existingSchedule.setEndTime(schedule.getEndTime());
//                scheduleRepository.saveAndFlush(existingSchedule);
//            }
//        }
//        return "redirect:/admin/schedule/listThisMonth";
//    }

}

