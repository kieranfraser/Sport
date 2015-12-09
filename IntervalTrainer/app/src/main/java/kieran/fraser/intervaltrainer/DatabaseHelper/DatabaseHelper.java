package kieran.fraser.intervaltrainer.DatabaseHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by kfraser on 31/10/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;

    public static final String DB_NAME = "WorkoutDB";
    public static final String TABLE_PLAYLIST = "playlist";
    public static final String SONG_ID = "_id";
    public static final String TITLE = "title";

    private static final String CREATE_TABLE = "create table " + TABLE_PLAYLIST +
            "("+ SONG_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            TITLE + " TEXT NOT NULL);";


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        Log.d("oncreate database: ", CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS "+ TABLE_PLAYLIST);
        onCreate(db);
        Log.d("onupgrade database: ", "");
    }
}
