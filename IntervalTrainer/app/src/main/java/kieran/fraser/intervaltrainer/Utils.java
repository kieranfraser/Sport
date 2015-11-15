package kieran.fraser.intervaltrainer;

import android.widget.TextView;

/**
 * Created by kfraser on 15/11/2015.
 */
public class Utils {

    public static int textViewToInt(TextView v){
        return Integer.valueOf(String.valueOf(v.getText()));
    }
}
