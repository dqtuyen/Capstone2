package com.example.capstone2;

public class DataActivity {
    private String title;
    private String time;

    private  String sign;
    private String money;

    public DataActivity(String title, String time, String sign, String money) {
        this.title = title;
        this.time = time;
        this.sign = sign;
        this.money = money;


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
}
