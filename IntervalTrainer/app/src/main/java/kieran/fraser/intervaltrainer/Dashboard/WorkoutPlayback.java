package kieran.fraser.intervaltrainer.Dashboard;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.util.Log;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import kieran.fraser.intervaltrainer.DatabaseHelper.DBManager;
import kieran.fraser.intervaltrainer.DatabaseHelper.DatabaseHelper;
import kieran.fraser.intervaltrainer.R;

/**
 * Created by kfraser on 09/12/2015.
 */
public class WorkoutPlayback implements MediaPlayer.OnCompletionListener{

    Context context;
    private DBManager dbManager;
    private ArrayList<String> playList;
    ArrayList<Integer> restPlayList;
    ArrayList<Integer> breakPlaylist;
    private MediaPlayer workMediaPlayer;
    private MediaPlayer breakMediaPlayer;
    private int currentSong;
    private float currentVolume;
    private float INITIAL_VOLUME = 1.0f;
    private Random r;

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
        currentVolume = INITIAL_VOLUME;
        workMediaPlayer = new MediaPlayer();
        workMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        breakMediaPlayer = new MediaPlayer();
        breakMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        getBreakPlaylist();
        currentSong = 0;
    }

    private void getBreakPlaylist(){
        restPlayList = new ArrayList<>();
        breakPlaylist = new ArrayList<>();
        Field[] fields = R.raw.class.getFields();
        for(int i=0; i< fields.length; i++ ){
            try{
                String name = fields[i].getName();
                int resourceId = fields[i].getInt(fields[i]);
                if(name.contains("loop")){
                    restPlayList.add(resourceId);
                }
                else{
                    breakPlaylist.add(resourceId);
                }
            }catch (Exception e){}
        }
    }

    public void startBreak(){
        r = new Random();
        String path = "android.resource://"+context.getPackageName()+"/raw/"+breakPlaylist.get(r.nextInt(breakPlaylist.size()));
        breakMediaPlayer.reset();
        try {
            breakMediaPlayer.setDataSource(context, Uri.parse(path));
            breakMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        breakMediaPlayer.setLooping(true);
        breakMediaPlayer.start();
    }

    public void startRest(){
         r = new Random();
        String path = "android.resource://"+context.getPackageName()+"/raw/"+restPlayList.get(r.nextInt(restPlayList.size()));
        breakMediaPlayer.reset();
        try {
            breakMediaPlayer.setDataSource(context, Uri.parse(path));
            breakMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        breakMediaPlayer.setLooping(true);
        breakMediaPlayer.start();
    }

    public void stopRest(){
        breakMediaPlayer.stop();
    }

    public void stopBreak(){
        breakMediaPlayer.stop();
    }

    public void pauseBreak(){
        breakMediaPlayer.pause();
    }

    public void resumeBreak(){
        breakMediaPlayer.start();
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

    public void start(){
        String data = playList.get(currentSong);
        workMediaPlayer.reset();
        try {
            workMediaPlayer.setDataSource(context, Uri.parse(data));
            workMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        workMediaPlayer.start();
    }

    public void pausePlaylist(){
        workMediaPlayer.pause();
    }

    public void resumePlaylist(){
        workMediaPlayer.start();
    }

    public void turnDown(){
        TurnDown turnDown = new TurnDown();
        turnDown.execute();
    }

    public void turnUp(){
        TurnUp turnUp = new TurnUp();
        turnUp.execute();
    }

    @Override
    public void onCompletion(MediaPlayer workMediaPlayer) {
        Log.d("finished", "song");
        if(currentSong < playList.size()){
            currentSong++;
            start();
        }
        else{
            currentSong=0;
            start();
        }
    }

    private class TurnDown extends AsyncTask<String, String, String> {
        int maxVolume = 50;
        @Override
        protected String doInBackground(String... strings) {
            while(currentVolume>0.0f){
                currentVolume -= 0.005;
                //audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, 0);
                workMediaPlayer.setVolume(currentVolume, currentVolume);
            }
            workMediaPlayer.pause();
            return null;
        }
    }

    private class TurnUp extends AsyncTask<String, String, String> {
        int maxVolume = 50;
        @Override
        protected String doInBackground(String... strings) {
            workMediaPlayer.start();
            while(currentVolume<1.0f){
                currentVolume += 0.005;
                //audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, 0);
                workMediaPlayer.setVolume(currentVolume, currentVolume);
            }
            return null;
        }
    }

}
