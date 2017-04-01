package cc.wxf.happygallery.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cc.wxf.happygallery.R;
import cc.wxf.happygallery.adapter.GalleryAdapter;
import cc.wxf.happygallery.bean.Config;
import cc.wxf.happygallery.util.GalleryPageTransformer;
import cc.wxf.happygallery.util.Util;

public class HomeActivity extends ImmerseActivity {

    private LinearLayout container;
    private ViewPager viewPager;
    private TextView nameView;

    private List<Config> configs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initConfig();
        initUI();
    }

    private void initUI() {
        container = (LinearLayout) findViewById(R.id.container);
        nameView = (TextView) findViewById(R.id.name);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(3);
        int pagerWidth = (int) (getResources().getDisplayMetrics().widthPixels * 3.0f / 5.0f);
        ViewGroup.LayoutParams params = viewPager.getLayoutParams();
        params.width = pagerWidth;
        viewPager.setLayoutParams(params);
        viewPager.setPageMargin(-50);
        container.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return viewPager.dispatchTouchEvent(motionEvent);
            }
        });
        viewPager.setPageTransformer(true, new GalleryPageTransformer());
        viewPager.setAdapter(new GalleryAdapter(this, configs));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeTipView(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(0);
        changeTipView(0);
    }

    private void initConfig() {
        configs = Config.getConfig(this);
    }

    private void changeTipView(int position){
        nameView.setText(configs.get(position).getName());
        Util.startZoomInAnim(nameView, 1.5f);
    }
}
