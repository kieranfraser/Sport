package kieran.fraser.intervaltrainer.DatabaseObjects;

/**
 * Created by kfraser on 09/12/2015.
 */
public class WorkoutSong {

    private String id;
    private String title;

    public WorkoutSong(String title){
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
