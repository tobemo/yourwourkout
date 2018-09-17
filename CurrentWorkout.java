package tobemo.yourworkout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class CurrentWorkout extends AppCompatActivity {

    private static final String TAG = "CurrentWorkout";

    //private CountDownTimer countDownTimer;
    private MoreAccurateTimer countDownTimer;

    private boolean booleanTimerRunning;
    private boolean booleanWorkoutRunning;
    private boolean booleanTakingBreak;   //TODO: change color during break
    private boolean booleanTypeOfExerciseIsReps;   //repetitions vs timebased exercise
    private boolean booleanExerciseIsNotRunning;

    private int AmountOfSets;
    private int intSetsLeft;
    private int intAmountOfReps;
    private int intRepsLeft;
    private int intIteratorIndex;

    private long longExerciseTimeInMillis = 4500000;    //todo leeg
    private long longTimeLeftInMillis;
    private long longEndTime;
    private long longBreakBetweenSetsInMillis = 60000;

    private FloatingActionButton buttonStartPause;    //TODO: if paused, change image on floating button
    private FloatingActionButton buttonExerciseDone;

    private Button buttonStop;          //TODO: stop workout

    private TextView textViewCountDown;
    private TextView textViewCurrentSet;
    private TextView mTextMessage;
    private TextView textViewCurrentExercise;

    private String millisLeft = "millisLeft";
    private String timerRunning = "timerRunning";
    private String endTime = "endTime";
    private String startTimeInMillis = "startTimeInMillis";
    private String workoutRunning = "workoutRunning";

    private String exerciseName;

    private List<Exercise> listExercises = new ArrayList<>();
    private Exercise currentExercise;

    private Iterator<Exercise> iterator;

    private RecyclerView recyclerView;
    private CurrentWorkoutAdapter adapter;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

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
        setContentView(R.layout.activity_current_workout);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        textViewCurrentExercise = findViewById(R.id.tv_current_exercise);
        textViewCountDown = findViewById(R.id.tv_time_counter);
        textViewCurrentSet = findViewById(R.id.tv_set_counter);

        buttonStartPause = findViewById(R.id.bt_startWorkout);
        buttonExerciseDone = findViewById(R.id.bt_exercise_done);
        buttonStartPause.setImageResource(R.drawable.ic_play_arrow_white_24dp);

        listExercises = getExercises();

        booleanWorkoutRunning = false;

        if (!listExercises.isEmpty()) {
            initRecyclerView();
            currentExercise = listExercises.get(0);
            booleanTypeOfExerciseIsReps = currentExercise.isBooleanTypeOfExerciseIsReps();
            intSetsLeft = currentExercise.getIntSets();
            longTimeLeftInMillis = currentExercise.getLongDurationInMillis();
            intRepsLeft = currentExercise.getIntReps();
            exerciseName = currentExercise.getName();


            //Only called once per lifecycle.
            setTimerText();
        }

        booleanExerciseIsNotRunning = true;


        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            int i = 0;
            @Override
            public void onClick(View view) {
                Log.d(TAG, "i = " + i);
                recyclerView.smoothScrollToPosition(i);
                i++;
            }
        });

        buttonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "workoutRunning = " + booleanWorkoutRunning);
                Log.d(TAG, "exerciseNotRunning = " + booleanExerciseIsNotRunning);
                if (booleanWorkoutRunning) {
                    pauseWorkout();
                    if(countDownTimer != null) {
                        pauseTimer();
                    }
                } else {
                    if(booleanExerciseIsNotRunning) {
                        startWorkout();
                    }   else {
                        resumeWorkout();
                            //TODO: fix pause button, user resume method
                    }

                }
            }
        });


        mTextMessage = findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.navigation_current_workout:
                        Intent currentWorkout = new Intent(CurrentWorkout.this, CurrentWorkout.class);
                        startActivity(currentWorkout);
                        break;

                    case R.id.navigation_workouts:
                        //TODO: create intent once activity exists
                        Intent ex = new Intent(CurrentWorkout.this, Exercise.class);
                        startActivity(ex);
                        break;

                    case R.id.navigation_exercises:
                        Intent exercises = new Intent(CurrentWorkout.this, Exercises.class);
                        startActivity(exercises);
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        longExerciseTimeInMillis = prefs.getLong(startTimeInMillis, 666);
        longTimeLeftInMillis = prefs.getLong(millisLeft, longExerciseTimeInMillis);
        booleanTimerRunning = prefs.getBoolean(timerRunning, false);
        booleanWorkoutRunning = prefs.getBoolean(workoutRunning, false);

        updateInterface();

        if (booleanTimerRunning) {
            longEndTime = prefs.getLong(endTime, 0);
            longTimeLeftInMillis = longEndTime - System.currentTimeMillis();

            if (longTimeLeftInMillis < 0) {
                longTimeLeftInMillis = 0;
                booleanTimerRunning = false;
                updateTimerText();
                updateInterface();
            } else {
                startTimer(longTimeLeftInMillis );
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void startWorkout() {
        booleanExerciseIsNotRunning = true;

        Log.d(TAG, "startWorkout");

        iterator = listExercises.iterator();

        Toast toast = Toast.makeText(getApplicationContext(), "GO!", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 400);
        toast.show();

        startFirstTimer(4000);
    }

    private void resumeWorkout() {
        booleanWorkoutRunning = true;
        updateInterface();

        if(booleanTakingBreak)  {
            startBreak(longTimeLeftInMillis);
        }   else {

            if (currentExercise.isBooleanTypeOfExerciseIsReps()) {

            } else {
                startTimer(longTimeLeftInMillis);
            }
        }

    }

    private void pauseWorkout() {
        booleanWorkoutRunning = false;
        updateInterface();

        if (booleanTimerRunning) {
            pauseTimer();
        }

    }

    private void endWorkout()   {
        Toast.makeText(CurrentWorkout.this, "Congrats, you're finsihed!", Toast.LENGTH_LONG).show();
    }

    private void startExercise() {
        if (iterator.hasNext() && booleanExerciseIsNotRunning) {

            currentExercise = iterator.next();
            intIteratorIndex++;

            booleanExerciseIsNotRunning = false;
            booleanTakingBreak = false;

            Log.d(TAG, "taking break = " + booleanTakingBreak);

            booleanTypeOfExerciseIsReps = currentExercise.isBooleanTypeOfExerciseIsReps();
            intSetsLeft = currentExercise.getIntSets();

            setSets();
            textViewCurrentExercise.setText(currentExercise.getName());

            if (booleanTypeOfExerciseIsReps) {

                intRepsLeft = currentExercise.getIntReps();
                setRepetitions();
                Toast.makeText(CurrentWorkout.this, "Do repetitions.", Toast.LENGTH_SHORT).show();

                buttonExerciseDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(booleanWorkoutRunning == false) {
                            booleanWorkoutRunning = true;
                            updateInterface();
                        }
                        setDone();
                    }
                });


            } else {
                startTimer(currentExercise.getLongDurationInMillis() + 1000);
            }

        }   else if(!iterator.hasNext())   {
            endWorkout();

        }
    }


    /**
     *  Using the traditional countdowntimer caused problems with last tick and OnFinish ot being called.
     */
