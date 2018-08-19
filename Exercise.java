package tobemo.yourworkout;



public class Exercise {

    protected String name;

    protected int intSets;
    protected int intReps;

    protected long longDurationInMillis;
    protected long longBreakDurationInMillis;

    protected boolean booleanTypeOfExerciseIsReps;
    protected int intTypeOfExerciseIsReps;
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

    public void setIntTypeOfExerciseIsReps(int intTypeOfExerciseIsReps) {
        this.intTypeOfExerciseIsReps = intTypeOfExerciseIsReps;
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
