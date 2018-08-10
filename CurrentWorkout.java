package tobemo.yourworkout;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class CurrentWorkout extends AppCompatActivity {

    private static final String TAG = "CurrentWorkout";

    private CountDownTimer countDownTimer;

    private boolean booleanTimerRunning;
    private boolean booleanWorkoutRunning;
    private boolean booleanBreak;   //TODO: change color during break
    private boolean booleanTypeOfExerciseIsReps;   //repetitions vs timebased exercise

    private int AmountOfSets;
    private int intSetsLeft;
    private int intAmountOfReps;
    private int intRepsLeft;

    private long longExerciseTimeInMillis = 4500000;    //todo leeg
    private long longTimeLeftInMillis;
    private long longEndTime;

    private FloatingActionButton buttonStartPause;    //TODO: if paused, change image on floating button

    private Button buttonStop;          //TODO: stop workout

    private TextView textViewCountDown;
    private TextView textViewCurrentSet;


    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_workout);

        textViewCountDown = findViewById(R.id.tv_time_counter);
        textViewCurrentSet = findViewById(R.id.tv_set_counter);

        buttonStartPause = findViewById(R.id.startWorkout);

        booleanTypeOfExerciseIsReps = true;


        buttonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(booleanWorkoutRunning) {
                    pauseWorkout();
                }   else    {
                    startWorkout();
                }
            }
        });


        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    private void startWorkout() {
        //TODO if(tpye of workout(reps-secs)) do:...

        //TODO extract data of first workout

        if(booleanTypeOfExerciseIsReps) {

            updateTimerText();

        }   else  {

            longExerciseTimeInMillis = 360000;  //TODO weg
            longTimeLeftInMillis = longExerciseTimeInMillis;
            startTimer(longTimeLeftInMillis);
        }



        booleanWorkoutRunning = true;
        updateInterface();
    }

    private void pauseWorkout() {
        if(booleanTimerRunning) {
            pauseTimer();
        }

        booleanWorkoutRunning = false;
        updateInterface();
    }

    private void startTimer(long timeInput) {

        longEndTime = System.currentTimeMillis() + timeInput;

        countDownTimer = new CountDownTimer(timeInput, 500) {   //if interval is set on the more logical number 1000, the timer stops on zero
            @Override
            public void onTick(long millisLeftUntilFinished) {
                longTimeLeftInMillis = millisLeftUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {

            }
        }.start();

        booleanTimerRunning = true;
    }

    private void updateTimerText() {

        if(booleanTypeOfExerciseIsReps) {

            intRepsLeft = 12;
            String repsLeftFormatted = "reps: " + String.format("%02d",intRepsLeft);
            textViewCountDown.setText(repsLeftFormatted);

        }   else {

            long seconds = (longTimeLeftInMillis / 1000) % 60;
            long minutes = (longTimeLeftInMillis / (1000 * 60)) % 60;
            long hours = (longTimeLeftInMillis / (1000 * 60 * 60)) % 24;

            String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
            textViewCountDown.setText(timeLeftFormatted);
        }

    }

    private void pauseTimer() {
        countDownTimer.cancel();
        booleanTimerRunning = false;
        updateInterface();
    }

    private void updateInterface() {
        if(booleanWorkoutRunning) {
            buttonStartPause.setImageResource(R.drawable.ic_pause_white_24dp);
        }   else    {
            buttonStartPause.setImageResource(R.drawable.ic_play_arrow_white_24dp);
        }
    }

}

//TODO save locally, button to say reps done; if reps done/timer out -> start break, set--; if set done next exercise