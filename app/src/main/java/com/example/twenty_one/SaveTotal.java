package com.example.twenty_one;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SaveTotal {
    private static final String KEY_U = "userKey";
    private static final String KEY_D = "dealerKey";

    public static void writeNum(Context context, int totalNum, String dataContent){
        Gson gson = new Gson();
        String s = Integer.toString(totalNum);
        String JsonString = gson.toJson(s);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor spEdit = sp.edit();

        if(dataContent == "U"){spEdit.putString(KEY_U, JsonString);}
        else {spEdit.putString(KEY_D, JsonString);}
        spEdit.apply();
    }

    public static String readNum(Context context, String dataContent){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String JsonString;

        if(dataContent == "U"){JsonString = sp.getString(KEY_U, "");}
        else {JsonString = sp.getString(KEY_D, "");}

        Gson gson = new Gson();
        Type type = new TypeToken<String>() {}.getType();
        String totalNum = gson.fromJson(JsonString, type);
        return totalNum;
    }
}
