package com.example.capstone2;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccountFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String userEmail) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putString("user_email", userEmail);
        fragment.setArguments(args);
        return fragment;
    }
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
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

    Button btn_information, btn_logout;
    TextView txt_name, txt_name2, txt_phone, txt_email, txt_address;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_account, container, false);
        btn_information = view.findViewById(R.id.btn_information);
        txt_name = view.findViewById(R.id.txt_name);
        txt_name2 = view.findViewById(R.id.txt_name2);
        txt_phone = view.findViewById(R.id.txt_phone);
        txt_email = view.findViewById(R.id.txt_email);
        txt_address = view.findViewById(R.id.txt_address);
        btn_logout = view.findViewById(R.id.btn_logout);

        // Lấy userEmail từ getArguments()
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("user_email")) {
            String userEmail = bundle.getString("user_email");
            // Gọi phương thức getUserByEmail() để lấy thông tin người dùng dựa trên email
            getUserByEmail(userEmail);
        }

        setEvent();
        return view;
    }

    private void setEvent() {
        btn_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditInformatinon.class);
                startActivity(intent);
            }
        });
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
    }

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
                        txt_name2.setText(user.getFull_name());
                        txt_email.setText(user.getEmail());
                        txt_phone.setText(user.getPhone_number());
                        txt_address.setText(user.getAddress());
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
    private GoogleSignInClient mGoogleSignInClient;
    private void logout() {
        // Xóa dữ liệu phiên đăng nhập từ SharedPreferences
        SharedPreferences.Editor editor = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).edit();
        editor.remove("userId");
        editor.remove("email");
        editor.apply();
        startActivity(new Intent(requireContext(), Login.class));
        requireActivity().finish();

        // Đăng xuất khỏi Google Sign-In
        // Đăng xuất khỏi Google Sign-In
//        mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()) {
//                    // Thực hiện các hành động sau khi đăng xuất thành công
//                    // Ví dụ: chuyển hướng người dùng đến màn hình đăng nhập
//                    startActivity(new Intent(requireContext(), Login.class));
//                    requireActivity().finish(); // Kết thúc Activity hiện tại để ngăn người dùng quay lại màn hình chính
//                } else {
//                    // Xử lý khi đăng xuất không thành công (nếu cần)
//                }
//            }
//        });
    }
}