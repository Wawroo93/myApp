package pl.dominikwawrzyn.category;

import lombok.*;
import pl.dominikwawrzyn.productsBar.ProductsBar;
import pl.dominikwawrzyn.productsKitchen.ProductsKitchen;
import pl.dominikwawrzyn.recipeBar.RecipeBar;
import pl.dominikwawrzyn.recipeKitchen.RecipeKitchen;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
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

    @NotBlank(message = "{jakarta.validation.constraints.NotBlank.name}")
    @Size(max = 25, message = "{jakarta.validation.constraints.Size.name}")
    private String name;

    private String type;

    @OneToMany(mappedBy = "category")
    private List<RecipeBar> recipesBar;

    @OneToMany(mappedBy = "category")
    private List<RecipeKitchen> recipesKitchen;

    @OneToMany(mappedBy = "category")
    private List<ProductsBar> productsBar;

    @OneToMany(mappedBy = "category")
    private List<ProductsKitchen> productsKitchen;
}