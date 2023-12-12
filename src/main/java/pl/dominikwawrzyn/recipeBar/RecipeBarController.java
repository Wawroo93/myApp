package pl.dominikwawrzyn.recipeBar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.dominikwawrzyn.category.Category;
import pl.dominikwawrzyn.category.CategoryRepository;

import java.util.List;

@Controller
@RequestMapping("/admin/recipeBar")
public class RecipeBarController {
    private final RecipeBarRepository recipeBarRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public RecipeBarController(RecipeBarRepository recipeBarRepository, CategoryRepository categoryRepository) {
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
        return "admin/recipeBar/adminRecipeBarList";
    }
    @GetMapping("/add/{categoryId}")
    public String showAddRecipeBarForm(@PathVariable Long categoryId, Model model) {
        RecipeBar recipeBar = new RecipeBar();
        model.addAttribute("recipeBar", recipeBar);
        model.addAttribute("categoryId", categoryId);
        return "admin/recipeBar/adminRecipeBarAdd";
    }

    @PostMapping("/add/{categoryId}")
    public String addRecipeBar(@PathVariable Long categoryId, @ModelAttribute RecipeBar recipeBar) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + categoryId));
        recipeBar.setCategory(category);
        recipeBarRepository.save(recipeBar);
        return "redirect:/admin/recipeBar/" + categoryId;
    }
    @GetMapping("/edit/{id}")
    public String showEditRecipeBarForm(@PathVariable Long id, Model model) {
        RecipeBar recipeBar = recipeBarRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid RecipeBar Id:" + id));
        model.addAttribute("recipeBar", recipeBar);
        return "admin/recipeBar/adminRecipeBarEdit";
    }

    @PostMapping("/edit/{id}")
    public String updateRecipeBar(@PathVariable Long id, @ModelAttribute RecipeBar updatedRecipeBar) {
        RecipeBar existingRecipeBar = recipeBarRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid RecipeBar Id:" + id));
        existingRecipeBar.setName(updatedRecipeBar.getName());
        existingRecipeBar.setIngredients(updatedRecipeBar.getIngredients());
        existingRecipeBar.setPreparation(updatedRecipeBar.getPreparation());
        recipeBarRepository.save(existingRecipeBar);
        return "redirect:/admin/recipeBar/" + existingRecipeBar.getCategory().getId();
    }
    @GetMapping("/details/{id}")
    public String showRecipeBarDetails(@PathVariable Long id, Model model) {
        RecipeBar recipeBar = recipeBarRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid RecipeBar Id:" + id));
        model.addAttribute("recipeBar", recipeBar);
        return "admin/recipeBar/adminRecipeBarDetails";
    }
    @GetMapping("/delete/{id}")
    public String deleteRecipeBar(@PathVariable Long id) {
        RecipeBar recipeBar = recipeBarRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid RecipeBar Id:" + id));
        Long categoryId = recipeBar.getCategory().getId();
        recipeBarRepository.delete(recipeBar);
        return "redirect:/admin/recipeBar/" + categoryId;
    }
}
