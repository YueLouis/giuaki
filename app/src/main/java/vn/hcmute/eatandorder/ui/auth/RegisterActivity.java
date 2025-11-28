package vn.hcmute.eatandorder.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import vn.hcmute.eatandorder.data.model.RegisterRequest;
import vn.hcmute.eatandorder.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnNextOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToOtp();
            }
        });
    }

    private void goToOtp() {
        String fullName = binding.edtFullName.getText().toString().trim();
        String username = binding.edtUsernameReg.getText().toString().trim();
        String password = binding.edtPasswordReg.getText().toString().trim();
        String email = binding.edtEmail.getText().toString().trim();
        String phone = binding.edtPhone.getText().toString().trim();

        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Nhập họ tên, username, password", Toast.LENGTH_SHORT).show();
            return;
        }

        RegisterRequest request = new RegisterRequest(
                username, password, fullName, email, phone
        );

        // Gửi object này sang OtpActivity
        Intent intent = new Intent(RegisterActivity.this, OtpActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("password", password);
        intent.putExtra("fullName", fullName);
        intent.putExtra("email", email);
        intent.putExtra("phone", phone);
        startActivity(intent);
    }
}
