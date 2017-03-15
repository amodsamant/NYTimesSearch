package com.nytimessearch.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NYTArticle {

    String webUrl;
    String headline;
    String snippet;
    String source;
    String publishedDate;
    String thumbnail;


    public String getWebUrl() {
        return webUrl;
    }

    public String getHeadline() {
        return headline;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public NYTArticle(JSONObject jsonObject) {

        try{
            this.webUrl = jsonObject.getString("web_url");
            this.headline = jsonObject.getJSONObject("headline").getString("main");
            JSONArray multimediaArray = jsonObject.getJSONArray("multimedia");

            if(multimediaArray.length() > 0) {
                this.thumbnail = "http://www.nytimes.com/" +
                        multimediaArray.getJSONObject(0).getString("url");
            } else {
                this.thumbnail = "";
            }
        } catch (JSONException e) {

        }
    }

    public static List<NYTArticle> fromJSONArray(JSONArray articlesJson) {

        List<NYTArticle> articles = new ArrayList<>();
        for(int x = 0; x < articlesJson.length(); x++) {
            try {
                articles.add(new NYTArticle(articlesJson.getJSONObject(x)));
            } catch (JSONException e) {

            }
        }

        return articles;
    }
}
