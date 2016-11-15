package es.upm.miw.ratingclient.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import es.upm.miw.ratingclient.R;
import es.upm.miw.ratingclient.activities.MainActivity;
import es.upm.miw.ratingclient.activities.MovieDetailsActivity;
import es.upm.miw.ratingclient.models.Movie;
import es.upm.miw.ratingclient.network.RatingMovieAsyncTask;

/**
 * Created by franlopez on 15/11/2016.
 */

public class RatingMovieDialogFragment extends DialogFragment {
    private static final String RATINGS_ENTITY = "ratings";
    protected static final String MOVIE_RATING_HAS_BEEN_SENT = "MOVIE RATING HAS BEEN SENT";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MovieDetailsActivity movieDetailsActivity = (MovieDetailsActivity) getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(View.inflate(movieDetailsActivity, R.layout.rating_movie_dialog, null))
                .setTitle(R.string.ratingMovieDialogTitle)
                .setPositiveButton(
                        R.string.yesRatingMovieDialogOption,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                Spinner spinnerToRateMovie = (Spinner) RatingMovieDialogFragment.this.getDialog()
                                        .findViewById(R.id.spinnerToRateMovie);

                                double rating = Double.parseDouble(spinnerToRateMovie.getSelectedItem()
                                        .toString());

                                asyncTaskToRateMovieByMovieID(rating);
                            }
                        }
                )
                .setNegativeButton(
                        R.string.noRatingMovieDialogOption,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                RatingMovieDialogFragment.this.getDialog().cancel();
                            }
                        }
                );

        return builder.create();
    }

    private void asyncTaskToRateMovieByMovieID(double rating) {
        MovieDetailsActivity movieDetailsActivity = (MovieDetailsActivity) getActivity();
        ImageView movieImage = (ImageView) movieDetailsActivity.findViewById(R.id.movieImage);
        Movie movie = (Movie) movieImage.getTag();
        ContentValues contentValues = new ContentValues();
        contentValues.put("rating", rating);
        movieDetailsActivity.getApplicationContext();

        RatingMovieAsyncTask myTask = new RatingMovieAsyncTask(movie.getMovieID(),
                movieDetailsActivity.getApplicationContext()) {
            @Override
            protected void onPreExecute() {
            }

            @Override
            protected Uri doInBackground(ContentValues... contentValues) {
                Uri uri = Uri.parse(MainActivity.CONTENT_URI + "/" + RATINGS_ENTITY + "/"
                        + this.getMovieID());
                ContentResolver contentResolver = this.getContext().getContentResolver();
                return contentResolver.insert(uri, contentValues[0]);
            }

            @Override
            protected void onPostExecute(Uri uri) {
                String ratingInternalID = uri.getLastPathSegment();

                if (Integer.parseInt(ratingInternalID) == -1) {
                    Log.i(MainActivity.FILTER_FOR_LOGS, MainActivity.UNSUCCESSFUL_REQUEST);
                    Toast.makeText(this.getContext(), MainActivity.UNSUCCESSFUL_REQUEST,
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this.getContext(), MOVIE_RATING_HAS_BEEN_SENT,
                            Toast.LENGTH_LONG).show();
                }
            }
        };

        myTask.execute(contentValues);
    }
}
