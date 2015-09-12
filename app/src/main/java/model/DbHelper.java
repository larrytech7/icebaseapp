package model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    //	public static final String DB_NAME = "Candidate.sqlite";
    public static final String DB_NAME = "profiles.db";
    private static final int DB_VERSION = 2;

    private final String CREATE_PERSON_SQL = "CREATE TABLE "+ORM.Person.TABLE_NAME+"("+
            ORM.Person.COLUM_ID+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "+
            ORM.Person.COLUMN_NAME+" TEXT NOT NULL,"+
            ORM.Person.COLUMN_IMAGE+" BLOB NULL,"+
            ORM.Person.COLUMN_PORTFOLIO+" TEXT NULL,"+
            ORM.Person.COLUMN_DATE+" STRING NULL,"+
            ORM.Person.COLUMN_ADDRESS_ID+" INTEGER NOT NULL, UNIQUE("+ORM.Person.COLUM_ID+"),FOREIGN KEY("+ORM.Person.COLUMN_ADDRESS_ID+
            ") REFERENCES "+ORM.Address.TABLE_NAME+"("
             +ORM.Address.COLUMN_ID+") );";

    private final String CREATE_ADDRESS_SQL = "CREATE TABLE "+ORM.Address.TABLE_NAME+"("+
            ORM.Address.COLUMN_ID+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"+
            ORM.Address.COLUMN_COUNTRY+" VARCHAR(255) NOT NULL, "+
            ORM.Address.COLUMN_CITY+" TEXT NOT NULL,"+
            ORM.Address.COLUMN_LONGITUDE+" FLOAT(8) NOT NULL,"+
            ORM.Address.COLUMN_LATITUDE+" FLOAT(8) NOT NULL, UNIQUE("+ORM.Address.COLUMN_ID+"));";

    public DbHelper(Context context) {
        super(context, DB_NAME , null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqliteDb) {
        sqliteDb.execSQL(CREATE_ADDRESS_SQL);
        sqliteDb.execSQL(CREATE_PERSON_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int arg1, int arg2) {
        database.execSQL("DROP TABLE IF EXISTS "+ORM.Address.TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS "+ORM.Person.TABLE_NAME);
        onCreate(database);
    }

}




