package cc.wxf.happygallery.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by cc520wxf on 2017/3/30.
 */
@DatabaseTable(tableName = GalleryPage.TABLE_NAME)
public class GalleryPage implements Serializable{

    public static final String TABLE_NAME = "gallery_page";

    public interface ${
        String id = "id";
        String title = "title";
        String iconUrl = "icon_url";
        String url = "url";
        String configId = "config_id";
    }

    @DatabaseField(generatedId = true)
    private long id;
    @DatabaseField(columnName = $.title)
    private String title;
    @DatabaseField(columnName = $.iconUrl)
    private String iconUrl;
    @DatabaseField(columnName = $.url)
    private String url;
    @DatabaseField(columnName = $.configId)
    private int configId;

    public GalleryPage() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public int getConfigId() {
        return configId;
    }

    public void setConfigId(int configId) {
        this.configId = configId;
    }

    @Override
    public String toString() {
        return "GalleryPage{" +
                "configId=" + configId +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if(o == null){
            return false;
        }
        if(!(o instanceof GalleryPage)){
            return false;
        }
        GalleryPage other = (GalleryPage) o;
        return iconUrl.equals(other.getIconUrl()) && title.equals(other.getTitle()) && url.equals(other.getUrl()) && configId == other.getConfigId();
    }
}
