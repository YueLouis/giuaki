package vn.hcmute.eatandorder.data.model;

public class Product {
    private Long id;
    private String name;
    private String imageUrl;
    private Double price;
    private Long categoryId;

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getImageUrl() { return imageUrl; }
    public Double getPrice() { return price; }
    public Long getCategoryId() { return categoryId; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setPrice(Double price) { this.price = price; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
}
