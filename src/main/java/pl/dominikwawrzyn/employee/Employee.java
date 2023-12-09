package pl.dominikwawrzyn.employee;

import jakarta.persistence.*;
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

    private String firstName;

    private String lastName;

    private String password;

    private String pesel;

    private String phoneNumber;

    private String address;

    private String city;

    private String email;

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