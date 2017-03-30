package cc.wxf.happygallery.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by chenchen on 2017/3/30.
 */

public class ImmerseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //设置无标题栏
        super.onCreate(savedInstanceState);
    }
}
