package com.nytimessearch.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nytimessearch.EndlessRecyclerViewScrollListener;
import com.nytimessearch.R;
import com.nytimessearch.adapters.NYTArticlesAdapter;
import com.nytimessearch.fragments.FilterFragment;
import com.nytimessearch.models.FilterWrapper;
import com.nytimessearch.models.NYTArticle;
import com.nytimessearch.models.NYTArticleResponse;
import com.nytimessearch.network.NYTimesRetroClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity
        implements FilterFragment.FilterDialogListener {

    RecyclerView rvArticles;

    List<NYTArticle> articles;
    NYTArticlesAdapter adapter;

    NYTimesRetroClient client;
    //NYTimesClient client;
    FilterWrapper filter;

    // Store a member variable for the listener
    private EndlessRecyclerViewScrollListener scrollListener;

    StaggeredGridLayoutManager gridLayoutManager =
            new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupViews();
    }

    private void setupViews() {

        rvArticles = (RecyclerView) findViewById(R.id.rvResults);

        articles = new ArrayList<>();
        adapter = new NYTArticlesAdapter(this, articles);
        rvArticles.setAdapter(adapter);

        // Attach the layout manager to the recycler view
        rvArticles.setLayoutManager(gridLayoutManager);

//        rvArticles.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View v) {
//
//                int position = rvArticles.indexOfChild(v);
//                NYTArticle article = articles.get(position);
//
//                Intent intent = new Intent(SearchActivity.this,ReadArticleActivity.class);
//                intent.putExtra("url",article.getWebUrl());
//                startActivity(intent);
//            }
//        });

        // Retain an instance so that you can call `resetState()` for fresh searches
        scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(page);
            }
        };
        // Adds the scroll listener to RecyclerView
        rvArticles.addOnScrollListener(scrollListener);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                searchArticles(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.action_filter:
                showFilterDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

//    public void searchArticles(String query) {
//
//        this.client = new NYTimesClient();
//
//        client.searchNYTimesArticles(query, filter,new JsonHttpResponseHandler(){
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                super.onSuccess(statusCode, headers, response);
//
//                try {
//                    JSONArray results = response.getJSONObject("response").getJSONArray("docs");
//                    articles.clear();
//
//                    articles.addAll(NYTArticle.getArticlesFromJson(response.toString()));
//
//                    //articles.addAll(NYTArticle.fromJSONArray(results));
//                    adapter.notifyDataSetChanged();
//                } catch (JSONException e) {
//
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable,
//                                  JSONObject errorResponse) {
//                super.onFailure(statusCode, headers, throwable, errorResponse);
//                Toast.makeText(SearchActivity.this, "Request failed!", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//    }


    public void searchArticles(String query) {

        this.client = new NYTimesRetroClient();

        articles.clear();

        if(filter!=null) {
            Call<NYTArticleResponse> call = client.getCallerWithFilter(query,filter);
            new NYTimesNetworkCall().execute(call);
        } else {
            Call<NYTArticleResponse> call = client.getCaller(query);
            new NYTimesNetworkCall().execute(call);
        }
        //articles.addAll(client.searchArticles(query));

    }

    private void showFilterDialog() {
        FragmentManager fm = getSupportFragmentManager();
        FilterFragment filterDialogFragment = FilterFragment.newInstance(filter);
        filterDialogFragment.show(fm, "fragment_filter");
    }

    @Override
    public void onFinishEditDialog(FilterWrapper filter) {
        this.filter = filter;
    }


    //TODO: Move this function outise of this activity
    // Append the next page of data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void loadNextDataFromApi(int offset) {
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`
    }

    private class NYTimesNetworkCall extends AsyncTask<Call, Void, NYTArticleResponse> {

        @Override
        protected NYTArticleResponse doInBackground(Call... params) {
            try {

                Call<NYTArticleResponse> call = params[0];
                Response<NYTArticleResponse> resp = call.execute();

                NYTArticleResponse nytArticleResponse = resp.body();

                Gson gson = new GsonBuilder().create();

                NYTArticleResponse articleResponse = gson.fromJson(gson.toJson(nytArticleResponse),
                        NYTArticleResponse.class);

                return articleResponse;

               // return nytArticleResponse;

            } catch (IOException e) {
                //TODO: Handle this
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(NYTArticleResponse response) {
            List<NYTArticle> nytArticles = NYTArticle.getArticlesFromJson(response);
            articles.addAll(nytArticles);
            adapter.notifyDataSetChanged();
        }
    }

}
