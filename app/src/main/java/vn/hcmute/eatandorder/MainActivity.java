package vn.hcmute.eatandorder;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.hcmute.eatandorder.data.api.ApiService;
import vn.hcmute.eatandorder.data.api.RetrofitClient;
import vn.hcmute.eatandorder.data.model.AuthResponse;
import vn.hcmute.eatandorder.data.model.Category;
import vn.hcmute.eatandorder.databinding.ActivityMainBinding;
import vn.hcmute.eatandorder.ui.product.CategoryAdapter;
import vn.hcmute.eatandorder.util.PrefManager;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ApiService apiService;
    private PrefManager prefManager;

    private CategoryAdapter categoryAdapter;
    private final List<Category> categoryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        apiService = RetrofitClient.getApiService();
        prefManager = new PrefManager(this);

        loadUserInfo();

        // RecyclerView ngang
        binding.rvCategory.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );
        categoryAdapter = new CategoryAdapter(categoryList);
        binding.rvCategory.setAdapter(categoryAdapter);

        loadCategories();

    }


    private void loadUserInfo() {
        AuthResponse user = prefManager.getUser();
        if (user != null) {
            String fullName = user.getFullName();
            if (fullName != null && !fullName.isEmpty()) {
                binding.tvWelcome.setText("Hi! " + fullName);
            } else {
                binding.tvWelcome.setText("Hi!");
            }

            Glide.with(this)
                    .load(user.getAvatarUrl())
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round)
                    .into(binding.ivAvatar);
        } else {
            // Nếu không có user, giữ nguyên text mặc định "Hi!"
            binding.tvWelcome.setText("Hi!");
        }
    }


    private void loadCategories() {
        apiService.getCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call,
                                   Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Category> list = response.body();

                    // log cho chắc
                    for (Category c : list) {
                        Log.d("MainActivity",
                                "Category: " + c.getId() + " - " + c.getName());
                    }

                    // đổ dữ liệu vào RecyclerView
                    categoryList.clear();
                    categoryList.addAll(list);
                    categoryAdapter.notifyDataSetChanged();

                    Toast.makeText(MainActivity.this,
                            "Load " + list.size() + " categories",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this,
                            "Load category thất bại",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(MainActivity.this,
                        "Lỗi kết nối: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
