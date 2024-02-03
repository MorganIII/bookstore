package org.morgan.bookstore.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.morgan.bookstore.dto.CategoryDTO;
import org.morgan.bookstore.entity.Category;
import org.morgan.bookstore.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/category")
    @ResponseStatus(HttpStatus.CREATED)
    public String addCategory(@RequestBody @Valid CategoryDTO request) {
        return categoryService.addCategory(request);
    }

    @PutMapping("/category/{name}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDTO updateCategory(@PathVariable("name") String categoryName,@RequestBody @Valid CategoryDTO request) {
        return categoryService.updateCategory(categoryName, request);
    }

    @DeleteMapping("/category/{name}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable("name") String categoryName) {
        categoryService.deleteCategory(categoryName);
    }

    @GetMapping("category/{name}")
    public Category getCategory(@PathVariable("name") String categoryName) {
        return categoryService.getCategory(categoryName);
    }

    @GetMapping("/category")
    public List<Category> getAllSections() {
        return categoryService.getAllCategories();
    }


}
