package cc.wxf.happygallery.util;

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
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

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
        if(sourceBitmap != null && !sourceBitmap.isRecycled()){
            sourceBitmap.recycle();
        }
        if(inverseBitmap != null && !inverseBitmap.isRecycled()){
            inverseBitmap.recycle();
        }
        return groupbBitmap;
    }

    public static void startZoomInAnim(final View animView, final float multiple){
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

    private static void startZoomOutAnim(final View animView, final float multiple){
        ScaleAnimation zoomOut = new ScaleAnimation(1.0f * multiple, 1.0f, 1.0f * multiple, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        zoomOut.setDuration(200);
        animView.startAnimation(zoomOut);
    }
}
