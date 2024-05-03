package com.example.capstone2;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;



public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder>{

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_money, txt_title, txt_date, txt_location, txt_checkin, txt_checkout;
        ImageView img_view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_money = itemView.findViewById(R.id.txt_wallet);
            txt_title = itemView.findViewById(R.id.txt_title);
            //txt_date = itemView.findViewById(R.id.txt_date);
            img_view = itemView.findViewById(R.id.img_view);
            txt_location = itemView.findViewById(R.id.txt_location);
            txt_checkin = itemView.findViewById(R.id.txt_checkin);
            txt_checkout = itemView.findViewById(R.id.txt_checkout);
        }
    }

    ArrayList<DataActivity> dataActivities;
    Context context;

    public ActivityAdapter(Context context, ArrayList<DataActivity> dataActivities) {
        this.context = context;
        this.dataActivities = dataActivities;
    }
    @NonNull
    @Override
    public ActivityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_activity, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityAdapter.ViewHolder holder, int position) {
        DataActivity dataActivity = dataActivities.get(position);
        holder.txt_title.setText(dataActivity.getTitle());
        //holder.txt_date.setText(dataActivity.getTime());
        String getMoney = dataActivity.getMoney();
        holder.txt_money.setText((dataActivity.getSign() + formatCurrency(getMoney)));
        if(dataActivity.getSign().toString().contains("+")) {
            holder.txt_money.setTextColor(Color.parseColor("#00A81B"));
            holder.img_view.setImageResource(R.drawable.ic_money);
            holder.txt_location.setText("");
            holder.txt_checkin.setText("");
            holder.txt_checkout.setText("");
        }
        if(dataActivity.getSign().toString().contains("-")) {
            holder.txt_location.setText(dataActivity.getLocation());
            holder.txt_checkin.setText(dataActivity.getCheckin());
            holder.txt_checkout.setText(dataActivity.getCheckout());
        }
    }
    @Override
    public int getItemCount() {
        return dataActivities.size();
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
