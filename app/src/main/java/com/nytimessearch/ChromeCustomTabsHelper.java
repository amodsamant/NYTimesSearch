package com.nytimessearch;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;

import com.nytimessearch.models.NYTArticle;

public class ChromeCustomTabsHelper {

    private Context mContext;

    public ChromeCustomTabsHelper(Context context) {
        this.mContext = context;
    }

    public void openChromeTab(NYTArticle article) {

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(ContextCompat.getColor(mContext, R.color.colorAccent));

        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.ic_action_share);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, article.getWebUrl());

        int requestCode = 100;
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setActionButton(bitmap, "Share Link", pendingIntent, true);
        builder.setStartAnimations(mContext, android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);

        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl((Activity) mContext, Uri.parse(article.getWebUrl()));
    }
}
