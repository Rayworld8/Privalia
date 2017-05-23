package com.simbiotic.privalia.views.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.simbiotic.privalia.R;
import com.simbiotic.privalia.common.Constants;
import com.simbiotic.privalia.model.entities.Movie;
import com.squareup.picasso.Picasso;

import java.util.Collection;
import java.util.List;

/**
 * Created by Rayworld on 19/05/2017.
 */

public class MoviesAdapter extends BaseRecyclerViewAdapter{

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private List<Movie> allDataset;
    private Context mContext;
    private int maxSize;
    private int mPosition;


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTitle, txtYear, txtResume;
        public ImageView imgMovie;
        public Movie item;

        public ViewHolder(View v) {
            super(v);
            txtTitle = (TextView) v.findViewById(R.id.txtTitle);
            txtYear = (TextView) v.findViewById(R.id.txtYear);
            txtResume = (TextView) v.findViewById(R.id.txtResume);
            imgMovie = (ImageView) v.findViewById(R.id.imageMovie);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onItemClick(item);
                    }
                }
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MoviesAdapter(Context context, List<Movie> movies) {
        mContext = context;
        allDataset = movies;
    }


    @Override
    protected int getHeaderItemCount() {
        return 0;
    }

    @Override
    protected int getFooterItemCount() {
        int count = getContentItemCount();

        if (count == 0 || maxSize <= count)
            return 0;

        return 1;
    }

    @Override
    protected int getContentItemCount() {
        return allDataset == null ? 0 : allDataset.size();
    }

    @Override
    protected RecyclerView.ViewHolder onCreateHeaderItemViewHolder(ViewGroup parent, int headerViewType) {
        return null;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateFooterItemViewHolder(ViewGroup parent, int footerViewType) {
        return null;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateContentItemViewHolder(ViewGroup parent, int contentViewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movies, parent, false);

        // set the view's size, margins, paddings and layout parameters
        return new MoviesAdapter.ViewHolder(v);
    }

    @Override
    protected void onBindHeaderItemViewHolder(RecyclerView.ViewHolder headerViewHolder, int position) {

    }

    @Override
    protected void onBindFooterItemViewHolder(RecyclerView.ViewHolder footerViewHolder, int position) {

    }

    @Override
    protected void onBindContentItemViewHolder(RecyclerView.ViewHolder contentViewHolder, int position) {

        if(contentViewHolder instanceof ViewHolder){
            final Movie item = allDataset.get(position);
            ViewHolder holder = (ViewHolder) contentViewHolder;
            holder.item = item;

            holder.txtTitle.setText(item.getTitle());
            holder.txtYear.setText(item.getYear());
            holder.txtResume.setText(item.getResume());
            Picasso.with(mContext).load(Constants.MOVIE_IMAGE_URL + item.getPhoto()).into(holder.imgMovie);

        }
    }

    @Override
    protected void updateDataset(Object[] objects) {

    }

    @Override
    protected void updateDataset(List<? extends Object> objects) {
        allDataset.clear();
        for (Object ob : objects) {
            allDataset.addAll((Collection<? extends Movie>) ob);
        }
        notifyDataSetChanged();
    }

    public void updateMaxSize(int total) {
        maxSize = total;
    }

    public void addItems(List<? extends Object> objects) {
        for (Object ob : objects) {
            allDataset.addAll((Collection<? extends Movie>) ob);
        }
        notifyDataSetChanged();
    }

    public void removeItem() {
        allDataset.remove(mPosition);
        notifyDataSetChanged();
    }


    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;
        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar)v.findViewById(R.id.progressBarList);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return allDataset.get(position)!=null? VIEW_ITEM: VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if(viewType==VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movies, parent, false);
            vh = new ViewHolder(v);
        }else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_item, parent, false);
            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

}
