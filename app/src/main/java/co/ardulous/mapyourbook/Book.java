package co.ardulous.mapyourbook;

import java.util.ArrayList;

/**
 * Created by ardulous on 21/11/17.
 */

public class Book {
    private String btitle, bpreviewLink, bAuthorString, bthumbnailUrl;

    public Book(String title, ArrayList<String> authors, String previewLink, String thumbnailUrl) {
        btitle = title;
        bpreviewLink = previewLink;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < authors.size(); ++i) {
            builder.append(authors.get(i));
            if (i != authors.size() - 1) {
                builder.append(",");
            }
        }
        bAuthorString = builder.toString();
        bthumbnailUrl = thumbnailUrl;
    }

    public String getBtitle() {
        return btitle;
    }

    public String getBpreviewLink() {
        return bpreviewLink;
    }

    public String getbAuthorString() {
        return bAuthorString;
    }

    public String getBthumbnailUrl() {
        return bthumbnailUrl;
    }
}
