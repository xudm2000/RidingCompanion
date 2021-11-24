package com.example.ridingcompanion;

public class History {
    private String username;
    private String distance;
    private String time;
    private String calories;
    private String avg_speed;
    private String date;
    private String image;

    public History(String username, String distance, String time, String calories, String avg_speed, String date, String image) {
        this.username = username;
        this.distance = distance;
        this.time = time;
        this.calories = calories;
        this.avg_speed = avg_speed;
        this.date = date;
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getAvg_speed() {
        return avg_speed;
    }

    public void setAvg_speed(String avg_speed) {
        this.avg_speed = avg_speed;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
