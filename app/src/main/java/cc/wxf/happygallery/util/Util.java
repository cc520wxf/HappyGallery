package cc.wxf.happygallery.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import java.io.File;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cc.wxf.happygallery.R;
import cc.wxf.happygallery.bean.Config;
import cc.wxf.happygallery.bean.GalleryPage;
import cc.wxf.happygallery.common.GalleryApplication;

/**
 * Created by chenchen on 2017/3/29.
 */

public class Util {

    public static int getDrawableByName(Context context, String name) {
        return context.getResources().getIdentifier(name, "drawable", context.getPackageName());
    }

    public static Bitmap getReverseBitmapById(int resId, Context context) {
        Bitmap sourceBitmap = BitmapFactory.decodeResource(context.getResources(), resId);
        //绘制原图的下一半图片
        Matrix matrix = new Matrix();
        //倒影翻转
        matrix.setScale(1, -1);

        Bitmap inverseBitmap = Bitmap.createBitmap(sourceBitmap, 0, sourceBitmap.getHeight() / 2, sourceBitmap.getWidth(), sourceBitmap.getHeight() / 3, matrix, false);
        //合成图片
        Bitmap groupbBitmap = Bitmap.createBitmap(sourceBitmap.getWidth(), sourceBitmap.getHeight() + sourceBitmap.getHeight() / 3 + 60, sourceBitmap.getConfig());
        //以合成图片为画布
        Canvas gCanvas = new Canvas(groupbBitmap);
        //将原图和倒影图片画在合成图片上
        gCanvas.drawBitmap(sourceBitmap, 0, 0, null);
        gCanvas.drawBitmap(inverseBitmap, 0, sourceBitmap.getHeight() + 50, null);
        //添加遮罩
        Paint paint = new Paint();
        Shader.TileMode tileMode = Shader.TileMode.CLAMP;
        LinearGradient shader = new LinearGradient(0, sourceBitmap.getHeight() + 50, 0,
                groupbBitmap.getHeight(), Color.BLACK, Color.TRANSPARENT, tileMode);
        paint.setShader(shader);
        //这里取矩形渐变区和图片的交集
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        gCanvas.drawRect(0, sourceBitmap.getHeight() + 50, sourceBitmap.getWidth(), groupbBitmap.getHeight(), paint);
        if (sourceBitmap != null && !sourceBitmap.isRecycled()) {
            sourceBitmap.recycle();
        }
        if (inverseBitmap != null && !inverseBitmap.isRecycled()) {
            inverseBitmap.recycle();
        }
        return groupbBitmap;
    }

    public static void startZoomInAnim(final View animView, final float multiple) {
        ScaleAnimation zoomIn = new ScaleAnimation(1.0f, 1.0f * multiple, 1.0f, 1.0f * multiple,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        zoomIn.setDuration(200);
        zoomIn.setFillAfter(true);
        zoomIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //放大结束后开启缩小动画
                startZoomOutAnim(animView, multiple);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animView.startAnimation(zoomIn);
    }

    private static void startZoomOutAnim(final View animView, final float multiple) {
        ScaleAnimation zoomOut = new ScaleAnimation(1.0f * multiple, 1.0f, 1.0f * multiple, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        zoomOut.setDuration(200);
        animView.startAnimation(zoomOut);
    }

    public static String getListUrl(Config config, int page) {
        StringBuilder sb = new StringBuilder(config.getUrl());
        sb.append("?offset=").append(30 * (page - 1)).append("&order=created&math=").append(Math.random());
        return sb.toString();
    }

    public static String format(String source) {
        source = source.substring(0, source.indexOf("<"));
        return unicode2String(source);
    }

    public static String filter(String source) {
        String regEx = "[\\\"\\\\]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(source);
        return m.replaceAll("").trim();
    }

    public static String unicode2String(String unicode) {
        StringBuffer string = new StringBuffer();
        String[] hex = unicode.split("\\\\u");
        for (int i = 1; i < hex.length; i++) {
            try{
                if(hex[i].length() == 4){
                    // 转换出每一个代码点
                    int data = Integer.parseInt(hex[i], 16);
                    // 追加成string
                    string.append((char) data);
                }else if(hex[i].length() > 4){
                    String first = hex[i].substring(0, 4);
                    String second = hex[i].substring(4, hex[i].length());
                    int data = Integer.parseInt(first, 16);
                    string.append((char) data);
                    string.append(second);
                }
            }catch (Exception e){
                string.append(hex[i]);
            }
        }
        return string.toString();
    }

    public static String getPageUrl(GalleryPage galleryPage, int page){
        String url = galleryPage.getUrl();
        try{
            StringBuilder sb = new StringBuilder(url.replace("gallery", "scroll"));
            sb.insert(sb.lastIndexOf("."), "/" + page);
            return sb.toString();
        }catch (Exception e){
            return url;
        }
    }

    public static File getDownloadDir(){
        File dir = new File(Environment.getExternalStorageDirectory(), GalleryApplication.getInstance().getString(R.string.app_name));
        if(!dir.exists()){
            dir.mkdirs();
        }
        return dir;
    }

    public static boolean isWifiConnection(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    public static int[] getScreenSizeWithoutVirtualBar(Activity activity){
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return new int[]{dm.widthPixels, dm.heightPixels};
    }

    public static boolean isCollectionEmpty(Collection collection){
        if(collection == null){
            return true;
        }
        if(collection.isEmpty()){
            return true;
        }
        return false;
    }
}
