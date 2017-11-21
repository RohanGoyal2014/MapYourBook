package co.ardulous.mapyourbook;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ardulous on 21/11/17.
 */

public class BookAdapter extends ArrayAdapter<Book>{
    public BookAdapter(Context context,ArrayList<Book> arrayList)
    {
        super(context,0,arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listView=convertView;
        if(listView==null)
        {
            listView= LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }
        final Book currBook=getItem(position);
        TextView titleView=listView.findViewById(R.id.booktitle);
        TextView authorView=listView.findViewById(R.id.authors);
        ImageView imageView=listView.findViewById(R.id.imagethumbnail);
        titleView.setText(currBook.getBtitle());
        authorView.setText(currBook.getbAuthorString());
        //Log.e("BookAdapter",currBook.getBthumbnailUrl());
        //I included picasso in app level build.grade and used it-->
        Picasso.with(getContext()).load(Uri.parse(currBook.getBthumbnailUrl())).into(imageView);
        //imageView.setImageURI(Uri.parse(currBook.getBthumbnailUrl()));
        listView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setData(Uri.parse(currBook.getBpreviewLink()));
                getContext().startActivity(intent);
            }
        });
        return listView;
    }
}
