package cc.wxf.happygallery.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import cc.wxf.happygallery.bean.Config;
import cc.wxf.happygallery.ui.ListActivity;

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
    public Object instantiateItem(ViewGroup container, final int position) {
        ImageView imageView = new ImageView(context);
        imageView.setImageBitmap(configs.get(position).getBitmap());
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ListActivity.class);
                intent.putExtra("Config", configs.get(position));
                context.startActivity(intent);
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
