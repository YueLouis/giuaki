package vn.hcmute.eatandorder.data.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import vn.hcmute.eatandorder.data.model.AuthResponse;
import vn.hcmute.eatandorder.data.model.Category;
import vn.hcmute.eatandorder.data.model.LoginRequest;
import vn.hcmute.eatandorder.data.model.Product;
import vn.hcmute.eatandorder.data.model.RegisterRequest;
import vn.hcmute.eatandorder.data.model.User;

public interface ApiService {

    // 1. Lấy tất cả categories
    @GET("categories")
    Call<List<Category>> getCategories();

    // 2. Lấy tất cả products
    @GET("products")
    Call<List<Product>> getProducts();

    // 3. Lấy products theo categoryId
    @GET("products")
    Call<List<Product>> getProductsByCategory(
            @Query("categoryId") long categoryId
    );

    // 4. Đăng ký
    @POST("auth/register")
    Call<String> register(@Body RegisterRequest body);

    // 5. Đăng nhập
    @POST("auth/login")
    Call<AuthResponse> login(@Body LoginRequest body);

    // 6. Profile
    @GET("auth/profile/{id}")
    Call<User> getProfile(@Path("id") long id);
}
