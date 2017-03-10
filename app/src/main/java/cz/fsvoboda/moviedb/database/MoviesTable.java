package cz.fsvoboda.moviedb.database;

import android.database.sqlite.SQLiteDatabase;

/**
 * @author Filip Svoboda
 */

public final class MoviesTable {
    public static final String TABLE_NAME = "movies";
    public static final String COLUMN_NAME_ID    = "_id";
    public static final String COLUMN_NAME_TITLE = "title";
    public static final String COLUMN_NAME_RATING = "rating";
    public static final String COLUMN_NAME_IMAGE_PATH = "image_path";
    public static final String COLUMN_NAME_RELEASE_DATE = "release_date";
    public static final String COLUMN_NAME_VOTE_COUNT = "vote_count";
    public static final String COLUMN_NAME_DESCRIPTION = "description";

    public static final String DATABASE_CREATE =
            "create table " + TABLE_NAME + "( "
                    + COLUMN_NAME_ID + " integer primary key,"
                    + COLUMN_NAME_TITLE + " text,"
                    + COLUMN_NAME_RATING + " real,"
                    + COLUMN_NAME_IMAGE_PATH + " text,"
                    + COLUMN_NAME_RELEASE_DATE + " text,"
                    + COLUMN_NAME_VOTE_COUNT + " integer,"
                    + COLUMN_NAME_DESCRIPTION + " text)";

    public static void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
