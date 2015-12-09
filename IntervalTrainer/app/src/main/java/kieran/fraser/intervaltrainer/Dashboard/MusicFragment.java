package kieran.fraser.intervaltrainer.Dashboard;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import kieran.fraser.intervaltrainer.DatabaseHelper.DBManager;
import kieran.fraser.intervaltrainer.DatabaseHelper.DatabaseHelper;
import kieran.fraser.intervaltrainer.R;
import kieran.fraser.intervaltrainer.Song;
import kieran.fraser.intervaltrainer.SongAdapter;

public class MusicFragment extends Fragment {

    private ArrayList<Song> musicList;
    private ListView musicView;
    private Button noSongs;
    private ArrayList<Song> selectedSongList;
    private AudioManager am;
    private OnFragmentInteractionListener mListener;
    private DBManager dbManager;


    public MusicFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        am = (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);
        selectedSongList = new ArrayList<Song>();
        View rootView = inflater.inflate(R.layout.fragment_music, container, false);
        musicView = (ListView) rootView.findViewById(R.id.lv_music_list);
        musicList = new ArrayList<>();
        getMusicList();

        dbManager = new DBManager(getActivity());
        try {
            dbManager.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        noSongs = (Button) rootView.findViewById(R.id.bt_no_songs);
        noSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedSongList = new ArrayList<Song>();
                for(Song song : musicList){
                    if(song.isSelected()){
                        selectedSongList.add(song);
                    }
                }

                deletePlaylist();
                savePlaylist();
                //playWorkoutMusic(selectedSongList);
            }
        });

        Collections.sort(musicList, new Comparator<Song>() {
            @Override
            public int compare(Song song, Song song2) {
                return song.getTitle().compareTo(song2.getTitle());
            }
        });

        SongAdapter songAdapter = new SongAdapter(getActivity(), musicList);
        musicView.setAdapter(songAdapter);

        musicView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                TextView tvSong = (TextView) view.findViewById(R.id.song_title);
                Log.d("Music: ", String.valueOf(tvSong.getText()));
            }
        });
        return rootView;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    public void getMusicList(){
        ContentResolver musicResolver = getActivity().getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;


        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        if(musicCursor!=null && musicCursor.moveToFirst()){
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            int data = (musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.DATA));
            /*MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mediaPlayer.setDataSource(getActivity(), Uri.parse(data));
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mediaPlayer.start();*/
            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                String thisData = musicCursor.getString(data);
                musicList.add(new Song(thisId, thisTitle, thisArtist, thisData));
            }
            while (musicCursor.moveToNext());
        }
    }

    private void playWorkoutMusic(ArrayList<Song> songs){
        Log.d("Length", String.valueOf(songs.size()));
        Song theSong = songs.get(1);
        String data = theSong.getData();
        Log.d("data ", data);
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(getActivity(), Uri.parse(data));
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.start();
    }

    private void savePlaylist(){
        for(Song song: selectedSongList){
            dbManager.insert(song.getTitle());
        }
    }

    private void deletePlaylist(){
        if(!dbManager.isEmpty()){
            dbManager.deleteAll();
        }
    }
}
