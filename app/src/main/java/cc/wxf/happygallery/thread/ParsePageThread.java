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

import cc.wxf.happygallery.bean.GalleryItem;
import cc.wxf.happygallery.bean.GalleryPage;
import cc.wxf.happygallery.manager.ImageManager;
import cc.wxf.happygallery.manager.ListManager;
import cc.wxf.happygallery.util.Util;

/**
 * Created by chenchen on 2017/3/31.
 */

public class ParsePageThread extends Thread {

    private GalleryPage galleryPage;
    private Handler handler;
    private List<GalleryItem> galleryItems = new ArrayList<GalleryItem>();
    private boolean hasThrowException = false;

    public ParsePageThread(GalleryPage galleryPage, Handler handler) {
        this.galleryPage = galleryPage;
        this.handler = handler;
    }

    @Override
    public void run() {
        parse(1, true, true);
        if(galleryItems.size() != 0){
            Message message = Message.obtain();
            message.what = ImageManager.OnParsePageListener.SUCCESS;
            message.obj = galleryItems;
            handler.sendMessage(message);
        }else{
            if(!hasThrowException){
                handler.sendEmptyMessage(ImageManager.OnParsePageListener.ERROR_SERVER);
            }
        }
    }

    private void parse(int page, boolean needQueryPageSize, boolean shouldThrowException){
        String url = Util.getPageUrl(galleryPage, page);
        try {
            Document document = Jsoup.connect(url).userAgent(ListManager.USER_AGENT).ignoreContentType(true).timeout(ListManager.TIME_OUT).post();
            if(document != null){
                //可能会有不同的模板，排除这个模板
                try {
                    //爬取所有Items
                    galleryItems.addAll(getGalleryItems(document));
                    //查询是否有更多页，有的话直接加载
                    if(needQueryPageSize){
                        int allPageSize = getAllPageSize(document);
                        for(int i = 1; i <= allPageSize; i++){
                            //加载其他页
                            if(i != page){
                                parse(i, false, false);
                            }
                        }
                    }
                }catch (Exception e){
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            if(shouldThrowException){
                hasThrowException = true;
                handler.sendEmptyMessage(ImageManager.OnParsePageListener.ERROR_NETWORK);
            }
        }
    }

    private int getAllPageSize(Document document){
        try{
            Elements aItems = document.select("div#yw0").get(0).select("a");
            if(aItems == null || aItems.size() == 0){
                return 1;
            }else{
                return aItems.size();
            }
        }catch (Exception e){
            e.printStackTrace();
            return 1;
        }
    }

    private List<GalleryItem> getGalleryItems(Document document){
        Elements divItems = document.select("div.pic-box");
        List<GalleryItem> galleryItems = new ArrayList<GalleryItem>();
        if(divItems != null && divItems.size() > 0){
            for(Element divItem : divItems){
                try{
                    String imgUrl = divItem.select("span.pic-box-item").get(0).attr("data-img");
                    String title = divItem.select("p.comment").get(0).text();
                    GalleryItem galleryItem = new GalleryItem();
                    galleryItem.setImgUrl(imgUrl);
                    galleryItem.setTitle(title);
                    galleryItems.add(galleryItem);
                }catch (Exception e){
                }
            }
        }
        return galleryItems;
    }
}
