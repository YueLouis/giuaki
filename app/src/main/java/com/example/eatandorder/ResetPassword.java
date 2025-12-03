package com.example.eatandorder;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPassword extends AppCompatActivity {

    private EditText edtOtp, edtNewPassword;
    private Button btnReset;
    private ProgressBar progressBar;

    private ApiService apiService;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        apiService = ApiClient.getClient().create(ApiService.class);

        email = getIntent().getStringExtra("email");

        edtOtp = findViewById(R.id.edtOtp);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        btnReset = findViewById(R.id.btnReset);
        progressBar = findViewById(R.id.progressBar);

        btnReset.setOnClickListener(v -> resetPassword());
    }

    private void resetPassword() {
        String otp = edtOtp.getText().toString().trim();
        String newPass = edtNewPassword.getText().toString().trim();

        if (otp.isEmpty() || newPass.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ OTP & mật khẩu!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        ResetPasswordRequest request = new ResetPasswordRequest(email, otp, newPass);

        apiService.resetPassword(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    Toast.makeText(ResetPassword.this, "Đặt lại mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                    finish(); // quay về Login
                } else {
                    Toast.makeText(ResetPassword.this, "OTP sai hoặc đã hết hạn!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ResetPassword.this, "Lỗi kết nối server!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
