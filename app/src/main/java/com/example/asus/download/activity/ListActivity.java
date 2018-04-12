package com.example.asus.download.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.asus.download.R;
import com.example.asus.download.adapter.ListAdapter;
import com.example.asus.download.okgo.ApkModel;
import com.example.asus.download.utils.LogUtil;
import com.example.asus.download.utils.RecycleViewDivider;
import com.lzy.okgo.db.DownloadManager;
import com.lzy.okgo.model.Progress;
import com.lzy.okserver.OkDownload;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2018/4/11.
 * 数据列表，用于加载所有后台返回的数据
 */

public class ListActivity extends Activity {

    private List<ApkModel> apks;
    private ListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        LogUtil.e("onCreate");

        /**
         * 设置下载器
         * 文件保存和浏览需要动态申请权限。已经在主界面设置了检查SD卡权限
         */
        OkDownload.getInstance().setFolder(Environment.getExternalStorageDirectory().getAbsolutePath() + "/aaa/");
        OkDownload.getInstance().getThreadPool().setCorePoolSize(3);

        initData();
        initView();
    }


    private void initView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        adapter = new ListAdapter(this, apks);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.VERTICAL,2,getResources().getColor(R.color.colorAccent)));

        /**
         * 从数据库恢复数据
         */
        List<Progress> progressList = DownloadManager.getInstance().getAll();
        OkDownload.restore(progressList);

    }

    /**
     * 进入详情活动如果删除了任务，返回到列表任务应该在onresum里面更新界面
     */
    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.e("onResume");
        adapter.notifyDataSetChanged();
    }

    private void initData() {
        apks = new ArrayList<>();
        ApkModel apk1 = new ApkModel();
        apk1.name = "爱奇艺";
        apk1.iconUrl = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/0c10c4c0155c9adf1282af008ed329378d54112ac";
        apk1.url = "http://121.29.10.1/f5.market.mi-img.com/download/AppStore/0b8b552a1df0a8bc417a5afae3a26b2fb1342a909/com.qiyi.video.apk";
        apks.add(apk1);
        ApkModel apk2 = new ApkModel();
        apk2.name = "微信";
        apk2.iconUrl = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/00814b5dad9b54cc804466369c8cb18f23e23823f";
        apk2.url = "http://116.117.158.129/f2.market.xiaomi.com/download/AppStore/04275951df2d94fee0a8210a3b51ae624cc34483a/com.tencent.mm.apk";
        apks.add(apk2);
        ApkModel apk3 = new ApkModel();
        apk3.name = "新浪微博";
        apk3.iconUrl = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/01db44d7f809430661da4fff4d42e703007430f38";
        apk3.url = "http://60.28.125.129/f1.market.xiaomi.com/download/AppStore/0ff41344f280f40c83a1bbf7f14279fb6542ebd2a/com.sina.weibo.apk";
        apks.add(apk3);
        ApkModel apk4 = new ApkModel();
        apk4.name = "QQ";
        apk4.iconUrl = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/072725ca573700292b92e636ec126f51ba4429a50";
        apk4.url = "http://121.29.10.1/f3.market.xiaomi.com/download/AppStore/0ff0604fd770f481927d1edfad35675a3568ba656/com.tencent.mobileqq.apk";
        apks.add(apk4);
        ApkModel apk5 = new ApkModel();
        apk5.name = "陌陌";
        apk5.iconUrl = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/06006948e655c4dd11862d060bd055b4fd2b5c41b";
        apk5.url = "http://121.18.239.1/f4.market.xiaomi.com/download/AppStore/096f34dec955dbde0597f4e701d1406000d432064/com.immomo.momo.apk";
        apks.add(apk5);
        ApkModel apk6 = new ApkModel();
        apk6.name = "手机淘宝";
        apk6.iconUrl = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/017a859792d09d7394108e0a618411675ec43f220";
        apk6.url = "http://121.29.10.1/f3.market.xiaomi.com/download/AppStore/0afc00452eb1a4dc42b20c9351eacacab4692a953/com.taobao.taobao.apk";
        apks.add(apk6);
        ApkModel apk7 = new ApkModel();
        apk7.name = "酷狗音乐";
        apk7.iconUrl = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/0f2f050e21e42f75c7ecca55d01ac4e5e4e40ca8d";
        apk7.url = "http://121.18.239.1/f5.market.xiaomi.com/download/AppStore/053ed49c1545c6eec3e3e23b31568c731f940934f/com.kugou.android.apk";
        apks.add(apk7);
        ApkModel apk8 = new ApkModel();
        apk8.name = "网易云音乐";
        apk8.iconUrl = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/02374548ac39f3b7cdbf5bea4b0535b5d1f432f23";
        apk8.url = "http://121.18.239.1/f4.market.xiaomi.com/download/AppStore/0f458c5661acb492e30b808a2e3e4c8672e6b55e2/com.netease.cloudmusic.apk";
        apks.add(apk8);
        ApkModel apk9 = new ApkModel();
        apk9.name = "ofo共享单车";
        apk9.iconUrl = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/0fe1a5c6092f3d9fa5c4c1e3158e6ff33f6418152";
        apk9.url = "http://60.28.125.1/f4.market.mi-img.com/download/AppStore/06954949fcd48414c16f726620cf2d52200550f56/so.ofo.labofo.apk";
        apks.add(apk9);
        ApkModel apk10 = new ApkModel();
        apk10.name = "摩拜单车";
        apk10.iconUrl = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/0863a058a811148a5174d9784b7be2f1114191f83";
        apk10.url = "http://60.28.125.1/f4.market.xiaomi.com/download/AppStore/00cdeb4865c5a4a7d350fe30b9f812908a569cc8a/com.mobike.mobikeapp.apk";
        apks.add(apk10);
        ApkModel apk11 = new ApkModel();
        apk11.name = "贪吃蛇大作战";
        apk11.iconUrl = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/09f7f5756d9d63bb149b7149b8bdde0769941f09b";
        apk11.url = "http://60.22.46.1/f3.market.xiaomi.com/download/AppStore/0b02f24ffa8334bd21b16bd70ecacdb42374eb9cb/com.wepie.snake.new.mi.apk";
        apks.add(apk11);
        ApkModel apk12 = new ApkModel();
        apk12.name = "蘑菇街";
        apk12.iconUrl = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/0ab53044735e842c421a57954d86a77aea30cc1da";
        apk12.url = "http://121.29.10.1/f5.market.xiaomi.com/download/AppStore/07a6ee4955e364c3f013b14055c37b8e4f6668161/com.mogujie.apk";
        apks.add(apk12);
        ApkModel apk13 = new ApkModel();
        apk13.name = "聚美优品";
        apk13.iconUrl = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/080ed520b76d943e5533017a19bc76d9f554342d0";
        apk13.url = "http://121.29.10.1/f5.market.mi-img.com/download/AppStore/0e70a572cd5fd6a3718941328238d78d71942aee0/com.jm.android.jumei.apk";
        apks.add(apk13);
        ApkModel apk14 = new ApkModel();
        apk14.name = "全民K歌";
        apk14.iconUrl = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/0f1f653261ff8b3a64324097224e40eface432b99";
        apk14.url = "http://60.28.123.129/f4.market.xiaomi.com/download/AppStore/04f515e21146022934085454a1121e11ae34396ae/com.tencent.karaoke.apk";
        apks.add(apk14);
        ApkModel apk15 = new ApkModel();
        apk15.name = "书旗小说";
        apk15.iconUrl = "http://file.market.xiaomi.com/thumbnail/PNG/l114/AppStore/0c9ce345aa2734b1202ddf32b6545d9407b18ba0b";
        apk15.url = "http://60.28.125.129/f5.market.mi-img.com/download/AppStore/02d9c4035b248753314f46600cf7347a306426dc1/com.shuqi.controller.apk";
        apks.add(apk15);
    }

}
