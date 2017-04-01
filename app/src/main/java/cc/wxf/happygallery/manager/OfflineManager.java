package cc.wxf.happygallery.manager;

import android.util.Log;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import cc.wxf.happygallery.bean.Config;
import cc.wxf.happygallery.bean.GalleryItem;
import cc.wxf.happygallery.bean.GalleryPage;
import cc.wxf.happygallery.common.DatabaseHelper;
import cc.wxf.happygallery.util.Util;

/**
 * Created by chenchen on 2017/4/1.
 */

public class OfflineManager {

    private static OfflineManager instance;

    private OfflineManager() {
    }

    public static OfflineManager getInstance() {
        if (instance == null) {
            instance = new OfflineManager();
        }
        return instance;
    }

    public List<GalleryPage> queryAllGalleryPage(Config config) {
        try {
            Dao dao = DatabaseHelper.getInstance().getDao(GalleryPage.class);
            return dao.queryBuilder().where().eq(GalleryPage.$.configId, config.getId()).query();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveGalleryPage(Config config, List<GalleryPage> galleryPages) {
        if (Util.isCollectionEmpty(galleryPages)) {
            return;
        }
        try {
            Dao dao = DatabaseHelper.getInstance().getDao(GalleryPage.class);
            List<GalleryPage> all = queryAllGalleryPage(config);
            if (Util.isCollectionEmpty(all)) {
                //数据库为空，直接插入
                for (GalleryPage item : galleryPages) {
                    //设置外链
                    item.setConfigId(config.getId());
                    try {
                        dao.create(item);
                    } catch (SQLException e) {
                        Log.w(OfflineManager.class.getSimpleName(), "saveGalleryPage 插入item[" + item.getTitle() + "]失败");
                    }
                }
            } else {
                //数据库不为空，进行对比筛选
                for (GalleryPage item : galleryPages) {
                    //设置外链
                    item.setConfigId(config.getId());
                    GalleryPage existItem = getExistItem(all, item);
                    if(existItem == null){
                        //说明数据库中没有这一条记录，直接插入
                        try {
                            dao.create(item);
                        } catch (SQLException e) {
                            Log.w(OfflineManager.class.getSimpleName(), "saveGalleryPage 插入item[" + item.getTitle() + "]失败");
                        }
                    }else{
                        item.setId(existItem.getId());
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Log.w(OfflineManager.class.getSimpleName(), "saveGalleryPage 获取DAO[" + GalleryPage.class.getSimpleName() + "]失败");
        }
    }

    private GalleryPage getExistItem(List<GalleryPage> all, GalleryPage item) {
        for (GalleryPage galleryPage : all) {
            if (item.equals(galleryPage)) {
                return galleryPage;
            }
        }
        return null;
    }

    public void fusionGalleryPage(List<GalleryPage> source, List<GalleryPage> dest){
        dest.removeAll(source);
        source.addAll(dest);
    }

    public List<GalleryItem> queryAllGalleryItem(GalleryPage page) {
        try {
            Dao dao = DatabaseHelper.getInstance().getDao(GalleryItem.class);
            return dao.queryBuilder().where().eq(GalleryItem.$.pageId, page.getId()).query();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveGalleryItem(GalleryPage page, List<GalleryItem> galleryItems) {
        if (Util.isCollectionEmpty(galleryItems)) {
            return;
        }
        try {
            Dao dao = DatabaseHelper.getInstance().getDao(GalleryItem.class);
            List<GalleryItem> all = queryAllGalleryItem(page);
            if (Util.isCollectionEmpty(all)) {
                //数据库为空，直接插入
                for (GalleryItem item : galleryItems) {
                    item.setPageId(page.getId());
                    try {
                        dao.create(item);
                    } catch (SQLException e) {
                        Log.w(OfflineManager.class.getSimpleName(), "saveGalleryItem 插入item[" + item.getTitle() + "]失败");
                    }
                }
            } else {
                //数据库不为空，进行对比筛选
                for (GalleryItem item : galleryItems) {
                    item.setPageId(page.getId());
                    GalleryItem existItem = getExistItem(all, item);
                    if(existItem == null){
                        //说明数据库中没有这一条记录，直接插入
                        try {
                            dao.create(item);
                        } catch (SQLException e) {
                            Log.w(OfflineManager.class.getSimpleName(), "saveGalleryItem 插入item[" + item.getTitle() + "]失败");
                        }
                    }else{
                        item.setId(existItem.getId());
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Log.w(OfflineManager.class.getSimpleName(), "saveGalleryItem 获取DAO[" + GalleryItem.class.getSimpleName() + "]失败");
        }
    }

    private GalleryItem getExistItem(List<GalleryItem> all, GalleryItem item) {
        for (GalleryItem galleryItem : all) {
            if (item.equals(galleryItem)) {
                return galleryItem;
            }
        }
        return null;
    }

    public void fusionGalleryItem(List<GalleryItem> source, List<GalleryItem> dest){
        dest.removeAll(source);
        source.addAll(dest);
    }
}
