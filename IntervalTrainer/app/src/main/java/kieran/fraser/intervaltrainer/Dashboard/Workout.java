package kieran.fraser.intervaltrainer.Dashboard;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import kieran.fraser.intervaltrainer.DatabaseHelper.DBManager;
import kieran.fraser.intervaltrainer.DatabaseHelper.DatabaseHelper;
import kieran.fraser.intervaltrainer.R;
import kieran.fraser.intervaltrainer.Session;
import kieran.fraser.intervaltrainer.State;
import kieran.fraser.intervaltrainer.Utils;

/**
 * Created by kfraser on 15/11/2015.
 */
public class Workout {

    private Session session;
    private TextView tvWork;
    private TextView tvRest;
    private TextView tvRounds;
    private TextView tvBreak;
    private TextView tvSets;

    private int work;
    private int rest;
    private int rounds;
    private int breakTime;
    private int sets;

    private CountDownTimer count;
    private boolean isPaused;
    private boolean isReset;

    private WorkoutPlayback workoutPlayback;

    public void setPaused(boolean isPaused) {
        this.isPaused = isPaused;
    }

    public void setReset(boolean isReset) {
        this.isReset = isReset;
    }

    public Workout(Session session, TextView work, TextView rest,
                   TextView rounds, TextView breaks, TextView sets, WorkoutPlayback workoutPlayback) {
        this.session = session;
        this.tvWork = work;
        this.tvRest = rest;
        this.tvRounds = rounds;
        this.tvBreak = breaks;
        this.tvSets = sets;

        initialize();

    }

    private void initialize(){

        this.work = Utils.textViewToInt(tvWork);
        this.rest = Utils.textViewToInt(tvRest);
        this.rounds = Utils.textViewToInt(tvRounds);
        this.breakTime = Utils.textViewToInt(tvBreak);
        this.sets = Utils.textViewToInt(tvSets);
        this.workoutPlayback = workoutPlayback;

        isPaused = false;
        session.setPrevState(State.WORK);
    }

    public void startWorkout() {
        // first save session to database (will have
        // to compare with the end of the session to
        // determine the performance

        session.setCurrentState(State.WORK);
        count = new CountDownTimer((Utils.textViewToInt(tvWork)+1)*1000, 1000) {
            public void onTick(long millisUntilFinished) {
                if(isPaused){
                    cancel();
                }
                else if(isReset){
                    reset();
                    cancel();
                }
                else {
                    long clockTime = millisUntilFinished / 1000;
                    if (Utils.textViewToInt(tvWork) > 1) {
                        tvWork.setText(String.valueOf(Utils.textViewToInt(tvWork) - 1));
                    } else {
                        tvWork.setText(String.valueOf(work));
                    }
                    Log.d("Kieran", "work time: " + clockTime);
                }
            }
            public void onFinish() {
                if(Utils.textViewToInt((tvRounds))>0){
                    if(Utils.textViewToInt(tvSets) == 0){
                        startBreak();
                    }
                    else{
                        decrementSet();
                        startRest();
                    }
                }
                else{
                    finish();
                }
            }
        }.start();

    }

    public void reset(){
        tvWork.setText(String.valueOf(work));
        tvRest.setText(String.valueOf(rest));
        tvRounds.setText(String.valueOf(rounds));
        tvBreak.setText(String.valueOf(breakTime));
        tvSets.setText(String.valueOf(sets));
    }

    public void startRest(){
        session.setCurrentState(State.REST);
        count = new CountDownTimer((Utils.textViewToInt(tvRest)+1)*1000, 1000) {
            public void onTick(long millisUntilFinished) {

                if(isPaused){
                    cancel();
                }
                else if(isReset){
                    reset();
                    cancel();
                }
                else {
                    long clockTime = millisUntilFinished / 1000;
                    if (Utils.textViewToInt(tvRest) > 1) {
                        tvRest.setText(String.valueOf(Utils.textViewToInt(tvRest) - 1));
                    } else {
                        tvRest.setText(String.valueOf(rest));
                    }
                    Log.d("Kieran", "rest time: " + clockTime);
                }
            }
            public void onFinish() {
                session.setPrevState(State.PAUSE);
                startWorkout();
            }
        }.start();
    }

    private void startBreak(){
        session.setCurrentState(State.BREAK);
        decrementRound();
        count = new CountDownTimer((Utils.textViewToInt(tvBreak)+1)*1000, 1000) {
            public void onTick(long millisUntilFinished) {

                if(isPaused){
                    cancel();
                }
                else if(isReset){
                    reset();
                    cancel();
                }
                else {
                    long clockTime = millisUntilFinished / 1000;
                    if (Utils.textViewToInt(tvBreak) > 1) {
                        tvBreak.setText(String.valueOf(Utils.textViewToInt(tvBreak) - 1));
                    } else {
                        tvBreak.setText(String.valueOf(breakTime));
                    }
                    Log.d("Kieran", "break time: " + clockTime);
                }
            }
            public void onFinish() {
                session.setPrevState(State.BREAK);
                startWorkout();
            }
        }.start();
    }

    private void finish(){
        Log.d("Finished", "Finished");
    }

    private void decrementSet(){
        tvSets.setText(String.valueOf(Utils.textViewToInt(tvSets)-1));
    }

    private void decrementRound(){
        tvRounds.setText(String.valueOf(Utils.textViewToInt(tvRounds)-1));
    }

    public int getWork() {
        return work;
    }

    public int getRest() {
        return rest;
    }

    public int getRounds() {
        return rounds;
    }

    public int getBreakTime() {
        return breakTime;
    }

    public int getSets() {
        return sets;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
