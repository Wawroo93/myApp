package pl.dominikwawrzyn.productsKitchen;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import lombok.*;
import pl.dominikwawrzyn.category.Category;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "productsKitchen")
public class ProductsKitchen {
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