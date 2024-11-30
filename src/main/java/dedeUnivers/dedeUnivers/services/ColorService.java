package dedeUnivers.dedeUnivers.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dedeUnivers.dedeUnivers.entities.Color;
import dedeUnivers.dedeUnivers.entities.Product;
import dedeUnivers.dedeUnivers.entities.ProductOption;
import dedeUnivers.dedeUnivers.repositories.ColorRepository;
import dedeUnivers.dedeUnivers.repositories.ProductOptionRepository;
import dedeUnivers.dedeUnivers.repositories.ProductRepository;

import java.util.List;

@Service
public class ColorService {

    @Autowired
    private ColorRepository colorRepository;

    @Autowired
    private ProductOptionRepository productOptionRepository;

    @Autowired
    private ProductRepository productRepository;

    public Color save(Color color) {
        return colorRepository.save(color);
    }

    public Color findById(Integer id) {
        return colorRepository.findById(id).get();
    }

    public List<Color> getAllColor() {
        return colorRepository.findAll();
    }

//    //public List<Color> getAllByProductId(Integer productId) {
//        return colorRepository.findByProductId(productId);
//    }


    public void remove(Integer id) {
        colorRepository.deleteById(id);
    }

    public Color addColor(Color color, Integer idProductOption){
        ProductOption productOption = productOptionRepository.findById(idProductOption).get();
        Color color1 = colorRepository.save(color);
        productOption.setColor(color1);
        productOptionRepository.save(productOption);
        return color1;
    }
}
