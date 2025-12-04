package vn.hcmute.eatandorder.data.model;

import com.google.gson.annotations.SerializedName;

public class Product {
    @SerializedName("id")
    private Long id;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("name")
    private String name;

    @SerializedName("imageUrl")
    private String imageUrl;

    @SerializedName("price")
    private Double price;

    @SerializedName("sold")
    private Integer sold;

    @SerializedName("categoryId")
    private Long categoryId;

    public Long getId() { return id; }
    public String getCreatedAt() { return createdAt; }
    public String getName() { return name; }
    public String getImageUrl() { return imageUrl; }
    public Double getPrice() { return price; }
    public Integer getSold() { return sold; }
    public Long getCategoryId() { return categoryId; }

    public void setId(Long id) { this.id = id; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public void setName(String name) { this.name = name; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setPrice(Double price) { this.price = price; }
    public void setSold(Integer sold) { this.sold = sold; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
}
