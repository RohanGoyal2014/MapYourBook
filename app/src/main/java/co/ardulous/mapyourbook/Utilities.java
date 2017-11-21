package co.ardulous.mapyourbook;

import android.graphics.Bitmap;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by ardulous on 21/11/17.
 */

public class Utilities {
    private Utilities() {
    }

    public static URL getUrlObj(String url) {
        if (url == null) {
            return null;
        } else {

            URL urlObj;
            try {
                urlObj = new URL(url);
            } catch (MalformedURLException e) {
                Log.e("Utilities", "Url is malformed...");
                return null;
            }
            return urlObj;
        }
    }

    public static String retreiveJSONString(String url) {
        URL reqdUrl = getUrlObj(url);
        if (reqdUrl != null) {
            HttpURLConnection httpURLConnection;
            try {
                httpURLConnection = (HttpURLConnection) reqdUrl.openConnection();
            } catch (IOException e) {
                Log.e("Utilities", "IOException while opening connection...");
                return null;
            }
            httpURLConnection.setReadTimeout(10000);
            int httpCode;
            httpURLConnection.setConnectTimeout(10000);
            try {
                httpURLConnection.connect();
                httpCode = httpURLConnection.getResponseCode();
            } catch (IOException e) {
                Log.e("Utilities", "IOException while connecting to Url...");
                return null;
            }
            StringBuilder JSONStringContent = new StringBuilder();
            if (httpCode == 200) {
                Log.v("Utilities","Successfully Connected");
                //everything seems Okay
                try {
                    InputStream inputStream = (InputStream) httpURLConnection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String bufferedContent;
                    bufferedContent = bufferedReader.readLine();
                    while (bufferedContent != null) {
                        JSONStringContent.append(bufferedContent);
                        bufferedContent = bufferedReader.readLine();
                    }
                } catch (IOException e) {
                    Log.e("Utilities", "Error Reading the file...");
                    return null;
                }
            } else {
                return null;
            }
            return JSONStringContent.toString();
        } else {
            return null;
        }
    }
    public static ArrayList<Book> getData(String url) {
        ArrayList<Book> bookArrayList = new ArrayList<>();
        String JSONString = retreiveJSONString(url);
        if(JSONString==null)
        {
            return null;
        }
        //Log.v("Utilities",JSONString);
        try {
            JSONObject jsonObject = new JSONObject(JSONString);
            //Log.e("Utilities","JSONObject Created");
            JSONArray jsonArray = jsonObject.getJSONArray("items");
            //Log.e("Utilities","jsonArrayCreated");
            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject volumeInfo = jsonArray.getJSONObject(i).getJSONObject("volumeInfo");
                String title = volumeInfo.getString("title");
                //Log.e("Utilities",title);
                JSONArray authors = volumeInfo.getJSONArray("authors");
                ArrayList<String> authorArrayList = new ArrayList<>();
                for (int j = 0; j < authors.length(); ++j) {
                    authorArrayList.add(authors.getString(j));
                    //Log.e("Utilities",authorArrayList.get(j));
                }
                String previewLink = volumeInfo.getString("previewLink");
                //Log.e("Utilities",previewLink);
                JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                //Log.e("Utilities","Imagelinks object accessed");
                String thumbnailUrl = imageLinks.getString("smallThumbnail");
                bookArrayList.add(new Book(title, authorArrayList, previewLink, thumbnailUrl));
            }
        } catch (JSONException e) {
            Log.e("Utilities", "Could not make a JSON out of String...");
            return null;
        }
        return bookArrayList;
    }
}
