package com.nytimessearch.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
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

    public NYTArticle() {

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

    public static List<NYTArticle> getArticlesFromJson(NYTArticleResponse articleResponse){

        List<NYTArticle> articles = new ArrayList<>();

        if(articleResponse!=null && articleResponse.getResponse()!=null) {
            List<Doc> docs = articleResponse.getResponse().getDocs();
            for (Doc doc : docs) {
                NYTArticle article = new NYTArticle();
                article.webUrl = doc.getWebUrl();
                if (doc.getHeadline() != null) {
                    article.headline = doc.getHeadline().getMain();
                }
                if (doc.getMultimedia() != null && !doc.getMultimedia().isEmpty()
                        && doc.getMultimedia().get(0) != null) {
                    article.thumbnail = "http://www.nytimes.com/" + doc.getMultimedia().get(0).getUrl();
                }

                articles.add(article);
            }
        } else {
            //TODO: Log error
        }


        return articles;
    }

//    public static List<NYTArticle> fromJSONArray(JSONArray articlesJson) {
//
//        List<NYTArticle> articles = new ArrayList<>();
//        for(int x = 0; x < articlesJson.length(); x++) {
//            try {
//                articles.add(new NYTArticle(articlesJson.getJSONObject(x)));
//            } catch (JSONException e) {
//
//            }
//        }
//
//        return articles;
//    }
}
