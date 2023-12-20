package pl.dominikwawrzyn.recipeKitchen;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeKitchenRepository extends JpaRepository<RecipeKitchen, Long> {
    List<RecipeKitchen> findByCategoryId(Long categoryId);

}
