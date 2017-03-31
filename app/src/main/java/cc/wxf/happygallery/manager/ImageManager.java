package cc.wxf.happygallery.manager;

import android.os.Handler;
import android.os.Message;

import java.util.List;

import cc.wxf.happygallery.bean.GalleryItem;
import cc.wxf.happygallery.bean.GalleryPage;
import cc.wxf.happygallery.thread.ParsePageThread;

/**
 * Created by chenchen on 2017/3/31.
 */

public class ImageManager {

    public static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.104 Safari/537.36 Core/1.53.2141.400 QQBrowser/9.5.10219.400";
    public static int TIME_OUT = 10 * 1000;

    private static ImageManager instance;

    private ImageManager() {
    }

    public static ImageManager getInstance(){
        if(instance == null){
            instance = new ImageManager();
        }
        return instance;
    }

    public interface OnParsePageListener{
        int SUCCESS = 1000;
        int ERROR_NETWORK = 1001;
        int ERROR_SERVER = 1002;

        void onSuccess(List<GalleryItem> galleryItems);
        void onError(int errorCode);
    }

    public void parsePage(final GalleryPage page, final OnParsePageListener listener){
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == ImageManager.OnParsePageListener.SUCCESS){
                    if(listener != null){
                        listener.onSuccess((List<GalleryItem>) msg.obj);
                    }
                }else {
                    if (listener != null) {
                        listener.onError(msg.what);
                    }
                }
            }
        };
        new ParsePageThread(page, handler).start();
    }
}
