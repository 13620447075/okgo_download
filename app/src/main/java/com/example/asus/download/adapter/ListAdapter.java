package com.example.asus.download.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.asus.download.R;
import com.example.asus.download.activity.DetailActivity;
import com.example.asus.download.activity.ListActivity;
import com.example.asus.download.activity.TaskActivity;
import com.example.asus.download.okgo.ApkModel;
import com.example.asus.download.okgo.LogDownloadListener;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.OkDownload;

import org.w3c.dom.Text;

import java.io.File;
import java.util.List;

/**
 * Created by asus on 2018/4/11.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private Context context;
    private List<ApkModel> apks;

    public ListAdapter(Context context, List<ApkModel> apks) {
        this.context = context;
        this.apks = apks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(apks.get(position));

    }

    @Override
    public int getItemCount() {
        return apks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView fileUrl;
        private TextView fileName;
        private Button start;
        private ApkModel apk;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fileName = itemView.findViewById(R.id.filename);
            fileUrl = itemView.findViewById(R.id.fileurl);
            start = itemView.findViewById(R.id.start);
        }

        public void bind(final ApkModel apkModel) {
            this.apk = apkModel;
            fileName.setText(apk.name);
            fileUrl.setText(apk.url);

            if (OkDownload.getInstance().getTask(apk.url) != null) {
                start.setText("已在队列中");
                start.setEnabled(false);
            } else {
                start.setText("下载");
                start.setEnabled(true);
            }
            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GetRequest<File> request = OkGo.<File>get(apk.url).headers("aa", "11").params("bb", "22");
                    OkDownload.request(apk.url, request)
                            .priority(apk.priority)
                            .extra1(apk)
                            .save()
                            .register(new LogDownloadListener())
                            .start();
                    /**
                     * 点击下载后更新界面数据？
                     */
                    notifyDataSetChanged();
                }
            });
            /**
             * 点击item跳转到下载详情
             */
            itemView.setOnClickListener(this);
        }

        /**
         * 点击item跳转到下载详情
         */
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, DetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("apk", apk);
            intent.putExtras(bundle);//这里传递实体类，所以实体类要实现Serializable
            context.startActivity(intent);

        }
    }
}
