package cc.wxf.happygallery.common;

import android.app.Application;

/**
 * Created by chenchen on 2017/3/30.
 */

public class GalleryApplication extends Application {

    private static GalleryApplication instance;

    public static GalleryApplication getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //初始化数据库
        DatabaseHelper.getInstance().init();
    }
}
