package com.nytimessearch.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nytimessearch.R;
import com.nytimessearch.ViewHolderImage;
import com.nytimessearch.ViewHolderNoImage;
import com.nytimessearch.activities.ReadArticleActivity;
import com.nytimessearch.models.NYTArticle;

import org.parceler.Parcels;

import java.util.List;

public class NYTArticlesHeteroAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<NYTArticle> articles;
    private Context mContext;

    private final int IMAGE = 0, NO_IMAGE = 1;

    DisplayMetrics displayMetrics = new DisplayMetrics();

    public NYTArticlesHeteroAdapter(Context context, List<NYTArticle> articles) {
        this.mContext =  context;
        this.articles = articles;
    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {

            case IMAGE:
                View imView = inflater.inflate(R.layout.item_article_image,parent,false);
                viewHolder = new ViewHolderImage(imView);
                break;

            case NO_IMAGE:
                View noImView = inflater.inflate(R.layout.item_article_no_image, parent, false);
                viewHolder = new ViewHolderNoImage(noImView);
                break;

            default:
                //TODO:

        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final NYTArticle article = articles.get(position);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ReadArticleActivity.class);
            intent.putExtra("article", Parcels.wrap(article));
            v.getContext().startActivity(intent);
        });

        switch (holder.getItemViewType()) {

            case IMAGE:
                ViewHolderImage viewHolderImage = (ViewHolderImage) holder;
                configureViewHolderImage(viewHolderImage, position);
                break;

            case NO_IMAGE:
                ViewHolderNoImage viewHolderNoImage = (ViewHolderNoImage) holder;
                configureViewHolderNoImage(viewHolderNoImage, position);
                break;

            default:

        }

    }

    @Override
    public int getItemCount() {
        return this.articles.size();
    }

    @Override
    public int getItemViewType(int position) {

        NYTArticle article =  articles.get(position);
        if(article.getThumbnail()!=null && !article.getThumbnail().isEmpty()) {
            return IMAGE;
        } else {
            return NO_IMAGE;
        }
    }

    private void configureViewHolderImage(ViewHolderImage viewHolderImage, int position) {
        NYTArticle article = articles.get(position);
        if(article!=null) {

            ((Activity) getContext()).getWindowManager()
                    .getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;

            ImageView ivThumbnail = viewHolderImage.ivThumbnail;
            ivThumbnail.setImageResource(0);

            Glide.with(getContext())
                    .load(article.getThumbnail())
                    .override(width/3, height/3)
                    .fitCenter()
                    .into(ivThumbnail);


            TextView tvHeadline = viewHolderImage.tvHeadline;
            tvHeadline.setText(article.getHeadline());
        }
    }

    private void configureViewHolderNoImage(ViewHolderNoImage viewHolderNoImage, int position) {

        NYTArticle article = articles.get(position);

        if(article!=null) {

            TextView tvHeadline = viewHolderNoImage.tvHeadline;
            tvHeadline.setText(article.getHeadline());

            TextView tvSynopsis = viewHolderNoImage.getTvSynopsis();
            tvSynopsis.setText(article.getHeadline());
        }

    }

}