package vn.hcmute.eatandorder.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.hcmute.eatandorder.data.api.ApiService;
import vn.hcmute.eatandorder.data.api.RetrofitClient;
import vn.hcmute.eatandorder.data.model.RegisterRequest;
import vn.hcmute.eatandorder.databinding.ActivityOtpBinding;

public class OtpActivity extends AppCompatActivity {

    private ActivityOtpBinding binding;
    private ApiService apiService;

    private String username, password, fullName, email, phone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        apiService = RetrofitClient.getApiService();

        // Lấy data từ RegisterActivity
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");
        fullName = intent.getStringExtra("fullName");
        email = intent.getStringExtra("email");
        phone = intent.getStringExtra("phone");

        binding.btnConfirmOtp.setOnClickListener(v -> confirmOtpAndRegister());
    }

    private void confirmOtpAndRegister() {
        String otp = binding.edtOtp.getText().toString().trim();

        // OTP demo: 123456
        if (!"123456".equals(otp)) {
            Toast.makeText(this, "OTP không đúng, thử lại 123456", Toast.LENGTH_SHORT).show();
            return;
        }

        RegisterRequest request = new RegisterRequest(
                username, password, fullName, email, phone
        );

        Call<String> call = apiService.register(request);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(OtpActivity.this,
                            response.body(),
                            Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(OtpActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    int code = response.code();
                    String errorBody = "";
                    if (response.errorBody() != null) {
                        try {
                            errorBody = response.errorBody().string();
                        } catch (IOException e) {
                            Log.e("API_REGISTER", "Error parsing error body", e);
                        }
                    }

                    String errorMessage = errorBody.isEmpty() ? "Đăng ký thất bại. Mã lỗi: " + code : errorBody;
                    Log.e("API_REGISTER",
                            "code = " + code + ", error = " + errorBody);

                    Toast.makeText(OtpActivity.this,
                            errorMessage,
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Toast.makeText(OtpActivity.this,
                        "Lỗi kết nối: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
                Log.e("API_REGISTER", "Failure: " + t.getMessage(), t);
            }
        });
    }
}
