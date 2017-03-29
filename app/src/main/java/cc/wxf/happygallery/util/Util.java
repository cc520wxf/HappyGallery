package cc.wxf.happygallery.util;

import android.content.Context;

/**
 * Created by chenchen on 2017/3/29.
 */

public class Util {

    public static int getDrawableByName(Context context, String name){
        return context.getResources().getIdentifier(name, "drawable", context.getPackageName());
    }
}
