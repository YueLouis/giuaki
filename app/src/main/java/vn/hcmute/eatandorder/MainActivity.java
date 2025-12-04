package vn.hcmute.eatandorder;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.hcmute.eatandorder.data.api.ApiService;
import vn.hcmute.eatandorder.data.api.RetrofitClient;
import vn.hcmute.eatandorder.data.model.AuthResponse;
import vn.hcmute.eatandorder.data.model.Category;
import vn.hcmute.eatandorder.data.model.Product;
import vn.hcmute.eatandorder.databinding.ActivityMainBinding;
import vn.hcmute.eatandorder.ui.product.CategoryAdapter;
import vn.hcmute.eatandorder.ui.product.ProductsAdapter;
import vn.hcmute.eatandorder.util.PrefManager;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ApiService apiService;
    private PrefManager prefManager;

    private CategoryAdapter categoryAdapter;
    private ProductsAdapter productsAdapter;
    private final List<Category> categoryList = new ArrayList<>();
    private final List<Product> productList = new ArrayList<>();

    private Category selectedCategory;
    private boolean isLoadingProducts = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        apiService = RetrofitClient.getApiService();
        prefManager = new PrefManager(this);

        loadUserInfo();
        setupCategoriesRecyclerView();
        setupProductsRecyclerView();
        loadCategories();
    }

    private void setupCategoriesRecyclerView() {
        binding.rvCategory.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );
        categoryAdapter = new CategoryAdapter(this, categoryList);
        categoryAdapter.setOnCategoryClickListener(new CategoryAdapter.OnCategoryClickListener() {
            @Override
            public void onClick(Category category) {
                onCategorySelected(category);
            }
        });
        binding.rvCategory.setAdapter(categoryAdapter);
    }

    private void setupProductsRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        binding.rvProducts.setLayoutManager(gridLayoutManager);
        productsAdapter = new ProductsAdapter(this, productList);
        binding.rvProducts.setAdapter(productsAdapter);

        // Lazy loading: Khi scroll đến cuối, load thêm sản phẩm
        binding.rvProducts.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                    // Nếu đã scroll gần đến cuối và đang có category được chọn
                    if (!isLoadingProducts && selectedCategory != null) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount - 3
                                && firstVisibleItemPosition >= 0) {
                            // Load thêm sản phẩm (trong trường hợp này API trả về tất cả, 
                            // nhưng có thể implement pagination sau)
                            // loadMoreProducts();
                        }
                    }
                }
            }
        });
    }

    private void onCategorySelected(Category category) {
        selectedCategory = category;
        Log.d("MainActivity", "Category selected: " + category.getName() + " (ID: " + category.getId() + ")");
        loadProductsByCategory(category.getId());
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

                    for (Category c : list) {
                        Log.d("MainActivity",
                                "Category: " + c.getId() + " - " + c.getName() + " - Image: " + c.getImageUrl());
                    }

                    categoryList.clear();
                    categoryList.addAll(list);
                    categoryAdapter.updateData(list);
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

    private void loadProductsByCategory(long categoryId) {
        isLoadingProducts = true;
        productList.clear();
        productsAdapter.notifyDataSetChanged();

        apiService.getProductsByCategory(categoryId).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call,
                                   Response<List<Product>> response) {
                isLoadingProducts = false;

                if (response.isSuccessful() && response.body() != null) {
                    List<Product> products = response.body();

                    // Sắp xếp theo giá tăng dần
                    Collections.sort(products, new Comparator<Product>() {
                        @Override
                        public int compare(Product p1, Product p2) {
                            Double price1 = p1.getPrice();
                            Double price2 = p2.getPrice();
                            if (price1 == null && price2 == null) return 0;
                            if (price1 == null) return 1;
                            if (price2 == null) return -1;
                            return price1.compareTo(price2);
                        }
                    });

                    productList.clear();
                    productList.addAll(products);
                    productsAdapter.updateData(productList);

                    // Hiển thị section products và cập nhật header
                    binding.layoutProductsSection.setVisibility(View.VISIBLE);
                    if (selectedCategory != null) {
                        String headerText = selectedCategory.getName() + ": " + products.size();
                        binding.tvProductsHeader.setText(headerText);
                    }

                    Log.d("MainActivity", "Loaded " + products.size() + " products for category " + categoryId);
                } else {
                    Toast.makeText(MainActivity.this,
                            "Load sản phẩm thất bại",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                isLoadingProducts = false;
                Toast.makeText(MainActivity.this,
                        "Lỗi kết nối: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
                Log.e("MainActivity", "Error loading products", t);
            }
        });
    }
}
