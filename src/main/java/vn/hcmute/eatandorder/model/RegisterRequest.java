package vn.hcmute.eatandorder.model;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String fullName;
    private String phone;
    private String avatarUrl;
}
