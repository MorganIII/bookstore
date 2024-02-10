package org.morgan.bookstore.repository;

import org.morgan.bookstore.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,String> {

    Optional<Category> findCategoryByNameAndSectionName(String categoryName, String sectionName);

    @Query("SELECT c.name FROM Category c WHERE c.section.name = :sectionName")
    List<String> findCategoryNamesBySectionName(String sectionName);

    @Query("SELECT c.name FROM Category c")
    List<String> getAllCategoryNames();
}
