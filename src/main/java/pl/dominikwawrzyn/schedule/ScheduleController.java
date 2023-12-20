package pl.dominikwawrzyn.schedule;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import pl.dominikwawrzyn.employee.Employee;
import pl.dominikwawrzyn.employee.EmployeeRepository;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
        LocalDate end = start.plusDays(21);
        List<LocalDate> days = Stream.iterate(start, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(start, end) + 1)
                .collect(Collectors.toList());
        List<Employee> employees = employeeRepository.findAll();
        List<Schedule> schedules = scheduleRepository.findAll();

        model.addAttribute("days", days);
        model.addAttribute("employees", employees);
        model.addAttribute("schedules", schedules);
        return "admin/schedule/adminScheduleList";
    }

    @GetMapping("/edit")
    public String showEditForm(Model model) {
        List<Employee> employees = employeeRepository.findAll();
        model.addAttribute("employees", employees);
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(21);
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
    public long calculateHours(Schedule schedule) {
        if (schedule.getStartTime() != null && !schedule.getStartTime().isEmpty()
                && schedule.getEndTime() != null && !schedule.getEndTime().isEmpty()) {
            int startHour = Integer.parseInt(schedule.getStartTime().split(":")[0]);
            int endHour = Integer.parseInt(schedule.getEndTime().split(":")[0]);
            return endHour - startHour;
        }
        return 0;
    }
    @GetMapping("/history")
    public String showScheduleHistory(Model model) {
        LocalDate today = LocalDate.now();
        List<Schedule> allSchedules = scheduleRepository.findAll();
        List<Schedule> pastSchedules = allSchedules.stream()
                .filter(schedule -> schedule.getDay().isBefore(today))
                .collect(Collectors.toList());

        Map<Month, Map<LocalDate, Map<Employee, List<Schedule>>>> sortedSchedulesByMonthAndDayAndEmployee = pastSchedules.stream()
                .collect(Collectors.groupingBy(schedule -> schedule.getDay().getMonth(),
                        Collectors.groupingBy(Schedule::getDay,
                                Collectors.groupingBy(Schedule::getEmployee))))
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        List<Employee> employees = pastSchedules.stream()
                .map(Schedule::getEmployee)
                .distinct()
                .collect(Collectors.toList());

        Map<Month, Map<Employee, Long>> employeeHoursByMonth = pastSchedules.stream()
                .collect(Collectors.groupingBy(schedule -> schedule.getDay().getMonth(),
                        Collectors.groupingBy(Schedule::getEmployee,
                                Collectors.summingLong(this::calculateHours))));
        model.addAttribute("employeeHoursByMonth", employeeHoursByMonth);

        model.addAttribute("schedulesByMonthAndDayAndEmployee", sortedSchedulesByMonthAndDayAndEmployee);
        model.addAttribute("employees", employees);
        model.addAttribute("schedules", pastSchedules);
        return "admin/schedule/adminScheduleHistory";
    }

    @GetMapping("/history/edit")
    public String showHistoryEditForm(Model model) {
        LocalDate today = LocalDate.now();
        List<Schedule> allSchedules = scheduleRepository.findAll();
        List<Schedule> pastSchedules = allSchedules.stream()
                .filter(schedule -> schedule.getDay().isBefore(today))
                .collect(Collectors.toList());

        List<LocalDate> days = pastSchedules.stream()
                .map(Schedule::getDay)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        Collections.reverse(days);

        List<Employee> employees = pastSchedules.stream()
                .map(Schedule::getEmployee)
                .distinct()
                .collect(Collectors.toList());

        model.addAttribute("days", days);
        model.addAttribute("employees", employees);
        model.addAttribute("schedules", pastSchedules);

        ScheduleForm scheduleForm = new ScheduleForm();
        scheduleForm.setSchedules(pastSchedules);
        model.addAttribute("scheduleForm", scheduleForm);

        return "admin/schedule/adminScheduleHistoryEdit";
    }
    @PostMapping("/history/save")
    public String saveScheduleHistory(@ModelAttribute("scheduleForm") ScheduleForm scheduleForm) {
        for (Schedule schedule : scheduleForm.getSchedules()) {
            if (schedule.getId() != null && scheduleRepository.existsById(schedule.getId())) {
                Schedule existingSchedule = scheduleRepository.findById(schedule.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid schedule Id:" + schedule.getId()));
                existingSchedule.setStartTime(schedule.getStartTime());
                existingSchedule.setEndTime(schedule.getEndTime());
                scheduleRepository.saveAndFlush(existingSchedule);
            }
        }
        return "redirect:/admin/schedule/history";
    }
    @GetMapping("/history/delete")
    public String deleteScheduleByDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, Model model) {
        List<Schedule> schedules = scheduleRepository.findAllByDay(date);
        scheduleRepository.deleteAll(schedules);

        List<LocalDate> days = scheduleRepository.findAll().stream()
                .map(Schedule::getDay)
                .distinct()
                .sorted()
                .toList();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<String> formattedDays = days.stream()
                .map(day -> day.format(formatter))
                .collect(Collectors.toList());

        model.addAttribute("days", formattedDays);
        return "redirect:/admin/schedule/history";
    }
}

