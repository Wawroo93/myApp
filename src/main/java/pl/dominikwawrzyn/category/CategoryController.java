package pl.dominikwawrzyn.category;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import pl.dominikwawrzyn.productsBar.ProductsBar;
import pl.dominikwawrzyn.productsBar.ProductsBarRepository;
import pl.dominikwawrzyn.productsKitchen.ProductsKitchen;
import pl.dominikwawrzyn.productsKitchen.ProductsKitchenRepository;
import pl.dominikwawrzyn.recipeBar.RecipeBar;
import pl.dominikwawrzyn.recipeBar.RecipeBarRepository;
import pl.dominikwawrzyn.recipeKitchen.RecipeKitchen;
import pl.dominikwawrzyn.recipeKitchen.RecipeKitchenRepository;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/category")
public class CategoryController {

    private final CategoryRepository categoryRepository;
    private final ProductsBarRepository productsBarRepository;
    private final ProductsKitchenRepository productsKitchenRepository;
    private final RecipeKitchenRepository recipeKitchenRepository;
    private final RecipeBarRepository recipeBarRepository;

    @Autowired
    public CategoryController(CategoryRepository categoryRepository, ProductsBarRepository productsBarRepository, ProductsKitchenRepository productsKitchenRepository, RecipeKitchenRepository recipeKitchenRepository, RecipeBarRepository recipeBarRepository) {
        this.categoryRepository = categoryRepository;
        this.productsBarRepository = productsBarRepository;
        this.productsKitchenRepository = productsKitchenRepository;
        this.recipeKitchenRepository = recipeKitchenRepository;
        this.recipeBarRepository = recipeBarRepository;
    }


    //      CATEGORY RECIPE BAR


