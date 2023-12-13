package pl.dominikwawrzyn.employee;

import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.dominikwawrzyn.schedule.Schedule;
import pl.dominikwawrzyn.schedule.ScheduleRepository;

import java.util.List;

@Controller
@RequestMapping("/admin/employee")
public class EmployeeController {

    private final EmployeeRepository employeeRepository;


    private final ScheduleRepository scheduleRepository;

    @Autowired
    public EmployeeController(EmployeeRepository employeeRepository, ScheduleRepository scheduleRepository) {
        this.employeeRepository = employeeRepository;
        this.scheduleRepository = scheduleRepository;
    }

    @GetMapping("/list")
    public String list(Model model) {
        List<Employee> employees = employeeRepository.findAll();
        model.addAttribute("employees", employees);
        return "admin/employee/adminEmployeeList";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable Long id, Model model) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Niepoprawne id pracownika: " + id));
        model.addAttribute("employee", employee);
        return "admin/employee/adminEmployeeDetails";
    }

    @GetMapping("/edit/{id}")
    public String showEdit(@PathVariable Long id, Model model) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Niepoprawne id pracownika: " + id));
        model.addAttribute("employee", employee);
        return "admin/employee/adminEmployeeEdit";
    }

    @PostMapping("/edit/{id}")
    public String updateEmployee(@PathVariable Long id, @Valid Employee employee, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/employee/adminEmployeeEdit";
        }
        employeeRepository.findById(id)
                .map(employees -> {
                    employee.setFirstName(employee.getFirstName());
                    employee.setLastName(employee.getLastName());
                    employee.setPhoneNumber(employee.getPhoneNumber());
                    employee.setAddress(employee.getAddress());
                    employee.setCity(employee.getCity());
                    employee.setPesel(employee.getPesel());
                    employee.setEmail(employee.getEmail());
                    employee.setPassword(employee.getPassword());
                    employee.setBarStaff(employee.getBarStaff());
                    employee.setKitchenStaff(employee.getKitchenStaff());
                    employee.setContractType(employee.getContractType());
                    employee.setSanitaryEpidemiologicalStudy(employee.getSanitaryEpidemiologicalStudy());
                    return employeeRepository.save(employee);
                })
                .orElseThrow(() -> new IllegalArgumentException("Niepoprawne id pracownika: " + id));
        return "redirect:/admin/employee/details/" + id;
    }

    @GetMapping("/add")
    public String showAdd(Model model) {
        model.addAttribute("employee", new Employee());
        return "admin/employee/adminEmployeeAdd";
    }

    @PostMapping("/add")
    public String addEmployee(@Valid Employee employee, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/employee/adminEmployeeAdd";
        }
        employeeRepository.save(employee);
        return "redirect:/admin/employee/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Niepoprawne id pracownika: " + id));
        employeeRepository.delete(employee);
        return "redirect:/admin/employee/list";
    }
}
