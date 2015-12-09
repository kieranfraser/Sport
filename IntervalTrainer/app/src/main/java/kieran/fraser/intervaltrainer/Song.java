package kieran.fraser.intervaltrainer;

/**
 * Created by kfraser on 25/10/2015.
 */
public class Song {


    private long id;
    private String title;
    private String artist;
    private String data;
    private boolean selected;

    public Song(long songID, String songTitle, String songArtist, String data) {
        id = songID;
        title = songTitle;
        artist = songArtist;
        this.data = data;
        selected = false;
    }

    public long getID(){return id;}
    public String getTitle(){return title;}
    public String getArtist(){return artist;}
    public String getData(){return data;}

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
