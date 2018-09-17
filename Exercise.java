package tobemo.yourworkout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Exercise extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_card);
    }

    protected int ID;
    protected int CurrentWorkoutID;

    protected String name;

    protected int intSets;
    protected int intReps;
    protected int intTypeOfExerciseIsReps;

    protected long longBreakAfterExerciseInMillis = 0;
    protected long longDurationInMillis;
    protected long longBreakDurationInMillis;

    protected boolean booleanTypeOfExerciseIsReps;

    public Exercise(String name, int sets, int type, int reps, long duration, long breakDuration)    {
        this.name = name;
        intSets = sets;
        if(type == 1) {
            booleanTypeOfExerciseIsReps = true;
            intTypeOfExerciseIsReps = 1;
        }   else    {
            booleanTypeOfExerciseIsReps = false;
            intTypeOfExerciseIsReps = 0;
        }
        intReps = reps;
        longDurationInMillis = duration;
        longBreakDurationInMillis = breakDuration;

    }

    public Exercise(){}

    public final void setID(int ID)   {
        this.ID = ID;
    }

    public void setCurrentWorkoutID(int currentWorkoutID) {
        CurrentWorkoutID = currentWorkoutID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIntSets(int intSets) {
        this.intSets = intSets;
    }

    public void setIntReps(int intReps) {
        this.intReps = intReps;
    }

    public void setLongDurationInMillis(long longDurationInMillis) {
        this.longDurationInMillis = longDurationInMillis;
    }

    public void setLongBreakDurationInMillis(long longBreakDurationInMillis) {
        this.longBreakDurationInMillis = longBreakDurationInMillis;
    }

    public void setLongBreakAfterExerciseInMillis(long longBreakAfterExerciseInMillis) {
        this.longBreakAfterExerciseInMillis = longBreakAfterExerciseInMillis;
    }

    public void setIntTypeOfExerciseIsReps(int intTypeOfExerciseIsReps) {
        this.intTypeOfExerciseIsReps = intTypeOfExerciseIsReps;
    }

    public void setBooleanTypeOfExerciseIsReps(boolean booleanTypeOfExerciseIsReps) {
        this.booleanTypeOfExerciseIsReps = booleanTypeOfExerciseIsReps;
    }

    public int getID()  {
        return ID;
    }

    public int getCurrentWorkoutID() {
        return CurrentWorkoutID;
    }

    public String getName() {
        return name;
    }

    public int getIntSets() {
        return intSets;
    }

    public int getIntReps() {
        return intReps;
    }

    public long getLongDurationInMillis() {
        return longDurationInMillis;
    }

    public long getLongBreakDurationInMillis() {
        return longBreakDurationInMillis;
    }

    public boolean isBooleanTypeOfExerciseIsReps() {
        return booleanTypeOfExerciseIsReps;
    }

    public int getIntTypeOfExerciseIsReps() {
        return intTypeOfExerciseIsReps;
    }

}
