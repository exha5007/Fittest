package com.example.acer.fittest;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class FootAnalysis extends AppCompatActivity {

    TextView foot_length, foot_width, mpj_width, h2b_length, foot_height, gender;
    double d_foot_length, d_foot_width, d_h2b_lengh, d_mpj_width, d_foot_height;
    ImageView btn_img_back,btn_img_next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foot_analysis);

        btn_img_back = (ImageView) findViewById(R.id.btn_img_back);
        btn_img_next = (ImageView) findViewById(R.id.btn_img_next);
        foot_length = (TextView) findViewById(R.id.text_foot_length);
        foot_width = (TextView) findViewById(R.id.text_foot_width);
        mpj_width = (TextView) findViewById(R.id.text_mpj_width);
        h2b_length = (TextView) findViewById(R.id.text_h2b);
        foot_height = (TextView) findViewById(R.id.text_foot_height);
        gender = (TextView) findViewById(R.id.text_gender);
        //foot_lengh,foot_width,h2b_lengh,mpj_width;



        foot_length.setText(String.format("%.1f mm",ImageProcess.foot_lengh));
        foot_width.setText(String.format("%.1f mm",ImageProcess.foot_width));
        mpj_width.setText(String.format("%.1f mm",ImageProcess.mpj_width));
        h2b_length.setText(String.format("%.1f mm",ImageProcess.h2b_lengh));
        foot_height.setText(String.format("%.1f mm",randomfoodmid("D")));


        btn_img_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FootAnalysis.this,ShoeInfo.class);
                startActivity(intent);
            }
        });
        if(MainActivity.gender == 1)
        gender.setText("男");
        else
            gender.setText("女");

    }
    public static double randomfoodmid(String a){
        double alpha = 0;
        switch(a) {
            case "A":alpha = 0.03;
                break;
            case "B":alpha = 0.0535;
                break;
            case "C":alpha = 0.068;
                break;
            case "D":alpha = 0.8;
                break;
            case "E":alpha = 0.997;
                break;
            case "EE":alpha = 1.106;
                break;
            case "EEE": alpha = 1.2;
                break;}
        double h =89.642490612312133856953274401502 + alpha * 3.3333 - Math.random();
        return h;
    }
}
