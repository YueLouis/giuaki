package vn.hcmute.eatandorder.data.model;

public class Category {
    private Long id;
    private String name;
    private String imageUrl;   // có thể null

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getImageUrl() { return imageUrl; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
