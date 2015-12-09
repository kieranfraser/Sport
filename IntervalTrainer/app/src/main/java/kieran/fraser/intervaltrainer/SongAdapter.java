package kieran.fraser.intervaltrainer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kfraser on 25/10/2015.
 */
public class SongAdapter extends BaseAdapter{


    private ArrayList<Song> songs;
    private LayoutInflater songInf;

    public SongAdapter(Context c, ArrayList<Song> theSongs){
        songs=theSongs;
        songInf= LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return songs.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    static class ViewHolder {
        protected TextView songView;
        TextView artistView;
        protected CheckBox checkBox;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //map to song layout
        LinearLayout songLay = (LinearLayout)songInf.inflate
                (R.layout.song, parent, false);
        //get title and artist views
        final ViewHolder viewHolder = new ViewHolder();
        viewHolder.songView = (TextView)songLay.findViewById(R.id.song_title);
        viewHolder.artistView = (TextView)songLay.findViewById(R.id.song_artist);
        viewHolder.checkBox = (CheckBox)songLay.findViewById(R.id.cb_song);
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                Song song = (Song) viewHolder.checkBox.getTag();
                song.setSelected(buttonView.isChecked());
            }
        });
        songLay.setTag(viewHolder);
        viewHolder.checkBox.setTag(songs.get(position));
        //get song using position
        Song currSong = songs.get(position);
        //get title and artist strings
        ViewHolder holder = (ViewHolder) songLay.getTag();
        holder.songView.setText(currSong.getTitle());
        holder.artistView.setText(currSong.getArtist());
        holder.checkBox.setChecked(songs.get(position).isSelected());
        //set position as tag
        //songLay.setTag(position);
        return songLay;
    }

}
