package cc.wxf.happygallery.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import cc.wxf.happygallery.R;
import cc.wxf.happygallery.bean.GalleryItem;
import cc.wxf.happygallery.bean.GalleryPage;
import cc.wxf.happygallery.manager.ImageManager;

/**
 * Created by chenchen on 2017/3/31.
 */

public class ImageActivity extends ImmerseActivity {

    private GalleryPage galleryPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        initGalleryPage();
        initParse();
    }

    private void initParse() {
        ImageManager.getInstance().parsePage(galleryPage, new ImageManager.OnParsePageListener() {
            @Override
            public void onSuccess(List<GalleryItem> galleryItems) {
                for(GalleryItem galleryItem : galleryItems){
                    Log.i(ImageActivity.class.getSimpleName(), galleryItem.toString());
                }
            }

            @Override
            public void onError(int errorCode) {
                Toast.makeText(ImageActivity.this, String.format(getString(R.string.parse_error), errorCode), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initGalleryPage() {
        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("GalleryPage")){
            galleryPage = (GalleryPage) intent.getSerializableExtra("GalleryPage");
        }
    }
}
