package com.example.asus.download;

import android.app.Application;

import com.lzy.okgo.OkGo;

/**
 * Created by asus on 2018/4/12.
 */

public class Gapp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        /**
         * 全局初始化OkGo
         */
        OkGo.getInstance().init(this);
    }
}
