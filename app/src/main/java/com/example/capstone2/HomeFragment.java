package com.example.capstone2;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.StrictMode;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstone2.zalopayAPI.CreateOrder;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private List<Transaction> transactions = new ArrayList<>();

    private String mParam1;
    private String mParam2;
    private static final String TAG = "HomeFragment";

    private  int userId;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String userEmail) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString("user_email", userEmail);
        fragment.setArguments(args);
        return fragment;
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    TextView txt_name, txt_money;

    TextView txt_wallet;

    TextView textView, txt_naptien;

    ImageView img_btn_scan, img_eye;
    RecyclerView recyclerView;
    ArrayList<DataActivity> dataActivities = new ArrayList<>();
    ActivityAdapter adapter;
    String userEmail;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        textView = view.findViewById(R.id.text);
        txt_name = view.findViewById(R.id.txt_name);
        img_btn_scan = view.findViewById(R.id.img_btn_scan);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        txt_wallet = view.findViewById(R.id.txt_wallet);
        img_eye = view.findViewById(R.id.img_eye);
        txt_naptien = view.findViewById(R.id.txt_naptien);

        // Lấy userEmail từ getArguments()
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("user_email")) {
            userEmail = bundle.getString("user_email");
            // Gọi phương thức getUserByEmail() để lấy thông tin người dùng dựa trên email
            getUserByEmail(userEmail);


        }

        img_btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = IntentIntegrator.forSupportFragment(HomeFragment.this);

                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setPrompt("Scan a QR Code");
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                intentIntegrator.initiateScan();
            }
        });

