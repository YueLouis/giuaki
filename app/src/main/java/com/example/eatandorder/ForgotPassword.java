package com.example.eatandorder;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassword extends AppCompatActivity {

    private EditText edtEmail;
    private Button btnSendOtp;
    private ProgressBar progressBar;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        apiService = ApiClient.getClient().create(ApiService.class);

        edtEmail = findViewById(R.id.edtEmail);
        btnSendOtp = findViewById(R.id.btnSendOtp);
        progressBar = findViewById(R.id.progressBar);

        btnSendOtp.setOnClickListener(v -> sendOtp());
    }

    private void sendOtp() {
        String email = edtEmail.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        apiService.forgotPassword(new ForgotPasswordRequest(email))
                .enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        progressBar.setVisibility(View.GONE);

                        if (response.isSuccessful()) {
                            Toast.makeText(ForgotPassword.this, "OTP đã gửi đến email!", Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(ForgotPassword.this, ResetPassword.class);
                            i.putExtra("email", email);
                            startActivity(i);
                        } else {
                            Toast.makeText(ForgotPassword.this, "Email không tồn tại!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ForgotPassword.this, "Lỗi kết nối server!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

