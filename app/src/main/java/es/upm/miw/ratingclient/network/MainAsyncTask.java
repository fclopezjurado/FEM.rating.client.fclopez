package es.upm.miw.ratingclient.network;

import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.ListView;

/**
 * Created by franlopez on 14/11/2016.
 */

public abstract class MainAsyncTask extends AsyncTask<ListView, Void, Cursor> {
    private ListView listView;
    private String requestParameter;

    public ListView getListView() {
        return listView;
    }

    public void setListView(ListView listView) {
        this.listView = listView;
    }

    public String getRequestParameter() {
        return requestParameter;
    }

    public void setRequestParameter(String requestParameter) {
        this.requestParameter = requestParameter;
    }

    protected abstract void onPreExecute();

    protected abstract Cursor doInBackground(ListView... ListViews);

    protected abstract void onPostExecute(Cursor cursor);
}
