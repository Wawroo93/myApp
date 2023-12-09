package pl.dominikwawrzyn.recipeKitchen;

import jakarta.persistence.*;
import lombok.*;
import pl.dominikwawrzyn.employee.Employee;
import pl.dominikwawrzyn.recipeKitchenCategory.CategoryKitchen;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "recipeKitchen")
public class RecipeKitchen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String ingredients;

    private String preparationTime;

    private String preparation;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryKitchen categoryKitchen;
}