package com.simbiotic.privalia.views.activities;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.simbiotic.privalia.R;
import com.simbiotic.privalia.common.Constants;
import com.simbiotic.privalia.domain.events.GetMoviesEvent;
import com.simbiotic.privalia.domain.events.GetSearchMoviesEvent;
import com.simbiotic.privalia.domain.usecases.GetMoviesUsecase;
import com.simbiotic.privalia.domain.usecases.SearchMoviesUsecase;
import com.simbiotic.privalia.model.entities.Movie;
import com.simbiotic.privalia.views.fragments.MovieFragment;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class MainActivity extends BaseActivity {

    private MovieFragment moviesFragment = null;
    private SearchManager searchManager;
    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        moviesFragment = new MovieFragment();
        showFragment(moviesFragment);

        EventBus.getDefault().post(new GetMoviesUsecase(Constants.FIRST_TIME));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setLayoutParams(new ActionBar.LayoutParams(Gravity.RIGHT));
        //searchView.setQueryHint(hint);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.onActionViewCollapsed();
                moviesFragment.txtNoResults.setVisibility(View.GONE);
                moviesFragment.getSwipe().setRefreshing(true);
                EventBus.getDefault().post(new SearchMoviesUsecase(query, Constants.FIRST_TIME));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                /*if (query.length() > 2) {
                    searchView.onActionViewCollapsed();
                    moviesFragment.txtNoResults.setVisibility(View.GONE);
                    moviesFragment.getSwipe().setRefreshing(true);
                    EventBus.getDefault().post(new SearchMoviesUsecase(query, Constants.FIRST_TIME));

                }*/
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                return false;
            }
        });

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void showFragment(MovieFragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }


    public void onEventMainThread(GetMoviesEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
        Log.d("GetMoviesEvent", "ok");
        ArrayList<Movie> mov = event.getMovies();
        int token = event.getToken();
        moviesFragment.setData(mov, token);

        if (mov.isEmpty()){
            moviesFragment.txtNoResults.setVisibility(View.VISIBLE);
        } else {
            moviesFragment.txtNoResults.setVisibility(View.GONE);
        }
    }

    public void onEventMainThread(GetSearchMoviesEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
        Log.d("GetSearchMoviesEvent", "ok");
        ArrayList<Movie> mov = event.getSearchMovies();
        int token = event.getToken();
        moviesFragment.setData(mov, token);

        if (mov.isEmpty()){
            moviesFragment.txtNoResults.setVisibility(View.VISIBLE);
        } else {
            moviesFragment.txtNoResults.setVisibility(View.GONE);
        }
    }
}
