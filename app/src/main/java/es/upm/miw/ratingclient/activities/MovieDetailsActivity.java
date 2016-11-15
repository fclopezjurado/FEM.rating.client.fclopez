package es.upm.miw.ratingclient.activities;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import es.upm.miw.ratingclient.R;
import es.upm.miw.ratingclient.adapters.MovieAdapter;
import es.upm.miw.ratingclient.dialogs.RatingMovieDialogFragment;
import es.upm.miw.ratingclient.models.Movie;

public class MovieDetailsActivity extends Activity {
    protected static final String MOVIE_STATE = "movieState";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details_activity);
        this.initView((Movie) getIntent().getSerializableExtra(MainActivity.MOVIE));
    }

    private void initView(Movie movie) {
        ImageView movieImage = (ImageView) findViewById(R.id.movieImage);
        TextView textViewForMovieDescription = (TextView) findViewById(R.id.movieDescription);

        textViewForMovieDescription.setMovementMethod(new ScrollingMovementMethod());
        movieImage.setTag(movie);

        Picasso.with(getApplicationContext())
                .load(MovieAdapter.CONTENT_URI + movie.getPosterPath())
                .placeholder(R.drawable.movie)
                .resize(900, 900)
                .centerCrop()
                .into(movieImage);

        String movieDescription = "MOVIE TITLE\n" + movie.getTitle() + "\n\nMOVIE ORIGINAL TITLE\n"
                + movie.getOriginalTitle() + "\n\nMOVIE ID IN TMDb\n" + movie.getMovieID()
                + "\n\nRELEASE DATE\n" + movie.getReleaseDate() + "\n\nNUMBER OF VOTES\n"
                + movie.getVoteCount() + "\n\nAVERAGE RATING\n" + movie.getVoteAverage()
                + "\n\nSYNOPSIS\n" + movie.getOverview() + "\n";

        textViewForMovieDescription.setText(movieDescription);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        ImageView movieImage = (ImageView) findViewById(R.id.movieImage);
        outState.putSerializable(MOVIE_STATE, (Movie)movieImage.getTag());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Movie movies = (Movie)savedInstanceState.getSerializable(MOVIE_STATE);
        this.initView(movies);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.movie_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ratingMovie:
                new RatingMovieDialogFragment().show(getFragmentManager(), "RATING MOVIE DIALOG");
                return true;
            default:
                this.finish();
                break;
        }

        return true;
    }
}
