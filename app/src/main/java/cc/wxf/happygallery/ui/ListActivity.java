package cc.wxf.happygallery.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import cc.wxf.happygallery.R;
import cc.wxf.happygallery.bean.Config;
import cc.wxf.happygallery.bean.GalleryPage;
import cc.wxf.happygallery.manager.ListManager;

/**
 * Created by cc520wxf on 2017/3/29.
 */

public class ListActivity extends ImmerseActivity {

    private Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        initConfig();
        initParse();
    }

    private void initParse() {
        ListManager.getInstance().parse(config, 1, new ListManager.OnParseListListener() {
            @Override
            public void onSuccess(List<GalleryPage> galleryPages) {
                for(GalleryPage page : galleryPages){
                    Log.i("ListActivty", page.toString());
                }
            }

            @Override
            public void onError(int errorCode) {
                Toast.makeText(ListActivity.this, "errorCode=" + errorCode, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initConfig() {
        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("Config")){
            config = (Config) intent.getSerializableExtra("Config");
        }
    }
}
