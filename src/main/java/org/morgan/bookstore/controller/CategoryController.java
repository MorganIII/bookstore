package org.morgan.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.morgan.bookstore.request.CategoryRequest;
import org.morgan.bookstore.model.Category;
import org.morgan.bookstore.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Category", description = "The category API")
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "Add category", description = "Add category")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Category added"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "409", description = "Category already exists")
            }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String addCategory(@RequestBody @Valid CategoryRequest request) {
        return categoryService.addCategory(request);
    }

    @Operation(summary = "Update category", description = "Update category")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Category updated"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "404", description = "Category not found")
            }
    )
    @PutMapping("/{name}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryRequest updateCategory(@PathVariable("name") String categoryName, @RequestBody @Valid CategoryRequest request) {
        return categoryService.updateCategory(categoryName, request);
    }

    @Operation(summary = "Delete category", description = "Delete category from the system")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "Category deleted"),
                    @ApiResponse(responseCode = "404", description = "Category not found")
            }
    )
    @DeleteMapping("/{name}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable("name") String categoryName) {
        categoryService.deleteCategory(categoryName);
    }

    @Operation(summary = "Get category", description = "Get category by name")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Category found"),
                    @ApiResponse(responseCode = "404", description = "Category not found")
            }
    )
    @GetMapping("/{name}")
    public Category getCategory(@PathVariable("name") String categoryName) {
        return categoryService.getCategory(categoryName);
    }

    @Operation(summary = "Get all categories", description = "Get all categories")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Categories returned successfully"),
            }
    )
    @GetMapping("/all")
    public List<String> getAllSections() {
        return categoryService.getAllCategories();
    }

    @Operation(summary = "Get categories by section", description = "Get categories by section")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Categories returned successfully"),
                    @ApiResponse(responseCode = "404", description = "Section not found")
            }
    )
    @GetMapping("/section")
    public List<String> getCategoriesBySection(@RequestParam("sectionName") String sectionName) {
        return categoryService.getCategoriesBySection(sectionName);

    }

}
