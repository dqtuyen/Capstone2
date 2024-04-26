package com.example.capstone2;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

public class MaHoaDuLieu {
    private static final String SHARED_PREFERENCES_NAME = "MySharedPreferences";
    private static final String SECRET_KEY_ALIAS = "MySecretKey";
    // Hàm mã hóa dữ liệu
    // Hàm mã hóa dữ liệu
    public static String encryptData(Context context, String data) throws Exception {
        // Kiểm tra xem secret key đã được lưu trong SharedPreferences chưa
        SecretKey secretKey = getSecretKey(context);

        // Tạo đối tượng Cipher cho AES
        Cipher cipher = Cipher.getInstance("AES");

        // Mã hóa dữ liệu
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedData = cipher.doFinal(data.getBytes());

        // Chuyển đổi dữ liệu đã mã hóa sang chuỗi Base64 để lưu trữ hoặc truyền đi
        return Base64.encodeToString(encryptedData, Base64.DEFAULT);
    }

    // Phương thức để tạo hoặc lấy secret key từ SharedPreferences
    public static SecretKey getSecretKey(Context context) throws Exception {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);

        // Kiểm tra xem secret key đã được lưu trong SharedPreferences chưa
        String encodedKey = sharedPreferences.getString(SECRET_KEY_ALIAS, null);
        if (encodedKey != null) {
            // Nếu đã có secret key được lưu, giải mã và trả về
            byte[] decodedKey = Base64.decode(encodedKey, Base64.DEFAULT);
            return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
        } else {
            // Nếu chưa có secret key, tạo một secret key mới và lưu vào SharedPreferences
            SecretKey newSecretKey = KeyGenerator.getInstance("AES").generateKey();
            String encodedSecretKey = Base64.encodeToString(newSecretKey.getEncoded(), Base64.DEFAULT);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(SECRET_KEY_ALIAS, encodedSecretKey);
            editor.apply();
            return newSecretKey;
        }
    }


    // Hàm giải mã dữ liệu
    // Hàm giải mã dữ liệu
    public static String decryptData(String encryptedDataString, SecretKey secretKey) throws Exception {
        // Chuyển đổi chuỗi Base64 thành dữ liệu đã mã hóa
        byte[] encryptedData = Base64.decode(encryptedDataString, Base64.DEFAULT);

        // Tạo đối tượng Cipher cho AES
        Cipher cipher = Cipher.getInstance("AES");

        // Giải mã dữ liệu
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedData = cipher.doFinal(encryptedData);

        // Chuyển đổi dữ liệu đã giải mã thành chuỗi
        return new String(decryptedData);
    }
}