package pl.dominikwawrzyn.recipeKitchen;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.dominikwawrzyn.category.Category;
import pl.dominikwawrzyn.category.CategoryRepository;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin/recipeKitchen")
public class RecipeKitchenController {

    private final RecipeKitchenRepository recipeKitchenRepository;
    private final CategoryRepository categoryRepository;

    public RecipeKitchenController(RecipeKitchenRepository recipeKitchenRepository, CategoryRepository categoryRepository) {
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
        return "admin/recipeKitchen/adminRecipeKitchenList";
    }
    @GetMapping("/add/{categoryId}")
    public String showAddRecipeKitchenForm(@PathVariable Long categoryId, Model model) {
        RecipeKitchen recipeKitchen = new RecipeKitchen();
        model.addAttribute("recipeKitchen", recipeKitchen);
        model.addAttribute("categoryId", categoryId);
        return "admin/recipeKitchen/adminRecipeKitchenAdd";
    }
    @PostMapping("/add/{categoryId}")
    public String addRecipeKitchen(@PathVariable Long categoryId, @ModelAttribute RecipeKitchen recipeKitchen) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + categoryId));
        recipeKitchen.setCategory(category);
        recipeKitchenRepository.save(recipeKitchen);
        return "redirect:/admin/recipeKitchen/" + categoryId;
    }
    @GetMapping("/edit/{id}")
    public String showEditRecipeKitchenForm(@PathVariable Long id, Model model) {
        RecipeKitchen recipeKitchen = recipeKitchenRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid RecipeBar Id:" + id));
        model.addAttribute("recipeKitchen", recipeKitchen);
        return "admin/recipeKitchen/adminRecipeKitchenEdit";
    }
    @PostMapping("/edit/{id}")
    public String updateRecipeKitchen(@PathVariable Long id, @ModelAttribute RecipeKitchen updatedRecipeKitchen) {
        RecipeKitchen existingRecipeKitchen = recipeKitchenRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid RecipeBar Id:" + id));
        existingRecipeKitchen.setName(updatedRecipeKitchen.getName());
        existingRecipeKitchen.setIngredients(updatedRecipeKitchen.getIngredients());
        existingRecipeKitchen.setPreparationTime(updatedRecipeKitchen.getPreparationTime());
        existingRecipeKitchen.setPreparation(updatedRecipeKitchen.getPreparation());
        RecipeKitchen savedRecipeKitchen = recipeKitchenRepository.save(existingRecipeKitchen);
        return "redirect:/admin/recipeKitchen/details/" + savedRecipeKitchen.getId();
    }
    @GetMapping("/details/{id}")
    public String showRecipeKitchenDetails(@PathVariable Long id, Model model) {
        RecipeKitchen recipeKitchen = recipeKitchenRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid RecipeBar Id:" + id));
        model.addAttribute("recipeKitchen", recipeKitchen);

        List<String> ingredients = Arrays.asList(recipeKitchen.getIngredients().split(","));
        model.addAttribute("ingredients", ingredients);

        List<String> preparationSteps = Arrays.asList(recipeKitchen.getPreparation().split("\\."));
        model.addAttribute("preparationSteps", preparationSteps);

        return "admin/recipeKitchen/adminRecipeKitchenDetails";
    }
    @GetMapping("/delete/{id}")
    public String deleteRecipeKitchen(@PathVariable Long id) {
        RecipeKitchen recipeKitchen = recipeKitchenRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid RecipeBar Id:" + id));
        Long categoryId = recipeKitchen.getCategory().getId();
        recipeKitchenRepository.delete(recipeKitchen);
        return "redirect:/admin/recipeKitchen/" + categoryId;
    }
}
