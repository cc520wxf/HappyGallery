package cc.wxf.happygallery.thread;

import android.os.Handler;
import android.os.Message;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cc.wxf.happygallery.bean.Config;
import cc.wxf.happygallery.bean.GalleryPage;
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
        try {
            Document document = Jsoup.connect(url).userAgent(ListManager.USER_AGENT).ignoreContentType(true).timeout(ListManager.TIME_OUT).post();
            if (document == null) {
                handler.sendEmptyMessage(ListManager.OnParseListListener.ERROR_SERVER);
            } else {
                Elements liItems = document.select("li");
                if(liItems == null || liItems.size() == 0){
                    handler.sendEmptyMessage(ListManager.OnParseListListener.ERROR_SERVER);
                }else{
                    List<GalleryPage> galleryPages = new ArrayList<GalleryPage>();
                    for(Element liItem : liItems){
                        Elements aItems = liItem.select("a");
                        if(aItems == null || aItems.size() < 2){
                            handler.sendEmptyMessage(ListManager.OnParseListListener.ERROR_SERVER);
                        }else{
                            GalleryPage galleryPage = new GalleryPage();
                            //第一个包含了跳转URL，缩略图；第二个包含了标题
                            Element aItem0 = aItems.get(aItems.size() - 2);
                            String href = Util.filter(aItem0.attr("href"));
                            galleryPage.setUrl(href);
                            try{
                                String icon = Util.filter(aItem0.select("img").get(0).attr("src"));
                                galleryPage.setIconUrl(icon);
                            }catch (Exception e){
                            }
                            Element aItem1 = aItems.get(aItems.size() - 1);
                            String title = Util.format(aItem1.text());
                            galleryPage.setTitle(title);
                            galleryPages.add(galleryPage);
                        }
                    }
                    //判断是否成功
                    if(galleryPages.size() > 0){
                        Message message = Message.obtain();
                        message.what = ListManager.OnParseListListener.SUCCESS;
                        message.obj = galleryPages;
                        handler.sendMessage(message);
                    }else{
                        handler.sendEmptyMessage(ListManager.OnParseListListener.ERROR_SERVER);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            handler.sendEmptyMessage(ListManager.OnParseListListener.ERROR_NETWORK);
        }
    }
}
