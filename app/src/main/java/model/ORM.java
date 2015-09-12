package model;

import android.content.ContentUris;
import android.net.Uri;

/**
 * Created by root on 9/11/15.
 */
public class ORM {

    public static final String AUTHORITY = "com.iceteck.provider.profiles";
    public static Uri BASE_CONTENT_URI = Uri.parse("content://"+ AUTHORITY );

    public static final class Person{
        ///this uri fully specifies the data to search/query for. in this case we manipulate category data
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath("person").build();

        public static final String CONTENT_TYPE ="vnd.android.cursor.dir/"+AUTHORITY+"/person";
        public static final String CONTENT_ITEM_TYPE ="vnd.android.cursor.item/"+AUTHORITY+"/person";

        public static final String TABLE_NAME = "person";

        public static final String COLUM_ID = "_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_PORTFOLIO = "portfolio";
        public static final String COLUMN_DATE = "datecreated";
        public static final String COLUMN_ADDRESS_ID = "person_address_id";

        public static Uri buildPersonUri(long id)
        {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class Address{
        //this uri fully specifies the data to search. In this case , we search for or manipulate nominees data
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath("address").build();

        public static final String CONTENT_TYPE ="vnd.android.cursor.dir/"+AUTHORITY+"/address";
        public static final String CONTENT_ITEM_TYPE ="vnd.android.cursor.item/"+AUTHORITY+"/address";

        public static final String TABLE_NAME = "address";

        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_COUNTRY = "country";
        public static final String COLUMN_CITY = "city";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_LATITUDE = "latitude";

        public static Uri buildAddressUri(long id)
        {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
