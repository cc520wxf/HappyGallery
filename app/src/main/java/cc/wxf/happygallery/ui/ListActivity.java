package cc.wxf.happygallery.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qq.e.ads.banner.ADSize;
import com.qq.e.ads.banner.AbstractBannerADListener;
import com.qq.e.ads.banner.BannerView;

import java.util.ArrayList;
import java.util.List;

import cc.wxf.happygallery.R;
import cc.wxf.happygallery.adapter.ListAdapter;
import cc.wxf.happygallery.bean.Config;
import cc.wxf.happygallery.bean.GalleryPage;
import cc.wxf.happygallery.manager.AdManager;
import cc.wxf.happygallery.manager.ListManager;
import cc.wxf.happygallery.manager.OfflineManager;
import cc.wxf.happygallery.util.Util;

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
        initBanner();
    }

    private void initBanner() {
        final ImageView bannerClose = (ImageView) findViewById(R.id.banner_close);
        final ViewGroup bannerContainer = (ViewGroup) findViewById(R.id.bannerContainer);
        final BannerView bannerView = new BannerView(this, ADSize.BANNER, AdManager.APP_ID, AdManager.BANNER_ID);
        bannerView.setRefresh(30);
        bannerView.setADListener(new AbstractBannerADListener() {
            @Override
            public void onNoAD(int i) {
                bannerClose.setVisibility(View.GONE);
            }

            @Override
            public void onADReceiv() {
                bannerClose.setVisibility(View.VISIBLE);
                bannerClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bannerClose.setVisibility(View.GONE);
                        bannerContainer.removeAllViews();
                        if(bannerView != null){
                            bannerView.destroy();
                        }
                    }
                });
            }
        });
        bannerContainer.addView(bannerView);
        bannerView.loadAD();
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
        if(Util.isCollectionEmpty(galleryPages)){
            //为空，则网络获取
            ListManager.getInstance().parse(config, new ListManager.OnParseListListener() {
                @Override
                public void onSuccess(List<GalleryPage> pages) {
                    loadingView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    loadMoreView.setVisibility(View.GONE);
                    //将当前集合和最新集合取并集
                    OfflineManager.getInstance().fusionGalleryPage(galleryPages, pages);
                    adapter.notifyDataSetChanged();
                    //插入数据库
                    OfflineManager.getInstance().saveGalleryPage(config, pages);
                }

                @Override
                public void onError(int errorCode) {
                    Toast.makeText(ListActivity.this, String.format(getString(R.string.parse_error), errorCode), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            //不为空，则加载本地数据库
            loadingView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            loadMoreView.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }
    }

    private void initConfig() {
        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("Config")){
            config = (Config) intent.getSerializableExtra("Config");
        }
        //从数据库中查找离线
        if(config != null){
            galleryPages.addAll(OfflineManager.getInstance().queryAllGalleryPage(config));
        }
    }
}
