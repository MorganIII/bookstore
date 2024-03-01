package org.morgan.bookstore.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.morgan.bookstore.request.CategoryRequest;
import org.morgan.bookstore.model.Category;
import org.morgan.bookstore.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String addCategory(@RequestBody @Valid CategoryRequest request) {
        return categoryService.addCategory(request);
    }

    @PutMapping("/{name}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryRequest updateCategory(@PathVariable("name") String categoryName, @RequestBody @Valid CategoryRequest request) {
        return categoryService.updateCategory(categoryName, request);
    }

    @DeleteMapping("/{name}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable("name") String categoryName) {
        categoryService.deleteCategory(categoryName);
    }

    @GetMapping("/{name}")
    public Category getCategory(@PathVariable("name") String categoryName) {
        return categoryService.getCategory(categoryName);
    }

    @GetMapping("/all")
    public List<String> getAllSections() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/section")
    public List<String> getCategoriesBySection(@RequestParam("sectionName") String sectionName) {
        return categoryService.getCategoriesBySection(sectionName);

    }

}
