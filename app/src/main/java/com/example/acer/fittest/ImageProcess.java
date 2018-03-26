package com.example.acer.fittest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.acer.fittest.MainActivity.currentFileName;
import static org.opencv.imgproc.Imgproc.CHAIN_APPROX_SIMPLE;

public class ImageProcess extends Activity {
    Mat imgRGBA, m1,imgHSV;
    Bitmap bm;
    LinearLayout im_loading,im_gender;
    Button btn_male,btn_female;
    static double foot_lengh,foot_width,h2b_lengh,mpj_width;
    static {
        if (!OpenCVLoader.initDebug()) {
            // Handle initialization error
        }
    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                    File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    Log.d("LoadImageFromFile:", path.toString());
                    String filename = currentFileName;
                    m1 = Imgcodecs.imread(currentFileName);
                    //Utils.bitmapToMat(getZippedBitmap(),m1);
                    //Imgcodecs.imwrite(CurrentTime()+"getZippedBitmap.jpg", m1);

                    if (m1.empty()) {
                        Toast.makeText(ImageProcess.this, filename, Toast.LENGTH_SHORT).show();
                    } else {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                im_loading = (LinearLayout) findViewById(R.id.impc_loading);
                                im_gender = (LinearLayout) findViewById(R.id.impc_gender);
                                //壓縮圖片 3.縮放並壓縮
                                Mat imggray = m1.clone();
                                imggray = grayImage(imggray); //灰階
                                System.out.println("grayImage");
                                imggray = matBinarized(imggray); //二值化
                                System.out.println("matBinarized");

                                imggray = invGrey(imggray); //黑白互換
                                System.out.println("invGrey");

                                imggray = ExtractNLargestBlobs(imggray); //取最大物件;
                                System.out.println("ExtractNLargestBlobs");

                                Point p1 = EthanOCV_Utils.calculateConvexHull(EthanOCV_Utils.getContours(imggray).get(0))[0];
                                Point p2 = EthanOCV_Utils.calculateConvexHull(EthanOCV_Utils.getContours(imggray).get(0))[1];
                                Point p3 = EthanOCV_Utils.calculateConvexHull(EthanOCV_Utils.getContours(imggray).get(0))[2];
                                Point p4 = EthanOCV_Utils.calculateConvexHull(EthanOCV_Utils.getContours(imggray).get(0))[3];
                                System.out.println("Point");

                                Mat imgHSV_sheet = imggray.clone();
                                imgHSV_sheet.convertTo(imgHSV_sheet,Imgproc.COLOR_GRAY2RGB);
                                Imgproc.circle(imgHSV_sheet, p1, 15, new Scalar(255,60,0),10);
                                Imgproc.circle(imgHSV_sheet, p2, 15, new Scalar(255,60,0),10);
                                Imgproc.circle(imgHSV_sheet, p3, 15, new Scalar(255,60,0),10);
                                Imgproc.circle(imgHSV_sheet, p4, 15, new Scalar(255,60,0),10);
                                System.out.println("circle");

                                Point p1cut=p1;p1cut.x=p1cut.x+20;p1cut.y=p1cut.y+20;
                                Point p2cut=p2;p2cut.x=p2cut.x-40;p2cut.y=p2cut.y+40;
                                Point p3cut=p3;p3cut.x=p3cut.x+40;p3cut.y=p3cut.y-40;
                                Point p4cut=p4;p4cut.x=p4cut.x-40;p4cut.y=p4cut.y-40;
                                System.out.println("Point");

                                m1 = grayImage(m1); //灰階
                                System.out.println("grayImage");

                                m1 = invGrey(m1); //黑白互換
                                System.out.println("invGrey");

                                m1 = FillTop(m1,p1,p2,p3,p4); //填滿上半部
                                System.out.println("FillTop");

                                m1 = FillBot(m1,p1,p2,p3,p4);//填滿下半部
                                System.out.println("FillBot");

                                m1 = FillLeft(m1,p1,p2,p3,p4);//填滿左半部
                                System.out.println("FillLeft");

                                m1 = FillRight(m1,p1,p2,p3,p4);//填滿右半部
                                System.out.println("FillRight");

                                m1=sobelEdge(m1);//邊緣化
                                System.out.println("sobelEdge");

                                m1 = halfFoot(m1);//填滿右半部
                                System.out.println("halfFoot");

                                m1 = matBinarized(m1); //二值化
                                System.out.println("matBinarized");

                                m1=dilate(m1,2,0);
                                System.out.println("dilate");

                                m1 = ExtractNLargestBlobs(m1); //取最大物件;
                                System.out.println("ExtractNLargestBlobs");

                                Point bpoint = findBotPoint(m1,p1,p2,p3,p4);
                                Point tpoint = findTopPoint(m1,p1,p2,p3,p4,bpoint);
                                Point lengthPoint = findfootlengthPoint(m1,p1,p2,p3,p4);
                                Imgproc.circle(m1,bpoint,10,new Scalar(255,255,255),6);
                                System.out.println("findpoint");

                                Imgproc.circle(m1,tpoint,10,new Scalar(255,255,255),6);
                                Imgproc.circle(m1,lengthPoint,10,new Scalar(255,255,255),6);
                                Imgcodecs.imwrite(currentFileName+"point.jpg",m1);
                                System.out.println("circle");
                                double K=WhiteMultiplier(p1,p2,p3,p4);
                                //double whiteK=297/(p3.x-p4.x);A4長度
                                System.out.println("WhiteMultiplier");

                                mpj_width=CalculationJointWidth(tpoint,bpoint,K);
                                Point footpoint=findfootlengthPoint(m1,p1,p2,p3,p4);
                                foot_lengh=CalculationFootLength(p2,footpoint,K);
                                foot_width=CalculationFootWidth(tpoint,bpoint,K);
                                h2b_lengh=CalculationJointlength(p2,bpoint,K);
                                System.out.printf("趾骨寬:%f",mpj_width);
                                System.out.println("");
                                System.out.printf("腳長:%f",foot_lengh);
                                System.out.println("");
                                System.out.printf("腳寬:%f",foot_width);
                                System.out.println("");
                                System.out.printf("趾骨長:%f",h2b_lengh);
                                System.out.println("");
                                System.out.println("單位mm");
/*
                        Bitmap bm = Bitmap.createBitmap(imgRGBA.cols(), imgRGBA.rows(),Bitmap.Config.ARGB_8888);
                        Utils.matToBitmap(imgRGBA, bm);
                         bm = Bitmap.createBitmap(m1.cols(), imgHSV.rows(), Bitmap.Config.ARGB_8888);
                         Utils.matToBitmap(m1, bm);*/
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        im_loading.setVisibility(View.INVISIBLE);
                                        im_gender.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                        }).start();


                    }

                    break;
                default:
                    super.onManagerConnected(status);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_process);

        btn_male = (Button) findViewById(R.id.btn_male);
        btn_female = (Button) findViewById(R.id.btn_female);
        btn_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.gender = 1;
                Intent intent = new Intent(ImageProcess.this,FootAnalysis.class);
                Bundle bundle = new Bundle();
                //foot_lengh,foot_width,h2b_lengh,mpj_width;
                bundle.putDouble("foot_lengh",foot_lengh);
                bundle.putDouble("foot_width",foot_width);
                bundle.putDouble("h2b_lengh",h2b_lengh);
                bundle.putDouble("mpj_width",mpj_width);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        btn_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.gender = 2;
                Intent intent = new Intent(ImageProcess.this,FootAnalysis.class);
                Bundle bundle = new Bundle();
                //foot_lengh,foot_width,h2b_lengh,mpj_width;
                bundle.putDouble("foot_lengh",foot_lengh);
                bundle.putDouble("foot_width",foot_width);
                bundle.putDouble("h2b_lengh",h2b_lengh);
                bundle.putDouble("mpj_width",mpj_width);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    public static Point findfootlengthPoint(Mat mat,Point p1,Point p2,Point p3,Point p4) {
        Mat srcClone = mat.clone();
        int flag = 0;
        Point firstpoint = new Point();
        int col = 1;
        int row = 20;
        while(col <p2.x-1 && flag != 1) {
            while (row <p3.y-1 && flag != 1) {
                if(srcClone.get(row,col)[0] == 255.0) {
                    firstpoint = new Point(col,row);
                    flag = 1;
                }
                row += 2;
            }
            row = 20;
            col += 2;
        }

        return firstpoint;
    }
    public static Point findTopPoint(Mat mat,Point p1,Point p2,Point p3,Point p4,Point ad) {
        Mat srcClone = mat.clone();
        Mat srcClone2 = mat.clone();
        int flag = 0;
        Point topP = new Point();
        Point topP2 = new Point();
        Point botP;
        int length=(int)(p2.x+(p1.x-p2.x)*0.7);
        int length2=(int)(p2.x+(p1.x-p2.x)*0.5);
        int ss=(int)ad.x;
        int row = 20;
        int col = ss+10;

        while (row < srcClone.rows()-1 && flag != 1) {
            while (col < length2-1 && flag != 1) {
                if(srcClone.get(row,col)[0] == 255.0) {
                    topP = new Point(col,row);
                    flag = 1;
                }
                col += 2;
            }
            col = ss + 10;
            row += 2;

        }

        int row1 = 10;
        int col1 = length2;
        int flag2 = 0;

        while (row1 < srcClone.rows()-1 && flag2 != 1) {
            while (col1 > ss+50 && flag2 != 1) {
                if(srcClone2.get(row1,col1)[0] == 255.0) {
                    Point point = new Point(col1,row1);
                    topP2 = new Point(col1,row1);
                    flag2 = 1;
                }
                col1 -= 2;
            }
            col1 = length2;
            row1 += 2;
        }

        topP.x=Math.ceil((topP.x+topP2.x)/2);
        return topP;
    }
    public static Point findBotPoint(Mat mat,Point p1,Point p2,Point p3,Point p4) {
        Mat srcClone = mat.clone();
        Mat srcClone2 = mat.clone();
        int flag = 0;
        int flag1 = 0;
        Point botP = new Point();
        Point botP2 = new Point();
        int length=(int)(p2.x+(p1.x-p2.x)*0.85);
        int length2=(int)(p2.x+(p1.x-p2.x)*0.55);

        int row = srcClone.rows()-1;
        int col = length;

        while (row > 20 && flag != 1 ) {
            while (col < length2-1 && flag != 1) {
                if(srcClone.get(row,col)[0] == 255.0) {
                    botP = new Point(col,row);
                    flag = 1;
                }
                col += 2;
            }
            col = length;
            row -= 2;
        }
        int row1 = srcClone.rows()-1;
        int col1 = length2;
        while (row1 > 20 && flag1 != 1) {
            while (col1 > length+1 && flag1 != 1) {
                if(srcClone2.get(row1,col1)[0] == 255.0) {
                    botP2 = new Point(col1,row1);
                    flag1 = 1;
                }
                col1 -= 2;
            }
            col1 = length2;
            row1 -= 2;
        }
        botP.x=Math.ceil((botP.x+botP2.x)/2);
        return botP;
    }

    public static Mat FillTop(Mat image_original,Point p1,Point p2,Point p3,Point p4) {
        Mat image_output = image_original.clone();
        Point l_stP = new Point(0,0);
        Point l_endP = new Point(0,image_output.height());
        Point r_stP = new Point(image_output.width(),0);
        Point r_endP = new Point(image_output.width(),image_output.height());
        Point l_stdP = new Point(0,((p1.y>p2.y)?p1.y:p2.y));
        Point r_stdP = new Point(image_output.width(),((p1.y>p2.y)?p1.y:p2.y));
        l_stdP.y=l_stdP.y+20;
        r_stdP.y=r_stdP.y+20;
        List<Point> allPoint1 = new ArrayList<>();
        allPoint1.add(0,r_stP);
        allPoint1.add(1,l_stP);
        allPoint1.add(2,l_stdP);
        allPoint1.add(3,r_stdP);
        MatOfPoint mop1 = new MatOfPoint();
        List<MatOfPoint> allMatOfPoint1 = new ArrayList<MatOfPoint>();
        mop1.fromList(allPoint1);
        allMatOfPoint1.add(mop1);
        Imgproc.fillPoly(image_output,allMatOfPoint1,new Scalar(0,0,0));
        return image_output;
    }
    public static Mat FillBot(Mat image_original,Point p1,Point p2,Point p3,Point p4) {
        Mat image_output = image_original.clone();
        Point l_stP = new Point(0,0);
        Point l_endP = new Point(0,image_output.height());
        Point r_stP = new Point(image_output.width(),0);
        Point r_endP = new Point(image_output.width(),image_output.height());
        Point l_stdP = new Point(0,((p3.y<p4.y)?p3.y:p4.y));
        Point r_stdP = new Point(image_output.width(),((p3.y<p4.y)?p3.y:p4.y));
        r_stdP.y=r_stdP.y-10;
        l_stdP.y=l_stdP.y-10;
        List<Point> allPoint1 = new ArrayList<>();
        allPoint1.add(0,r_stdP);
        allPoint1.add(1,l_stdP);
        allPoint1.add(2,l_endP);
        allPoint1.add(3,r_endP);
        MatOfPoint mop1 = new MatOfPoint();
        List<MatOfPoint> allMatOfPoint1 = new ArrayList<MatOfPoint>();
        mop1.fromList(allPoint1);
        allMatOfPoint1.add(mop1);
        Imgproc.fillPoly(image_output,allMatOfPoint1,new Scalar(0,0,0));
        return image_output;
    }
    public static Mat FillLeft(Mat image_original,Point p1,Point p2,Point p3,Point p4) {
        Mat image_output = image_original.clone();
        Point l_stP = new Point(0,0);
        Point l_endP = new Point(0,image_output.height());
        Point r_stP = new Point(image_output.width(),0);
        Point r_endP = new Point(image_output.width(),image_output.height());
        Point t_stdP = new Point(((p1.x>p4.x)?p1.x:p4.x),0);
        Point b_stdP = new Point(((p1.x>p4.x)?p1.x:p4.x),image_output.height());
        List<Point> allPoint1 = new ArrayList<>();
        allPoint1.add(0,l_stP);
        allPoint1.add(1,t_stdP);
        allPoint1.add(2,b_stdP);
        allPoint1.add(3,l_endP);
        t_stdP.x=t_stdP.x+10;
        b_stdP.x=b_stdP.x+10;
        MatOfPoint mop1 = new MatOfPoint();
        List<MatOfPoint> allMatOfPoint1 = new ArrayList<MatOfPoint>();
        mop1.fromList(allPoint1);
        allMatOfPoint1.add(mop1);
        Imgproc.fillPoly(image_output,allMatOfPoint1,new Scalar(0,0,0));
        return image_output;
    }
    public static Mat FillRight(Mat image_original,Point p1,Point p2,Point p3,Point p4) {
        Mat image_output = image_original.clone();
        Point l_stP = new Point(0,0);
        Point l_endP = new Point(0,image_output.height());
        Point r_stP = new Point(image_output.width(),0);
        Point r_endP = new Point(image_output.width(),image_output.height());
        Point t_stdP = new Point(((p2.x<p3.x)?p2.x:p3.x),0);
        Point b_stdP = new Point(((p2.x<p3.x)?p2.x:p3.x),image_output.height());

        List<Point> allPoint1 = new ArrayList<>();
        allPoint1.add(0,t_stdP);
        allPoint1.add(1,r_stP);
        allPoint1.add(2,r_endP);
        allPoint1.add(3,b_stdP);
        MatOfPoint mop1 = new MatOfPoint();
        List<MatOfPoint> allMatOfPoint1 = new ArrayList<MatOfPoint>();
        mop1.fromList(allPoint1);
        allMatOfPoint1.add(mop1);
        Imgproc.fillPoly(image_output,allMatOfPoint1,new Scalar(0,0,0));
        return image_output;
    }
    public static Mat halfFoot(Mat image_original) {
        Mat image_output = image_original.clone();
        Point l_stP = new Point(0,0);
        Point l_endP = new Point(0,image_output.height());
        Point r_stP = new Point(image_output.width(),0);
        Point r_endP = new Point(image_output.width(),image_output.height());
        Point t_hfP = new Point(image_output.width()/1.5,0);
        Point b_hfP = new Point(image_output.width()/1.5,image_output.height());
        List<Point> allPoint1 = new ArrayList<>();
		        /*allPoint1.add(0,l_stP);
		        allPoint1.add(1,r_stP);
		        allPoint1.add(2,p2);
		        allPoint1.add(3,p1);*/
        allPoint1.add(0,t_hfP);
        allPoint1.add(1,r_stP);
        allPoint1.add(2,r_endP);
        allPoint1.add(3,b_hfP);
        MatOfPoint mop1 = new MatOfPoint();
        List<MatOfPoint> allMatOfPoint1 = new ArrayList<MatOfPoint>();
        mop1.fromList(allPoint1);
        allMatOfPoint1.add(mop1);
        Imgproc.fillPoly(image_output,allMatOfPoint1,new Scalar(0,0,0));
        return image_output;
    }
    public static Mat grayImage(Mat srcMat){
        Mat grayImg=new Mat();
        Imgproc.cvtColor(srcMat,grayImg,Imgproc.COLOR_RGB2GRAY);
        return grayImg;
    }
    public static Mat removeNoiseGaussianBlur(Mat srcMat){
        final Mat blurredImage=new Mat();
        Size size=new Size(7,7);
        Imgproc.GaussianBlur(srcMat,blurredImage,size,0,0);
        return blurredImage;
    }
    public static Mat ExtractNLargestBlobs(Mat mat) {
        //Size size = new Size(3,3);
        //Imgproc.blur(mat,mat,size);
        //mat = sobelEdge(mat);
        final List<MatOfPoint> contours = new ArrayList<>();
        final Mat hierarchy = new Mat();
        Imgproc.findContours(mat,contours,hierarchy,Imgproc.RETR_TREE,Imgproc.CHAIN_APPROX_SIMPLE);
        Mat contourImg = new Mat(mat.size(), mat.type());
        int temp=0;
        int flag=0;
        int former_flag=0;
        for (int i = 0; i < contours.size(); i++) {
            Integer value = Integer.valueOf(contours.get(i).size().toString().replace("1x",""));
            if(value > temp) {
                flag = i;
                temp = value;
            }
        }

        Imgproc.drawContours(contourImg, contours, flag,new Scalar(255,255,255), -2);


        return contourImg;
    }

    public static Mat enhanceBright(Mat mat) {
        int rows = mat.rows();
        int cols = mat.cols();
        int ch = mat.channels();
        double[] data = {};
        for(int i=0;i <rows;i++) {
            for(int j=0;j <cols;j++) {
                for(int k=0;k <ch;k++) {
                    data = mat.get(i,j);
                    data[k] = data[k] * 1.5;
                }
                mat.put(i,j,data);
            }
        }
        return mat;
    }
    public static Mat enhanceBright1(Mat mat) {
        int rows = mat.rows();
        int cols = mat.cols();
        int ch = mat.channels();
        double[] data = {};
        for(int i=0;i <rows;i++) {
            for(int j=0;j <cols;j++) {
                for(int k=0;k <ch;k++) {
                    data = mat.get(i,j);
                    data[k] = data[k] * 3;
                }
                mat.put(i,j,data);
            }
        }
        return mat;
    }
    public static Mat invGrey(Mat mat) {
        Core.bitwise_not(mat,mat);
        return mat;
    }

    public static Mat matBinarized(Mat mat) {
        Mat mat_bi = new Mat(mat.rows(),mat.cols(),CvType.CV_8UC1);
        Imgproc.threshold(mat,mat_bi,0,255,Imgproc.THRESH_BINARY|Imgproc.THRESH_OTSU);
        return mat_bi;
    }
    public static double WhiteMultiplier(Point p1,Point p2,Point p3,Point p4) {
        double WhiteMultiplier=363.7/Math.sqrt((Math.pow((p2.x-p4.x),2)+Math.pow((p2.y-p4.y),2)));
        return WhiteMultiplier;
    }
    public static double CalculationJointWidth(Point tpoint,Point bpoint,double K) {
        double jointwidth;
        jointwidth=Math.sqrt(Math.pow((tpoint.x-bpoint.x),2)+Math.pow((tpoint.y-bpoint.y),2))*K;
        return jointwidth;
    }
    public static double CalculationFootLength(Point a,Point b,double K) {
        double FootLength= Math.abs(a.x-b.x)*K;
        return FootLength;
    }
    public static double CalculationFootWidth(Point tpoint,Point bpoint,double K) {
        double FootWidth=Math.abs((tpoint.y-bpoint.y))*K;
        return FootWidth;
    }
    public static double CalculationJointlength(Point p2,Point bpoint,double K) {
        double Jointlength=(p2.x-bpoint.x)*K;
        return Jointlength;
    }
    public static Mat dilate(Mat source,int ksize,int shape) {
        Mat destination =new Mat(source.rows(),source.cols(),source.type());
        Mat element=Imgproc.getStructuringElement(shape, new Size(2*ksize+1,2*ksize+1));
        Imgproc.dilate(source, destination,element);
        return destination;
    }
    public static Mat erode(Mat source,int ksize,int shape) {
        Mat destination =new Mat(source.rows(),source.cols(),source.type());
        Mat element=Imgproc.getStructuringElement(shape, new Size(2*ksize+1,2*ksize+1));
        Imgproc.erode(source, destination,element);
        return destination;
    }

    public static Mat sobelEdge(Mat srcMat) {
        Mat xDervative=new Mat();
        Mat yDervative=new Mat();
        int ddepth= CvType.CV_16S;
        Imgproc.Sobel(srcMat,xDervative,ddepth,1,0);
        Imgproc.Sobel(srcMat,yDervative,ddepth,0,1);
        Mat absXD=new Mat();
        Mat absYD=new Mat();
        Core.convertScaleAbs(xDervative,absXD);
        Core.convertScaleAbs(yDervative,absYD);
        Mat edgeImage=new Mat();
        Core.addWeighted(absXD,0.5,absYD,0.5,0,edgeImage);
        return edgeImage;
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



    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            //Internal OpenCV library not found. Using OpenCV Manager for initialization
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            //OpenCV library found inside package. Using it!
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }
}