package kieran.fraser.intervaltrainer.DatabaseHelper;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;

import kieran.fraser.intervaltrainer.Song;

/**
 * Created by kfraser on 31/10/2015.
 */
public class DeletePlaylist implements Runnable {
    private DBManager dbManager;

    public DeletePlaylist(Context context){
        dbManager = new DBManager(context);
        try {
            dbManager.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        if(!dbManager.isEmpty()){
            dbManager.deleteAll();
        }
        dbManager.close();
    }
}
