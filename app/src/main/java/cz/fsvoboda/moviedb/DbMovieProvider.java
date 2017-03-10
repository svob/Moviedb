package cz.fsvoboda.moviedb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cz.fsvoboda.moviedb.api.Movie;
import cz.fsvoboda.moviedb.database.MovieDbHelper;
import cz.fsvoboda.moviedb.database.MoviesTable;

/**
 * @author Filip Svoboda
 */

public class DbMovieProvider implements IMovieProvider {

    private SQLiteDatabase database;
    private MovieDbHelper dbHelper;
    private String[] allColumns = { MoviesTable.COLUMN_NAME_ID,
                                    MoviesTable.COLUMN_NAME_TITLE,
                                    MoviesTable.COLUMN_NAME_RATING,
                                    MoviesTable.COLUMN_NAME_IMAGE_PATH,
                                    MoviesTable.COLUMN_NAME_RELEASE_DATE,
                                    MoviesTable.COLUMN_NAME_VOTE_COUNT,
                                    MoviesTable.COLUMN_NAME_DESCRIPTION };

    public DbMovieProvider(Context context) {
        dbHelper = new MovieDbHelper(context);
        open();
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    @Override
    public void getMovies(int page, int pageSize, MainActivity.CallbackInterface callback) {
        List<Movie> movies = new ArrayList<>();
        String currentPage = String.valueOf((page - 1) * pageSize);

        Cursor cursor = database.query(
                MoviesTable.TABLE_NAME,
                allColumns,
                MoviesTable.COLUMN_NAME_ID + ">="+currentPage,
                null,
                null,
                null,
                MoviesTable.COLUMN_NAME_ID + " ASC",
                String.valueOf(pageSize)
                );

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Movie movie = cursorToMovie(cursor);
            movies.add(movie);
            cursor.moveToNext();
        }
        cursor.close();

        int numRows = (int)DatabaseUtils.queryNumEntries(database, MoviesTable.TABLE_NAME);
        callback.onMoviesDownloaded(movies, numRows);
    }

    private Movie cursorToMovie(Cursor cursor) {
        Movie movie = new Movie();
        movie.setTitle(cursor.getString(1));
        movie.setVote_average(cursor.getFloat(2));
        movie.setBackdrop_path(cursor.getString(3));
        movie.setRelease_date(cursor.getString(4));
        movie.setVote_count(cursor.getInt(5));
        movie.setOverview(cursor.getString(6));
        return movie;
    }

    public void saveMovies(List<Movie> movies, int page, int pageSize) {
        int position = (page - 1) * pageSize;
        for (Movie m : movies) {
            saveMovie(m, position);
            position++;
        }
    }

    public void saveMovie(Movie movie, int id) {
        ContentValues values = new ContentValues();
        values.put(MoviesTable.COLUMN_NAME_ID, id);
        values.put(MoviesTable.COLUMN_NAME_TITLE, movie.getTitle());
        values.put(MoviesTable.COLUMN_NAME_RATING, movie.getVote_average());
        values.put(MoviesTable.COLUMN_NAME_IMAGE_PATH, movie.getBackdrop_path());
        values.put(MoviesTable.COLUMN_NAME_RELEASE_DATE, movie.getRelease_date());
        values.put(MoviesTable.COLUMN_NAME_VOTE_COUNT, movie.getVote_count());
        values.put(MoviesTable.COLUMN_NAME_DESCRIPTION, movie.getOverview());

        database.insertWithOnConflict(MoviesTable.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }
}
