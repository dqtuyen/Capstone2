package com.example.capstone2;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.crypto.SecretKey;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QRFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QRFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_CODE_SAVE_IMAGE = 100;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public QRFragment() {
        // Required empty public constructor
    }
    public static QRFragment newInstance(String userEmail) {
        QRFragment fragment = new QRFragment();
        Bundle args = new Bundle();
        args.putString("user_email", userEmail);
        fragment.setArguments(args);
        return fragment;
    }
    public static QRFragment newInstance(String param1, String param2) {
        QRFragment fragment = new QRFragment();
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
    private static final int PERMISSION_REQUEST_CODE = 100;
    ImageView imageView;
    TextView txt_name, txt_dob, txt_countdown;
    private CountDownTimer countDownTimer;
    private Runnable countdownRunnable;
    private Handler handler;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_qr, container, false);
        imageView = view.findViewById(R.id.img_btn_naptien);
        txt_name = view.findViewById(R.id.txt_name);
        txt_dob = view.findViewById(R.id.txt_dob);
        txt_countdown = view.findViewById(R.id.txt_countdown);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        // Lấy dữ liệu từ SharedPreferences
        String createKEY;
        if (!sharedPreferences.getAll().isEmpty()) {
            createKEY = sharedPreferences.getString("createKEY", "");
        } else {
            createKEY = "gregagaefaefaef";
        }


        Log.d("test", "Lấy key từ share" + createKEY);
        createNewQR(createKEY);
        imageView.setImageBitmap(mergedBitmap);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImage();
            }
        });


        // Lấy userEmail từ getArguments()
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("user_email")) {
            String userEmail = bundle.getString("user_email");
            // Gọi phương thức getUserByEmail() để lấy thông tin người dùng dựa trên email
            getUserByEmail(userEmail);
        }
        handler = new Handler();
        // Start countdown timer

        String text = "textdulieumahoa";
        try {
            String data = "MÃ hóa dữ liệu!";

            // Mã hóa dữ liệu
            String encryptedData = MaHoaDuLieu.encryptData(getContext(),data);
            System.out.println("Encrypted data: " + encryptedData);


            SecretKey secretKey = MaHoaDuLieu.getSecretKey(getContext()); // Lấy secretKey từ nơi nào đó
            String decryptedData = MaHoaDuLieu.decryptData(encryptedData, secretKey);
            System.out.println("Decrypted data: " + decryptedData);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return  view;
    }

    private void saveImage() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_TITLE, "image.jpg");
        startActivityForResult(intent, REQUEST_CODE_SAVE_IMAGE);
    }
    Bitmap mergedBitmap;
    private void createNewQR(String text) {
        QrData qrData = new QrData(text);
        try {
            Bitmap qrCodeBitmap = QrGenerator.generateQrCode(qrData);
            Bitmap logoBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo_dtu_qr);
            // Set default size for logo (e.g., 100x100)
            int desiredWidth = 100;
            int desiredHeight = 100;
            // Scale logo bitmap to desired size
            logoBitmap = Bitmap.createScaledBitmap(logoBitmap, desiredWidth, desiredHeight, false);
            mergedBitmap = QrUtils.mergeBitmaps(qrCodeBitmap, logoBitmap);


        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
    HashMap<String, String> myHashMap = new HashMap<>();
    String name_dob = "";
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
                        txt_dob.setText(formatDateOfBirthView(user.getDate_of_birth()));
                        name_dob = user.getFull_name() + formatDateOfBirth(user.getDate_of_birth());
                        startCountdown();
                        Log.d("test", "Tên và ngày sinh" + name_dob);
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
    // Method to start the countdown timer
    private void startCountdown() {
        countdownRunnable = new Runnable() {
            @Override
            public void run() {
                // Countdown finished, do something here if needed
                Date now = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
                String formattedDateTime = formatter.format(now);
                String createKEY = name_dob + formattedDateTime;
                createNewQR(createKEY);
                Log.d("Test", "đã tạo KEY" + createKEY);

                //////////////////////////////ĐẨY LÊN DATABASE//////////////////







                //////////////////////////////ĐẨY LÊN DATABASE//////////////////
                // Lấy tham chiếu tới SharedPreferences từ Context của hoạt động mẹ (activity)
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                // Sử dụng Editor để chỉnh sửa SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("createKEY", createKEY);
                // Lưu các thay đổi
                editor.apply();

                // Update countdown text
                updateCountdownText();
                // Schedule the next countdown after 1 minute
                handler.postDelayed(this, 60000); // 1 minute
            }
        };
        handler.post(countdownRunnable);
    }
    // Method to start the countdown timer
    private void updateCountdownText() {
        countDownTimer = new CountDownTimer(60000, 1000) { // Countdown for 1 minute (60 seconds)
            @Override
            public void onTick(long millisUntilFinished) {
                long secondsUntilFinished = millisUntilFinished / 1000;
                txt_countdown.setText("Mã QR được làm mới sau: " +  String.format("%02d:%02d", secondsUntilFinished / 60, secondsUntilFinished % 60));
            }

            @Override
            public void onFinish() {
                imageView.setImageBitmap(mergedBitmap);
            }
        }.start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SAVE_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri uri = data.getData();
                try {
                    OutputStream outputStream = requireContext().getContentResolver().openOutputStream(uri);
                    if (outputStream != null) {
                        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                        Bitmap bitmap = drawable.getBitmap();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                        outputStream.close();
                        Toast.makeText(getContext(), "Image saved", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Failed to save image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    // Định nghĩa phương thức để định dạng ngày sinh
    private String formatDateOfBirth(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
        return sdf.format(date);
    }
    private String formatDateOfBirthView(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        // Cancel the countdown timer when the activity is destroyed
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}