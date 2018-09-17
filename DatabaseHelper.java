package tobemo.yourworkout;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.nfc.Tag;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper{

    private static final String TAG = "DatabaseHelper";

    private SQLiteDatabase sqLiteDatabase;

    public static final String DATABASE_NAME = "YourWorkout.db";

    public static final String EXERCISES_TABLE = "exercises_table";
    public static final String ID_COL = "ID";
    public static final String NAME_COL = "NAME";
    public static final String SETS_COL = "SETS";
    public static final String TYPE_COL = "TYPE";
    public static final String REPS_COL = "REPS";
    public static final String DURATION_COL = "DURATION";
    public static final String BREAK_COL = "BREAK";

    public static final String CURRENT_WORKOUT_TABLE = "current_workout_table";
    public static final String EXERCISE_COL = "EXERCISE";

    public static int intLastCurrentWorkoutID;

    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;

        String exercise_table = "CREATE TABLE " + EXERCISES_TABLE + "(" + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME_COL + " TEXT, " + SETS_COL + " INTEGER, " + TYPE_COL + " INTEGER, " + REPS_COL + " INTEGER, " + DURATION_COL + " INTEGER, " + BREAK_COL + " INTEGER" + ")";
        this.sqLiteDatabase.execSQL(exercise_table);

        String current_workout_table = "CREATE TABLE " + CURRENT_WORKOUT_TABLE + "(" + ID_COL  +" INTEGER PRIMARY KEY AUTOINCREMENT, " + EXERCISE_COL + " INTEGER" + ")";
        Log.d(TAG, "" + current_workout_table);
        this.sqLiteDatabase.execSQL(current_workout_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + EXERCISES_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CURRENT_WORKOUT_TABLE);
        onCreate(sqLiteDatabase);
    }

    public void insertExercise(Exercise exercise)    {
        sqLiteDatabase = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME_COL, exercise.getName());
        contentValues.put(SETS_COL, exercise.getIntSets());
        contentValues.put(TYPE_COL, exercise.getIntTypeOfExerciseIsReps());
        contentValues.put(REPS_COL, exercise.getIntReps());
        contentValues.put(DURATION_COL, exercise.getLongDurationInMillis());
        contentValues.put(BREAK_COL, exercise.getLongBreakDurationInMillis());
        sqLiteDatabase.insert(EXERCISES_TABLE, null, contentValues);
    }

    public void addExerciseToCurrentWorkout(Exercise exercise)   {
        sqLiteDatabase = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EXERCISE_COL, exercise.getID());
        sqLiteDatabase.insert(CURRENT_WORKOUT_TABLE, null, contentValues);

    }

    public Cursor getData(int id)   {
        List<Exercise> exercises = new ArrayList<>();
        sqLiteDatabase = getReadableDatabase();
        Cursor results = sqLiteDatabase.rawQuery("SELECT * FROM " + EXERCISES_TABLE + " WHERE id=" + id +"", null);
        return results;
    }

    public List<Integer> getCurrentWorkout()   {
        List<Integer> exerciseIDList = new ArrayList<>();
        sqLiteDatabase = getReadableDatabase();

        Cursor results = sqLiteDatabase.rawQuery("SELECT * FROM " + CURRENT_WORKOUT_TABLE, null);

        if (results.moveToFirst())  {
            do {
                int ID = results.getInt(results.getColumnIndex(EXERCISE_COL));
                exerciseIDList.add(ID);

            }   while (results.moveToNext());
        }

        results.close();
        return exerciseIDList;
    }

    //TODO add method to get certain exercises
    public Exercise getCertainExercise(int ID)    {

        Exercise exercise = new Exercise();

        sqLiteDatabase = getReadableDatabase();
        Cursor results = sqLiteDatabase.rawQuery("SELECT * FROM " + EXERCISES_TABLE + " WHERE " + ID_COL + " = " + ID, null);

        if (results.moveToFirst())  {
            do {
                exercise.setID(
                        results.getInt(results.getColumnIndex(ID_COL)));
                exercise.setName(
                        results.getString(results.getColumnIndex(NAME_COL)));
                exercise.setIntSets(
                        results.getInt(results.getColumnIndex(SETS_COL)));
                exercise.setIntTypeOfExerciseIsReps(
                        results.getInt(results.getColumnIndex(TYPE_COL)));
                exercise.setIntReps(
                        results.getInt(results.getColumnIndex(REPS_COL)));
                exercise.setLongDurationInMillis(
                        results.getLong(results.getColumnIndex(DURATION_COL)));
                exercise.setLongBreakDurationInMillis(
                        results.getLong(results.getColumnIndex(BREAK_COL)));

                if(exercise.getIntTypeOfExerciseIsReps() == 1)  {
                    exercise.setBooleanTypeOfExerciseIsReps(true);
                }

            }   while (results.moveToNext());
        }

        results.close();

        return exercise;
    }

    public List<Exercise> getAllExercises()   {
        List<Exercise> exerciseList = new ArrayList<>();
        sqLiteDatabase = getReadableDatabase();
        Cursor results = sqLiteDatabase.rawQuery("SELECT * FROM " + EXERCISES_TABLE, null);

        if (results.moveToFirst())  {
            do {
                Exercise exercise = new Exercise();
                exercise.setID(
                        results.getInt(results.getColumnIndex(ID_COL)));
                exercise.setName(
                        results.getString(results.getColumnIndex(NAME_COL)));
                exercise.setIntSets(
                        results.getInt(results.getColumnIndex(SETS_COL)));
                exercise.setIntTypeOfExerciseIsReps(
                        results.getInt(results.getColumnIndex(TYPE_COL)));
                exercise.setIntReps(
                        results.getInt(results.getColumnIndex(REPS_COL)));
                exercise.setLongDurationInMillis(
                        results.getLong(results.getColumnIndex(DURATION_COL)));
                exercise.setLongBreakDurationInMillis(
                        results.getLong(results.getColumnIndex(BREAK_COL)));

                if(exercise.getIntTypeOfExerciseIsReps() == 1)  {
                    exercise.setBooleanTypeOfExerciseIsReps(true);
                }

                exerciseList.add(exercise);

            }   while (results.moveToNext());
        }

        results.close();
        return exerciseList;
    }

    public List<Exercise> getAllExercisesInCurrentWorkout()    {
        List<Exercise> exerciseList = new ArrayList<>();
        sqLiteDatabase = getReadableDatabase();
        Cursor results = sqLiteDatabase.rawQuery("SELECT * FROM " + CURRENT_WORKOUT_TABLE, null);


        if (results.moveToFirst()) {
            do {
                int ID = results.getInt(results.getColumnIndex(EXERCISE_COL));
                Cursor exercises = sqLiteDatabase.rawQuery("SELECT * FROM " + EXERCISES_TABLE + " WHERE " + ID_COL + " ='" + ID + "'", null);
                if (exercises.moveToFirst()) {
                    do {
                        Exercise exercise = new Exercise();
                        exercise.setID(
                                exercises.getInt(exercises.getColumnIndex(ID_COL)));
                        exercise.setName(
                                exercises.getString(exercises.getColumnIndex(NAME_COL)));
                        exercise.setIntSets(
                                exercises.getInt(exercises.getColumnIndex(SETS_COL)));
                        exercise.setIntTypeOfExerciseIsReps(
                                exercises.getInt(exercises.getColumnIndex(TYPE_COL)));
                        exercise.setIntReps(
                                exercises.getInt(exercises.getColumnIndex(REPS_COL)));
                        exercise.setLongDurationInMillis(
                                exercises.getLong(exercises.getColumnIndex(DURATION_COL)));
                        exercise.setLongBreakDurationInMillis(
                                exercises.getLong(exercises.getColumnIndex(BREAK_COL)));

                        if (exercise.getIntTypeOfExerciseIsReps() == 1) {
                            exercise.setBooleanTypeOfExerciseIsReps(true);
                        }

                        exerciseList.add(exercise);

                    } while (exercises.moveToNext());
                }
            } while (results.moveToNext());
        }

        results.close();
        return exerciseList;
    }

    public List<Integer> getIntegers() {
        List<Integer> currentWorkout = new ArrayList<>();
        sqLiteDatabase = getReadableDatabase();
        Cursor results = sqLiteDatabase.rawQuery("SELECT * FROM " + CURRENT_WORKOUT_TABLE, null);

        if (results.moveToFirst()) {
            do {
                currentWorkout.add(results.getInt(results.getColumnIndex(ID_COL)));
            } while (results.moveToNext());

        }
        return currentWorkout;
    }



    public boolean isUniqueInCurrentWorkoutTable(Exercise exercise) {
        sqLiteDatabase = getReadableDatabase();
        Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + CURRENT_WORKOUT_TABLE + " WHERE " + ID_COL + " = " + exercise.getID(), null);
            if(result.getCount() <= 0){
                result.close();
                return true;
            }
        result.close();
        return false;

    }

    public void deleteExercise(int ID)  {
        sqLiteDatabase = getReadableDatabase();
        String query = "DELETE FROM " + EXERCISES_TABLE + " WHERE " + ID_COL + " = '" + ID + "'";
        sqLiteDatabase.execSQL(query);
    }

    public void removeExercise(int ID)  {
        sqLiteDatabase = getReadableDatabase();

        String query = "DELETE FROM " + CURRENT_WORKOUT_TABLE + " WHERE " + ID_COL + " in (SELECT " + ID_COL + " FROM " + CURRENT_WORKOUT_TABLE + " LIMIT 1 OFFSET " + ID + ")";
        sqLiteDatabase.execSQL(query);
    }

    public void deleteExerciseInCurrentWorkout(int ID)  {
        sqLiteDatabase = getReadableDatabase();
        String query = "DELETE FROM " + CURRENT_WORKOUT_TABLE + " WHERE " + EXERCISE_COL + " = '" + ID + "'";
        sqLiteDatabase.execSQL(query);
    }
}


