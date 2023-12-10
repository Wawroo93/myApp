package pl.dominikwawrzyn.employee;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import pl.dominikwawrzyn.recipeBar.RecipeBar;
import pl.dominikwawrzyn.recipeKitchen.RecipeKitchen;
import pl.dominikwawrzyn.schedule.Schedule;

import java.util.List;

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

    private Boolean admin;

    @OneToMany(mappedBy = "employee")
    private List<Schedule> schedules;

    @OneToMany(mappedBy = "employee")
    private List<RecipeBar> recipesBar;

    @OneToMany(mappedBy = "employee")
    private List<RecipeKitchen> recipesKitchen;
}