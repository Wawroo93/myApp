package pl.dominikwawrzyn.productsBar;

import javax.validation.constraints.NotBlank;
import javax.persistence.*;
import lombok.*;
import pl.dominikwawrzyn.category.Category;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "productsBar")
public class ProductsBar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "nazwa nie może być pusta")
    private String name;

    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}