package kieran.fraser.intervaltrainer.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

/**
 * Created by kfraser on 31/10/2015.
 */
public class DBManager {

    private DatabaseHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public DBManager(Context context){
        this.context = context;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dbHelper.close();
    }

    public void insert(String title){
        ContentValues contentValue = new ContentValues();

        contentValue.put(DatabaseHelper.TITLE, title);
        database.insert(DatabaseHelper.TABLE_PLAYLIST, null, contentValue);
   }

    public Cursor fetch() {
        String[] columns = new String[] { DatabaseHelper.SONG_ID, DatabaseHelper.TITLE};
        Cursor cursor = database.query(DatabaseHelper.TABLE_PLAYLIST, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(int id, String title) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.TITLE, title);
        int i = database.update(DatabaseHelper.TABLE_PLAYLIST, contentValues, DatabaseHelper.SONG_ID + "=" + id,null);
        return i;
    }

    public void delete(long id) {
        database.delete(DatabaseHelper.TABLE_PLAYLIST, DatabaseHelper.SONG_ID + "=" + id, null);
    }

    public void deleteAll(){
        database.delete(DatabaseHelper.TABLE_PLAYLIST, null, null);
    }

    public boolean isEmpty(){
        String[] columns = new String[] { DatabaseHelper.SONG_ID, DatabaseHelper.TITLE};
        Cursor cursor = database.query(DatabaseHelper.TABLE_PLAYLIST, columns, null, null, null, null, null);
        if (cursor != null && cursor.getCount()>0) {
            return false;
        }
        else{
            return true;
        }
    }
}