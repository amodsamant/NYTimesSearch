package com.nytimessearch.network;

import com.nytimessearch.models.FilterWrapper;
import com.nytimessearch.models.NYTArticleResponse;
import com.nytimessearch.utils.DateUtils;

import java.util.Date;

import retrofit2.Call;

import static com.nytimessearch.network.NYTArticeService.retrofit;

public class NYTimesRetroClient {

    private final String API_KEY = "dac417bd142940b1ae1ff7a36261426f";

    public NYTimesRetroClient() {
    }

    public Call<NYTArticleResponse> getCaller(String query) {

        NYTArticeService service = retrofit.create(NYTArticeService.class);

        Call<NYTArticleResponse> call = service.getArticles(API_KEY, query);
        return call;
    }

    public Call<NYTArticleResponse> getCallerWithFilter(String query, FilterWrapper filter) {

        NYTArticeService service = retrofit.create(NYTArticeService.class);

        Call<NYTArticleResponse> call =
                service.getArticlesWithFilters(API_KEY, query,
                        DateUtils.getYYYYMMddFormatDate(filter.getBeginDate()),
                        filter.getSort(), buildNewsDesk(filter));
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
