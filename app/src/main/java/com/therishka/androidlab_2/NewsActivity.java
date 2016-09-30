package com.therishka.androidlab_2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.therishka.androidlab_2.models.VkAttachments;
import com.therishka.androidlab_2.models.VkDialog;
import com.therishka.androidlab_2.models.VkDialogResponse;
import com.therishka.androidlab_2.models.VkFriend;
import com.therishka.androidlab_2.models.VkLikes;
import com.therishka.androidlab_2.models.VkNewsItem;
import com.therishka.androidlab_2.network.RxVk;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class NewsActivity extends AppCompatActivity {
    ProgressBar mProgress;
    RecyclerView mRecyclerList;
    RecyclerNewsAdapter mNewsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        mProgress = (ProgressBar) findViewById(R.id.loading_view);
        mRecyclerList = (RecyclerView) findViewById(R.id.news_list);
        mNewsAdapter = new RecyclerNewsAdapter(this);
        mRecyclerList.setAdapter(mNewsAdapter);
        mRecyclerList.setLayoutManager(new LinearLayoutManager(this));

        getNewsAndShowThem();
    }

    private void getNewsAndShowThem() {
        showLoading();
        RxVk api = new RxVk();
        api.getNews(new RxVk.RxVkListener<LinkedList<VkNewsItem>>() {
            @Override
            public void requestFinished(LinkedList<VkNewsItem> requestResult) {
                mNewsAdapter.setNewsList(requestResult);
                showNews();
            }
        });
    }

    private void showLoading() {
        mRecyclerList.setVisibility(View.GONE);
        mProgress.setVisibility(View.VISIBLE);
    }

    private void showNews() {
        mRecyclerList.setVisibility(View.VISIBLE);
        mProgress.setVisibility(View.GONE);
    }

    private class RecyclerNewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<VkNewsItem> mNewsList;
        private Context mContext;

        public RecyclerNewsAdapter(@NonNull Context context) {
            mContext = context;
        }

        public void setNewsList(@Nullable List<VkNewsItem> NewsList) {
            mNewsList = NewsList;
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
            return new NewsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof NewsViewHolder) {
                VkNewsItem news = mNewsList.get(position);
                ((NewsViewHolder) holder).bind(news);
                Glide.with(mContext).load(news.getPublisher().getPhoto_50())
                        .fitCenter()
                        .into(((NewsViewHolder) holder).avatar);
            }
        }

        @Override
        public int getItemCount() {
            return mNewsList != null ? mNewsList.size() : 0;
        }


        @SuppressWarnings("WeakerAccess")
        private class NewsViewHolder extends RecyclerView.ViewHolder {
            TextView date;
            TextView group;
            TextView news1;
            ImageView avatar;
            ImageView attachments;

            public NewsViewHolder(View itemView) {
                super(itemView);
                date = (TextView) itemView.findViewById(R.id.date);
                group = (TextView) itemView.findViewById(R.id.group);
                news1 = (TextView) itemView.findViewById(R.id.news);
                avatar = (ImageView) itemView.findViewById(R.id.group_avatar);
                attachments = (ImageView) itemView.findViewById(R.id.image_news);
            }

            public void bind(VkNewsItem news) {
                group.setText(news.getPublisher().getName());
                news1.setText(news.getText());
                String dateString;
                Date date = new Date(news.getDate() * 1000);
                DateFormat dateFormat = new SimpleDateFormat("HH:mm dd.MM.yyyy ");
                dateString = dateFormat.format(date);
                this.date.setText(dateString);
                if (news.getAttachments() != null) {
                    for (int i = 0; i < news.getAttachments().size(); i++) {
                        if (news.getAttachments().get(i).getPhoto() != null) {
                            Glide.with(mContext).load(news.getAttachments().get(i).getPhoto().getPhoto_2560()).fitCenter().into(attachments);
                        }
                    }
                }

            }
        }
    }
}