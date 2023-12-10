package pl.dominikwawrzyn.recipeBar;

import jakarta.persistence.*;
import lombok.*;
import pl.dominikwawrzyn.category.Category;
import pl.dominikwawrzyn.employee.Employee;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "recipeBar")
public class RecipeBar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String ingredients;

    private String preparation;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}