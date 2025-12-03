package vn.hcmute.eatandorder.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "image_url")
    private String imageUrl;

    private String name;
    private Double price;
    private Integer sold;

    // Cách đơn giản: lưu luôn id category
    @Column(name = "category_id")
    private Long categoryId;

    // Nếu muốn quan hệ ManyToOne thật sự:
    // @ManyToOne
    // @JoinColumn(name = "category_id")
    // private Category category;
}
