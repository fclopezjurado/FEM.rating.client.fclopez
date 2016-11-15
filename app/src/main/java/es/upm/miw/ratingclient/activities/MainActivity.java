package es.upm.miw.ratingclient.activities;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import es.upm.miw.ratingclient.R;
import es.upm.miw.ratingclient.adapters.MovieAdapter;
import es.upm.miw.ratingclient.models.Movie;
import es.upm.miw.ratingclient.models.MoviesContract;
import es.upm.miw.ratingclient.network.MainAsyncTask;

public class MainActivity extends Activity {
    public static final String FILTER_FOR_LOGS = "MiW16";
    public static final String UNSUCCESSFUL_REQUEST = "UNSUCCESSFUL REQUEST";
    public static final String CONTENT_URI =
            "content://es.upm.miw.ratingmanager.contentprovider.provider";

    protected static final String MOVIES_ENTITY = "movies";
    protected static final String MOVIE = "movie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinnerForMonths = (Spinner) findViewById(R.id.spinnerForMonths);
        Spinner spinnerForYears = (Spinner) findViewById(R.id.spinnerForYears);
        Button buttonToSearchMovies = (Button) findViewById(R.id.buttonToSearchFilms);
        ListView moviesList = (ListView) findViewById((R.id.moviesList));

        ArrayAdapter<CharSequence> adapterForSpinnerForMonths = ArrayAdapter.createFromResource(
                this, R.array.months, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterForSpinnerForYears = ArrayAdapter.createFromResource(
                this, R.array.years, android.R.layout.simple_spinner_item);

        adapterForSpinnerForMonths.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterForSpinnerForYears.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerForMonths.setAdapter(adapterForSpinnerForMonths);
        spinnerForYears.setAdapter(adapterForSpinnerForYears);

        buttonToSearchMovies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                asyncTaskToGetMoviesByReleaseDate();
            }
        });

        moviesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Movie movie = (Movie)view.findViewById(R.id.movieIcon).getTag();
                Intent intent = new Intent(getApplicationContext(), MovieDetailsActivity.class);

                intent.putExtra(MOVIE, movie);
                startActivity(intent);
            }
        });
    }

    public void onSaveInstanceState(Bundle outState) {
        ArrayList<Movie> movies = new ArrayList<>();
        ListView listViewForMovies = (ListView)findViewById(R.id.moviesList);

        for (int index = 0; index < listViewForMovies.getCount(); index++) {
            View view = listViewForMovies.getAdapter().getView(index, null, null);
            ImageView movieIcon = (ImageView)view.findViewById(R.id.movieIcon);

            movies.add((Movie)movieIcon.getTag());
        }

        outState.putSerializable(MOVIES_ENTITY, movies);
        super.onSaveInstanceState(outState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ListView listViewForMovies = (ListView)findViewById(R.id.moviesList);
        ArrayList<Movie> movies = (ArrayList<Movie>)savedInstanceState.getSerializable(MOVIES_ENTITY);

        MovieAdapter movieAdapter = new MovieAdapter(getApplicationContext(),
                R.layout.movie_listview_element, movies);

        listViewForMovies.setAdapter(movieAdapter);
    }

    private void asyncTaskToGetMoviesByReleaseDate() {
        MainAsyncTask myTask = new MainAsyncTask() {
            @Override
            protected void onPreExecute() {
                Spinner spinnerForMonths = (Spinner) findViewById(R.id.spinnerForMonths);
                Spinner spinnerForYears = (Spinner) findViewById(R.id.spinnerForYears);
                String selectedYear = spinnerForYears.getSelectedItem().toString();
                String selectedMonth = Integer.toString(spinnerForMonths.getSelectedItemPosition()
                        + 1);

                if (selectedMonth.length() < 2)
                    selectedMonth = "0" + selectedMonth;

                this.setRequestParameter(selectedYear + "-" + selectedMonth + "-01");
            }

            @Override
            protected Cursor doInBackground(ListView... ListViews) {
                Uri uri = Uri.parse(CONTENT_URI + "/" + MOVIES_ENTITY + "/"
                        + this.getRequestParameter());
                ContentResolver contentResolver = getContentResolver();

                this.setListView(ListViews[0]);
                return contentResolver.query(uri, null, null, null, null);
            }

            @Override
            protected void onPostExecute(Cursor cursor) {
                if (cursor != null) {
                    ArrayList<Movie> movies = new ArrayList<>();
                    int ID, voteCount;
                    String posterPath, overview, releaseDate, title, originalTitle;
                    double voteAverage;
                    long createdAt;

                    if (cursor.moveToFirst()) {
                        while (!cursor.isAfterLast()) {
                            ID = cursor.getInt(cursor.getColumnIndex(
                                    MoviesContract.moviesTableDefinition.COLUMN_NAME_MOVIE_ID));
                            posterPath = cursor.getString(cursor.getColumnIndex(
                                    MoviesContract.moviesTableDefinition.COLUMN_NAME_POSTER_PATH));
                            overview = cursor.getString(cursor.getColumnIndex(
                                    MoviesContract.moviesTableDefinition.COLUMN_NAME_OVERVIEW));
                            releaseDate = cursor.getString(cursor.getColumnIndex(
                                    MoviesContract.moviesTableDefinition.COLUMN_NAME_RELEASE_DATE));
                            title = cursor.getString(cursor.getColumnIndex(
                                    MoviesContract.moviesTableDefinition.COLUMN_NAME_MOVIE_TITLE));
                            originalTitle = cursor.getString(cursor.getColumnIndex(
                                    MoviesContract.moviesTableDefinition
                                            .COLUMN_NAME_ORIGINAL_TITLE));
                            voteCount = cursor.getInt(cursor.getColumnIndex(
                                    MoviesContract.moviesTableDefinition.COLUMN_NAME_VOTE_COUNT));
                            voteAverage = cursor.getDouble(cursor.getColumnIndex(
                                    MoviesContract.moviesTableDefinition.COLUMN_NAME_VOTE_AVERAGE));
                            createdAt = cursor.getLong(cursor.getColumnIndex(
                                    MoviesContract.moviesTableDefinition
                                            .COLUMN_NAME_RECORD_CREATED_AT));

                            Movie movie = new Movie(ID, posterPath, overview,
                                    releaseDate, title, originalTitle, voteCount, voteAverage,
                                    createdAt);

                            movies.add(movie);
                            cursor.moveToNext();
                        }

                        cursor.close();
                    }

                    MovieAdapter movieAdapter = new MovieAdapter(getApplicationContext(),
                            R.layout.movie_listview_element, movies);

                    this.getListView().setAdapter(movieAdapter);
                } else {
                    Log.i(FILTER_FOR_LOGS, UNSUCCESSFUL_REQUEST);
                    Toast.makeText(getApplicationContext(), UNSUCCESSFUL_REQUEST,
                            Toast.LENGTH_LONG).show();
                }
            }
        };

        myTask.execute((ListView) findViewById(R.id.moviesList));
    }
}
