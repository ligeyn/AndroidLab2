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
import com.therishka.androidlab_2.models.VkDialog;
import com.therishka.androidlab_2.models.VkDialogResponse;
import com.therishka.androidlab_2.network.RxVk;

import java.util.List;

public class Activity extends AppCompatActivity {
    ProgressBar mProgress;
    RecyclerView mRecyclerList;
    RecyclerDialogsAdapter mDialogsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_optimised);

        mProgress = (ProgressBar) findViewById(R.id.loading_view);
        mRecyclerList = (RecyclerView) findViewById(R.id.friends_list);
        mDialogsAdapter = new RecyclerDialogsAdapter(this);
        mRecyclerList.setAdapter(mDialogsAdapter);
        mRecyclerList.setLayoutManager(new LinearLayoutManager(this));

        getDialogsAndShowThem();
    }

    private void getDialogsAndShowThem() {
        showLoading();
        RxVk api = new RxVk();
        api.getDialogs(new RxVk.RxVkListener<VkDialogResponse>() {
            @Override
            public void requestFinished(VkDialogResponse requestResult) {
                mDialogsAdapter.setDialogsList(requestResult.getDialogs());
                showDialogs();
            }
        });
    }

    private void showLoading() {
        mRecyclerList.setVisibility(View.GONE);
        mProgress.setVisibility(View.VISIBLE);
    }

    private void showDialogs() {
        mRecyclerList.setVisibility(View.VISIBLE);
        mProgress.setVisibility(View.GONE);
    }
    private class RecyclerDialogsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<VkDialog> mDialogsList;
        private Context mContext;

        public RecyclerDialogsAdapter(@NonNull Context context) {
            mContext = context;
        }

        public void setDialogsList(@Nullable List<VkDialog> friendsList) {
            mDialogsList = friendsList;
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.massage_item, parent, false);
            return new DialogsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof DialogsViewHolder) {
                VkDialog dialog = mDialogsList.get(position);
                ((DialogsViewHolder) holder).bind(dialog);
                Glide.with(mContext).load(dialog.getPhoto())
                        .fitCenter()
                        .into(((DialogsViewHolder) holder).avatar);
            }
        }

        @Override
        public int getItemCount() {
            return mDialogsList != null ? mDialogsList.size() : 0;
        }
    }

    @SuppressWarnings("WeakerAccess")
    private class DialogsViewHolder extends RecyclerView.ViewHolder {
        TextView massage;
        TextView fullName;
        ImageView avatar;
        View wasReaden;

        public DialogsViewHolder(View itemView) {
            super(itemView);
            massage = (TextView) itemView.findViewById(R.id.massage);
            fullName = (TextView) itemView.findViewById(R.id.full_name1);
            avatar = (ImageView) itemView.findViewById(R.id.avatar1);
            wasReaden = itemView.findViewById(R.id.was_readen);
        }

        public void bind(VkDialog dialog) {
           massage.setText(dialog.getMessage());
            wasReaden.setVisibility(!dialog.is_read() ? View.VISIBLE : View.GONE);
            fullName.setText(dialog.getUsername());
        }
    }
}

