package dedeUnivers.dedeUnivers.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import dedeUnivers.dedeUnivers.entities.Category;
import dedeUnivers.dedeUnivers.entities.Product;
import dedeUnivers.dedeUnivers.entities.SubCategory;

import java.util.List;

public interface SubCategoryRepository   extends JpaRepository<SubCategory, Integer> {

    List<SubCategory> findByNameSubCategoryContaining(String nameSubCategory);

    List<SubCategory> findByNameSubCategoryOrderByIdAsc(String nameSubCategory);


    List<SubCategory> findByNameSubCategoryOrderByIdDesc(String nameSubCategory);

    SubCategory findByNameSubCategory(String nameSubCategory);


}

