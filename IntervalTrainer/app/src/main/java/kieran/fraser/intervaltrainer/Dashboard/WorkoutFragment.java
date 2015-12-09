package kieran.fraser.intervaltrainer.Dashboard;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import kieran.fraser.intervaltrainer.R;
import kieran.fraser.intervaltrainer.Session;
import kieran.fraser.intervaltrainer.State;
import kieran.fraser.intervaltrainer.Utils;

public class WorkoutFragment extends Fragment implements View.OnClickListener{

    private TextView workValue;
    private TextView restValue;
    private TextView roundValue;
    private TextView breakValue;
    private TextView setValue;

    private RadioGroup workUnit;
    private RadioGroup restUnit;
    private RadioGroup breakUnit;

    private ImageView workMinus;
    private ImageView workPlus;
    private ImageView restMinus;
    private ImageView restPlus;
    private ImageView roundMinus;
    private ImageView roundPlus;
    private ImageView breakMinus;
    private ImageView breakPlus;
    private ImageView setPlus;
    private ImageView setMinus;

    private ImageView go;
    private ImageView rest;
    private ImageView reset;

    private final String SIXTY = "60";
    private final String FIFTEEN = "15";
    private final String THREE = "3";
    private final String TWO = "2";
    private final String TEN = "10";

    private Session session;
    private Workout workOut;
    private WorkoutPlayback workoutPlayback;
    private Handler handler;

    private boolean isPaused;

    private OnFragmentInteractionListener mListener;

