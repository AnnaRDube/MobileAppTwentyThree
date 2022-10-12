package com.example.twenty_one;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class end_screen_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_screen);

        final String valueA = this.getIntent().getStringExtra("key");
        final int valueB = this.getIntent().getIntExtra("key2", 0);
        final int valueC = this.getIntent().getIntExtra("key3", 0);
        final int valueD = this.getIntent().getIntExtra("key4", 0);
        final int valueE = this.getIntent().getIntExtra("key5", 0);

        TextView message = findViewById(R.id.result_tv);
        message.setText(valueA);

        TextView u_points = findViewById(R.id.u_final);
        u_points.setText(Integer.toString(valueB));

        TextView d_points = findViewById(R.id.d_final);
        d_points.setText(Integer.toString(valueC));

        TextView d_total = findViewById(R.id.dealer_total);
        String d = SaveTotal.readNum(this, "D");
        if(d == null){d = "0";}
        int dtotal = Integer.parseInt(d);
        dtotal += valueE;
        d_total.setText(Integer.toString(dtotal));
        SaveTotal.writeNum(getApplicationContext(), dtotal, "D");

        TextView u_total = findViewById(R.id.user_total);
        String u = SaveTotal.readNum(this, "U");
        if(u == null){u = "0";}
        int utotal = Integer.parseInt(u);
        utotal += valueD;
        u_total.setText(Integer.toString(utotal));
        SaveTotal.writeNum(getApplicationContext(), utotal, "U");


        Button play = findViewById(R.id.play_again);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(end_screen_activity.this, Activity2.class);
                startActivity(i);
            }
        });


        Button mm = findViewById(R.id.main_menu);
        mm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(end_screen_activity.this, MainActivity.class);
                startActivity(i);
            }
        });

        Button exit = findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
            }
        });
    }
}