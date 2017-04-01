package cc.wxf.happygallery.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by chenchen on 2017/3/31.
 */
@DatabaseTable(tableName = GalleryItem.TABLE_NAME)
public class GalleryItem implements Serializable{

    public static final String TABLE_NAME = "gallery_item";

    public interface ${
        String id = "id";
        String imgUrl = "img_url";
        String title = "title";
        String pageId = "page_id";
    }

    @DatabaseField(generatedId = true)
    private long id;
    @DatabaseField(columnName = $.imgUrl)
    private String imgUrl;
    @DatabaseField(columnName = $.title)
    private String title;
    @DatabaseField(columnName = $.pageId)
    private long pageId;

    public GalleryItem() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public long getPageId() {
        return pageId;
    }

    public void setPageId(long pageId) {
        this.pageId = pageId;
    }

    @Override
    public String toString() {
        return "GalleryItem{" +
                "id=" + id +
                ", imgUrl='" + imgUrl + '\'' +
                ", title='" + title + '\'' +
                ", pageId=" + pageId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if(o == null){
            return false;
        }
        if(!(o instanceof GalleryItem)){
            return false;
        }
        GalleryItem other = (GalleryItem) o;
        return imgUrl.equals(other.getImgUrl()) && title.equals(other.getTitle()) && pageId == other.getPageId();
    }
}
