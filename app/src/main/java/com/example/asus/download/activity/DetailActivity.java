package com.example.asus.download.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.asus.download.R;
import com.example.asus.download.okgo.ApkModel;
import com.example.asus.download.okgo.LogDownloadListener;
import com.example.asus.download.utils.LogUtil;
import com.example.asus.download.utils.ToastUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadListener;
import com.lzy.okserver.download.DownloadTask;

import java.io.File;

/**
 * Created by asus on 2018/4/12.
 * 下载详情
 */

public class DetailActivity extends Activity implements View.OnClickListener {

    private ApkModel apk;
    private DownloadTask task;
    private TextView fileName;
    private TextView fileurl;
    private TextView filesize;
    private TextView filespeed;
    private Button start;
    private Button delete;
    private Button reDown;
    private ProgressBar progressBar;

    private void initView() {
        fileName = findViewById(R.id.filename);
        fileurl = findViewById(R.id.fileurl);
        filesize = findViewById(R.id.filesize);
        filespeed = findViewById(R.id.filespeed);
        start = findViewById(R.id.start);
        delete = findViewById(R.id.delete);
        reDown = findViewById(R.id.redown);
        progressBar = findViewById(R.id.progressbar);

        start.setOnClickListener(this);
        delete.setOnClickListener(this);
        reDown.setOnClickListener(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initView();
        initData();
        LogUtil.e("this is detailactivity");
    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
//        super.onCreate(savedInstanceState, persistentState);
//        setContentView(R.layout.activity_detail);
//        initView();
//        initData();
//        LogUtil.e("this is detailactivity");
//    }

    private void initData() {
        apk = (ApkModel) getIntent().getSerializableExtra("apk");
        LogUtil.e("apkname:" + apk.name);
        if (OkDownload.getInstance().hasTask(apk.url)) {
            //下面register()的tag对应destroy()里面unregister()
            task = OkDownload.getInstance().getTask(apk.url)
                    .register(new DetailListener("DetailListener"))
                    .register(new LogDownloadListener());
        }
        if (task != null) {
            refreshUi(task.progress);
        }
        fileName.setText(apk.name);
        fileurl.setText(apk.url);
    }

    private void refreshUi(Progress progress) {
        filesize.setText("当前大小：" + progress.currentSize + "kb" + "----总大小：" + progress.totalSize + "kb");
        filespeed.setText(progress.speed + "k/s");
        progressBar.setProgress((int) (progress.fraction * 100));
        switch (progress.status) {
            case Progress.NONE:
                start.setText("下载");
                break;
            case Progress.LOADING:
                start.setText("暂停");
                break;
            case Progress.PAUSE:
                start.setText("继续");
                break;
            case Progress.WAITING:
                start.setText("等待");
                break;
            case Progress.ERROR:
                start.setText("出错");
                break;
            case Progress.FINISH:
                start.setText("打开");
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:

                if (task == null) {
                    //下面的传参除headers、params非后台有要求传递参数请求，否则不用写
                    GetRequest<File> request = OkGo.<File>get(apk.url)
                            .headers("aaa", "111")
                            .params("bbb", "222");
                    task = OkDownload.request(apk.url, request).priority(apk.priority)
                            .extra1(apk)
                            .save()
                            .register(new DetailListener("DetailListener"))
                            .register(new LogDownloadListener());
                }
                switch (task.progress.status) {
                    case Progress.PAUSE:
                        task.start();
                        LogUtil.e("PAUSE");
                        break;
                    case Progress.NONE:
                        task.start();
                        LogUtil.e("NONE");
                        break;
                    case Progress.ERROR:
                        task.start();
                        LogUtil.e("ERROR");
                        break;
                    case Progress.LOADING: //正在下载
                        task.pause();
                        LogUtil.e("LOADING");
                        break;
                    case Progress.FINISH:
                        LogUtil.e("FINISH");
                        ToastUtil.showShort(this, "打开文件~~~");
                        break;
                }

                break;
            case R.id.delete:
                if (task!=null){
                    task.remove();
                    task = null;
                }
                LogUtil.e("删除~~~");
                //重置UI
                filesize.setText("0");
                filespeed.setText("0");
                progressBar.setProgress(0);
                start.setText("下载");
                break;
            case R.id.redown:
                if (task != null) {
                    task.restart();
                }
                break;
        }
    }

    private class DetailListener extends DownloadListener {

        public DetailListener(Object tag) {
            super(tag);
        }

        @Override
        public void onStart(Progress progress) {

        }

        @Override
        public void onProgress(Progress progress) {
            /**
             * 这里要时刻刷新UI？
             */
            refreshUi(progress);

        }

        @Override
        public void onError(Progress progress) {

        }

        @Override
        public void onFinish(File file, Progress progress) {

        }

        @Override
        public void onRemove(Progress progress) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (task != null) {
            task.unRegister("DetailListener");
        }
    }
}
