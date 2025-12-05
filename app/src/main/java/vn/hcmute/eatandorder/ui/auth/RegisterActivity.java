package vn.hcmute.eatandorder.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Properties;
import java.util.Random;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import vn.hcmute.eatandorder.R;

public class RegisterActivity extends AppCompatActivity {
    private EditText edtUsername, edtPassword, edtFullname, edtEmail, edtPhone;
    private Button btnRegister;
    private static final String DB_URL = "jdbc:mysql://192.168.149.80:3306/eatorder";
    private static final String DB_USER = "admin";
    private static final String DB_PASS = "123";
    private final String SENDER_EMAIL = "h12t2n2005@gmail.com";
    private final String SENDER_PASSWORD = "eugioisrcxcezyyx";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtFullname = findViewById(R.id.edtFullname);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(v -> handleRegister());
    }
    private void handleRegister() {
        String user = edtUsername.getText().toString().trim();
        String pass = edtPassword.getText().toString().trim();
        String name = edtFullname.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        if (user.isEmpty() || pass.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Thiếu thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }
        int otpValue = 100000 + new Random().nextInt(900000);
        String otpCode = String.valueOf(otpValue);
        long expiryTime = System.currentTimeMillis() + (3 * 60 * 1000); // 3 phút
        new Thread(() -> {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                String sql = "INSERT INTO users (username, password, full_name, email, phone, otp_code, otp_expiry, is_active) VALUES (?, ?, ?, ?, ?, ?, ?, 0)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, user);
                stmt.setString(2, pass);
                stmt.setString(3, name);
                stmt.setString(4, email);
                stmt.setString(5, phone);
                stmt.setString(6, otpCode);
                stmt.setLong(7, expiryTime);
                int rowInserted = stmt.executeUpdate();
                conn.close();
                if (rowInserted > 0) {
                    sendEmail(email, otpCode);
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Đăng ký thành công! Đã gửi OTP.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(RegisterActivity.this, OtpActivity.class);
                        intent.putExtra("KEY_USERNAME", user);
                        startActivity(intent);
                        finish();
                    });
                }
            }
            catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException e) {
                runOnUiThread(() -> Toast.makeText(this, "Tên đăng nhập đã tồn tại!", Toast.LENGTH_SHORT).show());
            }
            catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Lỗi kết nối: " + e.getMessage(), Toast.LENGTH_LONG).show());
                e.printStackTrace();
            }
        }).start();
    }
    private void sendEmail(String recipientEmail, String otpCode) throws Exception {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
            }
        });
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(SENDER_EMAIL));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
        message.setSubject("Mã OTP Eat&Order");
        message.setText("Mã OTP của bạn là: " + otpCode);
        Transport.send(message);
    }
}