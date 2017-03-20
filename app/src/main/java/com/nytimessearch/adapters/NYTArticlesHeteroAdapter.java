package com.nytimessearch.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nytimessearch.ChromeCustomTabsHelper;
import com.nytimessearch.R;
import com.nytimessearch.ViewHolderImage;
import com.nytimessearch.ViewHolderNoImage;
import com.nytimessearch.models.NYTArticle;
import com.nytimessearch.utils.GenericUtils;

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

            ChromeCustomTabsHelper helper = new ChromeCustomTabsHelper(v.getContext());

            helper.openChromeTab(article);


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
        if(article.getThumbnailGrid2()!=null && !article.getThumbnailGrid2().isEmpty()) {
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

            String thumbnail = null;

            if(GenericUtils.spanCount==1) {
                thumbnail = article.getThumbnailGrid1();

                Glide.with(getContext())
                        .load(thumbnail)
                        .override(width, height)
                        .fitCenter()
                        .into(ivThumbnail);

            } else {
                thumbnail = article.getThumbnailGrid2();

                Glide.with(getContext())
                        .load(thumbnail)
                        .override(width/2, height/2)
                        .fitCenter()
                        .into(ivThumbnail);
            }

            if(article.getNewsDesk() != null && !article.getNewsDesk().isEmpty()) {
                TextView tvTag = viewHolderImage.tvTag;
                tvTag.setText(article.getNewsDesk());
            }

            if(article.getHeadline() != null && !article.getHeadline().isEmpty()) {
                TextView tvHeadline = viewHolderImage.tvHeadline;
                tvHeadline.setText(article.getHeadline());
            }

            if(article.getPublishedDate()!=null && article.getPublishedDate().contains("T")) {
                TextView tvPublishedDate = viewHolderImage.tvPublishedDate;
                tvPublishedDate.setText("Published:  " +
                        article.getPublishedDate()
                                .substring(0,article.getPublishedDate().indexOf("T")));
            }
        }
    }

    private void configureViewHolderNoImage(ViewHolderNoImage viewHolderNoImage, int position) {

        NYTArticle article = articles.get(position);
        if(article!=null) {

            TextView tvTag = viewHolderNoImage.tvTag;
            tvTag.setText(article.getNewsDesk());

            TextView tvHeadline = viewHolderNoImage.tvHeadline;
            tvHeadline.setText(article.getHeadline());

            if(article.getPublishedDate()!=null && article.getPublishedDate().contains("T")) {
                TextView tvPublishedDate = viewHolderNoImage.tvPublishedDate;
                tvPublishedDate.setText("Published:  " +
                        article.getPublishedDate()
                                .substring(0,article.getPublishedDate().indexOf("T")));
            }
            TextView tvSynopsis = viewHolderNoImage.getTvSynopsis();
            tvSynopsis.setText(article.getHeadline());
        }

    }
}
