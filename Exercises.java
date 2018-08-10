package tobemo.yourworkout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

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

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {


        //---------------------------------------BOTTOM NAV-------------------------------------------
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
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        longExerciseTimeInMillis = prefs.getLong(startTimeInMillis,666);
        longTimeLeftInMillis = prefs.getLong(millisLeft, longExerciseTimeInMillis);
        booleanTimerRunning = prefs.getBoolean(timerRunning,false);

        updateCountDownText();
        updateInterface();

        if(booleanTimerRunning)   {
            longEndTime = prefs.getLong(endTime,0);
            longTimeLeftInMillis = longEndTime - System.currentTimeMillis();

            if(longTimeLeftInMillis < 0)   {
                longTimeLeftInMillis = 0;
                booleanTimerRunning = false;
                updateCountDownText();
                updateInterface();
            }   else    {
                startTimer(longTimeLeftInMillis);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong(startTimeInMillis, longExerciseTimeInMillis);
        editor.putLong(millisLeft, longTimeLeftInMillis);
        editor.putBoolean(timerRunning, booleanTimerRunning);
        editor.putLong(endTime, longEndTime);

        editor.apply();

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);

        //init layout elements--------------

//        editRestInput = findViewById(R.id.et_set_rest_time);
//        eButtonRestSet = findViewById(R.id.bt_set_rest);

        textViewCountDown = findViewById(R.id.tv_time_counter);
        editTextHoursInput = findViewById(R.id.et_set_time_hours);
        editTextMinutesInput = findViewById(R.id.et_set_time_minutes);
        editTextSecondsInput = findViewById(R.id.et_set_time_seconds);

//        buttonStartPause = findViewById(R.id.bt_start_pause);
//        buttonReset = findViewById(R.id.bt_reset);

        buttonTimeSet = findViewById(R.id.bt_ADD);

        editTextHoursInput = findViewById(R.id.et_set_time_hours);
        editTextMinutesInput = findViewById(R.id.et_set_time_minutes);
        editTextSecondsInput = findViewById(R.id.et_set_time_seconds);


        //read buttons-------------------

        buttonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (booleanTimerRunning)  {
                    pauseTimer();
                }   else    {
                    startTimer(longTimeLeftInMillis);
                }
            }
        });


        buttonTimeSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!booleanTimerRunning) {
                    long hours = 0;
                    long minutes = 0;
                    long seconds = 0;

                    Log.d(TAG, "hours in chars= " + editTextHoursInput.getText().toString());

                    if (editTextHoursInput.getText().toString().length() != 0) {
                        hours = Long.parseLong(editTextHoursInput.getText().toString())
                                * 60 * 60 * 1000;
                    }
                    if (editTextMinutesInput.getText().toString().length() != 0) {
                        minutes = Long.parseLong(editTextMinutesInput.getText().toString())
                                * 60 * 1000;
                    }
                    if (editTextSecondsInput.getText().toString().length() != 0) {
                        seconds = Long.parseLong(editTextSecondsInput.getText().toString())
                                * 1000;
                    }

                    long millisinput = hours + minutes + seconds;
                    Log.d(TAG, "millis = " + millisinput);

                    if (millisinput == 0) {
                        Toast.makeText(Exercises.this, "No time input", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    setTime(millisinput);
                    editTextHoursInput.setText("");
                    editTextMinutesInput.setText("");
                    editTextSecondsInput.setText("");
                }

            }
        });


        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                resetTimer();
            }
        });


       eButtonRestSet.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String input = editRestInput.getText().toString();
               if(input.length() == 0) {
                   Toast.makeText(Exercises.this, "Field can't be empty", Toast.LENGTH_SHORT).show();
                   return;
               }

               long millisinput = Long.parseLong(input)*60000;
               if(millisinput == 0)    {
                   Toast.makeText(Exercises.this, "Field can't be empty", Toast.LENGTH_SHORT).show();
                   return;
               }

               eRestTimeInMillis = millisinput;
               closeKeyboard();

           }
       });



        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    /**
     * Takes a time input from the user and intSets it as the countdowntime.
     * @param milliseconds
     */
    private void setTime(long milliseconds)  {
        longExerciseTimeInMillis = milliseconds;
        resetTimer();
        closeKeyboard();
    }

    private void startTimer(long time)   {

        longEndTime = System.currentTimeMillis() + longTimeLeftInMillis;

        countDownTimer = new CountDownTimer(time,500) {    //if interval is set on the more logical number 1000, the timer stops on zero
            @Override
            public void onTick(long millisLeftUntilFinished) {
                longTimeLeftInMillis = millisLeftUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                booleanTimerRunning = false;
                updateInterface();
            }
        }.start();

        booleanTimerRunning = true;
        updateInterface();
    }

    private void pauseTimer() {
        countDownTimer.cancel();
        booleanTimerRunning = false;
        updateInterface();

    }

    private void resetTimer() {
        longTimeLeftInMillis = longExerciseTimeInMillis;
        updateCountDownText();
        updateInterface();
    }

    private void updateCountDownText() {
        long seconds = (longTimeLeftInMillis / 1000) % 60;
        long minutes = (longTimeLeftInMillis / (1000 * 60)) % 60;
        long hours = (longTimeLeftInMillis / (1000 * 60 * 60)) % 24;

        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d:%02d",hours, minutes, seconds);

        textViewCountDown.setText(timeLeftFormatted);
    }

    private void updateInterface() {
        if (booleanTimerRunning) {
            buttonReset.setVisibility(View.INVISIBLE);
            buttonStartPause.setText("Pause");

        } else {
            buttonStartPause.setText("Start");
            buttonReset.setVisibility(View.VISIBLE);


            if (longTimeLeftInMillis < longExerciseTimeInMillis) {
                buttonReset.setVisibility(View.VISIBLE);
            } else {
                buttonReset.setVisibility(View.INVISIBLE);
            }
        }
    }


    //---------------------------------------OTHER----------------------------------------------------

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


}
