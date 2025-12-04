package vn.hcmute.eatandorder.data.model;

import com.google.gson.annotations.SerializedName;

public class Category {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    // API trả về "imageUrl" (camelCase) không phải "image_url" (snake_case)
    @SerializedName("imageUrl")
    private String imageUrl;

    @SerializedName("description")
    private String description;

    public Category() {
    }

    public Category(int id, String name, String imageUrl, String description) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    // Giữ method getImage() để tương thích
    public String getImage() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setImage(String image) {
        this.imageUrl = image;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}