package cc.wxf.happygallery.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cc.wxf.happygallery.R;
import cc.wxf.happygallery.adapter.ListAdapter;
import cc.wxf.happygallery.bean.Config;
import cc.wxf.happygallery.bean.GalleryPage;
import cc.wxf.happygallery.manager.ListManager;

/**
 * Created by cc520wxf on 2017/3/29.
 */

public class ListActivity extends ImmerseActivity {

    private Config config;
    private TextView titleView;
    private RecyclerView recyclerView;
    private ListAdapter adapter;
    private List<GalleryPage> galleryPages = new ArrayList<GalleryPage>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        initConfig();
        initTitle();
        initRecyclerView();
        parseList(1);
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        adapter = new ListAdapter(this, galleryPages);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(adapter.new SpacesItemDecoration(5));
    }

    private void initTitle() {
        titleView = (TextView) findViewById(R.id.title);
        titleView.setText(config == null ? getString(R.string.app_name) : config.getName());
        findViewById(R.id.back_txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void parseList(int page) {
        ListManager.getInstance().parse(config, page, new ListManager.OnParseListListener() {
            @Override
            public void onSuccess(List<GalleryPage> pages) {
                galleryPages.addAll(pages);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int errorCode) {
                Toast.makeText(ListActivity.this, String.format(getString(R.string.parse_error), errorCode), Toast.LENGTH_SHORT).show();
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
