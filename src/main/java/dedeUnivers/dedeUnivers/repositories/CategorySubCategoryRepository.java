package dedeUnivers.dedeUnivers.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import dedeUnivers.dedeUnivers.entities.CategorySubCategory;
import java.util.List;

public interface CategorySubCategoryRepository   extends JpaRepository<CategorySubCategory, Integer> {
    List<CategorySubCategory> findByCategoryId(int categoryId);
}

