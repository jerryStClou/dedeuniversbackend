package dedeUnivers.dedeUnivers.services;

import dedeUnivers.dedeUnivers.entities.Category;
import dedeUnivers.dedeUnivers.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public Category findById(Integer id) {
        return categoryRepository.findById(id).get();
    }


    public List<Category> getByName(String nameCategory){
        return categoryRepository.findByNameCategoryContaining(nameCategory);
    }

    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    public void remove(Integer id) {
        categoryRepository.deleteById(id);
    }
}
