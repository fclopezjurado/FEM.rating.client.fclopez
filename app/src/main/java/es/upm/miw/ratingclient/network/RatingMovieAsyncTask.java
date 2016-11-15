package es.upm.miw.ratingclient.network;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ListView;

import es.upm.miw.ratingclient.dialogs.RatingMovieDialogFragment;

/**
 * Created by franlopez on 14/11/2016.
 */

public abstract class RatingMovieAsyncTask extends AsyncTask<ContentValues, Void, Uri> {
    private Context context;
    private int movieID;

    public RatingMovieAsyncTask(int movieID, Context context) {
        this.movieID = movieID;
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public int getMovieID() {
        return movieID;
    }

    protected abstract void onPreExecute();

    protected abstract Uri doInBackground(ContentValues... contentValues);

    protected abstract void onPostExecute(Uri uri);
}
