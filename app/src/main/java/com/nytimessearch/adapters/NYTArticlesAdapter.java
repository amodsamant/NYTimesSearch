package com.nytimessearch.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nytimessearch.R;
import com.nytimessearch.activities.ReadArticleActivity;
import com.nytimessearch.models.NYTArticle;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NYTArticlesAdapter extends RecyclerView.Adapter<NYTArticlesAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivThumbnail;
        public TextView tvHeadline;

        public ViewHolder(View itemView) {
            super(itemView);
            ivThumbnail = (ImageView) itemView.findViewById(R.id.iv_thumbnail);
            tvHeadline = (TextView) itemView.findViewById(R.id.tv_headline);
        }
    }

    private List<NYTArticle> mArticles;
    private Context mContext;

    DisplayMetrics displayMetrics = new DisplayMetrics();

    public NYTArticlesAdapter(Context context, List<NYTArticle> articles) {
        mContext = context;
        mArticles = articles;
    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View articleView = inflater.inflate(R.layout.item_article_result, parent, false);
        return new ViewHolder(articleView);
    }

    @Override
    public void onBindViewHolder(NYTArticlesAdapter.ViewHolder holder, int position) {

        final NYTArticle article = mArticles.get(position);

        //TODO: Remove this out of this function
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ReadArticleActivity.class);
                intent.putExtra("url", article.getWebUrl());
                v.getContext().startActivity(intent);
            }
        });

        ImageView ivThumbnail = holder.ivThumbnail;
        ivThumbnail.setImageResource(0);
        if(!TextUtils.isEmpty(article.getThumbnail())) {
            Picasso.with(getContext()).load(article.getThumbnail())
                    .resize(width/4, height/4)
                    .centerInside()
                    .into(ivThumbnail);
        }

        TextView tvHeadline = holder.tvHeadline;
        tvHeadline.setText(article.getHeadline());
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }
}
