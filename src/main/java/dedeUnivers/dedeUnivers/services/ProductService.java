package dedeUnivers.dedeUnivers.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import dedeUnivers.dedeUnivers.entities.*;
import dedeUnivers.dedeUnivers.errors.ResourceNotFoundException;
import dedeUnivers.dedeUnivers.projections.ProductProjection;
import dedeUnivers.dedeUnivers.repositories.*;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductOptionRepository productOptionRepository;

    @Autowired
    private ProductImagesRepository productImagesRepository;

    @Autowired
    private PromotionRepository promotionRepository;


    @Autowired
    private SubCategoryRepository subCategoryRepository;

    public Product save(Product produit) {
        return productRepository.save(produit);
    }

    public Product findById(Integer id) {
        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }
//
//    public Optional<Product> getProductWithAllAttribute(Integer id){
//        return productRepository.findByIdWithAllAttribute(id);
//    }


    public Set<Product> getProductsWithImagesBySubCategory(String nameSubCategory) {
        return productRepository.findBySubCategory_NameSubCategoryWithImages(nameSubCategory);
    }


    public Set<Product> getProductsWithImagesBySubCategoryId(Integer subCategoryId) {
        return productRepository.findBySubCategoryIdWithImages(subCategoryId);
    }

    public List<Product> getProductsByIdSubCategory(Integer idSubCategory){
        return productRepository.findBySubCategory_IdOrderByProductImagesAsc(idSubCategory);
    }


//
//    public List<ProductProjection> getProductsBySubCategoryName(String nameSubCategory) {
//        return productRepository.findProductsBySubCategoryName(nameSubCategory);
//    }



//    public Map<Product, List<ProductImages>> getProductImagesMapBySubCategoryId(Integer subCategoryId) {
//        List<Product> products = productRepository.findBySubCategoryId(subCategoryId);
//        Map<Product, List<ProductImages>> productImagesMap = new HashMap<>();
//
//        for (Product product : products) {
//            List<ProductImages> productImages = productImagesRepository.findByProductId(product.getId());
//            productImagesMap.put(product, productImages);
//        }
//
//        return productImagesMap;
//    }


//
//    public ProductProjection getProductProjectionById(Integer id) {
//        return productRepository.findProductProjectionById(id);
//    }

    public ProductProjection getProductProjectionById(Integer id) {
        return productRepository.findProductById(id);
    }


    public List<ProductProjection> getTop10ProductsBySubCategoryId(Integer subCategoryId) {
        Pageable pageable = PageRequest.of(0, 10);
        return productRepository.findBySubCategoryId(subCategoryId, pageable);
    }

    public List<ProductProjection> getProductsByNameSubCateg(String nameSubCategory){
        return productRepository.findBySubCategoryName(nameSubCategory);
    }


    public List<ProductProjection> getProductsBySubCategoryId(Integer subCategoryId) {
        return productRepository.findBySubCategoryId(subCategoryId);
    }


    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    public List<Product> getByName(String nameProduct){
        return productRepository.findByNameProductContaining(nameProduct);
    }

    public List<Product> getAllProductBySubCategory(SubCategory subCategory){
        return productRepository.findBySubCategoryOrderBySubCategory_IdAsc(subCategory);
    }


    public Product getByNameProduct(String nameProduct){
        return productRepository.findByNameProduct(nameProduct);
    }


    public List<Product> getByNameOrderAsc(String nameProduit){
        return productRepository.findByNameProductOrderByIdAsc(nameProduit);
    }


    public List<Product> getByNameOrderDesc(String nameProduit){
        return productRepository.findByNameProductOrderByIdDesc(nameProduit);
    }



    public Set<Product> getAllProductsWithImage(){
        return productRepository.findAllProductsWithImages();
    }



//    public List<Product> getByPriceAsc(Float price){
//        return productRepository.findByPriceOrderByIdAsc(price);
//    }


//    public List<Product> getByPriceDesc(Float price){
//        return productRepository.findByPriceOrderByIdDesc(price);
//    }


    public void remove(Integer id) {
        // Supprimez le produit avec l'ID spécifié (utilisez votre propre logique ici)
        productRepository.deleteById(id);
    }

    public Integer countBySubCategory(Integer subCategoryId){
        return productRepository.countBySubCategoryId(subCategoryId);
    }


    public List<ProductProjection> getProductsBySubCategoryId(int subCategoryId) {
        // Utilisation de la méthode du repository pour récupérer les produits
        return productRepository.findBySubCategoryId(subCategoryId);
    }


    public Map<SubCategory, List<List<ProductImages>>> getProductImagesGroupedBySubCategory(int subCategoryId) {
        // Récupérer tous les produits avec leurs images filtrées
        List<Product> products = productRepository.findProductsBySubCategoryAndImageType(subCategoryId);

        // Regrouper les images par sous-catégorie
        Map<SubCategory, List<List<ProductImages>>> result = new HashMap<>();

        // Trouver la sous-catégorie correspondante (pour afficher dans la map)
        SubCategory subCategory = products.get(0).getSubCategory(); // Supposons que tous les produits ont la même sous-catégorie

        List<List<ProductImages>> groupedImages = new ArrayList<>();

        // Regrouper les images en 4 groupes de 4 images
        for (Product product : products) {
            List<ProductImages> filteredImages = product.getProductImages().stream()
                    .filter(pi -> pi.getTypeProductImages().equals("small carousel") || pi.getTypeProductImages().equals("big carousel"))
                    .collect(Collectors.toList());

            // Regrouper les images en groupes de 4
            for (int i = 0; i < filteredImages.size(); i += 4) {
                int end = Math.min(i + 4, filteredImages.size());
                List<ProductImages> group = filteredImages.subList(i, end);
                groupedImages.add(group);
            }
        }

        result.put(subCategory, groupedImages);
        return result;
    }


    public List<Product> getTop4ProductsBySubCategory(int subCategoryId) {
        // Crée un Pageable qui limite les résultats à 4 éléments
        Pageable pageable = PageRequest.of(0, 5); // 0 : page (première page), 4 : taille de la page
        return productRepository.findBySubCategoryId(subCategoryId, pageable);
    }



    public Product addProduct(Product product, Integer idSubCategory){
        SubCategory subCategory = subCategoryRepository.findById(idSubCategory).get();
        product.setSubCategory(subCategory);
        Product product1 = productRepository.save(product);
        ProductOption productOption = new ProductOption();
        productOption.setProduct(product1);
        productOptionRepository.save(productOption);
        return product1;
    }


}

