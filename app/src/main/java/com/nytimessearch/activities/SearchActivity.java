package com.nytimessearch.activities;

import android.content.Intent;
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
import android.view.View;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nytimessearch.R;
import com.nytimessearch.adapters.NYTArticlesAdapter;
import com.nytimessearch.fragments.FilterFragment;
import com.nytimessearch.models.FilterWrapper;
import com.nytimessearch.models.NYTArticle;
import com.nytimessearch.network.NYTimesClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity
        implements FilterFragment.FilterDialogListener {

    RecyclerView rvArticles;

    List<NYTArticle> articles;
    NYTArticlesAdapter adapter;

    NYTimesClient client;

    FilterWrapper filter;

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
//        rvArticles.setLayoutManager(new LinearLayoutManager(this));

        // Attach the layout manager to the recycler view
        rvArticles.setLayoutManager(gridLayoutManager);

        rvArticles.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                int position = rvArticles.indexOfChild(v);
                NYTArticle article = articles.get(position);

                Intent intent = new Intent(SearchActivity.this,ReadArticleActivity.class);
                intent.putExtra("url",article.getWebUrl());
                startActivity(intent);
            }
        });

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

    public void searchArticles(String query) {

        this.client = new NYTimesClient();

        client.searchNYTimesArticles(query, filter,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    JSONArray results = response.getJSONObject("response").getJSONArray("docs");
                    articles.addAll(NYTArticle.fromJSONArray(results));
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                  JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(SearchActivity.this, "Request failed!", Toast.LENGTH_SHORT).show();
            }
        });


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
}
