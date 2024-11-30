package dedeUnivers.dedeUnivers.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dedeUnivers.dedeUnivers.entities.CategorySubCategory;
import dedeUnivers.dedeUnivers.repositories.CategorySubCategoryRepository;

@Service
public class CategorySubCategoryService {

    @Autowired
    CategorySubCategoryRepository categorySubCategoryRepository;

    public CategorySubCategory save(CategorySubCategory categorySubCategory) {
        return categorySubCategoryRepository.save(categorySubCategory);
    }
}

