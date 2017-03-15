package com.nytimessearch.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nytimessearch.R;
import com.nytimessearch.models.NYTArticle;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NYTArticlesAdapter extends ArrayAdapter<NYTArticle> {

    public NYTArticlesAdapter(Context context, List<NYTArticle> articles) {
        super(context, android.R.layout.simple_list_item_1, articles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        NYTArticle article = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_article_result, parent, false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.ivThumbnail);
        TextView textView = (TextView) convertView.findViewById(R.id.tvHeadline);

        imageView.setImageResource(0);
        if(!TextUtils.isEmpty(article.getThumbnail())) {
            Picasso.with(getContext()).load(article.getThumbnail()).into(imageView);
        }

        textView.setText(article.getHeadline());


        return convertView;
    }
}
