package pl.dominikwawrzyn.employee;



import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.dominikwawrzyn.recipeBar.RecipeBar;
import pl.dominikwawrzyn.recipeKitchen.RecipeKitchen;
import pl.dominikwawrzyn.schedule.Schedule;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Imię nie może być puste")
    private String firstName;

    @NotBlank(message = "Nazwisko nie może być puste")
    private String lastName;

    @NotBlank(message = "Hasło nie może być puste")
    private String password;

    @Pattern(regexp = "\\d{11}", message = "PESEL musi składać się z 11 cyfr")
    private String pesel;

    @NotBlank(message = "Numer telefonu nie może być pusty")
    @Pattern(regexp = "\\d+", message = "Numer telefonu może składać się tylko z cyfr")
    private String phoneNumber;

    @NotBlank(message = "Adres nie może być pusty")
    private String address;

    @NotBlank(message = "Miasto nie może być puste")
    private String city;

    @NotBlank(message = "Email nie może być pusty")
    @Email(message = "Email musi być poprawny")
    private String email;

    @NotBlank(message = "Typ umowy nie może być pusty")
    private String contractType;

    private Boolean sanitaryEpidemiologicalStudy;

    private Boolean barStaff;

    private Boolean kitchenStaff;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "employee_roles",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    public boolean isAdmin() {
        return roles.stream()
                .anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
    }

    @OneToMany(mappedBy = "employee")
    private List<Schedule> schedules;

    @OneToMany(mappedBy = "employee")
    private List<RecipeBar> recipesBar;

    @OneToMany(mappedBy = "employee")
    private List<RecipeKitchen> recipesKitchen;

//    @PrePersist
//    @PreUpdate
//    public void encodePassword() {
//        this.password = new BCryptPasswordEncoder().encode(this.password);
//    }
}