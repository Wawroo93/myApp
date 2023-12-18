package pl.dominikwawrzyn.employee;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
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

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.ErrorManager;

@Controller
@RequestMapping("/admin/employee")
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    private final ScheduleRepository scheduleRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;
    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    public EmployeeController(EmployeeRepository employeeRepository, ScheduleRepository scheduleRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.employeeRepository = employeeRepository;
        this.scheduleRepository = scheduleRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
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

        List<Role> allRoles = roleRepository.findAll();
        model.addAttribute("allRoles", allRoles);

        return "admin/employee/adminEmployeeEdit";
    }

    @PostMapping("/edit/{id}")
    public String updateEmployee(@PathVariable Long id, @Valid Employee employee, BindingResult result) {
        if (result.hasErrors()) {
            logger.error("Validation errors occurred while updating employee: {}", result.getAllErrors());
            return "admin/employee/adminEmployeeEdit";
        }
        employeeRepository.findById(id)
                .map(existingEmployee -> {
                    existingEmployee.setFirstName(employee.getFirstName());
                    existingEmployee.setLastName(employee.getLastName());
                    existingEmployee.setPhoneNumber(employee.getPhoneNumber());
                    existingEmployee.setAddress(employee.getAddress());
                    existingEmployee.setCity(employee.getCity());
                    existingEmployee.setPesel(employee.getPesel());
                    existingEmployee.setPassword(employee.getPassword());
                    existingEmployee.setEmail(employee.getEmail());
                    existingEmployee.setBarStaff(employee.getBarStaff());
                    existingEmployee.setKitchenStaff(employee.getKitchenStaff());
                    existingEmployee.setContractType(employee.getContractType());
                    existingEmployee.setSanitaryEpidemiologicalStudy(employee.getSanitaryEpidemiologicalStudy());

                    Set<Role> roles = new HashSet<>();
                    for (Role role : employee.getRoles()) {
                        Role existingRole = roleRepository.findById(role.getId())
                                .orElseThrow(() -> new IllegalArgumentException("Niepoprawne id roli: " + role.getId()));
                        roles.add(existingRole);
                    }
                    existingEmployee.setRoles(roles);

                    return employeeRepository.save(existingEmployee);
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
        String encodedPassword = passwordEncoder.encode(employee.getPassword());
        employee.setPassword(encodedPassword);

        Role userRole = roleRepository.findByName("ROLE_USER");
        employee.getRoles().add(userRole);

        employeeRepository.save(employee);
        return "redirect:/admin/employee/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Niepoprawne id pracownika: " + id));

        List<Schedule> schedules = scheduleRepository.findAllByEmployeeId(id);
        for (Schedule schedule : schedules) {
            scheduleRepository.delete(schedule);
        }

        employeeRepository.delete(employee);
        return "redirect:/admin/employee/list";
    }
}
