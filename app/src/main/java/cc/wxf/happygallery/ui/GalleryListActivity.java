package cc.wxf.happygallery.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import cc.wxf.happygallery.R;
import cc.wxf.happygallery.common.Config;

/**
 * Created by cc520wxf on 2017/3/29.
 */

public class GalleryListActivity extends Activity {

    private Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        initConfig();
    }

    private void initConfig() {
        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("Config")){
            config = (Config) intent.getSerializableExtra("Config");
        }
    }
}
