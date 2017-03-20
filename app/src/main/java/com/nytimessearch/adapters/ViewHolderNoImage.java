package com.nytimessearch.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.nytimessearch.R;

/**
 * ViewHolder for view with no image
 */
public class ViewHolderNoImage extends RecyclerView.ViewHolder {

    public TextView tvTag;
    public TextView tvHeadline;
    public TextView tvPublishedDate;
    public TextView tvSynopsis;

    public ViewHolderNoImage(View itemView) {
        super(itemView);
        tvTag = (TextView) itemView.findViewById(R.id.tv_tag_main);
        tvHeadline = (TextView) itemView.findViewById(R.id.tv_headline_main);
        tvPublishedDate = (TextView) itemView.findViewById(R.id.tv_pub_date_main);
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