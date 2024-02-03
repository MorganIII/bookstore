package org.morgan.bookstore.service;


import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.morgan.bookstore.dto.CategoryDTO;
import org.morgan.bookstore.entity.Category;
import org.morgan.bookstore.entity.Section;
import org.morgan.bookstore.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final SectionService sectionService;

    public String addCategory(CategoryDTO request) {
        String categoryName = request.getCategoryName();
        String sectionName = request.getSectionName();
        String description = request.getDescription();
        validateCategoryDuplicate(categoryName);
        Section section = sectionService.getSection(sectionName);
        Category category = Category.builder()
                .name(categoryName)
                .section(section)
                .description(description)
                .build();
        categoryRepository.save(category);
        log.info("A category with name {} is added", categoryName);
        return categoryName;
    }

    public CategoryDTO updateCategory(String categoryName, CategoryDTO request) {
        Category category = getCategory(categoryName);
        Section section = sectionService.getSection(request.getSectionName());
        if(!categoryName.equals(request.getCategoryName())) {
            validateCategoryDuplicate(categoryName);
        }
        category.setSection(section);
        category.setName(request.getCategoryName());
        category.setDescription(request.getDescription());
        categoryRepository.save(category);
        log.info("A category with name {} has been updated", categoryName);
        return request;
    }

    public void deleteCategory(String categoryName) {
        Category category = getCategory(categoryName);
        categoryRepository.deleteById(categoryName);
        log.info("A category with name {} has been deleted", categoryName);
    }


    public Category getCategory(String categoryName) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryName);
        return categoryOptional.orElseThrow(()->new EntityNotFoundException(String.format("The category with name %s is not found", categoryName)));
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    private void validateCategoryDuplicate(String categoryName) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryName);
        if(optionalCategory.isPresent()) {
            throw new EntityExistsException(String.format("Category with name '%s' is already in the system.", categoryName));
        }
    }
}
