package com.example.capstone2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
public class Register extends AppCompatActivity {

    private EditText edtEmail, edtName, edtSdt, edtAddress;
    private RadioGroup radioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Khởi tạo các view
        edtEmail = findViewById(R.id.edt_email);
        edtName = findViewById(R.id.edt_name);
        edtSdt = findViewById(R.id.edt_sdt);
        edtAddress = findViewById(R.id.edt_address);
        radioGroup = findViewById(R.id.radioGroup);

        TextView LoginNowTextView = findViewById(R.id.txt_Login);
        LoginNowTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Bắt đầu activity Register khi TextView được nhấn
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });

        // Lắng nghe sự kiện khi nhấn nút Đăng ký
        Button btnRegister = findViewById(R.id.btn_login);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kiểm tra các trường thông tin đã được nhập đầy đủ hay không
                if (validateInputs()) {
                    // Nếu các trường thông tin đã được nhập đầy đủ, tiến hành đăng ký
                    registerUser();
                } else {
                    // Nếu có trường thông tin chưa được nhập đầy đủ, hiển thị thông báo
                    Toast.makeText(Register.this, "Bạn cần nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validateInputs() {
        String email = edtEmail.getText().toString().trim();
        String name = edtName.getText().toString().trim();
        String sdt = edtSdt.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();

        return !email.isEmpty() && !name.isEmpty() && !sdt.isEmpty() && !address.isEmpty();
    }
    // Hàm xử lý đăng ký người dùng
    private void registerUser() {
        // Thực hiện xử lý đăng ký ở đây
        Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
    }
}