package co.ardulous.mapyourbook;

import android.app.LoaderManager;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;

import java.util.ArrayList;

public class BookListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Book>> {

    private static String BOOK_URL = "https://www.googleapis.com/books/v1/volumes?q=android&maxResults=10";
    ListView listView;
    private BookAdapter bookAdapter;
    ProgressBar progressBar;
    ImageView noInternetView;
    NetworkInfo networkInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("Main", "Activity Created");
        setContentView(R.layout.activity_book_list);
        listView = findViewById(R.id.booklistview);
        progressBar =findViewById(R.id.progress);
        noInternetView=findViewById(R.id.nointernet);
        ConnectivityManager connectivityManager=(ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null) {
            getLoaderManager().initLoader(0, null, this);
        }
        else
        {
            progressBar.setVisibility(View.GONE);
            noInternetView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public Loader onCreateLoader(int i, Bundle bundle) {
        Log.v("Main", "Loader Created");
        return new BookAsyncLoader(this, BOOK_URL);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Book>> loader, ArrayList<Book> arrayList) {
        if(arrayList!=null) {
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
        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo2=connectivityManager.getActiveNetworkInfo();
        if(networkInfo2!=null&&networkInfo==null) {
            getLoaderManager().initLoader(0, null, this);
        }
        else if(networkInfo2==null && networkInfo!=null)
        {
            progressBar.setVisibility(View.GONE);
            noInternetView.setVisibility(View.VISIBLE);
        }
        networkInfo=networkInfo2;
    }
}
