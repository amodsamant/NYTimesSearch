package com.nytimessearch.network;

import com.nytimessearch.models.FilterWrapper;
import com.nytimessearch.models.NYTArticleResponse;
import com.nytimessearch.utils.DateUtils;

import java.util.Date;

import retrofit2.Call;

import static com.nytimessearch.network.NYTArticeService.retrofit;

public class NYTimesRetroClient {
    // dac417bd142940b1ae1ff7a36261426f
    // ae0446ab51eb416c9aa42ee2bb4ec024
    // 227c750bb7714fc39ef1559ef1bd8329
    private final String API_KEY = "ae0446ab51eb416c9aa42ee2bb4ec024";

    public NYTimesRetroClient() {
    }

    public Call<NYTArticleResponse> getCaller(String query, int offset) {

        NYTArticeService service = retrofit.create(NYTArticeService.class);

        Call<NYTArticleResponse> call = service.getArticles(API_KEY, query, offset);
        return call;
    }

    public Call<NYTArticleResponse> getCallerWithFilter(String query, FilterWrapper filter,
                                                        int offset) {

        NYTArticeService service = retrofit.create(NYTArticeService.class);

        Call<NYTArticleResponse> call = null;

        if(!filter.isArts() && !filter.isFashionStyle() && !filter.isSports()) {
            call = service.getArticlesWithFilters(API_KEY, query,
                    DateUtils.getYYYYMMddFormatDate(filter.getBeginDate()),
                    filter.getSort(), offset);
        } else {
            call = service.getArticlesWithFilters(API_KEY, query,
                    DateUtils.getYYYYMMddFormatDate(filter.getBeginDate()),
                    filter.getSort(), buildNewsDesk(filter), offset);
        }
        return call;
    }

    private String getBeginDate(String date) {
        String beginDate = DateUtils.getYYYYMMddFormatDate(date);
        if(beginDate!=null) {
            return beginDate;
        }
        return new Date().toString();
    }

    private String buildNewsDesk(FilterWrapper filter) {

        String values = "";

        if(filter.isArts()) {
            values = values + "\"" + filter.getArts() + "\"";
        }

        if(filter.isFashionStyle()) {
            if(filter.isArts()) {
                values = values + "%20";
            }
            values = values + "\"" + filter.getFashionStyle() + "\"";
        }

        if(filter.isSports()) {
            if(filter.isArts() || filter.isFashionStyle()) {
                values = values + "%20";
            }
            values = values + "\"" + filter.getSports() + "\"";
        }

        String newsDesk = "news_desk:(" + values+ ")";

        return newsDesk;
    }
    // fq=news_desk:("Education"%20"Health")


//    public List<NYTArticle> searchArticles() {
//        NYTArticeService service = retrofit.create(NYTArticeService.class);
//
//        Call<NYTArticleResponse> call = service.getArticles(API_KEY);
//
//
//        call.enqueue(new Callback<NYTArticleResponse>() {
//            @Override
//            public void onResponse(Call<NYTArticleResponse> call,
//                                   Response<NYTArticleResponse> response) {
//                NYTArticle.getArticlesFromJson(response.toString());
//                //articles.addAll(NYTArticle.getArticlesFromJson(response.toString()));
//
//            }
//
//            @Override
//            public void onFailure(Call<NYTArticleResponse> call, Throwable t) {
//                call.isCanceled();
//                //TODO: Handle this
//            }
//        });
//        return new ArrayList<>();
//    }



}
