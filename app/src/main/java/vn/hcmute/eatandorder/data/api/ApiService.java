package vn.hcmute.eatandorder.data.api;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import vn.hcmute.eatandorder.data.model.AuthResponse;
import vn.hcmute.eatandorder.data.model.Category;
import vn.hcmute.eatandorder.data.model.LoginRequest;
import vn.hcmute.eatandorder.data.model.Product;
import vn.hcmute.eatandorder.data.model.RegisterRequest;
import vn.hcmute.eatandorder.data.model.User;

public interface ApiService {

    // ĐỔI từ "categories" thành "api/categories"
    @GET("api/categories")
    Call<List<Category>> getCategories();

    @POST("auth/register")
    Call<ResponseBody> register(@Body RegisterRequest request);

    @POST("auth/login")
    Call<AuthResponse> login(@Body LoginRequest request);

    // sau này cần thêm API khác thì tiếp tục khai báo ở đây
}

