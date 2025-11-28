package vn.hcmute.eatandorder.ui.intro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import vn.hcmute.eatandorder.MainActivity;
import vn.hcmute.eatandorder.databinding.ActivityIntroBinding;
import vn.hcmute.eatandorder.ui.auth.LoginActivity;
import vn.hcmute.eatandorder.util.PrefManager;

public class IntroActivity extends AppCompatActivity {

    private ActivityIntroBinding binding;
    private PrefManager pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        pref = new PrefManager(this);

        pref.logout();

        binding.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pref.isLoggedIn()) {
                    // Đã đăng nhập trước đó -> thẳng Main
                    Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    // Chưa đăng nhập -> tới Login
                    Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
