package com.example.capstone2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import androidx.annotation.Nullable;


public class Login extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Khởi tạo GoogleSignInOptions với yêu cầu lấy thông tin email của người dùng
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Khởi tạo GoogleSignInClient
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Đăng ký sự kiện khi người dùng click vào nút đăng nhập bằng Google
        findViewById(R.id.google_btn).setOnClickListener(view -> signIn());
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult();
            // Lấy thông tin email từ tài khoản Google
            String email = account.getEmail();

            // Kiểm tra xem email có phải là dohuyhoang25062000@gmail.com không
            if ("dohuyhoang25062000@gmail.com".equals(email)) {
                // Đăng nhập thành công với tài khoản dohuyhoang25062000@gmail.com
                String displayName = account.getDisplayName();
                Toast.makeText(this, "Đăng nhập thành công với tài khoản: " + displayName, Toast.LENGTH_SHORT).show();

                // Tiếp tục xử lý tài khoản người dùng ở đây...
            } else {
                // Đăng nhập thất bại vì tài khoản không khớp
                Toast.makeText(this, "Bạn không được phép đăng nhập với tài khoản này", Toast.LENGTH_SHORT).show();
                signOut(); // Đăng xuất người dùng
            }
        } catch (Exception e) {
            // Đăng nhập thất bại
            Log.w("GoogleSignIn", "signInResult:failed code=" + e.getMessage());
            Toast.makeText(this, "Sign in failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void signOut() {
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                task -> {
                    if (task.isSuccessful()) {
                        Log.d("GoogleSignIn", "Sign out successful");
                    } else {
                        Log.w("GoogleSignIn", "Sign out failed");
                    }
                });
    }

}
