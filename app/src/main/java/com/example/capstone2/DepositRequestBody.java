package com.example.capstone2;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

public class DepositRequestBody {
    @SerializedName("user_id")
    private int user_id;

    @SerializedName("amount")
    private double amount;

    @SerializedName("tran_time")
    private String tran_time;

    // Constructors, getters, and setters...

    public DepositRequestBody(int user_id, double amount, String tran_time) {
        this.user_id = user_id;
        this.amount = amount;
        this.tran_time = tran_time;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }


    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTran_time() {
        return tran_time;
    }

    public void setTran_time(String tran_time) {
        this.tran_time = tran_time;
    }
}
