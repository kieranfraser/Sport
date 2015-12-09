package kieran.fraser.intervaltrainer.Dashboard;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import kieran.fraser.intervaltrainer.DatabaseHelper.DBManager;
import kieran.fraser.intervaltrainer.DatabaseHelper.DatabaseHelper;

/**
 * Created by kfraser on 09/12/2015.
 */
public class WorkoutPlayback {

    Context context;
    private DBManager dbManager;
    private ArrayList<String> playList;
    private MediaPlayer mediaPlayer;
    private int currentSong;

    public WorkoutPlayback(Context context){
        this.context = context;
        init();
    }

    private void init(){
        dbManager = new DBManager(context);
        try {
            dbManager.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        getPlaylist();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        currentSong = 0;
    }

    private void  getPlaylist(){

        Cursor cursor = dbManager.fetch();
        ArrayList<String> selectedList = new ArrayList<>();
        playList = new ArrayList<>();
        while (cursor.isAfterLast() == false) {
            String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TITLE));
            selectedList.add(name);
            cursor.moveToNext();
        }

        ContentResolver musicResolver = context.getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        if(musicCursor!=null && musicCursor.moveToFirst()){
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int data = (musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.DATA));
            do {
                String thisTitle = musicCursor.getString(titleColumn);
                if(selectedList.contains(thisTitle)){
                    playList.add(musicCursor.getString(data));
                }
            }
            while (musicCursor.moveToNext());
        }
    }

    public void startPlaylist(){
        String data = playList.get(currentSong);
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(context, Uri.parse(data));
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
    }
}
