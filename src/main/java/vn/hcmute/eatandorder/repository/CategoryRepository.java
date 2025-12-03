package vn.hcmute.eatandorder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.hcmute.eatandorder.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
