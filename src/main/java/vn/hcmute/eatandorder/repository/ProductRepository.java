package vn.hcmute.eatandorder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.hcmute.eatandorder.model.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategoryIdOrderByPriceAsc(Long categoryId);

    List<Product> findAllByOrderByPriceAsc();
}

