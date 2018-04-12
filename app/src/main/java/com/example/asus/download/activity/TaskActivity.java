package com.example.asus.download.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.asus.download.R;
import com.example.asus.download.adapter.TaskRVAdapter;
import com.example.asus.download.utils.ToastUtil;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.task.XExecutor;

/**
 * 全部任务列表
 */
public class TaskActivity extends Activity implements XExecutor.OnAllTaskEndListener {

    private OkDownload okDownload;
    private TaskRVAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        initDown();
        initView();
    }

    /**
     *实现XExecutor.OnAllTaskEndListener接口的方法
     */
    @Override
    public void onAllTaskEnd() {
        ToastUtil.showShort(this,"所有任务已经结束");

    }

    private void initDown() {
        okDownload = OkDownload.getInstance();
        okDownload.addOnAllTaskEndListener(this);
    }

    private void initView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        adapter =new TaskRVAdapter(this);
        recyclerView.setLayoutManager(manager);
        adapter.updateData(TaskRVAdapter.TYPE_ALL);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        okDownload.removeOnAllTaskEndListener(this);
        adapter.unRegister();
    }
}
