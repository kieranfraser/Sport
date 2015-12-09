package kieran.fraser.intervaltrainer.DatabaseHelper;

import android.app.Notification;
import android.content.Context;
import android.database.Cursor;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;

import kieran.fraser.intervaltrainer.Song;

/**
 * Created by kfraser on 31/10/2015.
 */
public class SavePlaylist implements Runnable {
    private Context context;
    private DBManager dbManager;
    ArrayList<Song> playlist;

    public SavePlaylist(Context context, ArrayList<Song> playlist){
        this.context = context;
        this.playlist = playlist;
        dbManager = new DBManager(context);
        try {
            dbManager.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        for(Song song: playlist){
            dbManager.insert(song.getTitle());
        }
        dbManager.close();
//        Cursor cursor = dbManager.fetch();
//        Log.d("Cursor: ", cursor.toString());
//        Log.d("Cursor: ", String.valueOf(cursor.getCount()));
//        Log.d("Cursor: ", cursor.getString(cursor.getColumnIndex(DatabaseHelper.NOTIFICATION_SENDER)));
    }
}
