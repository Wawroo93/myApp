package pl.dominikwawrzyn.category;

import jakarta.persistence.*;
import lombok.*;
import pl.dominikwawrzyn.recipeBar.RecipeBar;
import pl.dominikwawrzyn.recipeKitchen.RecipeKitchen;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String type;

    @OneToMany(mappedBy = "category")
    private List<RecipeBar> recipeBars;

    @OneToMany(mappedBy = "category")
    private List<RecipeKitchen> recipeKitchens;
}