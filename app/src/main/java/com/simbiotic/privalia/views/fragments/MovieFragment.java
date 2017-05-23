package com.simbiotic.privalia.views.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.simbiotic.privalia.R;
import com.simbiotic.privalia.common.Constants;
import com.simbiotic.privalia.domain.usecases.GetMoviesUsecase;
import com.simbiotic.privalia.domain.usecases.SearchMoviesUsecase;
import com.simbiotic.privalia.model.entities.Movie;
import com.simbiotic.privalia.model.ws.MovieApi;
import com.simbiotic.privalia.views.adapter.BaseRecyclerViewAdapter;
import com.simbiotic.privalia.views.adapter.EndlessRecyclerOnScrollListener;
import com.simbiotic.privalia.views.adapter.MoviesAdapter;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

import static com.simbiotic.privalia.model.ws.MovieApi.searching;

/**
 * Created by Rayworld on 18/05/2017.
 */

public class MovieFragment extends BaseFragment implements BaseRecyclerViewAdapter.OnItemClickListener{

    private MoviesAdapter mAdapter;
    private RecyclerView mRecyclerView;
    SwipeRefreshLayout swipe;
    private ArrayList<Movie> mObjects = new ArrayList<>();
    public TextView txtNoResults;

    public MovieFragment () {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);

        txtNoResults = (TextView) rootView.findViewById(R.id.noResults);
        swipe = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                txtNoResults.setVisibility(View.GONE);

                EventBus.getDefault().post(new GetMoviesUsecase(Constants.FIRST_TIME));

            }
        });

        swipe.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        mRecyclerView = (RecyclerView) rootView.findViewById(android.R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new MoviesAdapter(getActivity(), mObjects);
        mAdapter.setOnItemClickListener((BaseRecyclerViewAdapter.OnItemClickListener) this);

        mRecyclerView.setAdapter(mAdapter);

        swipe.setRefreshing(true);

        mRecyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener((LinearLayoutManager) mRecyclerView.getLayoutManager()) {
            @Override
            public void onLoadMore(int current_page) {
                //add progress item
                if (MovieApi.total_pages > MovieApi.page) {
                    mObjects.add(null);
                    mAdapter.notifyItemInserted(mObjects.size() - 1);
                    //seguent crida de movies
                    Log.d("END", "ok");

                    if (searching) {
                        EventBus.getDefault().post(new SearchMoviesUsecase(MovieApi.wordQuery, MovieApi.page+1));

                    } else {
                        EventBus.getDefault().post(new GetMoviesUsecase(MovieApi.page+1));

                    }

                }


            }
        });


        return rootView;
    }


    @Override
    public void onItemClick(Object item) {

    }


    public void setData(ArrayList<Movie> auxMovies, int token){
        if (swipe != null) {
            swipe.setRefreshing(false);
        }
        if (token==1) {
            mObjects.clear();
        } else {
            mObjects.remove(mObjects.size() - 1);
            mAdapter.notifyItemInserted(mObjects.size());
        }
        mObjects.addAll(auxMovies);
        mAdapter.notifyDataSetChanged();
    }

    public SwipeRefreshLayout getSwipe(){
        return  swipe;
    }
}
