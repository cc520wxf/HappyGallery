package cc.wxf.happygallery.manager;

import android.os.Handler;
import android.os.Message;

import java.util.List;

import cc.wxf.happygallery.bean.GalleryPage;
import cc.wxf.happygallery.thread.ParseListThread;
import cc.wxf.happygallery.bean.Config;

/**
 * Created by chenchen on 2017/3/30.
 */

public class ListManager {

    public static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.104 Safari/537.36 Core/1.53.2141.400 QQBrowser/9.5.10219.400";
    public static int TIME_OUT = 10 * 1000;

    private static ListManager instance;

    private int currentPage = 1;

    private ListManager() {
    }

    public static ListManager getInstance(){
        if(instance == null){
            instance = new ListManager();
        }
        return instance;
    }

    public interface OnParseListListener{

        int SUCCESS = 1000;
        int ERROR_NETWORK = 1001;
        int ERROR_SERVER = 1002;

        void onSuccess(List<GalleryPage> galleryPages);
        void onError(int errorCode);
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void parse(Config config, final OnParseListListener listener){
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == OnParseListListener.SUCCESS){
                    if(listener != null){
                        listener.onSuccess((List<GalleryPage>) msg.obj);
                    }
                }else{
                    if(listener != null){
                        listener.onError(msg.what);
                    }
                }
            }
        };
        new ParseListThread(config, handler, currentPage++).start();
    }
}
