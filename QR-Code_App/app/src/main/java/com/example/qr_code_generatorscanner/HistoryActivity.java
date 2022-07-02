package com.example.qr_code_generatorscanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Set;

public class HistoryActivity extends AppCompatActivity {
    DataSharePreferences dataSharePreferences;
    ArrayList<String> list;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        dataSharePreferences = new DataSharePreferences(this);
        list = dataSharePreferences.getList();
        if(list.size() == 0){
            list.add("GEN History's Scan is empty");
        }
        recyclerView = findViewById(R.id.recylHistory);
        HistoryAdapter historyAdapter = new HistoryAdapter(list, R.layout.item_history, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(historyAdapter);

    }
}