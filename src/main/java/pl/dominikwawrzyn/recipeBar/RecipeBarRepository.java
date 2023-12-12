package pl.dominikwawrzyn.recipeBar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeBarRepository extends JpaRepository<RecipeBar, Long> {
    List<RecipeBar> findByCategoryId(Long categoryId);

}
