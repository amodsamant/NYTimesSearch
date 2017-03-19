package com.nytimessearch.activities;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
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
import com.nytimessearch.adapters.NYTArticlesHeteroAdapter;
import com.nytimessearch.fragments.FilterFragment;
import com.nytimessearch.models.FilterWrapper;
import com.nytimessearch.models.NYTArticle;
import com.nytimessearch.models.NYTArticleResponse;
import com.nytimessearch.network.NYTimesRetroClient;
import com.nytimessearch.network.NetworkUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity
        implements FilterFragment.FilterDialogListener {

    private CoordinatorLayout coordinatorLayout;

    private RecyclerView rvArticles;

    private List<NYTArticle> articles;
    //private NYTArticlesAdapter adapter;
    private NYTArticlesHeteroAdapter adapter;

    private NYTimesRetroClient client;

    private FilterWrapper filter;

    private String currentQuery;

    // Store a member variable for the listener
    private EndlessRecyclerViewScrollListener scrollListener;

    private StaggeredGridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupViews();

        checkNetworkInternetConnectivity();

        currentQuery = "";
    }

    private void setupViews() {

        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinatorLayout);

        gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        rvArticles = (RecyclerView) findViewById(R.id.rvResults);

        articles = new ArrayList<>();
        adapter = new NYTArticlesHeteroAdapter(this, articles);
        rvArticles.setAdapter(adapter);

        // Attach the layout manager to the recycler view
        rvArticles.setLayoutManager(gridLayoutManager);

        // Retain an instance so that you can call `resetState()` for fresh searches
        scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data is needed. Endless scrolling
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

                currentQuery = query;

                if(checkNetworkInternetConnectivity()) {
                    //Only place to reset adapter and articles for the new query
                    //and reset the scroll listener state as this is a new query
                    articles.clear();
                    adapter.notifyDataSetChanged();
                    scrollListener.resetState();

                    searchArticles(query, 0);
                }
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

    public void searchArticles(String query, int offset) {

        this.client = new NYTimesRetroClient();

        if(filter!=null) {
            Call<NYTArticleResponse> call = client.getCallerWithFilter(query,filter, offset);
            new NYTimesNetworkCall().execute(call);
        } else {
            Call<NYTArticleResponse> call = client.getCaller(query, offset);
            new NYTimesNetworkCall().execute(call);
        }

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

        // API request to retrieve appropriate paginated data
        if(checkNetworkInternetConnectivity()) {
            searchArticles(currentQuery, offset);
        }

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
            int curSize = adapter.getItemCount();
            adapter.notifyItemRangeInserted(curSize, articles.size() - 1);
        }
    }


    private boolean checkNetworkInternetConnectivity() {

        boolean network = NetworkUtils.isNetworkAvailable(getBaseContext());

        boolean internetAvailable = NetworkUtils.isOnline();

        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG)
                .setAction("RETRY", view -> {
                    //TODO:
                });
        snackbar.setActionTextColor(Color.RED);

        if(!network || !internetAvailable) {
            snackbar.show();
            return false;
        }

        return true;
    }

}
