package com.example.capstone2;

public class DataActivity {
    private String title;
    private String time;
    private  String sign;
    private String money;
    private  String location;
    private  String checkin;


    private  String checkout;


    public DataActivity(String title, String time, String sign, String money, String location, String checkin, String checkout) {
        this.title = title;
        this.time = time;
        this.sign = sign;
        this.money = money;
        this.location = location;
        this.checkin = checkin;
        this.checkout = checkout;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSign(){
        return sign;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    public String getCheckin() {
        return checkin;
    }

    public void setCheckin(String checkin) {
        this.checkin = checkin;
    }
    public String getCheckout() {
        return checkout;
    }

    public void setCheckout(String checkout) {
        this.checkout = checkout;
    }

}
