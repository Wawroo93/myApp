package pl.dominikwawrzyn.productsKitchen;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.dominikwawrzyn.category.Category;
import pl.dominikwawrzyn.category.CategoryRepository;
import pl.dominikwawrzyn.productsBar.ProductsBar;

@Controller
@RequestMapping("/admin/productsKitchen")
public class ProductsKitchenController {
    private final ProductsKitchenRepository productsKitchenRepository;
    private final CategoryRepository categoryRepository;

    public ProductsKitchenController(ProductsKitchenRepository productsKitchenRepository, CategoryRepository categoryRepository) {
        this.productsKitchenRepository = productsKitchenRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/add/{categoryId}")
    public String showAddProductsKitchenForm(@PathVariable Long categoryId, Model model) {
        ProductsKitchen productsKitchen = new ProductsKitchen();
        model.addAttribute("productsKitchen", productsKitchen);
        model.addAttribute("categoryId", categoryId);
        return "admin/productsKitchen/adminProductsKitchenAdd";
    }
    @PostMapping("/add/{categoryId}")
    public String addProductsKitchen(@PathVariable Long categoryId, @Valid @ModelAttribute ProductsKitchen productsKitchen, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/productsKitchen/adminProductsKitchenAdd";
        }
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + categoryId));
        productsKitchen.setCategory(category);
        productsKitchenRepository.save(productsKitchen);
        return "redirect:/admin/category/listProductsKitchen";
    }
    @PostMapping("/updateQuantity/{productId}")
    public String updateQuantity(@PathVariable Long productId, @RequestParam("quantity") Integer quantity) {
        ProductsKitchen productsKitchen = productsKitchenRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + productId));
        productsKitchen.setQuantity(quantity);
        productsKitchenRepository.save(productsKitchen);
        return "redirect:/admin/category/listProductsKitchen";
    }

    @PostMapping("/delete/{productId}")
    public String deleteProductKitchen(@PathVariable Long productId) {
        ProductsKitchen productsKitchen = productsKitchenRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + productId));
        productsKitchenRepository.delete(productsKitchen);
        return "redirect:/admin/category/listProductsKitchen";
    }
}
