package com.example.capstone2;

public class DataActivity {
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

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    String title;
    String time;
    String money;

    public DataActivity(String title, String time, String money) {
        this.title = title;
        this.time = time;
        this.money = money;
    }
}
