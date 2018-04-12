package com.example.asus.download.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.download.R;
import com.example.asus.download.okgo.ApkModel;
import com.example.asus.download.okgo.LogDownloadListener;
import com.example.asus.download.utils.LogUtil;
import com.example.asus.download.utils.ToastUtil;
import com.lzy.okgo.db.DownloadManager;
import com.lzy.okgo.model.Progress;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadListener;
import com.lzy.okserver.download.DownloadTask;

import java.io.File;
import java.util.List;

public class TaskRVAdapter extends RecyclerView.Adapter<TaskRVAdapter.ViewHolder> {

    private Context context;
    public static final int TYPE_ALL = 0;
    public static final int TYPE_FINISH = 1;
    public static final int TYPE_ING = 2;
    private List<DownloadTask> values;
    private int type;

    public TaskRVAdapter(Context context) {
        this.context = context;
    }

    /**
     * 恢复数据库的数据
     */
    public void updateData(int type) {
        if (type == TYPE_ALL) {
            values = OkDownload.restore(DownloadManager.getInstance().getAll());
        }
        if (type == TYPE_FINISH) {
            values = OkDownload.restore(DownloadManager.getInstance().getFinished());
        }
        if (type == TYPE_ING) {
            values = OkDownload.restore(DownloadManager.getInstance().getDownloading());
        }
        notifyDataSetChanged();

    }

    public void unRegister() {
    }

    private String createTag(DownloadTask task) {
        return type + "_" + task.progress.tag;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DownloadTask task = values.get(position);
        String tag = createTag(task);
        task.register(new ListDownloadListener(tag, holder)).register(new LogDownloadListener());
        holder.setTag(tag);
        holder.setTask(task);
        holder.bind();
        holder.refresh(task.progress);
    }

    @Override
    public int getItemCount() {
        return values == null ? 0 : values.size();
    }


    /**
     * ViewHolder
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView fileUrl;
        private final TextView fileName;
        private DownloadTask task;
        private final ProgressBar progressBar;
        private final Button start;

        public void setTask(DownloadTask task) {
            this.task = task;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        private String tag;

        public ViewHolder(View itemView) {
            super(itemView);
            fileName = itemView.findViewById(R.id.filename);
            fileUrl = itemView.findViewById(R.id.fileurl);
            start = itemView.findViewById(R.id.start);
            progressBar = itemView.findViewById(R.id.progressbar);

        }

        public void bind() {
            Progress progress = task.progress;
            ApkModel apk = (ApkModel) progress.extra1;
            if (apk != null) {
                fileName.setText(apk.name);
                fileUrl.setText(apk.url);
            } else {
                fileName.setText(progress.fileName);
            }

            start.setOnClickListener(this);
        }

        public void refresh(Progress progress) {
            switch (progress.status) {
                case Progress.NONE:
//                    netSpeed.setText("停止");
                    start.setText("下载");
                    break;
                case Progress.PAUSE:
//                    netSpeed.setText("暂停中");
                    start.setText("继续");
                    break;
                case Progress.ERROR:
//                    netSpeed.setText("下载出错");
                    start.setText("出错");
                    break;
                case Progress.WAITING:
//                    netSpeed.setText("等待中");
                    start.setText("等待");
                    break;
                case Progress.FINISH:
//                    netSpeed.setText("下载完成");
                    start.setText("完成");
                    break;
                case Progress.LOADING:
//                    String speed = Formatter.formatFileSize(context, progress.speed);
//                    netSpeed.setText(String.format("%s/s", speed));
                    start.setText("暂停");
                    break;
                default:
                    break;
            }
            LogUtil.e("progre:" + progress.fraction);
            progressBar.setProgress((int) (progress.fraction * 100));
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.start:
                    switch (task.progress.status) {
                        case Progress.NONE:
                            task.start();
                            break;
                        case Progress.ERROR:
                            task.start();
                            break;
                        case Progress.PAUSE:
                            task.start();
                            break;
                        case Progress.LOADING:
                            task.pause();
                            break;
                        case Progress.FINISH:
                            ToastUtil.showShort(context, "打开吧~~~");
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * ViewHolder此结束
     */

    private class ListDownloadListener extends DownloadListener {

        private ViewHolder holder;

        ListDownloadListener(Object tag, ViewHolder holder) {
            super(tag);
            this.holder = holder;
        }

        @Override
        public void onStart(Progress progress) {
        }

        @Override
        public void onProgress(Progress progress) {
            if (tag == holder.getTag()) {
                holder.refresh(progress);
            }
        }

        @Override
        public void onError(Progress progress) {
            Throwable throwable = progress.exception;
            if (throwable != null) {
                throwable.printStackTrace();
            }
        }

        @Override
        public void onFinish(File file, Progress progress) {
            Toast.makeText(context, "下载完成:" + progress.filePath, Toast.LENGTH_SHORT).show();
            updateData(type);
        }

        @Override
        public void onRemove(Progress progress) {
        }
    }


}
