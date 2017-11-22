package co.ardulous.mapyourbook;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ardulous on 21/11/17.
 */

public class BookAsyncLoader extends AsyncTaskLoader<ArrayList<Book>> {
    private String MUrl;
    private ArrayList<Book> bookArrayList;

    public BookAsyncLoader(Context context, String url) {
        super(context);
        MUrl = url;
        //Log.e("AsyncLoader",MUrl);
    }

    @Override
    public ArrayList<Book> loadInBackground() {
        bookArrayList = Utilities.getData(MUrl);
        /*for(int i=0;i<bookArrayList.size();++i)
        {
            Log.e("BookAsyncLoader",String.valueOf(i));
        }*/
        return bookArrayList;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