    public WorkoutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_workout, container, false);
        initialise(rootView);
        setDefaultValues();

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
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

    @Override
    public void onClick(View view) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    public void initialise(View root){

        workValue = (TextView) root.findViewById(R.id.tv_work_value);
        restValue = (TextView) root.findViewById(R.id.tv_rest_value);
        roundValue = (TextView) root.findViewById(R.id.tv_round_value);
        breakValue = (TextView) root.findViewById(R.id.tv_break_value);
        setValue = (TextView) root.findViewById(R.id.tv_set_value);

        workUnit = (RadioGroup) root.findViewById(R.id.rg_work_unit);
        restUnit = (RadioGroup) root.findViewById(R.id.rg_rest_unit);
        breakUnit = (RadioGroup) root.findViewById(R.id.rg_break_unit);

        workMinus = (ImageView) root.findViewById(R.id.ib_work_down);
        workMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decrement(view);
            }
        });
        workPlus = (ImageView) root.findViewById(R.id.ib_work_up);
        workPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increment(view);
            }
        });
        restMinus = (ImageView) root.findViewById(R.id.ib_rest_down);
        restMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decrement(view);
            }
        });
        restPlus = (ImageView) root.findViewById(R.id.ib_rest_up);
        restPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increment(view);
            }
        });
        roundMinus = (ImageView) root.findViewById(R.id.ib_round_down);
        roundMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decrement(view);
            }
        });
        roundPlus = (ImageView) root.findViewById(R.id.ib_round_up);
        roundPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increment(view);
            }
        });
        breakMinus = (ImageView) root.findViewById(R.id.ib_break_down);
        breakMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decrement(view);
            }
        });
        breakPlus = (ImageView) root.findViewById(R.id.ib_break_up);
        breakPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increment(view);
            }
        });
        setPlus = (ImageView) root.findViewById(R.id.ib_set_up);
        setPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increment(view);
            }
        });
        setMinus = (ImageView) root.findViewById(R.id.ib_set_down);
        setMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decrement(view);
            }
        });

        go = (ImageView) root.findViewById(R.id.ib_start);
        rest = (ImageView) root.findViewById(R.id.ib_pause);
        reset = (ImageView) root.findViewById(R.id.ib_reset);

        isPaused = false;

        go.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Animation animation1 = AnimationUtils.loadAnimation(getActivity(), R.anim.blink);
                go.startAnimation(animation1);
                go();
            }
        });

        rest.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Animation animation1 = AnimationUtils.loadAnimation(getActivity(), R.anim.blink);
                rest.startAnimation(animation1);
                rest();
            }
        });

        reset.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Animation animation1 = AnimationUtils.loadAnimation(getActivity(), R.anim.blink);
                reset.startAnimation(animation1);
                reset();
            }
        });
        setSessionValues();
        workoutPlayback = new WorkoutPlayback(getActivity());
        workOut = new Workout(session, workValue, restValue, roundValue, breakValue, setValue, workoutPlayback);
    }

    public void setDefaultValues(){
        workUnit.check(R.id.rb_work_sec);
        restUnit.check(R.id.rb_rest_sec);
        breakUnit.check(R.id.rb_break_min);

        workValue.setText(SIXTY);
        restValue.setText(FIFTEEN);
        roundValue.setText(THREE);
        breakValue.setText(TWO);
        setValue.setText(TEN);
    }

    public void go(){
        Toast.makeText(getActivity(), "Go", Toast.LENGTH_LONG).show();
        setSessionValues();
        disableWorkoutButtons();
        workOut = new Workout(session, workValue, restValue, roundValue, breakValue, setValue, workoutPlayback);
        workOut.startWorkout();
        workoutPlayback.startPlaylist();
    }

    private void disableWorkoutButtons(){
        go.setEnabled(false);
        reset.setEnabled(false);
        rest.setEnabled(true);
        workPlus.setEnabled(false);
        workMinus.setEnabled(false);
        restPlus.setEnabled(false);
        restMinus.setEnabled(false);
        setMinus.setEnabled(false);
        setPlus.setEnabled(false);
        breakPlus.setEnabled(false);
        breakMinus.setEnabled(false);
        roundPlus.setEnabled(false);
        roundMinus.setEnabled(false);

    }

    private void enableWorkoutButtons(){
        go.setEnabled(true);
        rest.setEnabled(false);
        workPlus.setEnabled(true);
        workMinus.setEnabled(true);
        restPlus.setEnabled(true);
        restMinus.setEnabled(true);
        setMinus.setEnabled(true);
        setPlus.setEnabled(true);
        breakPlus.setEnabled(true);
        breakMinus.setEnabled(true);
        roundPlus.setEnabled(true);
        roundMinus.setEnabled(true);
    }

    public void rest(){
        Toast.makeText(getActivity(), "Rest", Toast.LENGTH_LONG).show();
        reset.setEnabled(true);
        Session session = workOut.getSession();
        if(isPaused){
            session.setCurrentState(session.getPrevState());
            workOut.setSession(session);
            isPaused = false;
            workOut.setPaused(false);
            switch(session.getCurrentState()){
                case WORK:
                    workOut.startWorkout();
                    break;
                case REST:
                    workOut.startRest();
                    break;
            }
        }
        else {
            isPaused = true;
            session.setPrevState(session.getCurrentState());
            session.setCurrentState(State.PAUSE);
            workOut.setSession(session);
            workOut.setPaused(true);
        }
    }

    public void reset(){
        Toast.makeText(getActivity(), "Reset", Toast.LENGTH_LONG).show();
        enableWorkoutButtons();
        isPaused = false;
        if(!workOut.getSession().getCurrentState().equals(State.BEGIN)){
            workValue.setText(String.valueOf(workOut.getWork()));
            restValue.setText(String.valueOf(workOut.getRest()));
            roundValue.setText(String.valueOf(workOut.getRounds()));
            breakValue.setText(String.valueOf(workOut.getBreakTime()));
            setValue.setText(String.valueOf(workOut.getSets()));

        }
        else{
            setDefaultValues();
        }
    }

    public void increment(View v){
        switch(v.getId()){
            case R.id.ib_work_up:
                incrementValue(workValue, true);
                break;
            case R.id.ib_rest_up:
                incrementValue(restValue, true);
                break;
            case R.id.ib_round_up:
                incrementValue(roundValue, true);
                break;
            case R.id.ib_break_up:
                incrementValue(breakValue, true);
                break;
            case R.id.ib_set_up:
                incrementValue(setValue, true);
            default:
                break;
        }
    }
    public void decrement(View v){
        switch(v.getId()){
            case R.id.ib_work_down:
                incrementValue(workValue, false);
                break;
            case R.id.ib_rest_down:
                incrementValue(restValue, false);
                break;
            case R.id.ib_round_down:
                incrementValue(roundValue, false);
                break;
            case R.id.ib_break_down:
                incrementValue(breakValue, false);
                break;
            case R.id.ib_set_down:
                incrementValue(setValue, false);
            default:
                break;
        }
    }
    public String incrementValue(TextView v, boolean increment){
        String result = null;
        int prev = Utils.textViewToInt(v);
        if(increment){
            if(prev<60){
                v.setText(String.valueOf(prev+1));
            }
            else{
                v.setText("0");
            }
        }
        else{
            if(prev>0){
                v.setText(String.valueOf(prev-1));
            }
            else{
                v.setText("60");
            }
        }
        return result;
    }

    private void setSessionValues(){
        session = new Session();
        session.setBreakTime(Utils.textViewToInt(breakValue));
        session.setRestTime(Utils.textViewToInt(restValue));
        session.setRoundNum(Utils.textViewToInt(roundValue));
        session.setWorkTime(Utils.textViewToInt(workValue));
        session.setSetValue(Utils.textViewToInt(setValue));
        session.setCurrentState(State.BEGIN);
        session.setPrevState(State.WORK);
    }


}
