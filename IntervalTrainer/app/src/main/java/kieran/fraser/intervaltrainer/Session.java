package kieran.fraser.intervaltrainer;

/**
 * Created by kfraser on 15/11/2015.
 */
public class Session {

    private int workTime;
    private int restTime;
    private int roundNum;
    private int breakTime;
    private int setValue;

    private State currentState;
    private State prevState;

    public int getSetValue() {return setValue; }

    public void setSetValue(int setValue) { this.setValue = setValue; }

    public int getWorkTime() {
        return workTime;
    }

    public void setWorkTime(int workTime) {
        this.workTime = workTime;
    }

    public int getRestTime() {
        return restTime;
    }

    public void setRestTime(int restTime) {
        this.restTime = restTime;
    }

    public int getRoundNum() {
        return roundNum;
    }

    public void setRoundNum(int roundNum) {
        this.roundNum = roundNum;
    }

    public int getBreakTime() {
        return breakTime;
    }

    public void setBreakTime(int breakTime) {
        this.breakTime = breakTime;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public State getPrevState() {
        return prevState;
    }

    public void setPrevState(State prevState) {
        this.prevState = prevState;
    }
}
