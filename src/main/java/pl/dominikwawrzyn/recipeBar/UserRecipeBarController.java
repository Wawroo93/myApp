package pl.dominikwawrzyn.recipeBar;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.dominikwawrzyn.category.Category;
import pl.dominikwawrzyn.category.CategoryRepository;

import java.util.List;

@Controller
@RequestMapping("/user/recipeBar")
public class UserRecipeBarController {
    private final RecipeBarRepository recipeBarRepository;
    private final CategoryRepository categoryRepository;

    public UserRecipeBarController(RecipeBarRepository recipeBarRepository, CategoryRepository categoryRepository) {
        this.recipeBarRepository = recipeBarRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/{categoryId}")
    public String showRecipeBarByCategory(@PathVariable Long categoryId, Model model) {
        List<RecipeBar> recipesBar = recipeBarRepository.findByCategoryId(categoryId);
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + categoryId));
        model.addAttribute("recipesBar", recipesBar);
        model.addAttribute("categoryName", category.getName());
        return "user/userRecipeBarList";
    }
    @GetMapping("/details/{id}")
    public String userRecipeBarDetails(@PathVariable Long id, Model model) {
        RecipeBar recipeBar = recipeBarRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid recipeBar Id:" + id));
        model.addAttribute("recipeBar", recipeBar);
        return "user/userRecipeBarDetails";
    }
}
