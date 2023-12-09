package pl.dominikwawrzyn.recipeKitchenCategory;

import jakarta.persistence.*;
import lombok.*;
import pl.dominikwawrzyn.recipeKitchen.RecipeKitchen;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "categoryKitchen")
public class CategoryKitchen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "categoryKitchen")
    private List<RecipeKitchen> recipeKitchens;
}