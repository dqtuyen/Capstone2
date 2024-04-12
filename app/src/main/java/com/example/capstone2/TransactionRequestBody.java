package com.example.capstone2;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

public class TransactionRequestBody {
    @SerializedName("user_id")
    private int user_id;

    @SerializedName("transaction_type")
    private int transaction_type;

    @SerializedName("amount")
    private double amount;

    @SerializedName("tran_time")
    private Date tran_time;

    // Constructors, getters, and setters...

    public TransactionRequestBody(int user_id, int transaction_type, double amount, Date tran_time) {
        this.user_id = user_id;
        this.transaction_type = transaction_type;
        this.amount = amount;
        this.tran_time = tran_time;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(int transaction_type) {
        this.transaction_type = transaction_type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getTran_time() {
        return tran_time;
    }

    public void setTran_time(Date tran_time) {
        this.tran_time = tran_time;
    }
}
