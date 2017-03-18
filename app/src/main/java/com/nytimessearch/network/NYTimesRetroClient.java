package com.nytimessearch.network;

import com.nytimessearch.models.NYTArticleResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NYTimesRetroClient {

    private final String API_KEY = "dac417bd142940b1ae1ff7a36261426f";
    private final String API_BASE_URL = "https://api.nytimes.com/svc/search/v2/";

    Retrofit retrofit;

    public NYTimesRetroClient() {

        this.retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public Call<NYTArticleResponse> getCaller(String query) {

        NYTArticeService service = retrofit.create(NYTArticeService.class);

        Call<NYTArticleResponse> call = service.getArticles(API_KEY);
        return call;
    }

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
