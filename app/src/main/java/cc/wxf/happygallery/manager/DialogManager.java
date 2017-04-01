package cc.wxf.happygallery.manager;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import cc.wxf.happygallery.R;

/**
 * Created by chenchen on 2017/4/1.
 */

public class DialogManager {

    private static DialogManager instance;

    private DialogManager() {
    }

    public static DialogManager getInstance(){
        if(instance == null){
            instance = new DialogManager();
        }
        return instance;
    }

    public interface Callback{
        void confirm();
        void cancle();
    }

    private void showTwoButtonDialog(final Activity activity, int contentStrId, int confirmStrId, int cancleStrId, final Callback callback){
        View customView = LayoutInflater.from(activity).inflate(R.layout.dialog_two_buttons, null);
        final Dialog dialog = new Dialog(activity, R.style.mainDialogTheme);
        dialog.setContentView(customView);

        TextView dialogContent = (TextView) customView.findViewById(R.id.dialog_content);
        dialogContent.setText(contentStrId);
        TextView confirm = (TextView) customView.findViewById(R.id.dialog_confirm);
        confirm.setText(confirmStrId);
        confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(callback != null){
                    callback.confirm();
                }
            }
        });
        TextView cancel = (TextView) customView.findViewById(R.id.dialog_cancel);
        cancel.setText(cancleStrId);
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(callback != null){
                    callback.cancle();
                }
            }
        });

        dialog.setCancelable(false);
        if(!activity.isFinishing()){
            dialog.show();
        }
    }

    public void showWifiConfirmDialog(final Activity activity, final Callback callback){
        showTwoButtonDialog(activity, R.string.dialog_wifi_content, R.string.dialog_wifi_confirm, R.string.dialog_wifi_cancle, callback);
    }

    public void showDownloadDialog(final Activity activity, final Callback callback){
        showTwoButtonDialog(activity, R.string.dialog_download_content, R.string.dialog_download_confirm, R.string.dialog_download_cancle, callback);
    }
}
