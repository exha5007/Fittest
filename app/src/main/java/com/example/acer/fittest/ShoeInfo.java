package com.example.acer.fittest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import static com.example.acer.fittest.ImageProcess.foot_lengh;
import static com.example.acer.fittest.ImageProcess.foot_width;
import static com.example.acer.fittest.ImageProcess.mpj_width;

public class ShoeInfo extends AppCompatActivity {

    TextView text_shoe_size;
    ImageView btn_img_back_info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoe_info);
        btn_img_back_info = (ImageView) findViewById(R.id.btn_img_back_info);
        btn_img_back_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        text_shoe_size = (TextView) findViewById(R.id.text_shoe_size);
        shoeSize s=new shoeSize();
        System.out.println(s.transform(MainActivity.gender==1?"男":"女", foot_lengh, mpj_width, foot_width));
        text_shoe_size.setText(String.format("推薦鞋碼：US%s",s.transform(MainActivity.gender==1?"男":"女", foot_lengh, mpj_width, foot_width)));
        //"推薦鞋碼：US8"
    }
}
