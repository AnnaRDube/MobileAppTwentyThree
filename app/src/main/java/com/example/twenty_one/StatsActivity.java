package com.example.twenty_one;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StatsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        setTextViews();

        Button mainM = findViewById(R.id.button_main_stats);
        mainM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(StatsActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        Button r = findViewById(R.id.reset_btn);
        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int ut = 0, dt = 0;
                SaveTotal.writeNum(getApplicationContext(), ut, "U");
                SaveTotal.writeNum(getApplicationContext(), dt, "D");
                setTextViews();
            }
        });
    }
    private void setTextViews(){
        TextView d_total = findViewById(R.id.d_wins_total);
        String d = SaveTotal.readNum(this, "D");
        if(d == null){d = "0";}
        d_total.setText(d);

        TextView u_total = findViewById(R.id.utotal_wins);
        String u = SaveTotal.readNum(this, "U");
        if(u == null){u = "0";}
        u_total.setText(u);

        TextView p = findViewById(R.id.percentage);
        int ugame = Integer.parseInt(u);
        int dgame = Integer.parseInt(d);

        float perc = (((float)ugame) / (dgame + ugame)) * 100;
        perc = Math.round(perc);
        p.setText(perc + "%");
    }
}