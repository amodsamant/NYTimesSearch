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
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nytimessearch.helpers.EndlessRecyclerViewScrollListener;
import com.nytimessearch.R;
import com.nytimessearch.adapters.NYTArticlesHeteroAdapter;
import com.nytimessearch.fragments.FilterFragment;
import com.nytimessearch.models.FilterWrapper;
import com.nytimessearch.models.NYTArticle;
import com.nytimessearch.models.NYTArticleResponse;
import com.nytimessearch.network.NYTimesRetroClient;
import com.nytimessearch.network.NetworkUtils;
import com.nytimessearch.utils.GenericUtils;

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
    private NYTArticlesHeteroAdapter adapter;
    private NYTimesRetroClient client;
    private FilterWrapper filter;
    private String currentQuery;

    private EndlessRecyclerViewScrollListener scrollListenerSpan1;
    private EndlessRecyclerViewScrollListener scrollListenerSpan2;

    private StaggeredGridLayoutManager gridLayoutManagerSpan1;
    private StaggeredGridLayoutManager gridLayoutManagerSpan2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Removing the title from the tool bar
        getSupportActionBar().setTitle("");

        //Setup the view for this SearchActivity
        setupViews();

        //Check for network/internet
        checkNetworkInternetConnectivity();

        currentQuery = null;
        searchArticles(currentQuery,0);

    }

    /**
     * Function sets up all the necessary views and initializes the inputs for
     * adapters. Here 2 StaggeredGridLayoutManager are used with different span
     * counts to allow user to have functionality to view the articles in different
     * forms.
     */
    private void setupViews() {

        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinatorLayout);

        //Setup StaggeredGridLayoutManager with 1 span count
        gridLayoutManagerSpan1 = new StaggeredGridLayoutManager(1,
                StaggeredGridLayoutManager.VERTICAL);

        //Setup StaggeredGridLayoutManager with 2 span count
        gridLayoutManagerSpan2 = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);

        // Recycler view setup with NYTArticlesHeteroAdapter
        rvArticles = (RecyclerView) findViewById(R.id.rvResults);
        articles = new ArrayList<>();
        adapter = new NYTArticlesHeteroAdapter(this, articles);
        rvArticles.setAdapter(adapter);

        // Default layout manager set to the recycler view
        rvArticles.setLayoutManager(gridLayoutManagerSpan1);

        scrollListenerSpan1 = new EndlessRecyclerViewScrollListener(gridLayoutManagerSpan1) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data is needed. Endless scrolling
                loadNextDataFromApi(page);
            }
        };

        scrollListenerSpan2 = new EndlessRecyclerViewScrollListener(gridLayoutManagerSpan2) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data is needed. Endless scrolling
                loadNextDataFromApi(page);
            }
        };

        // Adds the scroll listener to RecyclerView
        rvArticles.addOnScrollListener(scrollListenerSpan1);
        //rvArticles.addOnScrollListener(scrollListenerSpan2);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);

        // Search View menu item
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        EditText etSearchView = (EditText) searchView.
                findViewById(android.support.v7.appcompat.R.id.search_src_text);
        // Assigning text color for search view
        etSearchView.setTextColor(Color.GRAY);
        etSearchView.setHintTextColor(Color.GRAY);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                //Current query is always set to this query
                currentQuery = query;

                if(checkNetworkInternetConnectivity()) {
                    //Reset adapter and articles for the new query and
                    //reset the scroll listener state as this is a new query
                    articles.clear();
                    adapter.notifyDataSetChanged();
                    scrollListenerSpan1.resetState();
                    scrollListenerSpan2.resetState();
                    searchArticles(currentQuery, 0);
                }
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if(newText.length()>0) {
                    currentQuery = newText;
                } else {
                    currentQuery = null;
                }
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
            case R.id.action_grid:
                //Changes the grid pattern and sets correct listeners
                rvArticles.clearOnScrollListeners();
                if(GenericUtils.spanCount==1) {
                    // Change the grid pattern to 2 spans
                    GenericUtils.spanCount = 2;
                    item.setIcon(R.drawable.ic_grid1);
                    rvArticles.setLayoutManager(gridLayoutManagerSpan2);
                    rvArticles.addOnScrollListener(scrollListenerSpan2);
                } else {
                    // Change the grid pattern to 1 span
                    GenericUtils.spanCount = 1;
                    item.setIcon(R.drawable.ic_grid2);
                    rvArticles.setLayoutManager(gridLayoutManagerSpan1);
                    rvArticles.addOnScrollListener(scrollListenerSpan1);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Function uses the query parameter and offset to make an API call
     * using the {@link NYTimesNetworkCall}
     * @param query
     * @param offset
     */
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

    /**
     * Call the filter dialog for modal overlay
     */
    private void showFilterDialog() {
        FragmentManager fm = getSupportFragmentManager();
        FilterFragment filterDialogFragment = FilterFragment.newInstance(filter);
        filterDialogFragment.show(fm, "fragment_filter");
    }

    @Override
    public void onFinishEditDialog(FilterWrapper filter) {
        this.filter = filter;

        Toast.makeText(this,"Filters Applied!",Toast.LENGTH_SHORT).show();

        //Start a new search with the new filters applied
        articles.clear();
        adapter.notifyDataSetChanged();
        scrollListenerSpan1.resetState();
        scrollListenerSpan2.resetState();
        searchArticles(currentQuery,0);
    }

    /**
     * Function to load infinitely
     * @param offset
     */
    public void loadNextDataFromApi(int offset) {
        // API request to retrieve appropriate paginated data
        if(checkNetworkInternetConnectivity()) {
            searchArticles(currentQuery, offset);
        }

    }

    /**
     *  Runs a sync task to call new york times api and retreives the result in
     *  the onPostExecute method
     */
    private class NYTimesNetworkCall extends AsyncTask<Call, Void, NYTArticleResponse> {

        @Override
        protected NYTArticleResponse doInBackground(Call... params) {
            try {

                Call<NYTArticleResponse> call = params[0];
                Response<NYTArticleResponse> resp = call.execute();
                NYTArticleResponse nytArticleResponse = resp.body();

                Gson gson = new GsonBuilder().create();
                return gson.fromJson(gson.toJson(nytArticleResponse),
                        NYTArticleResponse.class);

            } catch (IOException e) {
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Error retrieving results!", Snackbar.LENGTH_LONG)
                        .setAction("RETRY", view -> {
                            //TODO:
                        });
                snackbar.setActionTextColor(Color.RED);
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

    /**
     * Function checks the network/internet connectivity. The main call is moved out
     * into the {@link NetworkUtils} which returns a boolean if network or internet is
     * not available.
     *
     * @return true if network/internet available else false
     */
    private boolean checkNetworkInternetConnectivity() {

        boolean network = NetworkUtils.isNetworkAvailable(getBaseContext());
        boolean internetAvailable = NetworkUtils.isOnline();
        if(!network || !internetAvailable) {

            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG)
                    .setAction("RETRY", view -> {
                        //TODO:
                    });
            snackbar.setActionTextColor(Color.RED);

            snackbar.show();
            return false;
        }
        return true;
    }

}
