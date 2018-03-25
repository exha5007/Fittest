package com.example.acer.fittest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import java.io.ByteArrayInputStream;
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
                    Utils.bitmapToMat(getZippedBitmap(),m1);

                    if (m1.empty()) {
                        Toast.makeText(ImageProcess.this, filename, Toast.LENGTH_SHORT).show();
                    } else {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                im_loading = (LinearLayout) findViewById(R.id.impc_loading);
                                im_gender = (LinearLayout) findViewById(R.id.impc_gender);
                                //壓縮圖片 3.縮放並壓縮
                                Mat imgHSV = m1.clone();

                                Imgproc.cvtColor(imgHSV,imgHSV,Imgproc.COLOR_RGB2HSV,3);
                                List<Mat> hsv = new ArrayList<Mat>(3);
                                Core.split(imgHSV,hsv);
                                imgHSV = hsv.get(1).clone();// hsv_s
                                imgHSV=enhanceBright1(imgHSV);
                                System.out.println("enhanceBright1");
                                imgHSV = matBinarized(imgHSV); //二值化
                                System.out.println("matBinarized");
                                imgHSV = invGrey(imgHSV); //黑白互換
                                System.out.println("invGrey");
                                imgHSV = ExtractNLargestBlobs(imgHSV); //取最大物件;
                                System.out.println("ExtractNLargestBlobs");
                                Point p1 = EthanOCV_Utils.calculateConvexHull(EthanOCV_Utils.getContours(imgHSV).get(0))[0];
                                Point p2 = EthanOCV_Utils.calculateConvexHull(EthanOCV_Utils.getContours(imgHSV).get(0))[1];
                                Point p3 = EthanOCV_Utils.calculateConvexHull(EthanOCV_Utils.getContours(imgHSV).get(0))[2];
                                Point p4 = EthanOCV_Utils.calculateConvexHull(EthanOCV_Utils.getContours(imgHSV).get(0))[3];
                                Mat imgHSV_sheet = imgHSV.clone();
                                imgHSV_sheet.convertTo(imgHSV_sheet,Imgproc.COLOR_GRAY2RGB);
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
                                Point p22 =new Point();p22=p2;
                                Point p33 =new Point();p33=p3;
                                p33.x=p3.x-10;
                                p22.x=p2.x-10;
                                m1 = FillRight(m1,p1,p22,p33,p4);//填滿右半部
                                System.out.println("FillRight");

                                m1 = matBinarized(m1); //二值化
                                m1=dilate(m1,3,0);
                                System.out.println("matBinarized");

                                m1 = ExtractNLargestBlobs(m1); //取最大物件;
                                System.out.println("ExtractNLargestBlobs");
                                m1 = halfFoot(m1);//保留關注區域
                                System.out.println("halfFoot");

                                Point ad=findBotPoint(m1,p1,p2,p3,p4);
                                System.out.println("ad");

                                Imgcodecs.imwrite(CurrentTime()+"findPoint.jpg", m1);

                                Point tpoint=findTopPoint(m1,p1,p2,p3,p4,ad);
                                System.out.println("tpoint");

                                Point bpoint=findBotPoint(m1,p1,p2,p3,p4);
                                System.out.println("bpoint");

                                double K=WhiteMultiplier(p1,p2,p3,p4);
                                System.out.println("WhiteMultiplier");

                                //double whiteK=297/(p3.x-p4.x);A4長度
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
                                shoeSize s=new shoeSize();
                                System.out.println(s.transform("男", foot_lengh, mpj_width, foot_width));


/*
                        Bitmap bm = Bitmap.createBitmap(imgRGBA.cols(), imgRGBA.rows(),Bitmap.Config.ARGB_8888);
                        Utils.matToBitmap(imgRGBA, bm);

                        */
                                bm = Bitmap.createBitmap(m1.cols(), imgHSV.rows(), Bitmap.Config.ARGB_8888);
                                Utils.matToBitmap(m1, bm);
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
        for(int col=1;col<p2.x-1;col++) {
            if(flag == 1) {
                break;
            }
            for(int row=20;row<p3.y-1;row++) {
                if(flag == 1) {
                    break;
                }
                if(srcClone.get(row,col)[0] == 255.0) {
                    Point point = new Point(col,row);
                    firstpoint = new Point(col,row);
                    flag = 1;
                }
            }
        }
        return firstpoint;
    }
    public static Point findTopPoint(Mat mat,Point p1,Point p2,Point p3,Point p4,Point ad) {
        Mat srcClone = mat.clone();
        Mat srcClone2 = mat.clone();
        int flag = 0;
        int flag2 = 0;
        Point topP = new Point();
        Point topP2 = new Point();
        Point botP;
        int length=(int)(p2.x+(p1.x-p2.x)*0.7);
        int length2=(int)(p2.x+(p1.x-p2.x)*0.5);
        int ss=(int)ad.x;
        for(int row=20;row<srcClone.rows()-1;row++) {
            if(flag == 1) {
                break;
            }
            for(int col=ss+10;col<length2-1;col++) {
                if(flag == 1) {
                    break;
                }
                if(srcClone.get(row,col)[0] == 255.0) {
                    Point point = new Point(col,row);
                    topP = new Point(col,row);
                    flag = 1;
                }
            }
        }
        for(int row=10;row<srcClone.rows()-1;row++) {
            if(flag2 == 1) {
                break;
            }
            for(int col=length2;col>ss+50;col--) {
                if(flag2 == 1) {
                    break;
                }
                if(srcClone2.get(row,col)[0] == 255.0) {
                    Point point = new Point(col,row);
                    topP2 = new Point(col,row);
                    flag2 = 1;
                }
            }
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
        for(int row=srcClone.rows()-1;row>20;row--) {
            if(flag == 1) {
                break;
            }
            for(int col=length;col<length2-1;col++) {
                if(flag == 1) {
                    break;
                }
                if(srcClone.get(row,col)[0] == 255.0) {
                    Point point = new Point(col,row);
                    botP = new Point(col,row);
                    flag = 1;
                }
            }
        }
        for(int row=srcClone.rows()-1;row>20;row--) {
            if(flag1 == 1) {
                break;
            }
            for(int col=length2;col>length+1;col--) {
                if(flag1 == 1) {
                    break;
                }
                if(srcClone2.get(row,col)[0] == 255.0) {
                    Point point = new Point(col,row);
                    botP2 = new Point(col,row);
                    flag1 = 1;
                }
            }
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
        List<MatOfInt> hull = new ArrayList<MatOfInt>(contours.size());
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
         int width;
         int height;
         double alpha = 2;
         double beta = 50;
        Mat destination = new Mat(mat.rows(),mat.cols(),mat.type());
        mat.convertTo(destination,-1,alpha,beta);
        return destination;
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
    public static double CalculationDistance(Point tpoint,Point bpoint) {
        double jointwidth;
        jointwidth=Math.sqrt(Math.pow((tpoint.x-bpoint.x),2)+Math.pow((tpoint.y-bpoint.y),2));
        return jointwidth;
    }
    public static Mat sobelEdge(Mat srcMat) {
        Mat xDervative=new Mat();
        Mat yDervative=new Mat();
        /*一般?入?片的depth与?出?片的depth值相同，但是?我?一???的?候?出???-255—255，使用unsignded 8-bit depth只能包含0-255，因此采用了16-bit*/
        int ddepth= CvType.CV_16S;
        //?算x,y的一???，??1，0??置x，y?
        Imgproc.Sobel(srcMat,xDervative,ddepth,1,0);
        Imgproc.Sobel(srcMat,yDervative,ddepth,0,1);
        Mat absXD=new Mat();
        Mat absYD=new Mat();
        //Mat??
        Core.convertScaleAbs(xDervative,absXD);
        Core.convertScaleAbs(yDervative,absYD);
        //根?（x*x+y*y）
        Mat edgeImage=new Mat();
        Core.addWeighted(absXD,0.5,absYD,0.5,0,edgeImage);
        return edgeImage;
    }

    public static String CurrentTime() {
        String currentTime = String.format("/sdcard/%d.jpg",
                System.currentTimeMillis());
        return currentTime;
    }

    public static Mat cannyEdge(Mat srcMat){
        final Mat edgeImage=new Mat();
        Imgproc.Canny(srcMat,edgeImage,100,200);
        return edgeImage;
        //100低阈值 200：高阈值
    }/*
    public Mat filter(final Mat dist) {
    }*/
    public static void findWidthPoint(Mat mat) {
        Mat srcClone = mat.clone();
        int flag = 0;
        for(int row=20;row<srcClone.rows();row++) {
                if(flag == 1) {
                    break;
                }
            for(int col=20;col<srcClone.cols();col++) {
                if(flag == 1) {
                    break;
                }
                if(srcClone.get(row,col)[0] == 255.0) {
                    Point point = new Point(col,row);
                    Log.d("AAAAB",row+"row"+col+"col");
                    flag = 1;
                }
            }
        }
    }



    public static Point findTopPoint(Mat mat, Point p1, Point p2, Point p3, Point p4) {
        Mat srcClone = mat.clone();
        int flag = 0;
        Point topP = new Point();
        Point botP;
        for(int row=20;row<srcClone.rows()-1;row++) {
            if(flag == 1) {
                break;
            }
            for(int col=20;col<srcClone.cols()-1;col++) {
                if(flag == 1) {
                    break;
                }
                if(srcClone.get(row,col)[0] == 255.0) {
                    Point point = new Point(col,row);
                    System.out.printf("row=%d,col=%d\n",row,col);
                    topP = new Point(col,row);
                    flag = 1;
                }
            }
        }
        return topP;
    }
    public static Point findBotPoint(Mat mat) {
        Mat srcClone = mat.clone();
        int flag = 0;
        Point botP = new Point();
        for(int row=srcClone.rows()-1;row>20;row--) {
            if(flag == 1) {
                break;
            }
            for(int col=20;col<srcClone.cols()-1;col++) {
                if(flag == 1) {
                    break;
                }
                if(srcClone.get(row,col)[0] == 255.0) {
                    Point point = new Point(col,row);
                    System.out.printf("row=%d,col=%d\n",row,col);
                    botP = new Point(col,row);
                    flag = 1;
                }
            }
        }
        return botP;
    }

    public static void convexDot(Mat mat) {
        Mat thresh_output = new Mat(mat.rows(),mat.cols(),mat.type());
        Mat target = mat.clone();
        Mat srcClone = mat.clone();
        Mat hierarchy = new Mat(target.rows(),target.cols(), CvType.CV_8UC1,new Scalar(0));
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Imgproc.findContours(thresh_output,contours,hierarchy, Imgproc.RETR_TREE,CHAIN_APPROX_SIMPLE);
        for(int i=0;i<contours.size();i++) {
            MatOfInt hull = new MatOfInt();
            MatOfPoint temContour = contours.get(i);
            //針對每一個外圍輪廓進行凸包運算
            Imgproc.convexHull(temContour,hull,false);
            //匯出該外圍輪廓的凸多邊形
            int index = (int)hull.get(((int)hull.size().height)-1,0)[0];
            Point pt,pt0 = new Point(temContour.get(index,0)[0],temContour.get(index,0)[1]);
            for(int j=0;j<hull.size().height-1;j++) {
                index = (int) hull.get(j,0)[0];
                pt = new Point(temContour.get(index,0)[0],temContour.get(index,0)[0]);
                pt0 = pt;
            }
        }
        String currentTime = String.format("/sdcard/good%d.jpg",
                System.currentTimeMillis());
        Imgcodecs.imwrite(currentTime,srcClone);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, //計算圖片的縮放值
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }
    public static Bitmap getSmallBitmap(String filePath) {  //根據路徑獲得圖片並壓縮，回傳bitmap用於顯示
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }
    public static String bitmapToString(String filePath) { //把bitmap 轉換成 string

        Bitmap bm = getSmallBitmap(filePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }
    public static Bitmap getZippedBitmap() {
        Bitmap bmp = BitmapFactory.decodeFile(currentFileName);
        int ratio = 2;
        // 压缩Bitmap到对应尺寸
        Bitmap result = Bitmap.createBitmap(bmp.getWidth() / ratio, bmp.getHeight() / ratio, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Rect rect = new Rect(0, 0, bmp.getWidth() / ratio, bmp.getHeight() / ratio);
        canvas.drawBitmap(bmp, null, rect, null);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 把压缩后的数据存放到baos中
        result.compress(Bitmap.CompressFormat.JPEG, 100 ,baos);
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
        /*BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(currentFileName,options);
        options.inSampleSize = calculateInSampleSize(options,240,400);
        options.inJustDecodeBounds = false;
        Bitmap zipped_bitmap = BitmapFactory.decodeFile(currentFileName,options);*/
    }


    public static void saveBitmapToFile(Bitmap bmp){
        try {
            // 取得外部儲存裝置路徑
            String path = Environment.getExternalStorageDirectory().toString ();
            // 開啟檔案
            String nowTime = String.format("/sdcard/%d.jpg",
                    System.currentTimeMillis());
            File file = new File( nowTime);
            // 開啟檔案串流
            FileOutputStream out = new FileOutputStream(file );
            // 將 Bitmap壓縮成指定格式的圖片並寫入檔案串流
            bmp.compress ( Bitmap. CompressFormat.JPEG , 90 , out);
            // 刷新並關閉檔案串流
            out.flush ();
            out.close ();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace ();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace ();
        }
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