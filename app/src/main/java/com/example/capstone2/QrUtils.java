package com.example.capstone2;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class QrUtils {

    public static Bitmap mergeBitmaps(Bitmap qrCodeBitmap, Bitmap logoBitmap) {
        int width = qrCodeBitmap.getWidth();
        int height = qrCodeBitmap.getHeight();

        // Tạo bitmap mới với kích thước phù hợp
        Bitmap mergedBitmap = Bitmap.createBitmap(width, height, qrCodeBitmap.getConfig());

        // Vẽ mã QR vào bitmap mới
        Canvas canvas = new Canvas(mergedBitmap);
        canvas.drawBitmap(qrCodeBitmap, 0, 0, null);

        // Vẽ logo vào giữa mã QR
        int logoWidth = logoBitmap.getWidth();
        int logoHeight = logoBitmap.getHeight();
        int x = (width - logoWidth) / 2;
        int y = (height - logoHeight) / 2;
        canvas.drawBitmap(logoBitmap, x, y, null);

        return mergedBitmap;
    }
}