//        dataActivities.add(new DataActivity("Thanh toán", "19:30, 03 thg 03 2024", "-","2000"));
//
//        adapter = new ActivityAdapter(getContext(), dataActivities);
//        recyclerView.setAdapter(adapter);

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ZaloPaySDK.init(553, Environment.SANDBOX);


        txt_naptien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TopUpMyWallet.class);
                intent.putExtra("userId", userId);
                intent.putExtra("money", txt_wallet.getText());

                startActivity(intent);

            }
        });
        setEvent();

        return view;
    }
    Boolean check = true;
    void setEvent() {
        img_eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check) {
                    img_eye.setImageResource(R.drawable.ic_eye_hide);
                    txt_wallet.setTransformationMethod(new PasswordTransformationMethod());
                    check = false;
                } else {
                    img_eye.setImageResource(R.drawable.ic_eye);
                    txt_wallet.setTransformationMethod(null);
                    check = true;
                }

            }
        });
    }
    void loadDataFromDatabase() {
        // Gọi phương thức getUserByEmail(email) từ ApiService
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<User> call = apiService.getUserByEmail(userEmail);
        call.enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    if (user != null) {
                        txt_name.setText(user.getFull_name());
                        // Gán giá trị của wallet vào TextView txt_wallet
                        money = String.valueOf(user.getWallet());
                        String wallet = formatCurrency(String.valueOf(user.getWallet()));
                        txt_wallet.setText(wallet);

                        userId = user.getUser_id();
                        Log.d(TAG, "User ID: " + userId);
                        // Gọi API để lấy lịch sử giao dịch dựa trên user ID
                        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
                        Call<List<Transaction>> transactionCall = apiService.getTransactionHistory(userId);
                        transactionCall.enqueue(new Callback<List<Transaction>>() {
                            @Override
                            public void onResponse(Call<List<Transaction>> call, Response<List<Transaction>> response) {
                                if (response.isSuccessful()) {
                                    List<Transaction> transactions = response.body();
                                    if (transactions != null && !transactions.isEmpty()) {
                                        // Xóa dữ liệu cũ trong dataActivities
                                        dataActivities.clear();

                                        // Duyệt qua từng giao dịch và thêm vào dataActivities
                                        for (Transaction transaction : transactions) {
                                            String title = transaction.getTransactionTypeString();
                                            String time = transaction.getFormattedTranTime();
                                            String sign = transaction.getSign();
                                            String money = String.valueOf(transaction.getAmount());
                                            String location = transaction.getLocation();
                                            String checkin = transaction.getFormattedCheckinTime();
                                            String checkout = transaction.getFormattedCheckoutTime();

                                            dataActivities.add(new DataActivity(title, time, sign, money, location, checkin, checkout));
                                        }

                                        // Tạo adapter mới với dữ liệu mới và thiết lập cho RecyclerView
                                        adapter = new ActivityAdapter(getContext(), dataActivities);
                                        recyclerView.setAdapter(adapter);
                                    } else {
                                        Log.d(TAG, "No transaction history found for user ID: " + userId);
                                    }
                                } else {
                                    Log.e(TAG, "Failed to get transaction history");
                                }
                            }
                            @Override
                            public void onFailure(Call<List<Transaction>> call, Throwable t) {
                                Log.e(TAG, "API call failed", t);
                            }
                        });

                        // Gán fullname vào txt_name
                        txt_name.setText(user.getFull_name());
                        // Log fullName lên console
                        Log.d(TAG, "FullName: " + user.getFull_name());
                    } else {
                        Log.e(TAG, "User object is null");
                    }
                } else {
                    Log.e(TAG, "API call failed");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "API call failed", t);
            }
        });

        //Toast.makeText(getContext(), "Reload", Toast.LENGTH_SHORT).show();
    }
    String money = "";
    private void getUserByEmail(String email) {
        // Gọi phương thức getUserByEmail(email) từ ApiService
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<User> call = apiService.getUserByEmail(email);
        call.enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    if (user != null) {
                        txt_name.setText(user.getFull_name());
                        // Gán giá trị của wallet vào TextView txt_wallet
                        money = String.valueOf(user.getWallet());
                        String wallet = formatCurrency(String.valueOf(user.getWallet()));
                        txt_wallet.setText(wallet);

                        userId = user.getUser_id();
                        Log.d(TAG, "User ID: " + userId);
                        // Gọi API để lấy lịch sử giao dịch dựa trên user ID
                        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
                        Call<List<Transaction>> transactionCall = apiService.getTransactionHistory(userId);
                        transactionCall.enqueue(new Callback<List<Transaction>>() {
                            @Override
                            public void onResponse(Call<List<Transaction>> call, Response<List<Transaction>> response) {
                                if (response.isSuccessful()) {
                                    List<Transaction> transactions = response.body();
                                    if (transactions != null && !transactions.isEmpty()) {
                                        // Xóa dữ liệu cũ trong dataActivities
                                        dataActivities.clear();

                                        // Duyệt qua từng giao dịch và thêm vào dataActivities
                                        for (Transaction transaction : transactions) {
                                            String title = transaction.getTransactionTypeString();
                                            String time = transaction.getFormattedTranTime();
                                            String sign = transaction.getSign();
                                            String money = String.valueOf(transaction.getAmount());
                                            String location = transaction.getLocation();
                                            String checkin = transaction.getFormattedCheckinTime();
                                            String checkout = transaction.getFormattedCheckoutTime();

                                            dataActivities.add(new DataActivity(title, time, sign, money, location, checkin, checkout));
                                        }

                                        // Tạo adapter mới với dữ liệu mới và thiết lập cho RecyclerView
                                        adapter = new ActivityAdapter(getContext(), dataActivities);
                                        recyclerView.setAdapter(adapter);
                                    } else {
                                        Log.d(TAG, "No transaction history found for user ID: " + userId);
                                    }
                                } else {
                                    Log.e(TAG, "Failed to get transaction history");
                                }
                            }
                            @Override
                            public void onFailure(Call<List<Transaction>> call, Throwable t) {
                                Log.e(TAG, "API call failed", t);
                            }
                        });

                        // Gán fullname vào txt_name
                        txt_name.setText(user.getFull_name());
                        // Log fullName lên console
                        Log.d(TAG, "FullName: " + user.getFull_name());
                    } else {
                        Log.e(TAG, "User object is null");
                    }
                } else {
                    Log.e(TAG, "API call failed");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "API call failed", t);
            }
        });
    }



    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            String contents = intentResult.getContents();
            if (contents != null) {
                textView.setText(contents);
            }
        }
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
    @Override
    public void onResume() {
        super.onResume();
        // Load dữ liệu từ database mỗi khi Fragment hiển thị
        loadDataFromDatabase();
    }
}