//    private void startFirstTimer(long timeInput)    {
//
//        longEndTime = System.currentTimeMillis() + timeInput;
//
//        countDownTimer = new CountDownTimer(timeInput, 1000) {   //if interval is set on the more logical number 1000, the timer stops on 1
//            @Override
//            public void onTick(long millisLeftUntilFinished) {
//                longTimeLeftInMillis = millisLeftUntilFinished;
//                Log.d(TAG, "secon left: " + longTimeLeftInMillis/1000 );
//                setTime();
//                ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
//                if(longTimeLeftInMillis < 4000) {
//                    toneGen1.startTone(ToneGenerator.TONE_CDMA_SOFT_ERROR_LITE,100);
//                }
//            }
//
//            @Override
//            public void onFinish() {
//                Log.d(TAG, "millis left: " + longTimeLeftInMillis );
//                textViewCountDown.setText("00:00:00");
//                booleanWorkoutRunning = true;
//                updateInterface();
//
//                startExercise();
//            }
//        }.start();
//    }

    private void startFirstTimer(long timeInput)    {
        longEndTime = System.currentTimeMillis() + timeInput;

        countDownTimer = new MoreAccurateTimer(timeInput, 1000) {

            @Override
            public void onTick(long millisLeftUntilFinished) {
                longTimeLeftInMillis = millisLeftUntilFinished;
                Log.d(TAG, "secon left: " + longTimeLeftInMillis/1000 );
                setTime();
                ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                if(longTimeLeftInMillis < 4000) {
                    toneGen1.startTone(ToneGenerator.TONE_CDMA_SOFT_ERROR_LITE,100);
                }
            }

            @Override
            public void onFinish() {
                booleanWorkoutRunning = true;
                updateInterface();

                startExercise();
            }
        }.start();
    }



    private void startTimer(long timeInput) {

        booleanTimerRunning = true;
        longEndTime = System.currentTimeMillis() + timeInput;
        longTimeLeftInMillis = timeInput;

        setSets();

        countDownTimer = new MoreAccurateTimer(timeInput , 500) {
            @Override
            public void onTick(long millisLeftUntilFinished) {
                longTimeLeftInMillis = millisLeftUntilFinished;
                Log.d(TAG, "timeleft" + (longTimeLeftInMillis / 1000) % 60);
                setTime();
                ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                if(longTimeLeftInMillis < 4000) {
                    toneGen1.startTone(ToneGenerator.TONE_CDMA_SOFT_ERROR_LITE,100);
                }
            }

            @Override
            public void onFinish() {
                Log.d(TAG, "timer finished");
                setDone();

                booleanTimerRunning = false;
            }
        }.start();

    }

    private void startBreak(long timeInput)   {

        booleanTimerRunning = true;
        longEndTime = System.currentTimeMillis() + timeInput;
        longTimeLeftInMillis = timeInput;
        setSets();

        countDownTimer = new MoreAccurateTimer(timeInput + 1000, 1000) {
        @Override                                                                                   // + 1000 is so the first second is also displayed
            public void onTick(long millisLeftUntilFinished) {
                longTimeLeftInMillis = millisLeftUntilFinished;
                Log.d(TAG, "timeleft" + (longTimeLeftInMillis / 1000) % 60);
                setTime();
                ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                if(longTimeLeftInMillis < 4000) {
                    toneGen1.startTone(ToneGenerator.TONE_CDMA_SOFT_ERROR_LITE,100);
                }
            }

            @Override
            public void onFinish() {
                Log.d(TAG, "timer finished");
                breakDone();
                booleanTimerRunning = false;
            }
        }.start();
    }

    private void setDone() {

        Log.d(TAG, "setDone");

        //If there are still sets to do, the break of an exercise followed by the same exercise.
        if (intSetsLeft > 1) {  //TODO; change to 1
            intSetsLeft--;
            booleanTakingBreak = true;
            Log.d(TAG, "intsets = " + intSetsLeft);

            startBreak(currentExercise.getLongBreakDurationInMillis());

        } else {
            //TODO: break between sets
            recyclerView.smoothScrollToPosition(intIteratorIndex);
            textViewCurrentSet.setText("0x");
            booleanExerciseIsNotRunning = true;
            booleanTakingBreak = false;
            Log.d(TAG, "exerciseIsDone");

            startExercise();
        }

    }

    private void breakDone() {

        Log.d(TAG, "breakDone");
        booleanTakingBreak = false;
        setSets();

        if (booleanTypeOfExerciseIsReps)    {

            setRepetitions();

        }   else    {

            startTimer(currentExercise.getLongDurationInMillis() + 1000);
        }

    }

    private void beepThreeTimes()    {
        ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        for(int i = 0; i < 3 ; i++) {
            toneGen1.startTone(ToneGenerator.TONE_CDMA_SOFT_ERROR_LITE,900);
        }
    }

    /**
     * Sets the ui to show the amount of sets and reps/time that the first exercise will take.
     */
    private void setTimerText() {
        if (booleanTypeOfExerciseIsReps) {

            String repsLeftFormatted = "reps: " + String.format("%02d", intRepsLeft);
            textViewCountDown.setText(repsLeftFormatted);

        } else {

            long seconds = (currentExercise.getLongDurationInMillis() / 1000) % 60;
            long minutes = (currentExercise.getLongDurationInMillis() / (1000 * 60)) % 60;
            long hours = (currentExercise.getLongDurationInMillis() / (1000 * 60 * 60)) % 24;

            String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
            textViewCountDown.setText(timeLeftFormatted);
        }

        String setsLeftFormatted = currentExercise.getIntSets() + "x";
        textViewCurrentSet.setText(setsLeftFormatted);

        textViewCurrentExercise.setText(exerciseName);
    }

    /**
     * Updates the ui to show the progression of an exercise.
     */
    private void updateTimerText() {
        Log.d(TAG, "updateTimerText ");

        if (booleanTypeOfExerciseIsReps) {
            setRepetitions();
        } else {
            setTime();
        }
        setSets();
    }

    private void setRepetitions()   {
        String repsLeftFormatted = "reps: " + String.format("%02d", intRepsLeft);
        textViewCountDown.setText(repsLeftFormatted);
    }

    private void setTime()  {
        Log.d(TAG, "millis left: " + longTimeLeftInMillis );
        long seconds = (longTimeLeftInMillis / 1000) % 60;
        long minutes = (longTimeLeftInMillis / (1000 * 60)) % 60;
        long hours = (longTimeLeftInMillis / (1000 * 60 * 60)) % 24;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
        Log.d(TAG, "time left: " + timeLeftFormatted );
        textViewCountDown.setText(timeLeftFormatted);
    }

    private void setSets()  {
        if(booleanTakingBreak)  {
            textViewCurrentSet.setTextSize(20);
            textViewCurrentSet.setText("break:");
        }   else    {
            textViewCurrentSet.setTextSize(60);
            String setsLeftFormatted = intSetsLeft + "x";
            textViewCurrentSet.setText(setsLeftFormatted);
        }
    }

    private void pauseTimer() {
        countDownTimer.cancel();
        booleanTimerRunning = false;
        updateInterface();
    }

    private void updateInterface() {
        if (booleanWorkoutRunning) {
            buttonStartPause.setImageResource(R.drawable.ic_pause_white_24dp);
        } else {
            buttonStartPause.setImageResource(R.drawable.ic_play_arrow_white_24dp);
        }
    }


    /*Gets all the exercises from  the current_workout_table and puts them in a list.
     *
     */

    private List<Exercise> getExercises() {
        Log.d(TAG, "getExercises");
        DatabaseHelper databaseHelper = new DatabaseHelper(CurrentWorkout.this);
        return databaseHelper.getAllExercisesInCurrentWorkout();
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.rv_current_workout);
        adapter = new CurrentWorkoutAdapter(this, listExercises);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
        editor.putBoolean(workoutRunning, booleanWorkoutRunning);

        editor.apply();

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

}

//TODO: fill exercises; delete exercises also in table; currentworkout different recyclerveiw adapter;drag exercises; button to say reps done; if reps done/timer out -> start break, set--; if set done next exercise; save workouts; chance bottom nav icons; dimens en color extracten