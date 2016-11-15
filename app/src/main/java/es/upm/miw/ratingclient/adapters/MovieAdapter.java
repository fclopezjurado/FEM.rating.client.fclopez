package es.upm.miw.ratingclient.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import es.upm.miw.ratingclient.R;
import es.upm.miw.ratingclient.models.Movie;

/**
 * Created by franlopez on 14/11/2016.
 */

public class MovieAdapter extends ArrayAdapter {
    public static final String CONTENT_URI = "https://image.tmdb.org/t/p/original/";

    private Context APPContext;
    private int layoutID;
    private ArrayList<Movie> movies;

    public MovieAdapter(Context context, int layoutID, List<Movie> movies) {
        super(context, layoutID, movies);
        this.setAPPContext(context);
        this.setLayoutID(layoutID);
        this.setMovies((ArrayList<Movie>)movies);
    }

    public Context getAPPContext() {
        return APPContext;
    }

    private void setAPPContext(Context APPContext) {
        this.APPContext = APPContext;
    }

    public int getLayoutID() {
        return layoutID;
    }

    private void setLayoutID(int layoutID) {
        this.layoutID = layoutID;
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    private void setMovies(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout view;

        if (null != convertView)
            view = (LinearLayout) convertView;
        else {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            view = (LinearLayout) layoutInflater.inflate(this.getLayoutID(), parent, false);
        }

        ImageView movieIcon = (ImageView)view.findViewById(R.id.movieIcon);
        TextView movieTitle = (TextView)view.findViewById(R.id.movieTitle);
        TextView movieDescription = (TextView)view.findViewById(R.id.movieDescription);

        movieIcon.setTag(this.getMovies().get(position));
        Picasso.with(this.getContext())
                .load(CONTENT_URI + this.getMovies().get(position).getPosterPath())
                .placeholder(R.drawable.movie)
                .resize(300, 300)
                .centerCrop()
                .into(movieIcon);

        movieTitle.setText(this.getMovies().get(position).getTitle());
        movieDescription.setText(this.getMovies().get(position).getOverview());

        return view;
    }
}
