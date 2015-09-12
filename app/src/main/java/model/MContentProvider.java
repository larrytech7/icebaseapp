package model;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class MContentProvider extends ContentProvider{

    private static final int PERSON  =  1;
    private static final int PERSON_ID  =  2;
    private static final int ADDRESS = 3;
    private static final int ADDRESS_ID = 4;
    private DbHelper database;
    private SQLiteDatabase sqliteDB;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static{
        uriMatcher.addURI(ORM.AUTHORITY, ORM.Person.TABLE_NAME, PERSON);
        uriMatcher.addURI(ORM.AUTHORITY, ORM.Person.TABLE_NAME+"/#",PERSON_ID );
        uriMatcher.addURI(ORM.AUTHORITY, ORM.Person.TABLE_NAME+"/*",PERSON_ID );
        uriMatcher.addURI(ORM.AUTHORITY, ORM.Address.TABLE_NAME, ADDRESS);
        uriMatcher.addURI(ORM.AUTHORITY, ORM.Address.TABLE_NAME+"/#", ADDRESS_ID);
    }

    @Override
    public int delete(Uri uri, String where, String[] whereargs) {

        switch(uriMatcher.match(uri)){
            case PERSON:
                return sqliteDB.delete(ORM.Person.TABLE_NAME, where, whereargs);
            default:
                throw new UnsupportedOperationException("Unknown URI: "+uri);
        }
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        sqliteDB = database.getWritableDatabase();
        long id = -1 ;
        switch(uriMatcher.match(uri)){
            case PERSON:
                id = sqliteDB.insert(ORM.Person.TABLE_NAME, null, values);
                if(id > 0){
                    return  ORM.Person.buildPersonUri(id);
                }else{
                    throw new android.database.SQLException("Failed to insert into: "+uri);
                }

            case ADDRESS:
                id = sqliteDB.insert(ORM.Address.TABLE_NAME, null, values);
                if(id>0){
                    return ORM.Address.buildAddressUri(id);
                }else{
                    throw new android.database.SQLException("Failed to insert into: "+uri);
                }

            default:
                throw new UnsupportedOperationException("Unknown URI: "+uri);
        }
    }

    @Override
    public boolean onCreate() {
        database = new DbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
        //sqlBuilder.setTables(Candidate.TABLE_NAME);

        int uriType = uriMatcher.match(uri);
        switch (uriType) {
            case PERSON: //get a list of all nominees in the database
		    	/*sqlBuilder.setTables(Person.TABLE_NAME+", "+Nominee.TABLE_NAME);
		    	sqlBuilder.setTables(ORM.Person.TABLE_NAME+" INNER JOIN "+ORMModel.Nominee.TABLE_NAME+
		    			" ON ("+ORM.Person.TABLE_NAME+"."+ORM.Person.COLUMN_ADDRESS_ID+" = "+
		    			ORM.Address.COLUM_ID+")");*/
                sqlBuilder.setTables(ORM.Person.TABLE_NAME);
                break;
            case PERSON_ID: //gets all the nominees belonging to a certain category id
                // adding the ID to the original query
                sqlBuilder.setTables(ORM.Person.TABLE_NAME);
                sqlBuilder.appendWhere(ORM.Person.COLUMN_ADDRESS_ID + "="
                        + uri.getLastPathSegment());
                break;
            case ADDRESS: //get a list of all categories
                sqlBuilder.setTables(ORM.Address.TABLE_NAME);
                break;
            case ADDRESS_ID: //get the category from a given id
                sqlBuilder.setTables(ORM.Address.TABLE_NAME);
                sqlBuilder.appendWhere(ORM.Address.COLUMN_ID +" = "
                        +uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = database.getReadableDatabase();
        Cursor  cursor  =  sqlBuilder.query(db, projection, selection, selectionArgs, null,
                null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = database.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int rowsUpdated = 0;
        switch(match){
            case PERSON_ID:
                rowsUpdated = db.update(ORM.Person.TABLE_NAME,
                        values, selection, selectionArgs);
                break;
            case ADDRESS:
                rowsUpdated = db.update(ORM.Address.TABLE_NAME,
                        values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("unknown URI  "+uri);
        }
        if(null == selection || 0 != rowsUpdated){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
    //optimal way to do multiple insert into the database. Efficiency increases as size of the data to be inserted increases
    @Override
    public int bulkInsert(Uri uri, ContentValues[] values){
        SQLiteDatabase db = database.getWritableDatabase();
        switch(uriMatcher.match(uri)){
            case ADDRESS:
                db.beginTransaction();
                int insertCount =  0;
                try{
                    for(ContentValues contentValue: values){
                        long id = db.insert(ORM.Address.TABLE_NAME, null, contentValue);
                        if(-1 != id){
                            insertCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                }finally{
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return insertCount;
            case PERSON:
                db.beginTransaction();
                int nCount =  0;
                try{
                    for(ContentValues contentValue: values){
                        long id = db.insert(ORM.Person.TABLE_NAME, null, contentValue);
                        if(-1 != id){
                            nCount++;
                        }else{
                            id = update(ORM.Person.buildPersonUri(contentValue.getAsInteger(ORM.Person.COLUM_ID)), contentValue, ORM.Person.COLUM_ID
                                    , new String[]{Long.toString(contentValue.getAsInteger(ORM.Person.COLUM_ID))});
                        }
                    }
                    db.setTransactionSuccessful();
                }finally{
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return nCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

}