    @GetMapping("/addBar")
    public String showAddCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "admin/category/adminCategoryAddBar";
    }

    @PostMapping("/addBar")
    public String addCategory(@Valid Category category, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/category/adminCategoryAddBar";
        }
        category.setType("bar");
        categoryRepository.save(category);
        return "redirect:/admin/category/listBar";
    }

    @GetMapping("/listBar")
    public String listCategories(Model model) {
        List<Category> categories = categoryRepository.findByType("bar");
        model.addAttribute("categories", categories);
        return "admin/category/adminCategoryListBar";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Niepoprawne id kategorii: " + id));

        List<RecipeBar> recipesBar = recipeBarRepository.findByCategoryId(id);
        recipeBarRepository.deleteAll(recipesBar);

        categoryRepository.delete(category);
        return "redirect:/admin/category/listBar";
    }

    @GetMapping("/editBar/{id}")
    public String showEditCategoryForm(@PathVariable Long id, Model model) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + id));
        model.addAttribute("category", category);
        return "admin/category/adminCategoryEditBar";
    }

    @PostMapping("/editBar/{id}")
    public String updateCategory(@PathVariable Long id, @Valid @ModelAttribute Category category, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/category/adminCategoryEditBar";
        }
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + id));
        existingCategory.setName(category.getName());
        categoryRepository.save(existingCategory);
        return "redirect:/admin/category/listBar";
    }


    //      CATEGORY RECIPE KITCHEN


    @GetMapping("/addKitchen")
    public String showAddCategoryFormKitchen(Model model) {
        model.addAttribute("category", new Category());
        return "admin/category/adminCategoryAddKitchen";
    }

    @PostMapping("/addKitchen")
    public String addCategoryKitchen(@Valid Category category, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/category/adminCategoryAddKitchen";
        }
        category.setType("kitchen");
        categoryRepository.save(category);
        return "redirect:/admin/category/listKitchen";
    }

    @GetMapping("/listKitchen")
    public String listCategoriesKitchen(Model model) {
        List<Category> categories = categoryRepository.findByType("kitchen");
        model.addAttribute("categories", categories);
        return "admin/category/adminCategoryListKitchen";
    }

    @GetMapping("/deleteKitchen/{id}")
    public String deleteCategoryKitchen(@PathVariable Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Niepoprawne id kategorii: " + id));

        List<RecipeKitchen> recipesKitchen = recipeKitchenRepository.findByCategoryId(id);
        recipeKitchenRepository.deleteAll(recipesKitchen);

        categoryRepository.delete(category);
        return "redirect:/admin/category/listKitchen";
    }

    @GetMapping("/editKitchen/{id}")
    public String showEditCategoryFormKitchen(@PathVariable Long id, Model model) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + id));
        model.addAttribute("category", category);
        return "admin/category/adminCategoryEditKitchen";
    }

    @PostMapping("/editKitchen/{id}")
    public String updateCategoryKitchen(@PathVariable Long id, @Valid @ModelAttribute Category category, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/category/adminCategoryEditKitchen";
        }
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + id));
        existingCategory.setName(category.getName());
        categoryRepository.save(existingCategory);
        return "redirect:/admin/category/listKitchen";
    }


    // CATEGORY PRODTUCTS BAR


    @GetMapping("/addProductsBar")
    public String showAddCategoryFormProductsBar(Model model) {
        model.addAttribute("category", new Category());
        return "admin/category/adminCategoryAddProductsBar";
    }

    @PostMapping("/addProductsBar")
    public String addCategoryProductsBar(@Valid Category category, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/category/adminCategoryAddProductsBar";
        }
        category.setType("productsBar");
        categoryRepository.save(category);
        return "redirect:/admin/category/listProductsBar";
    }

    @GetMapping("/listProductsBar")
    public String listCategoriesProductsBar(Model model) {
        List<Category> categories = categoryRepository.findByType("productsBar");
        for (Category category : categories) {
            List<ProductsBar> productsBar = productsBarRepository.findByCategory(category);
            category.setProductsBar(productsBar);
        }
        model.addAttribute("categories", categories);
        return "admin/category/adminCategoryListProductsBar";
    }

    @GetMapping("/deleteProductsBar/{id}")
    public String deleteCategoryProductsBar(@PathVariable Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Niepoprawne id kategorii: " + id));

        List<ProductsBar> productsBar = productsBarRepository.findByCategoryId(id);
        productsBarRepository.deleteAll(productsBar);

        categoryRepository.delete(category);
        return "redirect:/admin/category/listProductsBar";
    }

    @GetMapping("/editProductsBar/{id}")
    public String showEditCategoryFormProductsBar(@PathVariable Long id, Model model) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + id));
        model.addAttribute("category", category);
        return "admin/category/adminCategoryEditProductsBar";
    }

    @PostMapping("/editProductsBar/{id}")
    public String updateCategoryProductsBar(@PathVariable Long id, @Valid @ModelAttribute Category category, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/category/adminCategoryEditProductsBar";
        }
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + id));
        existingCategory.setName(category.getName());
        categoryRepository.save(existingCategory);
        return "redirect:/admin/category/listProductsBar";
    }


    // CATEGORY PRODUCTS KITCHEN


    @GetMapping("/addProductsKitchen")
    public String showAddCategoryFormProductsKitchen(Model model) {
        model.addAttribute("category", new Category());
        return "admin/category/adminCategoryAddProductsKitchen";
    }

    @PostMapping("/addProductsKitchen")
    public String addCategoryProductsKitchen(@Valid Category category, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/category/adminCategoryAddProductsKitchen";
        }
        category.setType("productsKitchen");
        categoryRepository.save(category);
        return "redirect:/admin/category/listProductsKitchen";
    }

    @GetMapping("/listProductsKitchen")
    public String listCategoriesProductsKitchen(Model model) {
        List<Category> categories = categoryRepository.findByType("productsKitchen");
        for (Category category : categories) {
            List<ProductsKitchen> productsKitchen = productsKitchenRepository.findByCategory(category);
            category.setProductsKitchen(productsKitchen);
        }
        model.addAttribute("categories", categories);
        return "admin/category/adminCategoryListProductsKitchen";
    }

    @GetMapping("/deleteProductsKitchen/{id}")
    public String deleteCategoryProductsKitchen(@PathVariable Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Niepoprawne id kategorii: " + id));

        List<ProductsKitchen> productsKitchen = productsKitchenRepository.findByCategoryId(id);
        productsKitchenRepository.deleteAll(productsKitchen);

        categoryRepository.delete(category);
        return "redirect:/admin/category/listProductsKitchen";
    }

    @GetMapping("/editProductsKitchen/{id}")
    public String showEditCategoryFormProductsKitchen(@PathVariable Long id, Model model) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + id));
        model.addAttribute("category", category);
        return "admin/category/adminCategoryEditProductsKitchen";
    }

    @PostMapping("/editProductsKitchen/{id}")
    public String updateCategoryProductsKitchen(@PathVariable Long id, @Valid @ModelAttribute Category category, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/category/adminCategoryEditProductsKitchen";
        }
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + id));
        existingCategory.setName(category.getName());
        categoryRepository.save(existingCategory);
        return "redirect:/admin/category/listProductsKitchen";
    }
}
