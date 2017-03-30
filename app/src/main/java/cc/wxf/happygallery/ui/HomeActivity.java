package cc.wxf.happygallery.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cc.wxf.happygallery.R;
import cc.wxf.happygallery.common.Config;
import cc.wxf.happygallery.util.Util;

public class HomeActivity extends FullScreenActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View contentView = View.inflate(this, R.layout.activity_home, null);
        setContentView(contentView);
        initView((ViewGroup) contentView);
    }

    private void initView(ViewGroup contentView) {
        List<Config> configs = Config.getConfig(this);
        for(int i = 0; i < contentView.getChildCount(); i++){
            ViewGroup horizontalViewGroup = (ViewGroup) contentView.getChildAt(i);
            for(int j = 0; j < horizontalViewGroup.getChildCount(); j++){
                ViewGroup itemViewGroup = (ViewGroup) horizontalViewGroup.getChildAt(j);
                ImageView imageView = (ImageView) itemViewGroup.getChildAt(0);
                TextView textView = (TextView) itemViewGroup.getChildAt(1);
                final Config config = configs.get(i * horizontalViewGroup.getChildCount() + j);
                itemViewGroup.setBackgroundResource(Util.getDrawableByName(this, config.getBackground()));
                imageView.setImageResource(Util.getDrawableByName(this, config.getIcon()));
                textView.setText(config.getName());
                itemViewGroup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HomeActivity.this, ListActivity.class);
                        intent.putExtra("Config", config);
                        startActivity(intent);
                    }
                });
            }
        }
    }

}
