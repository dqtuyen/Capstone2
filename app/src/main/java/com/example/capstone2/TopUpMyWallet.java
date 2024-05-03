package com.example.capstone2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstone2.zalopayAPI.CreateOrder;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class TopUpMyWallet extends AppCompatActivity {

    EditText edt_money;
    TextView txt_soduvi;
    Button btn_50, btn_100, btn_200, btn_naptien;
    ProgressBar load;
    private DecimalFormat decimalFormat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up_my_wallet);
        edt_money  = findViewById(R.id.edt_money);
        txt_soduvi  = findViewById(R.id.txt_soduvi);
        btn_50  = findViewById(R.id.btn_50);
        btn_100  = findViewById(R.id.btn_100);
        btn_200  = findViewById(R.id.btn_200);
        btn_naptien  = findViewById(R.id.btn_naptien);
        load = findViewById(R.id.load);
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ZaloPaySDK.init(553, Environment.SANDBOX);

// Trong Activity
        Intent intent = getIntent();
        if (intent != null) {
            String money = intent.getStringExtra("money");
            // Xử lý dữ liệu nhận được ở đây
            txt_soduvi.setText(formatCurrency(String.valueOf(money)));
        }

        setEvent();
    }

    void setEvent() {
        // Tạo một DecimalFormat để định dạng số
        decimalFormat = (DecimalFormat) NumberFormat.getInstance(Locale.getDefault());
        DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        decimalFormat.setDecimalFormatSymbols(symbols);

        // Thêm một TextWatcher cho EditText
        edt_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không cần thực hiện gì ở đây
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Xóa các ký tự không phải là số và dấu chấm
                String cleanString = s.toString().replaceAll("[^\\d]", "");

                try {
                    // Định dạng số
                    double parsed = Double.parseDouble(cleanString);
                    String formatted = decimalFormat.format((parsed));

                    // Hiển thị số đã được định dạng trong EditText
                    edt_money.removeTextChangedListener(this);
                    edt_money.setText(formatted + "đ");
                    edt_money.setSelection(formatted.length());
                    edt_money.addTextChangedListener(this);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                // Không cần thực hiện gì ở đây
            }
        });
        btn_naptien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                load5s();
                String amount = edt_money.getText().toString();
                requestZalo(removeCurrencyFormat(amount));
                load.setVisibility(View.GONE);
            }
        });
        btn_50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                load5s();
                String amount = btn_50.getText().toString();
                requestZalo(removeCurrencyFormat(amount));
            }
        });
        btn_100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                load5s();
                String amount = btn_100.getText().toString();
                requestZalo(removeCurrencyFormat(amount));
            }
        });
        btn_200.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                load5s();
                String amount = btn_200.getText().toString();
                requestZalo(removeCurrencyFormat(amount));
            }
        });
    }

    private void requestZalo(String amount) {
        CreateOrder orderApi = new CreateOrder();

        try {
            JSONObject data = orderApi.createOrder(amount);
            String code = data.getString("returncode");

            if (code.equals("1")) {

                String token = data.getString("zptranstoken");

                ZaloPaySDK.getInstance().payOrder(TopUpMyWallet.this, token, "demozpdk://app", new PayOrderListener() {
                    @Override
                    public void onPaymentSucceeded(final String transactionId, final String transToken, final String appTransID) {
                        Toast.makeText(TopUpMyWallet.this, "Thanh toán thành công", Toast.LENGTH_SHORT).show();
                        // Chuyển hướng về MainActivity
                        Intent intent = new Intent(TopUpMyWallet.this, MainActivity.class);
                        startActivity(intent);
                        intent.putExtra("load", "load");
                        finish();
                    }

                    @Override
                    public void onPaymentCanceled(String zpTransToken, String appTransID) {
                        Toast.makeText(TopUpMyWallet.this, "Thanh toán bị hủy", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPaymentError(ZaloPayError zaloPayError, String zpTransToken, String appTransID) {
                        Toast.makeText(TopUpMyWallet.this, "Thanh toán thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }

    // Loại bỏ ký tự tiền tệ và định dạng số
    private String removeCurrencyFormat(String input) {
        // Loại bỏ tất cả các ký tự không phải là số
        String cleanString = input.replaceAll("[^\\d]", "");

        // Trả về chuỗi đã được xử lý
        return cleanString;
    }
    private void load5s() {
        // Hiển thị phần tử load
        load.setVisibility(View.VISIBLE);
        // Sử dụng Handler để đợi 5 giây
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Ẩn phần tử load sau 5 giây
                load.setVisibility(View.GONE);
            }
        }, 5000); // 5000 milliseconds = 5 giây
    }
    public static String formatCurrency(String numberString) {
        try {
            // Chuyển đổi chuỗi thành số nguyên
            int number = Integer.parseInt(numberString);

            // Sử dụng DecimalFormat để định dạng số và thêm ký tự tiền tệ
            DecimalFormat decimalFormat = new DecimalFormat("#,###đ");
            return decimalFormat.format(number);
        } catch (NumberFormatException e) {
            // Xử lý nếu chuỗi không phải là số
            e.printStackTrace();
            return ""; // hoặc return numberString; nếu bạn muốn trả về chuỗi không thay đổi
        }
    }
}