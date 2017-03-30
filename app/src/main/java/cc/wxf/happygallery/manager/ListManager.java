package cc.wxf.happygallery.manager;

import android.os.Handler;
import android.os.Message;

import cc.wxf.happygallery.ParseListThread;
import cc.wxf.happygallery.bean.Config;

/**
 * Created by chenchen on 2017/3/30.
 */

public class ListManager {

    public static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.104 Safari/537.36 Core/1.53.2141.400 QQBrowser/9.5.10219.400";
    public static String CONTENT_TYPE = "application/x-javascript";
    public static int TIME_OUT = 10 * 1000;

    private static ListManager instance;

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

        void onSuccess();
        void onError(int errorCode);
    }

    public void parse(Config config, int page, final OnParseListListener listener){
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what == OnParseListListener.SUCCESS){
                    if(listener != null){
                        listener.onSuccess();
                    }
                }else{
                    if(listener != null){
                        listener.onError(msg.what);
                    }
                }
            }
        };
        new ParseListThread(config, handler, page).start();
    }
}
