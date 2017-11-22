package co.ardulous.mapyourbook;

import android.app.LoaderManager;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;

import java.util.ArrayList;

public class BookListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Book>> {

    ListView listView;
    ProgressBar progressBar;
    ImageView noInternetView;
    NetworkInfo networkInfo;
    SearchView searchView;
    private String BOOK_URL = "https://www.googleapis.com/books/v1/volumes?q=All&maxResults=10";
    private BookAdapter bookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("Main", "Activity Created");
        setContentView(R.layout.activity_book_list);
        listView = findViewById(R.id.booklistview);
        progressBar = findViewById(R.id.progress);
        noInternetView = findViewById(R.id.nointernet);
        searchView = findViewById(R.id.searchbar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (searchView.getQuery() == null) {
                    return false;
                }
                int index = BOOK_URL.indexOf('?');
                String temp = BOOK_URL.substring(0, index + 3);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(temp).append(s);
                int nextIndex = BOOK_URL.indexOf("&maxResults=10");
                stringBuilder.append(BOOK_URL.substring(nextIndex, BOOK_URL.length()));
                //Log.e("Something",stringBuilder.toString());
                BOOK_URL = stringBuilder.toString();
                Log.e("AWw", BOOK_URL);
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo == null) {
                    return false;
                }
                loaderUpdate();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            getLoaderManager().initLoader(0, null, this);
        } else {
            progressBar.setVisibility(View.GONE);
            noInternetView.setVisibility(View.VISIBLE);
        }
    }

    private void loaderUpdate() {
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public Loader onCreateLoader(int i, Bundle bundle) {
        Log.v("Main", "Loader Created");
        return new BookAsyncLoader(this, BOOK_URL);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Book>> loader, ArrayList<Book> arrayList) {
        if (arrayList != null) {
            bookAdapter = new BookAdapter(this, arrayList);
            updateUI();
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Book>> loader) {
        bookAdapter.clear();
        bookAdapter = new BookAdapter(this, null);
    }

    private void updateUI() {
        progressBar.setVisibility(View.GONE);
        noInternetView.setVisibility(View.GONE);
        if (bookAdapter != null) {
            listView.setAdapter(bookAdapter);
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo2 = connectivityManager.getActiveNetworkInfo();
        if (networkInfo2 != null && networkInfo == null) {
            getLoaderManager().initLoader(0, null, this);
        } else if (networkInfo2 == null && networkInfo != null) {
            progressBar.setVisibility(View.GONE);
            noInternetView.setVisibility(View.VISIBLE);
        }
        networkInfo = networkInfo2;
    }
}
