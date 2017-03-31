package cc.wxf.happygallery.common;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import cc.wxf.happygallery.R;
import cc.wxf.happygallery.util.Util;

/**
 * Created by chenchen on 2017/3/31.
 */

public class DownloadReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
            Toast.makeText(context, String.format(context.getString(R.string.download_complete), Util.getDownloadDir().getAbsolutePath()), Toast.LENGTH_SHORT).show();
        } else if (DownloadManager.ACTION_NOTIFICATION_CLICKED.equals(intent.getAction())) {
            long[] ids = intent.getLongArrayExtra(DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS);
            //点击通知栏取消下载
            manager.remove(ids);
            Toast.makeText(context, context.getString(R.string.download_cancle), Toast.LENGTH_SHORT).show();
        }
    }

}
