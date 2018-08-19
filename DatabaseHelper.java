package tobemo.yourworkout;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper{

    private SQLiteDatabase sqLiteDatabase;

    public static final String DATABASE_NAME = "YourWorkout.db";
    public static final String TABLE_NAME = "exercises_table";
    public static final String COL1 = "ID";
    public static final String COL2 = "NAME";
    public static final String COL3 = "SETS";
    public static final String COL4 = "TYPE";
    public static final String COL5 = "REPS";
    public static final String COL6 = "DURATION";
    public static final String COL7 = "BREAK";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;
        String sql = "CREATE TABLE " + TABLE_NAME + "(" + COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL2 + " TEXT, " + COL3 + " INTEGER, " + COL4 + " INTEGER, " + COL5 + " INTEGER, " + COL6 + " INTEGER, " + COL7 + " INTEGER" + ")";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void insertExercise(Exercise exercise)    {
        sqLiteDatabase = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, exercise.getName());
        contentValues.put(COL3, exercise.getIntSets());
        contentValues.put(COL4, exercise.getIntTypeOfExerciseIsReps());
        contentValues.put(COL5, exercise.getIntReps());
        contentValues.put(COL6, exercise.getLongDurationInMillis());
        contentValues.put(COL7, exercise.getLongBreakDurationInMillis());
        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
    }

    protected void fillTable()    {
        Exercise exercise = new Exercise("1",3,1,3,0,60000);
        insertExercise(exercise);
        Exercise exercise2 = new Exercise("2",3,1,3,0,60000);
        insertExercise(exercise2);

    }

    public Cursor getData(int id)   {
        List<Exercise> exercises = new ArrayList<>();
        sqLiteDatabase = getReadableDatabase();
        Cursor results = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE id=" + id +"", null);
        return results;
    }

    public List<Exercise> getAllData()   {
        List<Exercise> exerciseList = new ArrayList<>();
        sqLiteDatabase = getReadableDatabase();
        Cursor results = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME , null);

        if (results.moveToFirst())  {
            do {
                Exercise exercise = new Exercise();
                exercise.setName(
                        results.getString(results.getColumnIndex(COL2)));
                exercise.setIntSets(
                        results.getInt(results.getColumnIndex(COL3)));
                exercise.setIntTypeOfExerciseIsReps(
                        results.getInt(results.getColumnIndex(COL4)));
                exercise.setIntReps(
                        results.getInt(results.getColumnIndex(COL5)));
                exercise.setLongDurationInMillis(
                        results.getLong(results.getColumnIndex(COL6)));
                exercise.setLongBreakDurationInMillis(
                        results.getLong(results.getColumnIndex(COL7)));
                exerciseList.add(exercise);

            }   while (results.moveToNext());
        }

        results.close();
        return exerciseList;
    }
}


