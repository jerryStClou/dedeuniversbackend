package dedeUnivers.dedeUnivers.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dedeUnivers.dedeUnivers.entities.*;
import dedeUnivers.dedeUnivers.repositories.CategoryRepository;
import dedeUnivers.dedeUnivers.repositories.CategorySubCategoryRepository;
import dedeUnivers.dedeUnivers.repositories.SubCategoryRepository;

import java.util.List;

@Service
public class CategorySubCategoryService {

    @Autowired
    CategorySubCategoryRepository categorySubCategoryRepository;

    @Autowired
    CategoryRepository categoryRepository;


    @Autowired
    SubCategoryRepository subCategoryRepository;

    public CategorySubCategory save(CategorySubCategory categorySubCategory) {
        return categorySubCategoryRepository.save(categorySubCategory);
    }


    public List<CategorySubCategory> getAll(){
        return categorySubCategoryRepository.findAll();
    }

    public CategorySubCategory addCategorySubCategory(Integer idCategory, Integer idSubCategory){
        Category category = categoryRepository.findById(idCategory).get();
        SubCategory subCategory = subCategoryRepository.findById(idSubCategory).get();
        CategorySubCategory categorySubCategory = new CategorySubCategory();
        categorySubCategory.setCategory(category);
        categorySubCategory.setSubCategory(subCategory);
        return categorySubCategoryRepository.save(categorySubCategory);
    }

    public CategorySubCategory updateCategorySubCategory(Integer idCategorySubCategory,Integer idCategory, Integer idSubCategory){
        Category category = categoryRepository.findById(idCategory).get();
        SubCategory subCategory = subCategoryRepository.findById(idSubCategory).get();
        CategorySubCategory categorySubCategory = categorySubCategoryRepository.findById(idCategorySubCategory).get();
        categorySubCategory.setSubCategory(subCategory);
        categorySubCategory.setCategory(category);
        return categorySubCategoryRepository.save(categorySubCategory);
    }

    public void deleteCategorySubCategory(Integer idCategorySubCategory){
        categorySubCategoryRepository.deleteById(idCategorySubCategory);
    }
}
