package org.morgan.bookstore.repository;

import org.morgan.bookstore.entity.MainCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MainCategoryRepository extends JpaRepository<MainCategory,Integer> {


}
