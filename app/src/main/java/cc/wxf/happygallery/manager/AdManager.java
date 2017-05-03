package cc.wxf.happygallery.manager;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qq.e.ads.banner.ADSize;
import com.qq.e.ads.banner.AbstractBannerADListener;
import com.qq.e.ads.banner.BannerView;
import com.qq.e.ads.interstitial.AbstractInterstitialADListener;
import com.qq.e.ads.interstitial.InterstitialAD;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;

import cc.wxf.happygallery.R;
import cc.wxf.happygallery.ui.HomeActivity;
import cc.wxf.happygallery.ui.SplashActivity;

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
    // 是否展示广告
    private static final boolean IS_OPEN_AD = false;

    public static boolean isOpenAd(){
        return IS_OPEN_AD;
    }

    /**
     *  展示开屏广告
     * @param activity
     * @param container 广告容器
     * @param skipView 跳过控件
     * @param splashHolder 默认广告
     * @param time 展示时间，[3000-5000]，0表示默认
     */
    public static void showSplashAD(final SplashActivity activity, ViewGroup container, final TextView skipView, final View splashHolder, int time){
        if(IS_OPEN_AD){
            new SplashAD(activity, container, skipView, AdManager.APP_ID, AdManager.SPLASH_ID, new SplashADListener() {
                @Override
                public void onADDismissed() {
                    if (activity.isCanJump()) {
                        activity.startActivity(new Intent(activity, HomeActivity.class));
                        activity.finish();
                    } else {
                        activity.setCanJump(true);
                    }
                }

                @Override
                public void onNoAD(int i) {
                    activity.startActivity(new Intent(activity, HomeActivity.class));
                    activity.finish();
                }

                @Override
                public void onADPresent() {
                    splashHolder.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onADClicked() {

                }

                @Override
                public void onADTick(long millisUntilFinished) {
                    skipView.setText(String.format(activity.getString(R.string.skip_text), Math.round(millisUntilFinished / 1000f)));
                }
            }, time);
        }
    }

    /**
     *  展示Banner广告
     * @param activity
     * @param bannerContainer 广告容器
     * @param bannerClose 广告关闭按钮
     */
    public static void showBannerAD(Activity activity, final ViewGroup bannerContainer, final ImageView bannerClose){
        if(IS_OPEN_AD){
            final BannerView bannerView = new BannerView(activity, ADSize.BANNER, APP_ID, BANNER_ID);
            bannerView.setRefresh(30);
            bannerView.setADListener(new AbstractBannerADListener() {
                @Override
                public void onNoAD(int i) {
                    bannerClose.setVisibility(View.GONE);
                }

                @Override
                public void onADReceiv() {
                    bannerClose.setVisibility(View.VISIBLE);
                    bannerClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bannerClose.setVisibility(View.GONE);
                            bannerContainer.removeAllViews();
                            if(bannerView != null){
                                bannerView.destroy();
                            }
                        }
                    });
                }
            });
            bannerContainer.addView(bannerView);
            bannerView.loadAD();
        }
    }

    /**
     *  展示插屏广告
     * @param activity
     */
    public static void showInterteristalAD(final Activity activity) {
        if(IS_OPEN_AD){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(activity.isFinishing()){
                        return;
                    }
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
}
