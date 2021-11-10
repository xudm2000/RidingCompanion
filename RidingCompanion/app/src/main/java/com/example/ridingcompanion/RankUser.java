package com.example.ridingcompanion;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class RankUser {
    public String username;
    public long numOfDay;

    public RankUser(){

    }

    public RankUser(String username, long numOfDay) {
        this.username = username;
        this.numOfDay = numOfDay;
    }
}
