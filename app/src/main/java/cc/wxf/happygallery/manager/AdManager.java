package cc.wxf.happygallery.manager;

import android.app.Activity;
import android.os.Handler;

import com.qq.e.ads.interstitial.AbstractInterstitialADListener;
import com.qq.e.ads.interstitial.InterstitialAD;

/**
 * Created by chenchen on 2017/5/2.
 */

public class AdManager {
    //应用ID
    public static final String APP_ID = "1101152570";
    //开屏广告位ID
    public static final String SPLASH_ID = "8863364436303842593";
    //Banner广告位
    public static final String BANNER_ID = "9079537218417626401";
    //插屏广告
    public static final String INTERTERISTAL_ID = "8575134060152130849";
    //插屏广告弹出延迟
    public static final int INTERTERISTAL_DELAY = 2000;

    public static void showInterteristalAD(final Activity activity) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final InterstitialAD ad = new InterstitialAD(activity, APP_ID, INTERTERISTAL_ID);
                ad.setADListener(new AbstractInterstitialADListener() {
                    @Override
                    public void onADReceive() {
                        ad.show();
                    }

                    @Override
                    public void onNoAD(int i) {

                    }
                });
                ad.loadAD();
            }
        }, INTERTERISTAL_DELAY);
    }
}
