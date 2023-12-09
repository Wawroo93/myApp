package pl.dominikwawrzyn.productsAvailabilityBarCat;


import jakarta.persistence.*;
import lombok.*;
import pl.dominikwawrzyn.productsAvailabilityBar.ProductsAvailabilityBar;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "productsAvailabilityBarCategory")
public class ProductsAvailabilityBarCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "productsAvailabilityBarCategory")
    private List<ProductsAvailabilityBar> productsAvailabilityBar;
}