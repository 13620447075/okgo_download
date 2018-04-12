package com.example.asus.download.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by asus on 2018/3/25.
 */

public class ToastUtil {

    private static boolean isShow = true;
    private static Toast mToast = null;

    private ToastUtil() {
        throw new UnsupportedOperationException("不能被实例化");
    }

    public static void controlShow(boolean isShowToast) {
        isShow = isShowToast;
    }

    public void cancelToast() {
        if (isShow && mToast != null) {
            mToast.cancel();
        }
    }

    public static void showShort(Context context, CharSequence message) {
        if (isShow) {
            if (mToast == null) {
                mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            } else {
                mToast.setText(message);
            }
            mToast.show();
        }
    }

    public static void showLong(Context context, CharSequence message) {
        if (isShow) {
            if (mToast == null) {
                mToast = Toast.makeText(context, message, Toast.LENGTH_LONG);
            } else {
                mToast.setText(message);
            }
            mToast.show();
        }
    }
}
