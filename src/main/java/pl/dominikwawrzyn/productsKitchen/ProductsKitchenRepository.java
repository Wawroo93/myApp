package pl.dominikwawrzyn.productsKitchen;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.dominikwawrzyn.category.Category;

import java.util.List;

@Repository
public interface ProductsKitchenRepository extends JpaRepository<ProductsKitchen, Long> {
    List<ProductsKitchen> findByCategory(Category category);
}
