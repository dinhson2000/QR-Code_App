package com.example.qr_code_generatorscanner;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DataSharePreferences {
    private SharedPreferences sharedPreferences;
    public Set<String> set;
    public DataSharePreferences(Context context) {
        sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        set = sharedPreferences.getStringSet("data", null);
        if(set == null){
            set = new HashSet<>();
        }
    }
    public void addData(String a){
        set.add(a);
        if(set.size() == 20){
            String data1= "";
            for(String i : set){
                data1 = i;
                break;
            }
            set.remove(data1);
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().commit();
        editor.putStringSet("data", set).commit();

    }
    public ArrayList getList(){
        Set<String> set = sharedPreferences.getStringSet("data", null);
        ArrayList<String> list = new ArrayList<>();
        for(String i : set) list.add(i);
        return  list;
    }
}
