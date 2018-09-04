package tobemo.yourworkout;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class Exercises extends AppCompatActivity {

    private static final String TAG = "Exercises";

    private FloatingActionButton buttonCreateExercise;


    private EditText editRestInput;
    private Button eButtonRestSet;

    private long eRestTimeInMillis;

    //reps
    private EditText editTextHoursInput;
    private EditText editTextMinutesInput;
    private EditText editTextSecondsInput;

    private TextView textViewCountDown;

    private Button buttonStartPause;
    private Button buttonReset;
    private Button buttonTimeSet;

    private CountDownTimer countDownTimer;

    private boolean booleanTimerRunning;

    private long longExerciseTimeInMillis;
    private long longTimeLeftInMillis;
    private long longEndTime;

    private String millisLeft = "millisLeft";
    private String timerRunning = "timerRunning";
    private String endTime = "endTime";
    private String startTimeInMillis = "startTimeInMillis";

    private TextView mTextMessage;
    private TextView exercises;

    private List<Exercise> listExercises;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {


        //---------------------------------------BOTTOM NAV-------------------------------------------
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_current_workout:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_workouts:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_exercises:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);

        Log.d(TAG,"onCreate");

        buttonCreateExercise = findViewById(R.id.bt_create_exercise);
        buttonCreateExercise.setImageResource(R.drawable.ic_add_white_24dp);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        listExercises = getExercises();

        if(!listExercises.isEmpty())    {
            initRecyclerView();
        }

        buttonCreateExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createExercise = new Intent(Exercises.this, CreateExercise.class);
                startActivity(createExercise);

            }
        });

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.navigation_current_workout:
                        Intent currentWorkout = new Intent(Exercises.this, CurrentWorkout.class);
                        startActivity(currentWorkout);
                        break;

                    case R.id.navigation_workouts:
                        //TODO: create intent once activity exists
                        break;

                    case R.id.navigation_exercises:
                        Intent exercises = new Intent(Exercises.this,Exercises.class);
                        startActivity(exercises);
                        break;
                }
                return false;
            }
        });

    }

    private List<Exercise> getExercises() {
        Log.d(TAG,"getExercises");
        DatabaseHelper databaseHelper = new DatabaseHelper(Exercises.this);
        return databaseHelper.getAllExercises();
    }

    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.rv_exercises);
        ExerciseAdapter adapter = new ExerciseAdapter(this, listExercises );
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}
