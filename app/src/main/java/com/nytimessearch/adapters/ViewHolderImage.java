package com.nytimessearch.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nytimessearch.R;

public class ViewHolderImage extends RecyclerView.ViewHolder {

    public ImageView ivThumbnail;
    public TextView tvTag;
    public TextView tvPublishedDate;
    public TextView tvHeadline;

    public ViewHolderImage(View itemView) {
        super(itemView);
        ivThumbnail = (ImageView) itemView.findViewById(R.id.iv_thumbnail);
        tvTag = (TextView) itemView.findViewById(R.id.tv_tag);
        tvHeadline = (TextView) itemView.findViewById(R.id.tv_headline);
        tvPublishedDate = (TextView) itemView.findViewById(R.id.tv_pub_date);
    }

    public ImageView getIvThumbnail() {
        return ivThumbnail;
    }

    public void setIvThumbnail(ImageView ivThumbnail) {
        this.ivThumbnail = ivThumbnail;
    }

    public TextView getTvHeadline() {
        return tvHeadline;
    }

    public void setTvHeadline(TextView tvHeadline) {
        this.tvHeadline = tvHeadline;
    }
}