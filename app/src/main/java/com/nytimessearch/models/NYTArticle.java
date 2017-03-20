package com.nytimessearch.models;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class NYTArticle {

    String webUrl;
    String headline;
    String source;
    String publishedDate;
    String thumbnailGrid1;
    String thumbnailGrid2;
    String newsDesk;

    public String getWebUrl() {
        return webUrl;
    }

    public String getHeadline() {
        return headline;
    }

    public String getThumbnailGrid1() {
        return thumbnailGrid1;
    }
    public String getThumbnailGrid2() {
        return thumbnailGrid2;
    }

    public String getNewsDesk() { return newsDesk; }

    public String getPublishedDate() {
        return publishedDate;
    }

    public NYTArticle() {

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
                if (doc.getMultimedia() != null && !doc.getMultimedia().isEmpty()) {

                    List<Multimedium> multimediums = doc.getMultimedia();
                    for(Multimedium multimedium: multimediums) {

                        if(multimedium.getSubtype().equalsIgnoreCase("thumbnail")) {
                            article.thumbnailGrid2 = "http://www.nytimes.com/" +
                                    multimedium.getUrl();
                        }

                        if(multimedium.getSubtype().equalsIgnoreCase("xlarge")) {
                            article.thumbnailGrid1 = "http://www.nytimes.com/" +
                                    multimedium.getUrl();
                        }

                    }


//                    if(doc.getMultimedia().size()>2) {
//                        if(doc.getMultimedia().get(2) != null) {
//                            article.thumbnailGrid2 = "http://www.nytimes.com/" +
//                                    doc.getMultimedia().get(2).getUrl();
//                        }
//                        if(doc.getMultimedia().get(1) != null) {
//                            article.thumbnailGrid1 = "http://www.nytimes.com/" +
//                                    doc.getMultimedia().get(1).getUrl();
//                        }
//                    } else if(doc.getMultimedia().get(0)!=null) {
//                        article.thumbnailGrid1 = "http://www.nytimes.com/" +
//                                doc.getMultimedia().get(0).getUrl();
//                        article.thumbnailGrid2 = "http://www.nytimes.com/" +
//                                doc.getMultimedia().get(0).getUrl();
//                    }
                }

                if(doc.getNewsDesk() != null && !doc.getNewsDesk().isEmpty()) {

                    String tag = doc.getNewsDesk();
                    if(tag.equalsIgnoreCase("NONE") && doc.getSectionName()!=null ) {
                        tag = doc.getSectionName();
                    }
                    article.newsDesk = "# " + tag + " ";
                }

                if(doc.getPubDate()!=null && !doc.getPubDate().isEmpty()) {
                    article.publishedDate = doc.getPubDate();
                }

                articles.add(article);
            }
        } else {
            //TODO: Log error
        }
        return articles;
    }

}
