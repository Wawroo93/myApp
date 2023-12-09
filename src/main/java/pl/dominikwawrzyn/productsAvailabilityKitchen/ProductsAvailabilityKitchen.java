package pl.dominikwawrzyn.productsAvailabilityKitchen;


import jakarta.persistence.*;
import lombok.*;
import pl.dominikwawrzyn.productsAvailabilityKitCat.ProductsAvailabilityKitchenCategory;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "productsAvailabilityKitchen")
public class ProductsAvailabilityKitchen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private ProductsAvailabilityKitchenCategory productsAvailabilityKitchenCategory;
}