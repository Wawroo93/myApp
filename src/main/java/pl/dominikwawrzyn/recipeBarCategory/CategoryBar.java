package pl.dominikwawrzyn.recipeBarCategory;

import jakarta.persistence.*;
import lombok.*;
import pl.dominikwawrzyn.recipeBar.RecipeBar;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "categoryBar")
public class CategoryBar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "categoryBar")
    private List<RecipeBar> recipeBars;
}