package vn.hcmute.eatandorder.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.hcmute.eatandorder.R;
import vn.hcmute.eatandorder.data.api.ApiService;
import vn.hcmute.eatandorder.data.api.RetrofitClient;
import vn.hcmute.eatandorder.data.model.Category;

public class CategoryFragment extends Fragment {

    private static final String TAG = "CategoryFragment";

    private RecyclerView recyclerViewCategories;
    private ProgressBar progressBar;
    private TextView tvError;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList;
    private ApiService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        try {
            initViews(view);
            setupRecyclerView();
            loadCategories();
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreateView: " + e.getMessage(), e);
            if (getContext() != null) {
                Toast.makeText(getContext(), "Lỗi khởi tạo: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        return view;
    }

    private void initViews(View view) {
        recyclerViewCategories = view.findViewById(R.id.recyclerViewCategories);
        progressBar = view.findViewById(R.id.progressBar);
        tvError = view.findViewById(R.id.tvError);

        if (recyclerViewCategories == null) {
            Log.e(TAG, "recyclerViewCategories is null - check layout file");
            return;
        }

        categoryList = new ArrayList<>();

        try {
            apiService = RetrofitClient.getApiService();
        } catch (Exception e) {
            Log.e(TAG, "Error creating ApiService: " + e.getMessage(), e);
            showError("Lỗi kết nối API");
        }
    }

    private void setupRecyclerView() {
        if (recyclerViewCategories == null || getContext() == null) {
            Log.e(TAG, "Cannot setup RecyclerView - null reference");
            return;
        }

        try {
            LinearLayoutManager layoutManager = new LinearLayoutManager(
                    getContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
            );
            recyclerViewCategories.setLayoutManager(layoutManager);
            recyclerViewCategories.setHasFixedSize(true);

            categoryAdapter = new CategoryAdapter(getContext(), categoryList, new CategoryAdapter.OnCategoryClickListener() {
                @Override
                public void onCategoryClick(Category category) {
                    handleCategoryClick(category);
                }
            });

            recyclerViewCategories.setAdapter(categoryAdapter);
        } catch (Exception e) {
            Log.e(TAG, "Error setting up RecyclerView: " + e.getMessage(), e);
            showError("Lỗi hiển thị danh sách");
        }
    }

    private void loadCategories() {
        if (apiService == null) {
            Log.e(TAG, "ApiService is null, cannot load categories");
            showError("Lỗi kết nối");
            return;
        }

        showLoading(true);

        try {
            Call<List<Category>> call = apiService.getCategories();
            call.enqueue(new Callback<List<Category>>() {
                @Override
                public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                    if (!isAdded() || getContext() == null) {
                        Log.w(TAG, "Fragment not attached, skipping response");
                        return;
                    }

                    showLoading(false);

                    if (response.isSuccessful() && response.body() != null) {
                        List<Category> categories = response.body();
                        Log.d(TAG, "Loaded " + categories.size() + " categories");

                        categoryList.clear();
                        categoryList.addAll(categories);

                        if (categoryAdapter != null) {
                            categoryAdapter.notifyDataSetChanged();
                        }

                        if (tvError != null) {
                            tvError.setVisibility(View.GONE);
                        }
                        if (recyclerViewCategories != null) {
                            recyclerViewCategories.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Log.e(TAG, "Response not successful: " + response.code());
                        showError("Không thể tải danh mục (Code: " + response.code() + ")");
                    }
                }

                @Override
                public void onFailure(Call<List<Category>> call, Throwable t) {
                    if (!isAdded() || getContext() == null) {
                        Log.w(TAG, "Fragment not attached, skipping failure");
                        return;
                    }

                    Log.e(TAG, "API call failed: " + t.getMessage(), t);
                    showLoading(false);
                    showError("Lỗi kết nối: " + t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error making API call: " + e.getMessage(), e);
            showLoading(false);
            showError("Lỗi gọi API");
        }
    }

    private void showLoading(boolean isLoading) {
        if (!isAdded() || getContext() == null) return;

        try {
            if (progressBar != null) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }
            if (recyclerViewCategories != null) {
                recyclerViewCategories.setVisibility(isLoading ? View.GONE : View.VISIBLE);
            }
            if (tvError != null && isLoading) {
                tvError.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error in showLoading: " + e.getMessage(), e);
        }
    }

    private void showError(String message) {
        if (!isAdded() || getContext() == null) return;

        try {
            if (tvError != null) {
                tvError.setText(message);
                tvError.setVisibility(View.VISIBLE);
            }
            if (recyclerViewCategories != null) {
                recyclerViewCategories.setVisibility(View.GONE);
            }

            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Error in showError: " + e.getMessage(), e);
        }
    }

    private void handleCategoryClick(Category category) {
        if (!isAdded() || getContext() == null) return;

        try {
            Log.d(TAG, "Category clicked: " + category.getName());
            Toast.makeText(getContext(), "Selected: " + category.getName(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Error handling category click: " + e.getMessage(), e);
        }
    }

    public void refreshCategories() {
        if (isAdded() && getContext() != null) {
            loadCategories();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recyclerViewCategories = null;
        progressBar = null;
        tvError = null;
        categoryAdapter = null;
        apiService = null;
    }
}