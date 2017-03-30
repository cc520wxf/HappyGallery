package cc.wxf.happygallery;

import android.os.Handler;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import cc.wxf.happygallery.bean.Config;
import cc.wxf.happygallery.manager.ListManager;
import cc.wxf.happygallery.util.Util;

/**
 * Created by chenchen on 2017/3/30.
 */

public class ParseListThread extends Thread {

    private Config config;
    private Handler handler;
    private int page;

    public ParseListThread(Config config, Handler handler, int page) {
        this.config = config;
        this.handler = handler;
        this.page = page;
    }

    @Override
    public void run() {
        String url = Util.getListUrl(config, page);
        Log.i(ListManager.class.getSimpleName(), url);
        try {
            Document document = Jsoup.connect(url).userAgent(ListManager.USER_AGENT).ignoreContentType(true).timeout(ListManager.TIME_OUT).post();
            if (document == null) {
                handler.sendEmptyMessage(ListManager.OnParseListListener.ERROR_SERVER);
            } else {
                Elements liItems = document.select("li");
                if(liItems == null || liItems.size() == 0){
                    handler.sendEmptyMessage(ListManager.OnParseListListener.ERROR_SERVER);
                }else{
                    for(Element liItem : liItems){
                        Elements aItems = liItem.select("a");
                        if(aItems == null || aItems.size() != 2){
                            handler.sendEmptyMessage(ListManager.OnParseListListener.ERROR_SERVER);
                        }else{
                            //第一个包含了跳转URL，缩略图；第二个包含了标题
                            Element aItem0 = aItems.get(0);
                            String href = aItem0.attr("href");
                            Log.i(ListManager.class.getSimpleName(), href);
                            try{
                                String pic = aItem0.select("img").get(0).attr("src");
                                Log.i(ListManager.class.getSimpleName(), pic);
                            }catch (Exception e){
                            }
                            Element aItem1 = aItems.get(1);
                            String name = aItem1.text();
                            Log.i(ListManager.class.getSimpleName(), name);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            handler.sendEmptyMessage(ListManager.OnParseListListener.ERROR_NETWORK);
        }
    }
}
