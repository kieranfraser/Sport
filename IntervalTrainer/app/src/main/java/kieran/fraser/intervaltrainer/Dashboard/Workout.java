package kieran.fraser.intervaltrainer.Dashboard;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import kieran.fraser.intervaltrainer.R;
import kieran.fraser.intervaltrainer.Session;
import kieran.fraser.intervaltrainer.State;
import kieran.fraser.intervaltrainer.Utils;

/**
 * Created by kfraser on 15/11/2015.
 */
public class Workout implements Runnable {

    Session session;
    TextView tvWork;
    TextView tvRest;
    TextView tvRounds;
    TextView tvBreak;
    Context context;

    public Workout(Session session, TextView work, TextView rest,
                   TextView rounds, TextView breaks) {
        this.session = session;
        this.tvWork = work;
        this.tvRest = rest;
        this.tvRounds = rounds;
        this.tvBreak = breaks;
    }

    @Override
    public void run() {
        // first save session to database (will have
        // to compare with the end of the session to
        // determine the performance

        while(session.getRoundNum() > 0){
            switch(session.getCurrentState()){
                case BEGIN:
                    session.setCurrentState(State.WORKING);
                    BeginCountDown();
                    break;
                case WORK:
                    session.setCurrentState(State.WORKING);
                    countDown(tvWork);
                    break;
                case REST:
                    session.setCurrentState(State.WORKING);
                    countDown(tvRest);
                    break;
                case BREAK:
                    session.setCurrentState(State.WORKING);
                    countDown(tvBreak);
                    break;
                case PAUSE:
                    break;
                case END:
                    break;
                case WORKING:
                    break;
                default:
                    break;
            }
        }
    }

    private void BeginCountDown(){
        new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                long clockTime = millisUntilFinished / 1000;
                Log.d("Kieran", "time: "+clockTime);
            }
            public void onFinish() {
                session.setCurrentState(State.WORK);
            }
        }.start();
    }

    private void countDown(final TextView v){
        CountDownTimer count = new CountDownTimer(Utils.textViewToInt(v)*1000, 1000) {

            public void onTick(long millisUntilFinished) {
                long clockTime = millisUntilFinished / 1000;
                v.setText(String.valueOf(clockTime));
            }
            public void onFinish() {
                switch (v.getId()){
                    case R.id.tv_work_value:
                        tvRounds.setText(String.valueOf(Utils.textViewToInt(tvRounds) - 1));
                        if(Utils.textViewToInt(tvRounds)>= 0){
                            session.setCurrentState(State.REST);
                        }
                        else{
                            session.setCurrentState(State.END);
                        }
                        break;
                    case R.id.tv_rest_value:
                        session.setCurrentState(State.WORK);
                        break;
                    case R.id.tv_break_value:
                        session.setCurrentState(State.WORK);
                        break;
                }
            }
        }.start();
    }


}
