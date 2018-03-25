package com.example.acer.fittest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.core.Mat;

public class MainActivity extends Activity {
    static String currentFileName;
    static int CurrentTutPage = 0;
    public static int gender = 0; //1=male,2=female
    private final String PERMISSION_WRITE_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";
    private final String PERMISSION_CAMERA = "android.permission.CAMERA";
    private final static int CAMERA_PIC_REQUEST1 = 0;
    Context con;
    static Bitmap bitmapFrontCam;
    Button btn_tut_back;
    Button btn_tut_skip;
    LinearLayout loading;
    LinearLayout tut1;
    TextView tut_text;
    ImageView tut_img;
    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep( 3000 );
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ChangeUI(CurrentTutPage);
                    CurrentTutPage++;
                    Log.d("TAG","running");
                }
            });
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loading = (LinearLayout)findViewById(R.id.loading_page);
        tut1 = (LinearLayout) findViewById(R.id.layout_tut1);
        tut_text = (TextView) findViewById(R.id.tut_text);
        tut_img = (ImageView) findViewById(R.id.tut_img);
        btn_tut_back = (Button) findViewById(R.id.btn_tut_back);
        btn_tut_skip = (Button) findViewById(R.id.btn_tut_skip);
        btn_tut_back.setOnClickListener(back_click);
        btn_tut_skip.setOnClickListener(skip_click);

        thread.start();


    }
    public void ChangeUI(int page) {
        Log.d("TAG","changing");

        switch(page) {
            case 0: loading.setVisibility(View.INVISIBLE);
                    tut1.setVisibility(View.VISIBLE);
                break;
            case 1: tut_text.setText(R.string.str_tut1);
                    tut_img.setImageResource(R.drawable.tut1);
                break;
            case 2: tut_text.setText(R.string.str_tut2);
                tut_img.setImageResource(R.drawable.tut2);
                break;
            case 3: tut_text.setText(R.string.str_tut3);
                tut_img.setImageResource(R.drawable.tut3);
                break;
            case 4: tut_text.setText(R.string.str_tut4);
                tut_img.setImageResource(R.drawable.tut4);
                break;
            case 5: if(!hasPermission()) {
                if(needCheckPermission()) {

                    return;
                }
            }
                Intent intent = new Intent(MainActivity.this,BackCameraActivity.class);
                startActivity(intent);
                break;

        }
    }
    public View.OnClickListener skip_click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            CurrentTutPage++;
            ChangeUI(CurrentTutPage);
        }
    };
    public View.OnClickListener back_click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(CurrentTutPage >0) {
                CurrentTutPage--;
                ChangeUI(CurrentTutPage);
            }
        }
    };
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST1) {
            if (resultCode == RESULT_OK) {
                try {
                    bitmapFrontCam = data
                            .getParcelableExtra("BitmapImage");

                } catch (Exception e) {
                }
                Intent intent = new Intent(this,ImageProcess.class);
                startActivity(intent);
            }

        }
        else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT)
                    .show();
        }
    }
    int getFrontCameraId() {
        Camera.CameraInfo ci = new Camera.CameraInfo();
        for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
            Camera.getCameraInfo(i, ci);
            if (ci.facing == Camera.CameraInfo.CAMERA_FACING_FRONT)
                return i;
        }
        return -1; // No front-facing camera found
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 200){
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(">>>", "取得授權，可以執行動作了");

                }
            }
        }
    }
    private boolean hasPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            return(ActivityCompat.checkSelfPermission(this, PERMISSION_WRITE_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this,PERMISSION_CAMERA) == PackageManager.PERMISSION_GRANTED);
        }
        return true;
    }

    private boolean needCheckPermission() {
        //MarshMallow(API-23)之後要在 Runtime 詢問權限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] perms = {PERMISSION_WRITE_STORAGE,PERMISSION_CAMERA};
            int permsRequestCode = 200;
            requestPermissions(perms, permsRequestCode);
            return true;
        }
        return false;
    }
}