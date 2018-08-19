package tobemo.yourworkout;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class Exercises extends AppCompatActivity {

    private static final String TAG = "Exercises";



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

        exercises = findViewById(R.id.tv_exercises);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        DatabaseHelper databaseHelper = new DatabaseHelper(Exercises.this);
        List<Exercise> exerciseList = databaseHelper.getAllData();

        for(int i = 0; i < exerciseList.size(); i++)    {
            exercises.append("\n" + exerciseList.get(i).getName());
        }

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



}
