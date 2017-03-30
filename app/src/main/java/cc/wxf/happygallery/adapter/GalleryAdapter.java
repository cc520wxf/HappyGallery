package cc.wxf.happygallery.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import cc.wxf.happygallery.common.Config;

/**
 * Created by chenchen on 2017/3/30.
 */

public class GalleryAdapter extends PagerAdapter {

    private Context context;
    private List<Config> configs;

    public GalleryAdapter(Context context, List<Config> configs) {
        this.context = context;
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
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        imageView.setImageBitmap(configs.get(position).getBitmap());
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
