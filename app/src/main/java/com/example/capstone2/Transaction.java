package com.example.capstone2;

import java.util.Date;
import java.text.SimpleDateFormat;

public class Transaction {
    private String full_name;
    private double wallet;
    private int transaction_id;
    private int user_id;
    private int transaction_type;
    private Integer check_time;
    private double amount;
    private Date tran_time;
    private String location;
    private String license_plate;
    private Date checkin_time;
    private Date checkout_time;

    public Transaction(String full_name, double wallet, int transaction_id, int user_id, int transaction_type, int check_time, double amount, Date tran_time, String location, String license_plate, Date checkin_time, Date checkout_time) {
        this.full_name = full_name;
        this.wallet = wallet;
        this.transaction_id = transaction_id;
        this.user_id = user_id;
        this.transaction_type = transaction_type;
        this.check_time = check_time;
        this.amount = amount;
        this.tran_time = tran_time;
        this.location = location;
        this.license_plate = license_plate;
        this.checkin_time = checkin_time;
        this.checkout_time = checkout_time;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public long getWallet() {
        return (long) wallet;
    }

    public void setWallet(double wallet) {
        this.wallet = wallet;
    }

    public int getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
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

    public int getCheck_time() {
        return check_time;
    }

    public void setCheck_time(int check_time) {
        this.check_time = check_time;
    }

    public long getAmount() {
        return (long) amount;
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

    public String getTransactionTypeString() {
        switch (transaction_type) {
            case 1:
                return "Nạp tiền";
            case 2:
                return "Thanh toán gửi xe";
            case 3:
                return "Rút tiền";
            default:
                return "Unknown";
        }
    }

    public String getSign() {
        switch (transaction_type) {
            case 1:
                return "+";
            case 2:
            case 3:
                return "-";
            default:
                return "";
        }
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLicense_plate() {
        return license_plate != null ? license_plate : "";
    }

    public void setLicense_plate(String license_plate) {
        this.license_plate = license_plate;
    }

    public Date getCheckin_time() {
        return checkin_time;
    }

    public void setCheckin_time(Date checkin_time) {
        this.checkin_time = checkin_time;
    }

    public Date getCheckout_time() {
        return checkout_time;
    }

    public void setCheckout_time(Date checkout_time) {
        this.checkout_time = checkout_time;
    }

    public String getFormattedCheckinTime() {
        if (checkin_time != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm, dd-MM-yyyy");
            return sdf.format(checkin_time);
        } else {
            return ""; // Trả về chuỗi trắng nếu checkin_time là null
        }
    }

    public String getFormattedCheckoutTime() {
        if (checkin_time != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm, dd-MM-yyyy");
            return sdf.format(checkout_time);
        } else {
            return ""; // Trả về chuỗi trắng nếu checkin_time là null
        }
    }


    public String getFormattedTranTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm, dd-MM-yyyy");
        return sdf.format(tran_time);
    }
}
