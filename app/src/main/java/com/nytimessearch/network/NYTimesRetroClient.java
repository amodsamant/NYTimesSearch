package com.nytimessearch.network;

import com.nytimessearch.interfaces.NYTArticleService;
import com.nytimessearch.models.FilterWrapper;
import com.nytimessearch.models.NYTArticleResponse;
import com.nytimessearch.utils.DateUtils;

import retrofit2.Call;

import static com.nytimessearch.interfaces.NYTArticleService.retrofit;

public class NYTimesRetroClient {
    // dac417bd142940b1ae1ff7a36261426f
    // ae0446ab51eb416c9aa42ee2bb4ec024
    // 227c750bb7714fc39ef1559ef1bd8329
    private final String API_KEY = "ae0446ab51eb416c9aa42ee2bb4ec024";

    public NYTimesRetroClient() {
    }

    public Call<NYTArticleResponse> getCaller(String query, int offset) {
        NYTArticleService service = retrofit.create(NYTArticleService.class);
        return service.getArticles(API_KEY, query, offset);
    }

    public Call<NYTArticleResponse> getCallerWithFilter(String query, FilterWrapper filter,
                                                        int offset) {

        NYTArticleService service = retrofit.create(NYTArticleService.class);

        Call<NYTArticleResponse> call = null;
        if (!filter.isArts() && !filter.isFashionStyle() && !filter.isSports()) {
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

    private String buildNewsDesk(FilterWrapper filter) {

        String values = "";

        if(filter.isArts()) {
            values = values + filter.getArts();
        }

        if(filter.isFashionStyle()) {
            if(filter.isArts()) {
                values = values + "%20";
            }
            values = values + filter.getFashionStyle();
        }

        if(filter.isSports()) {
            if(filter.isArts() || filter.isFashionStyle()) {
                values = values + "%20";
            }
            values = values + filter.getSports();
        }

        return "news_desk:(" + values+ ")";
    }

}
