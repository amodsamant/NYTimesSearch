package com.nytimessearch.network;

import com.nytimessearch.models.NYTArticleResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NYTArticeService {
    @GET("articlesearch.json")
    public Call<NYTArticleResponse> getArticles(@Query("api-key") String apiKey,
                                                @Query("q") String query);

    @GET("articlesearch.json")
    public Call<NYTArticleResponse> getArticles(@Query("api-key") String apiKey);



}
