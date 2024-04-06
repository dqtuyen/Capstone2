package com.example.capstone2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder>{

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
        holder.txt_date.setText(dataActivity.getTime());
        holder.txt_money.setText((dataActivity.getMoney()));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_money, txt_title, txt_date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_money = itemView.findViewById(R.id.txt_money);
            txt_title = itemView.findViewById(R.id.txt_title);
            txt_date = itemView.findViewById(R.id.txt_date);
        }
    }
    @Override
    public int getItemCount() {
        return dataActivities.size();
    }
}
