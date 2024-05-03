package com.example.capstone2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import java.util.Date;
import java.util.Locale;

import retrofit2.Callback;
import retrofit2.Response;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

import retrofit2.Call;
import java.util.Calendar;
import java.text.SimpleDateFormat;

public class TopUpMyWallet extends AppCompatActivity {

    EditText edt_money;
    TextView txt_soduvi;
    Button btn_50, btn_100, btn_200, btn_naptien;
    ProgressBar load;
    private DecimalFormat decimalFormat;
    private  int userId;

    Calendar calendar = Calendar.getInstance();
    private Date tranTime = calendar.getTime();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    String formattedTranTime = sdf.format(tranTime);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up_my_wallet);
        // Kiểm tra xem có userId được chuyển từ HomeFragment hay không
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId = extras.getInt("userId");
        } else {
            // Nếu không có userId được chuyển, ghi thông báo lên terminal
            Log.e("TopUpMyWallet", "Không có userId được chuyển từ HomeFragment");
        }
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
                        Log.d("RequestZalo", "Payment succeeded. Transaction ID: " + transactionId);
                        Log.d("RequestZalo", "User ID: " + userId);
                        Log.d("RequestZalo", "Amount: " + amount);
                        Log.d("RequestZalo", "Transaction Time: " + formattedTranTime);
                        // Chuyển đổi amount thành kiểu double
                        double amountDouble = Double.parseDouble(amount);
                        // Tạo requestBody từ dữ liệu cần gửi đi
                        DepositRequestBody requestBody = new DepositRequestBody(userId, amountDouble, formattedTranTime);

                        // Gọi API deposit để nạp tiền vào ví
                        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
                        Call<Transaction> call = apiService.deposit(requestBody);
                        call.enqueue(new Callback<Transaction>() {
                            @Override
                            public void onResponse(Call<Transaction> call, Response<Transaction> response) {
                                if (response.isSuccessful()) {
                                    // Xử lý khi API deposit thành công
                                    Toast.makeText(TopUpMyWallet.this, "Nạp tiền thành công", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(TopUpMyWallet.this, MainActivity.class)
                                            .setAction(MainActivity.ACTION_RELOAD_HOME_FRAGMENT));
                                    Intent intent = new Intent(TopUpMyWallet.this, MainActivity.class);
                                    intent.putExtra("fragmentToLoad", "HomeFragment"); // Gửi dữ liệu để chỉ định fragment cần tải
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // Xử lý khi API deposit thất bại
                                    Toast.makeText(TopUpMyWallet.this, "Nạp tiền thất bại", Toast.LENGTH_SHORT).show();

                                }
                            }

                            @Override
                            public void onFailure(Call<Transaction> call, Throwable t) {
                                // Xử lý khi có lỗi xảy ra trong quá trình gọi API deposit
                                Toast.makeText(TopUpMyWallet.this, "Nạp tiền thành công ", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(TopUpMyWallet.this, MainActivity.class)
                                        .setAction(MainActivity.ACTION_RELOAD_HOME_FRAGMENT));
                                Intent intent = new Intent(TopUpMyWallet.this, MainActivity.class);
                                intent.putExtra("fragmentToLoad", "HomeFragment"); // Gửi dữ liệu để chỉ định fragment cần tải
                                startActivity(intent);
                                finish();
                            }
                        });
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
}