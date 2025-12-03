package vn.hcmute.eatandorder.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import vn.hcmute.eatandorder.model.Product;
import vn.hcmute.eatandorder.repository.ProductRepository;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ProductController {

    private final ProductRepository productRepository;

    @GetMapping
    public List<Product> getProducts(@RequestParam(required = false) Long categoryId) {
        if (categoryId == null) {
            // tất cả sản phẩm, tăng dần theo giá
            return productRepository.findAllByOrderByPriceAsc();
        }
        // sản phẩm theo category, tăng dần theo giá
        return productRepository.findByCategoryIdOrderByPriceAsc(categoryId);
    }
}
