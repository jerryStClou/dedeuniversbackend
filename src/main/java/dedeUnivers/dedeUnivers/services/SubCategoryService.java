package dedeUnivers.dedeUnivers.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dedeUnivers.dedeUnivers.entities.Category;
import dedeUnivers.dedeUnivers.entities.SubCategory;
import dedeUnivers.dedeUnivers.entities.CategorySubCategory;
import dedeUnivers.dedeUnivers.repositories.CategorySubCategoryRepository;
import dedeUnivers.dedeUnivers.repositories.SubCategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubCategoryService {

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private CategorySubCategoryRepository CategorySubCategoryRepository;


    public SubCategory save(SubCategory subCategory) {
        return subCategoryRepository.save(subCategory);
    }


    public List<SubCategory> getSubCategoriesByCategoryId(Integer categoryId) {
        List<CategorySubCategory> categorySubCategories = CategorySubCategoryRepository.findByCategoryId(categoryId);
        return categorySubCategories.stream()
                .map(CategorySubCategory::getSubCategory)
                .collect(Collectors.toList());
    }



    public SubCategory findById(Integer id) {
        return subCategoryRepository.findById(id).get();
    }

    public List<SubCategory> getAllSubCategory() {
        return subCategoryRepository.findAll();
    }

    public List<SubCategory> getByName(String nameSubCategory){
        return subCategoryRepository.findByNameSubCategoryContaining(nameSubCategory);
    }
//
//    public List<SubCategory> getAllSubCategoryByCategory(String nameCategory){
//        return subCategoryRepository.findByCategoryName(nameCategory);
//    }

    public SubCategory getByNameSubCategory(String nameSubCategory){
        return subCategoryRepository.findByNameSubCategory(nameSubCategory);
    }

    public List<SubCategory> getByNameOrderAsc(String nameSubCategory){
        return subCategoryRepository.findByNameSubCategoryOrderByIdAsc(nameSubCategory);
    }

    public List<SubCategory> getByNameOrderDesc(String nameSubCategory){
        return subCategoryRepository.findByNameSubCategoryOrderByIdDesc(nameSubCategory);
    }

    public void remove(Integer id) {
        subCategoryRepository.deleteById(id);
    }

}

