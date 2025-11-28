package vn.hcmute.eatandorder.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import okhttp3.ResponseBody;
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

        binding.btnConfirmOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmOtpAndRegister();
            }
        });
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

        Call<ResponseBody> call = apiService.register(request);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(OtpActivity.this,
                            "Đăng ký thành công, mời đăng nhập",
                            Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(OtpActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    int code = response.code();
                    String errorBody = "";
                    try {
                        if (response.errorBody() != null) {
                            errorBody = response.errorBody().string();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    android.util.Log.e("API_REGISTER",
                            "code = " + code + ", error = " + errorBody);

                    Toast.makeText(OtpActivity.this,
                            "Đăng ký thất bại. Mã lỗi: " + code,
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(OtpActivity.this,
                        "Lỗi kết nối: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
