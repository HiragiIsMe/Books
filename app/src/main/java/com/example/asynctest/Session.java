package com.example.asynctest;

import android.content.Context;
import android.content.SharedPreferences;

import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Session {
    SharedPreferences sharedPreferences;

    public Session(Context ctx){
        sharedPreferences = ctx.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
    }
    
    public void SetToken(String token){
        sharedPreferences.edit().putString("token", token).commit();
        sharedPreferences.edit().putLong("date", System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)).commit();
    }

    public String getToken(){
        return sharedPreferences.getString("token", "");
    }
}
