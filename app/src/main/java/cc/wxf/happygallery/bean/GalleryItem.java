package cc.wxf.happygallery.bean;

import java.io.Serializable;

/**
 * Created by chenchen on 2017/3/31.
 */

public class GalleryItem implements Serializable{

    private String imgUrl;
    private String title;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "GalleryItem{" +
                "imgUrl='" + imgUrl + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
