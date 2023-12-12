package pl.dominikwawrzyn.productsBar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.dominikwawrzyn.category.Category;

import java.util.List;

@Repository
public interface ProductsBarRepository extends JpaRepository<ProductsBar, Long> {
    List<ProductsBar> findByCategoryId(Long categoryId);
    List<ProductsBar> findByCategory(Category category);
}
