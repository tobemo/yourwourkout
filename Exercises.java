package tobemo.yourworkout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class Exercises extends AppCompatActivity {

    //sets
    private int setCounter;

    private EditText eEditRestInput;
    private Button eButtonRestSet;

    private long eRestTimeInMillis;

    //reps
    private EditText eEditTimeInput;
    private TextView eTextViewCountDown;
    private Button eButtonStartPause;
    private Button eButtonReset;
    private Button eButtonTimeSet;

    private CountDownTimer eCountDownTimer;

    private boolean eTimerRunning;
    private boolean eTimeSet = false;

    private long eStartTimeInMillis;
    private long eTimeLeftInMillis;
    private long eEndTime;

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



//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
//
//        eStartTimeInMillis = prefs.getLong(startTimeInMillis,666);
//        eTimeLeftInMillis = prefs.getLong(millisLeft, eStartTimeInMillis);
//        eTimerRunning = prefs.getBoolean(timerRunning,false);
//        startTimer(eTimeLeftInMillis);
//
//        updateCountDownText();
//        updateInterface();
//
//        if(eTimerRunning)   {
//            eEndTime = prefs.getLong(endTime,0);
//            eTimeLeftInMillis = eEndTime - System.currentTimeMillis();
//
//            if(eTimeLeftInMillis < 0)   {
//                eTimeLeftInMillis = 0;
//                eTimerRunning = false;
//                updateCountDownText();
//                updateInterface();
//            }   else    {
//                startTimer(eTimeLeftInMillis);
//            }
//        }
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//
//        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
//        SharedPreferences.Editor editor = prefs.edit();
//
//        editor.putLong(startTimeInMillis, eStartTimeInMillis);
//        editor.putLong(millisLeft,eTimeLeftInMillis);
//        editor.putBoolean(timerRunning,eTimerRunning);
//        editor.putLong(endTime,eEndTime);
//
//        editor.apply();
//
//        if (eCountDownTimer != null) {
//            eCountDownTimer.cancel();
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);

        //init layout elements--------------

        eEditRestInput = findViewById(R.id.et_set_rest_time);
        eButtonRestSet = findViewById(R.id.bt_set_rest);

        eTextViewCountDown = findViewById(R.id.tv_counter);
        eEditTimeInput = findViewById(R.id.et_set_time);

        eButtonStartPause = findViewById(R.id.bt_start_pause);
        eButtonReset = findViewById(R.id.bt_reset);
        eButtonTimeSet = findViewById(R.id.bt_set);

        //read buttons-------------------

        eButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (eTimerRunning)  {
                    pauseTimer();
                }   else    {
                    if(!eTimeSet)    {
                        Toast.makeText(Exercises.this, "Please set a time duration.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    startTimer(eTimeLeftInMillis);
                }
            }
        });


        eButtonTimeSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String input = eEditTimeInput.getText().toString();

                if(input.length() == 0) {
                    Toast.makeText(Exercises.this, "Field can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                long millisinput = Long.parseLong(input)*60000;

                if(millisinput == 0)    {
                    Toast.makeText(Exercises.this, "Field can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                eTimeSet = true;
                setTime(millisinput);
                eEditTimeInput.setText("");
            }
        });


        eButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                resetTimer();
            }
        });


       eButtonRestSet.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String input = eEditRestInput.getText().toString();
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
     * Takes a time input from the user and sets it as the countdowntime.
     * @param milliseconds
     */
    private void setTime(long milliseconds)  {
        eStartTimeInMillis = milliseconds;
        resetTimer();
        closeKeyboard();
    }

    private void startTimer(long time)   {

        eEndTime = System.currentTimeMillis() + eTimeLeftInMillis;

        eCountDownTimer = new CountDownTimer(time,1000) {
            @Override
            public void onTick(long millisLeftUntilFinished) {
                eTimeLeftInMillis = millisLeftUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                setCounter--;
                if(setCounter > 0) {
                    rest();
                }

                eTimerRunning = false;
                updateInterface();
            }
        }.start();

        eTimerRunning = true;
        updateInterface();
    }

    private void pauseTimer() {
        eCountDownTimer.cancel();
        eTimerRunning = false;
        updateInterface();

    }

    private void resetTimer() {
        eTimeLeftInMillis = eStartTimeInMillis;
        updateCountDownText();
        updateInterface();
    }

    private void updateCountDownText() {
        long seconds = (eTimeLeftInMillis / 1000) % 60;
        long minutes = (eTimeLeftInMillis / (1000 * 60)) % 60;
        long hours = (eTimeLeftInMillis / (1000 * 60 * 60)) % 24;

        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d:%02d",hours, minutes, seconds);

        eTextViewCountDown.setText(timeLeftFormatted);
    }

    private void updateInterface() {
        if (eTimerRunning) {
            eEditTimeInput.setVisibility(View.INVISIBLE);
            eButtonTimeSet.setVisibility(View.INVISIBLE);

            eButtonReset.setVisibility(View.INVISIBLE);
            eButtonStartPause.setText("Pause");
        } else {
            eButtonStartPause.setText("Start");

            eEditTimeInput.setVisibility(View.VISIBLE);
            eButtonTimeSet.setVisibility(View.VISIBLE);

            if (eTimeLeftInMillis < 1000) {
                eButtonStartPause.setVisibility(View.INVISIBLE);
            } else {
                eButtonStartPause.setVisibility(View.VISIBLE);
            }

            if (eTimeLeftInMillis < eStartTimeInMillis) {
                eButtonReset.setVisibility(View.VISIBLE);
            } else {
                eButtonReset.setVisibility(View.INVISIBLE);
            }
        }
    }

    //-------------------------------------- SETS ----------------------------------------------------
    private void rest() {
        startTimer(eRestTimeInMillis);
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
