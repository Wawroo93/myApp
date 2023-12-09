package pl.dominikwawrzyn.productsAvailabilityBar;


import jakarta.persistence.*;
import lombok.*;
import pl.dominikwawrzyn.productsAvailabilityBarCat.ProductsAvailabilityBarCategory;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "productsAvailabilityBar")
public class ProductsAvailabilityBar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private ProductsAvailabilityBarCategory productsAvailabilityBarCategory;
}