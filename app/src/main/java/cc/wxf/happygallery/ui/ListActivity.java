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
import cc.wxf.happygallery.manager.OfflineManager;

/**
 * Created by cc520wxf on 2017/3/29.
 */

public class ListActivity extends ImmerseActivity {

    private Config config;
    private View loadMoreView;
    private RecyclerView recyclerView;
    private View loadingView;
    private ListAdapter adapter;
    private List<GalleryPage> galleryPages = new ArrayList<GalleryPage>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        initConfig();
        initTitle();
        initLoading();
        initRecyclerView();
        ListManager.getInstance().setCurrentPage(1);
        parseList();
    }

    private void initLoading() {
        loadingView = findViewById(R.id.loadingView);
    }

    private void initRecyclerView() {
        loadMoreView = findViewById(R.id.loadMoreView);
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        //设置瀑布流布局
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        //设置Adapter
        adapter = new ListAdapter(this, galleryPages);
        recyclerView.setAdapter(adapter);
        //设置padding
        recyclerView.addItemDecoration(adapter.new SpacesItemDecoration(5));
        //设置滑动到底部监听器
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int[] result = layoutManager.findLastVisibleItemPositions(null);
                    if (Math.max(result[0], result[1]) == recyclerView.getAdapter().getItemCount() - 1) {
                        //触发加载更多
                        loadMoreView.setVisibility(View.VISIBLE);
                        parseList();
                    }
                }
            }
        });
    }

    private void initTitle() {
        TextView titleView = (TextView) findViewById(R.id.title);
        titleView.setText(config == null ? getString(R.string.app_name) : config.getName());
        findViewById(R.id.back_txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void parseList() {
        if(config == null){
            Toast.makeText(ListActivity.this, getString(R.string.unknown_error), Toast.LENGTH_SHORT).show();
            return;
        }
        ListManager.getInstance().parse(config, new ListManager.OnParseListListener() {
            @Override
            public void onSuccess(List<GalleryPage> pages) {
                loadingView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                loadMoreView.setVisibility(View.GONE);
                galleryPages.addAll(pages);
                adapter.notifyDataSetChanged();
                //保存Config外链
                for(GalleryPage item : pages){
                    item.setConfigId(config.getId());
                }
                //插入数据库
                OfflineManager.getInstance().saveGalleryPage(pages);
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
