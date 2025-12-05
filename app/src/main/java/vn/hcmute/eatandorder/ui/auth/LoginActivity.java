package vn.hcmute.eatandorder.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import vn.hcmute.eatandorder.MainActivity;
import vn.hcmute.eatandorder.R;

public class LoginActivity extends AppCompatActivity {
    private EditText edtUser, edtPass;
    private Button btnLogin;
    private TextView tvGoToRegister;
    private static final String DB_URL = "jdbc:mysql://192.168.149.80:3306/eatorder";
    private static final String DB_USER = "admin";
    private static final String DB_PASS = "123";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtUser = findViewById(R.id.edtLoginUser);
        edtPass = findViewById(R.id.edtLoginPass);
        btnLogin = findViewById(R.id.btnLogin);
        tvGoToRegister = findViewById(R.id.tvGoToRegister);
        btnLogin.setOnClickListener(v -> checkLogin());
        tvGoToRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }
    private void checkLogin() {
        String u = edtUser.getText().toString().trim();
        String p = edtPass.getText().toString().trim();
        if (u.isEmpty() || p.isEmpty()) {
            Toast.makeText(this, "Nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }
        new Thread(() -> {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                String sql = "SELECT * FROM users WHERE username = ? AND password = ? AND is_active = 1";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, u);
                stmt.setString(2, p);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    });
                }
                else {
                    runOnUiThread(() -> Toast.makeText(this, "Sai tài khoản hoặc chưa kích hoạt!", Toast.LENGTH_LONG).show());
                }
                conn.close();
            }
            catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Lỗi kết nối: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }
}