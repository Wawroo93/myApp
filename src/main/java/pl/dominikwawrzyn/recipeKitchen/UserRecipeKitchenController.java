package pl.dominikwawrzyn.recipeKitchen;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.dominikwawrzyn.category.Category;
import pl.dominikwawrzyn.category.CategoryRepository;

import java.util.List;

@Controller
@RequestMapping("/user/recipeKitchen")
public class UserRecipeKitchenController {
    private final RecipeKitchenRepository recipeKitchenRepository;
    private final CategoryRepository categoryRepository;

    public UserRecipeKitchenController(RecipeKitchenRepository recipeKitchenRepository, CategoryRepository categoryRepository) {
        this.recipeKitchenRepository = recipeKitchenRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/{categoryId}")
    public String showRecipeKitchenByCategory(@PathVariable Long categoryId, Model model) {
        List<RecipeKitchen> recipesKitchen = recipeKitchenRepository.findByCategoryId(categoryId);
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + categoryId));
        model.addAttribute("recipesKitchen", recipesKitchen);
        model.addAttribute("categoryName", category.getName());
        return "user/userRecipeKitchenList";
    }
    @GetMapping("/details/{id}")
    public String showRecipeKitchenDetails(@PathVariable Long id, Model model) {
        RecipeKitchen recipeKitchen = recipeKitchenRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid RecipeBar Id:" + id));
        model.addAttribute("recipeKitchen", recipeKitchen);
        return "user/userRecipeKitchenDetails";
    }
}
