package cc.wxf.happygallery.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import cc.wxf.happygallery.bean.Config;
import cc.wxf.happygallery.manager.DialogManager;
import cc.wxf.happygallery.ui.ListActivity;
import cc.wxf.happygallery.util.Util;

/**
 * Created by chenchen on 2017/3/30.
 */

public class GalleryAdapter extends PagerAdapter {

    private Activity activity;
    private List<Config> configs;

    public GalleryAdapter(Activity activity, List<Config> configs) {
        this.activity = activity;
        this.configs = configs;
    }

    @Override
    public int getCount() {
        return configs == null ? 0 : configs.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ImageView imageView = new ImageView(activity);
        imageView.setImageBitmap(configs.get(position).getBitmap());
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Util.isWifiConnection(activity)){
                    DialogManager.getInstance().showWifiConfirmDialog(activity, new DialogManager.Callback() {
                        @Override
                        public void confirm() {
                            Intent intent = new Intent(activity, ListActivity.class);
                            intent.putExtra("Config", configs.get(position));
                            activity.startActivity(intent);
                        }

                        @Override
                        public void cancle() {
                            activity.finish();
                        }
                    });
                }else{
                    Intent intent = new Intent(activity, ListActivity.class);
                    intent.putExtra("Config", configs.get(position));
                    activity.startActivity(intent);
                }
            }
        });
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
