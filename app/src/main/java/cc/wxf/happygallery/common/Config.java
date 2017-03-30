package cc.wxf.happygallery.common;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import cc.wxf.happygallery.R;
import cc.wxf.happygallery.util.Util;

/**
 * Created by zhangjiancheng on 16/10/21.
 */

public class Config implements Serializable{
    private static final String TAG_MAP = "map";
    private static final String TAG_ARRAY = "array";
    private static final String TAG_ENTRY = "entry";

    private String name;
    private String url;
    private String icon;
    private Bitmap bitmap;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static List<Config> getConfig(Context context){
        XmlResourceParser parser = context.getResources().getXml(R.xml.config);
        List<Config> configList = new LinkedList<Config>();
        Config config = null;
        try {
            while(parser.getEventType() != XmlResourceParser.END_DOCUMENT){
                int type = parser.getEventType();
                switch (type) {
                    case XmlResourceParser.START_DOCUMENT:

                        break;
                    case XmlResourceParser.START_TAG:
                        String name = parser.getName();
                        if (TAG_ARRAY.equals(name)) {

                        }else if (TAG_MAP.equals(name)) {
                            config = new Config();
                        }else if(TAG_ENTRY.equals(name)){
                            String key = parser.getAttributeValue(0);
                            String value = parser.nextText();
                            if(key.equals("name")){
                                config.setName(value);
                            }else if(key.equals("url")){
                                config.setUrl(value);
                            }else if(key.equals("icon")){
                                config.setIcon(value);
                                //将处理过的Bitmap放入
                                config.setBitmap(Util.getReverseBitmapById(Util.getDrawableByName(context, value), context));
                            }
                        }
                        break;
                    case XmlResourceParser.END_TAG:
                        if (TAG_MAP.equals(parser.getName())){
                            configList.add(config);
                        }
                        break;
                }
                parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(Config.class.getSimpleName(), "parse xml error");
        }
        return configList;
    }

}
