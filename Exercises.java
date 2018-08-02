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

    private EditText eRestInput;
    private Button eRestSet;

    //reps
    private EditText eEditTextInput;
    private TextView eTextViewCountDown;
    private Button eButtonStartPause;
    private Button eButtonReset;
    private Button eButtonSet;

    private CountDownTimer eCountDownTimer;

    private boolean eTimerRunning;

    private long eStatrTimeInMillis;
    private long eTimeLeftInMillis;
    private long eEndTime;

    private String millisLeft = "millisLeft";
    private String timerRunning = "timerRunning";
    private String endTime = "endTime";
    private String startTimeInMillis = "startTimeInMillis";

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
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        eStatrTimeInMillis = prefs.getLong(startTimeInMillis,666);
        eTimeLeftInMillis = prefs.getLong(millisLeft, eStatrTimeInMillis);
        eTimerRunning = prefs.getBoolean(timerRunning,false);
        startTimer();

        updateCountDownText();
        updateInterface();

        if(eTimerRunning)   {
            eEndTime = prefs.getLong(endTime,0);
            eTimeLeftInMillis = eEndTime - System.currentTimeMillis();

            if(eTimeLeftInMillis < 0)   {
                eTimeLeftInMillis = 0;
                eTimerRunning = false;
                updateCountDownText();
                updateInterface();
            }   else    {
                startTimer();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong(startTimeInMillis, eStatrTimeInMillis);
        editor.putLong(millisLeft,eTimeLeftInMillis);
        editor.putBoolean(timerRunning,eTimerRunning);
        editor.putLong(endTime,eEndTime);

        editor.apply();

        if (eCountDownTimer != null) {
            eCountDownTimer.cancel();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);

        eRestInput = findViewById(R.id.et_set_rest_time);
        eRestSet = findViewById(R.id.bt_set_rest);

        eTextViewCountDown = findViewById(R.id.tv_counter);
        eEditTextInput = findViewById(R.id.et_set_time);

        eButtonStartPause = findViewById(R.id.bt_start_pause);
        eButtonReset = findViewById(R.id.bt_reset);
        eButtonSet = findViewById(R.id.bt_set);

        eButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (eTimerRunning)  {
                    pauseTimer();
                }   else    {
                    startTimer();
                }
            }
        });

        eButtonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = eEditTextInput.getText().toString();
                if(input.length() == 0) {
                    Toast.makeText(Exercises.this, "Field can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                long millisinput = Long.parseLong(input)*60000;
                if(millisinput == 0)    {
                    Toast.makeText(Exercises.this, "Field can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                setTime(millisinput);
                eEditTextInput.setText("");
            }
        });

        eButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();
            }
        });

       eRestSet.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

           }
       });



        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void setTime(long milliseconds)  {
        eStatrTimeInMillis = milliseconds;
        resetTimer();
        closeKeyboard();
    }

    private void startTimer()   {
        eEndTime = System.currentTimeMillis() + eTimeLeftInMillis;

        eCountDownTimer = new CountDownTimer(eTimeLeftInMillis,1000) {
            @Override
            public void onTick(long millisLeftUntilFinished) {
                eTimeLeftInMillis = millisLeftUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                setCounter--;
                if(setCounter > 0) {
                    //rest();
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
        eTimeLeftInMillis = eStatrTimeInMillis;
        updateCountDownText();
        updateInterface();
    }

    private void updateCountDownText() {
        int hours = (int) (eTimeLeftInMillis / 1000) / 36000;
        int minutes = (int) ((eTimeLeftInMillis/1000)/ 3600) /60;
        int seconds = (int) (eTimeLeftInMillis/1000)%60;

        String timeLeftFormatted = String.format(Locale.getDefault(),"%d:%02d:%02d",hours, minutes, seconds);

        eTextViewCountDown.setText(timeLeftFormatted);
    }

    private void updateInterface() {
        if (eTimerRunning) {
            eEditTextInput.setVisibility(View.INVISIBLE);
            eButtonSet.setVisibility(View.INVISIBLE);
            eButtonReset.setVisibility(View.INVISIBLE);
            eButtonStartPause.setText("Pause");
        } else {
            eButtonStartPause.setText("Start");
            eEditTextInput.setVisibility(View.VISIBLE);
            eButtonSet.setVisibility(View.VISIBLE);
            if (eTimeLeftInMillis < 1000) {
                eButtonStartPause.setVisibility(View.INVISIBLE);
            } else {
                eButtonStartPause.setVisibility(View.VISIBLE);
            }

            if (eTimeLeftInMillis < eStatrTimeInMillis) {
                eButtonReset.setVisibility(View.VISIBLE);
            } else {
                eButtonReset.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


}
