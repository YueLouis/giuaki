package vn.hcmute.eatandorder.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import vn.hcmute.eatandorder.R;

public class OtpActivity extends AppCompatActivity {
    private EditText edtOtp;
    private String usernameReceived;
    private static final String DB_URL = "jdbc:mysql://192.168.149.80:3306/eatorder";
    private static final String DB_USER = "admin";
    private static final String DB_PASS = "123";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        edtOtp = findViewById(R.id.edtOtp);
        Button btnVerify = findViewById(R.id.btnVerifyOtp);
        usernameReceived = getIntent().getStringExtra("KEY_USERNAME");
        btnVerify.setOnClickListener(v -> verifyOtp());
    }
    private void verifyOtp() {
        String otpInput = edtOtp.getText().toString().trim();
        if (otpInput.length() < 6) {
            Toast.makeText(this, "OTP phải 6 số", Toast.LENGTH_SHORT).show();
            return;
        }
        new Thread(() -> {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                String sql = "SELECT otp_code, otp_expiry FROM users WHERE username = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, usernameReceived);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    String dbOtp = rs.getString("otp_code");
                    long expiry = rs.getLong("otp_expiry");

                    if (!otpInput.equals(dbOtp)) {
                        runOnUiThread(() -> Toast.makeText(this, "Sai mã OTP!", Toast.LENGTH_SHORT).show());
                    }
                    else if (System.currentTimeMillis() > expiry) {
                        runOnUiThread(() -> Toast.makeText(this, "OTP hết hạn!", Toast.LENGTH_SHORT).show());
                    }
                    else {
                        String updateSql = "UPDATE users SET is_active = 1, otp_code = NULL WHERE username = ?";
                        PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                        updateStmt.setString(1, usernameReceived);
                        updateStmt.executeUpdate();
                        runOnUiThread(() -> {
                            Toast.makeText(this, "Kích hoạt thành công!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(OtpActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        });
                    }
                }
                conn.close();
            }
            catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Lỗi DB: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}