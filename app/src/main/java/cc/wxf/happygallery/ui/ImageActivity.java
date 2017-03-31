package cc.wxf.happygallery.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
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
    private View loadingView;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        initGalleryPage();
        initTitle();
        initLoading();
        initWebView();
        initParse();
    }

    private void initWebView() {
        webView = (WebView) findViewById(R.id.webView);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        // 初始缩放
        webView.setInitialScale(50);
        // 支持无限缩放
        settings.setUseWideViewPort(true);
        // 支持双击缩放
        settings.setSupportZoom(true);
        // 自适应屏幕
        settings.setUseWideViewPort(true);
        // 自适应图片
        settings.setLoadsImagesAutomatically(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        // 设置默认编码
        settings.setDefaultTextEncodingName("utf-8");
    }

    private void initLoading() {
        loadingView = findViewById(R.id.loadingView);
    }

    private void initTitle() {
        TextView titleView = (TextView) findViewById(R.id.title);
        titleView.setText(galleryPage == null ? getString(R.string.app_name) : galleryPage.getTitle());
        findViewById(R.id.back_txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initParse() {
        if(galleryPage == null){
            Toast.makeText(ImageActivity.this, getString(R.string.unknown_error), Toast.LENGTH_SHORT).show();
            return;
        }
        ImageManager.getInstance().parsePage(galleryPage, new ImageManager.OnParsePageListener() {
            @Override
            public void onSuccess(List<GalleryItem> galleryItems) {
                loadingView.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                String html = ImageManager.getInstance().createHTML(ImageActivity.this, galleryItems);
                webView.loadData(html, "text/html; charset=UTF-8", "utf-8");
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
