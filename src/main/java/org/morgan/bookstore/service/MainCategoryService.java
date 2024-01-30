package org.morgan.bookstore.service;

import lombok.RequiredArgsConstructor;
import org.morgan.bookstore.dto.category.MainCategoryRequest;
import org.morgan.bookstore.entity.MainCategory;
import org.morgan.bookstore.repository.MainCategoryRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MainCategoryService {

    private final MainCategoryRepository mainCategoryRepository;
//
//    public MainCategory addCategory(MainCategoryRequest mainCategory) {
//
//    }
}
