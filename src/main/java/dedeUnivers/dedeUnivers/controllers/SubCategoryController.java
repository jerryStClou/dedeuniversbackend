package dedeUnivers.dedeUnivers.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import dedeUnivers.dedeUnivers.entities.Category;
import dedeUnivers.dedeUnivers.entities.CategorySubCategory;
import dedeUnivers.dedeUnivers.entities.SubCategory;
import dedeUnivers.dedeUnivers.services.CategoryService;
import dedeUnivers.dedeUnivers.services.CategorySubCategoryService;
import dedeUnivers.dedeUnivers.services.SubCategoryService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/subCategory")
@CrossOrigin(origins = "http://127.0.0.1:3000")
public class SubCategoryController {

    @Autowired
    SubCategoryService subCategoryService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    CategorySubCategoryService categorySubCategoryService;


    @GetMapping("/all")
    public ResponseEntity<List<SubCategory>> getAllSubCategories(@RequestParam(required = false) String nameSubCategory) {

        try {
            List<SubCategory> subCategories = new ArrayList<>();
            if(nameSubCategory == null || nameSubCategory.trim().isEmpty())
            {
                subCategoryService.getAllSubCategory().forEach(subCategories::add);
            }else {
                subCategoryService.getByName(nameSubCategory).forEach(subCategories::add);
            }
            if (subCategories.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            for(SubCategory subCategory: subCategories){
            }
            return new ResponseEntity<>(subCategories, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<SubCategory> getSubCategoryById(@PathVariable Integer id) {
        try{
            if (id <= 0) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            else {
                SubCategory subCategory = subCategoryService.findById(id);
//                produit.getImageProduits().forEach(p->p.setProduit(null));
                return new ResponseEntity<>(subCategory, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("all/{idCategory}")
    public ResponseEntity<List<SubCategory>> getSubCategoriesByCategoryId(@PathVariable Integer idCategory) {
        try{
            List<SubCategory> subCategories = subCategoryService.getSubCategoriesByCategoryId(idCategory);
            return new ResponseEntity<>(subCategories, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/add")
    public ResponseEntity<SubCategory> addSubCategory(@RequestBody SubCategory subCategory){
        try {
            if (subCategory == null
                    || subCategory.getNameSubCategory() == null || subCategory.getNameSubCategory().trim().isEmpty()) {

                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            SubCategory _subCategory = subCategoryService.save(subCategory);
            if (_subCategory != null) {
                return new ResponseEntity<>(_subCategory, HttpStatus.CREATED);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }




    @PutMapping("/update/{id}")
    public ResponseEntity<SubCategory>updateSubCategory(@RequestBody SubCategory subCategory,@PathVariable Integer id){
        try{

            SubCategory subCategory1 = subCategoryService.findById(id);
//
//            if (subCategory == null
//                    || subCategory.getNameSubCategory() == null || subCategory.getNameSubCategory().trim().isEmpty()
//                    || subCategory.getId() <= 0
//            ) {
//
//                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
//            }
            subCategory1.setNameSubCategory(subCategory.getNameSubCategory());
            subCategory1.setImageSubCategory(subCategory.getImageSubCategory());
            SubCategory subCategory2 = subCategoryService.save(subCategory1);
            return new ResponseEntity<>(subCategory2,HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> removeSubCategory(@PathVariable("id") Integer id) {

        try {
            if (id <= 0) {
                return new ResponseEntity<String>("Erreur : L'id du produit à supprimer doit être > 0 !",
                        HttpStatus.BAD_REQUEST);
            }
            subCategoryService.remove(id);
            return new ResponseEntity<String>("Suppression de la sous categorie  avec id = '" + id + "' effectuée avec succès.",
                    HttpStatus.ACCEPTED);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }



}

