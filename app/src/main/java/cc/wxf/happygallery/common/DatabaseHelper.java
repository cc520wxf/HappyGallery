package cc.wxf.happygallery.common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cc.wxf.happygallery.bean.GalleryItem;
import cc.wxf.happygallery.bean.GalleryPage;

/**
 * Created by chenchen on 2017/4/1.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "happy_gallery";
    private static final int DB_VERSION = 1;

    private static DatabaseHelper instance;

    private List<Class> classes = new ArrayList<Class>();

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    public static DatabaseHelper getInstance(){
        if(instance == null){
            synchronized (DatabaseHelper.class){
                if(instance == null){
                    instance = new DatabaseHelper(GalleryApplication.getInstance());
                }
            }
        }
        return instance;
    }

    public void init(){
        classes.add(GalleryPage.class);
        classes.add(GalleryItem.class);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        for(Class clazz : classes){
            try {
                TableUtils.createTable(connectionSource, clazz);
            } catch (SQLException e) {
                e.printStackTrace();
                Log.w(DatabaseHelper.class.getSimpleName(), "the table init error:" + clazz.getSimpleName());
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        for(Class clazz : classes){
            try {
                TableUtils.dropTable(connectionSource, clazz, true);
            } catch (SQLException e) {
                e.printStackTrace();
                Log.w(DatabaseHelper.class.getSimpleName(), "the table drop error:" + clazz.getSimpleName());
            }
        }
        onCreate(sqLiteDatabase, connectionSource);
    }
}
