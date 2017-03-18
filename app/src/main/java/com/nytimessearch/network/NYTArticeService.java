package com.nytimessearch.network;

import com.nytimessearch.models.NYTArticleResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NYTArticeService {
    @GET("articlesearch.json")
    public Call<NYTArticleResponse> getArticles(@Query("api-key") String apiKey,
                                                @Query("q") String query);

    @GET("articlesearch.json")
    public Call<NYTArticleResponse> getArticles(@Query("api-key") String apiKey);

    @GET("articlesearch.json")
    public Call<NYTArticleResponse> getArticlesWithFilters(@Query("api-key") String apiKey,
                                                           @Query("q") String query,
                                                           @Query("begin_date") String beginDate,
                                                           @Query("sort") String sort,
                                                           @Query("fq") String newsDesk);

    @GET("articlesearch.json")
    public Call<NYTArticleResponse> getArticlesWithFilters(@Query("api-key") String apiKey,
                                                           @Query("q") String query,
                                                           @Query("begin_date") String beginDate,
                                                           @Query("sort") String sort);

    public final String API_BASE_URL = "https://api.nytimes.com/svc/search/v2/";

    public static final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

}
