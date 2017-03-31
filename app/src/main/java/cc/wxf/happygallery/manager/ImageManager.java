package cc.wxf.happygallery.manager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

    private String readHTML(Context context){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open("templete")));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null){
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String createHTML(Context context, List<GalleryItem> galleryItems){
        String html = readHTML(context);
        if(html == null){
            return null;
        }
        Document document = Jsoup.parse(html);
        if(document == null){
            return null;
        }
        try{
            Element body = document.body();
            //生成div
            for(GalleryItem item : galleryItems){
                Element div = body.appendElement("div");
                Element img = div.appendElement("img");
                img.attr("src", item.getImgUrl());
                div.appendElement("br");
                Element p = div.appendElement("p");
                p.text(item.getTitle());
                body.appendElement("br");
            }
            return document.toString();
        }catch (Exception e){
            return null;
        }
    }
}
