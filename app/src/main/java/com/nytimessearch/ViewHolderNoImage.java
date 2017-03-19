package com.nytimessearch;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class ViewHolderNoImage extends RecyclerView.ViewHolder {

    public TextView tvHeadline;
    public TextView tvSynopsis;

    public ViewHolderNoImage(View itemView) {
        super(itemView);

        tvHeadline = (TextView) itemView.findViewById(R.id.tv_headline_main);
        tvSynopsis = (TextView) itemView.findViewById(R.id.tv_content);
    }

    public TextView getTvHeadline() {
        return tvHeadline;
    }

    public void setTvHeadline(TextView tvHeadline) {
        this.tvHeadline = tvHeadline;
    }

    public TextView getTvSynopsis() {
        return tvSynopsis;
    }

    public void setTvSynopsis(TextView tvSynopsis) {
        this.tvSynopsis = tvSynopsis;
    }
}