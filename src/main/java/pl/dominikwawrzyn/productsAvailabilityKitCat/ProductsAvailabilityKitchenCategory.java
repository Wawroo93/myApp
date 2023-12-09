package pl.dominikwawrzyn.productsAvailabilityKitCat;


import jakarta.persistence.*;
import lombok.*;
import pl.dominikwawrzyn.productsAvailabilityKitchen.ProductsAvailabilityKitchen;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "productsAvailabilityKitchenCategory")
public class ProductsAvailabilityKitchenCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "productsAvailabilityKitchenCategory")
    private List<ProductsAvailabilityKitchen> productsAvailabilityKitchen;
}