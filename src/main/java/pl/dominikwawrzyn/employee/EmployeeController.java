package pl.dominikwawrzyn.employee;

import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/employee")
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/list")
    public String list(Model model) {
        List<Employee> employees = employeeRepository.findAll();
        model.addAttribute("employees", employees);
        return "admin/adminEmployeeList";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable Long id, Model model) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Niepoprawne id pracownika: " + id));
        model.addAttribute("employee", employee);
        return "admin/adminEmployeeDetails";
    }

    @GetMapping("/edit/{id}")
    public String showEdit(@PathVariable Long id, Model model) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Niepoprawne id pracownika: " + id));
        model.addAttribute("employee", employee);
        return "admin/adminEmployeeEdit";
    }

    @PostMapping("/edit/{id}")
    public String updateEmployee(@PathVariable Long id, Employee updatedEmployee) {
        employeeRepository.findById(id)
                .map(employee -> {
                    employee.setFirstName(updatedEmployee.getFirstName());
                    employee.setLastName(updatedEmployee.getLastName());
                    employee.setPhoneNumber(updatedEmployee.getPhoneNumber());
                    employee.setAddress(updatedEmployee.getAddress());
                    employee.setCity(updatedEmployee.getCity());
                    employee.setPesel(updatedEmployee.getPesel());
                    employee.setEmail(updatedEmployee.getEmail());
                    employee.setPassword(updatedEmployee.getPassword());
                    employee.setBarStaff(updatedEmployee.getBarStaff());
                    employee.setKitchenStaff(updatedEmployee.getKitchenStaff());
                    employee.setContractType(updatedEmployee.getContractType());
                    employee.setSanitaryEpidemiologicalStudy(updatedEmployee.getSanitaryEpidemiologicalStudy());
                    return employeeRepository.save(employee);
                })
                .orElseThrow(() -> new IllegalArgumentException("Niepoprawne id pracownika: " + id));
        return "redirect:/admin/employee/details/" + id;
    }

    @GetMapping("/add")
    public String showAdd(Model model) {
        model.addAttribute("employee", new Employee());
        return "admin/adminEmployeeAdd";
    }

    @PostMapping("/add")
    public String addEmployee(Employee employee) {
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
