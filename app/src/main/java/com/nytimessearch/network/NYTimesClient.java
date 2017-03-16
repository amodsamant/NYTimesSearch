package com.nytimessearch.network;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class NYTimesClient {

    private final String API_KEY = "dac417bd142940b1ae1ff7a36261426f";
    private final String API_BASE_URL = "https://api.nytimes.com/svc/search/v2/";

    private AsyncHttpClient client;

    public NYTimesClient() {
        this.client = new AsyncHttpClient();
    }

    /**
     * Function returns the complete url by appending the relative url provided in the parameter
     * with the base url.
     *
     * @param relativeUrl
     * @return
     */
    private String getApiUrl(String relativeUrl) {
        return API_BASE_URL + relativeUrl;
    }

    public void searchNYTimesArticles(String query, JsonHttpResponseHandler handler) {

        String url = getApiUrl("articlesearch.json");

        RequestParams params = new RequestParams();
        params.put("api-key", API_KEY);
        params.put("q",query);

        client.get(url,params,handler);
    }



}
