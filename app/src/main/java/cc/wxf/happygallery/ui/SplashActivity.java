package cc.wxf.happygallery.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cc.wxf.happygallery.R;
import cc.wxf.happygallery.manager.AdManager;

/**
 * Created by chenchen on 2017/5/2.
 */

public class SplashActivity extends FullScreenActivity {

    public boolean canJump = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initAd();
    }

    private void initAd() {
        ViewGroup container = (ViewGroup) this.findViewById(R.id.splash_container);
        final TextView skipView = (TextView) findViewById(R.id.skip_view);
        final View splashHolder = findViewById(R.id.splash_holder);
        AdManager.showSplashAD(this, container, skipView, splashHolder, 0);
        //如果未开启广告，则直接跳转
        if(!AdManager.isOpenAd()){
            startActivity(new Intent(SplashActivity.this, HomeActivity.class));
            finish();
        }
    }

    public boolean isCanJump() {
        return canJump;
    }

    public void setCanJump(boolean canJump) {
        this.canJump = canJump;
    }

    @Override
    protected void onPause() {
        super.onPause();
        canJump = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (canJump) {
            next();
        }
        canJump = true;
    }

    private void next() {
        if (canJump) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        } else {
            canJump = true;
        }
    }

    /**
     * 开屏页一定要禁止用户对返回按钮的控制，否则将可能导致用户手动退出了App而广告无法正常曝光和计费
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
