package cc.wxf.happygallery.bean;

import java.io.Serializable;

/**
 * Created by cc520wxf on 2017/3/30.
 */

public class GalleryPage implements Serializable{

    private String title;
    private String iconUrl;
    private String url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "GalleryPage{" +
                "title='" + title + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
