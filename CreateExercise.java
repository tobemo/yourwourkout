package tobemo.yourworkout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;


public class CreateExercise extends AppCompatActivity {

    private static final String TAG = "CreateExercise";

    protected String name;

    protected int intSets;
    protected int intReps;

    protected long longDurationInMillis;    //TODO: implement reps
    protected long longBreakDurationInMillis;

    private ViewFlipper viewFlipperRepsOrSecs;

    private EditText editTextNameInput;
    private EditText editTextHoursInput;
    private EditText editTextMinutesInput;
    private EditText editTextSecondsInput;
    private EditText editTextBreakMinutesInput;
    private EditText editTextBreakSecondsInput;
    private EditText editTextReps;
    private EditText editTextSets;

    private Button buttonAdd;
    private Button buttonRepsSecs;

    private boolean booleanTypeOfExerciseIsReps;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_exercise);

        viewFlipperRepsOrSecs = findViewById(R.id.vf_reps_or_secs);

        buttonAdd = findViewById(R.id.bt_ADD);

        buttonRepsSecs = findViewById(R.id.bt_reps_or_duration);

        editTextNameInput = findViewById(R.id.et_exercise_name_input);

        editTextHoursInput = findViewById(R.id.et_set_time_hours);
        editTextMinutesInput = findViewById(R.id.et_set_time_minutes);
        editTextSecondsInput = findViewById(R.id.et_set_time_seconds);
        editTextBreakMinutesInput = findViewById(R.id.et_set_rest_time_minutes);
        editTextBreakSecondsInput = findViewById(R.id.et_set_rest_time_seconds);
        editTextReps = findViewById(R.id.et_set_exercise_reps);
        editTextSets = findViewById(R.id.et_set_exercise_sets);



        //switch between sec or reps type of exercise
        buttonRepsSecs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(booleanTypeOfExerciseIsReps) {

                    viewFlipperRepsOrSecs.setDisplayedChild(0);

                    buttonRepsSecs.setText("Secs");
                    booleanTypeOfExerciseIsReps = false;
                }   else    {

                    viewFlipperRepsOrSecs.setDisplayedChild(1);

                    buttonRepsSecs.setText("Reps");
                    booleanTypeOfExerciseIsReps = true;
                }


            }
        });


        //remembering things
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //save name
                name = editTextNameInput.getText().toString();

                if(name.length() == 0)  {
                    Toast.makeText(CreateExercise.this, "Add exercise name.", Toast.LENGTH_SHORT).show();
                    return;
                }


                //save intSets
                if(editTextSets.getText().toString().length() != 0)  {

                    intSets = Integer.parseInt(editTextSets.getText().toString());

                }   else    {
                    Toast.makeText(CreateExercise.this, "Choose the amount of intSets.", Toast.LENGTH_SHORT).show();
                    return;
                }



                //save reps or duration
                if(booleanTypeOfExerciseIsReps) {
                    if(editTextReps.getText().toString().length() != 0)  {
                        intReps = Integer.parseInt(editTextReps.getText().toString());
                    }   else    {
                        Toast.makeText(CreateExercise.this, "No amount of repetitions set.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }   else {
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
                        Toast.makeText(CreateExercise.this, "No time input.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    longDurationInMillis = millisinput;


                }

                //save break
                long minutesBreak = 0;
                long secondsBreak = 0;


                if (editTextBreakMinutesInput.getText().toString().length() != 0) {
                    minutesBreak = Long.parseLong(editTextBreakMinutesInput.getText().toString())
                            * 60 * 1000;
                }
                if (editTextBreakSecondsInput.getText().toString().length() != 0) {
                    secondsBreak = Long.parseLong(editTextBreakSecondsInput.getText().toString())
                            * 1000;
                }

                long millisInputBreak =  minutesBreak + secondsBreak;

                if (millisInputBreak == 0) {
                    Toast.makeText(CreateExercise.this, "No time input", Toast.LENGTH_SHORT).show();
                    return;
                }

                longBreakDurationInMillis = millisInputBreak;


                if(longBreakDurationInMillis == 0)  {
                    Toast.makeText(CreateExercise.this, "No duration set.", Toast.LENGTH_SHORT).show();
                    return;
                }

                editTextHoursInput.setText("");
                editTextMinutesInput.setText("");
                editTextSecondsInput.setText("");
                editTextBreakMinutesInput.setText("");
                editTextBreakSecondsInput.setText("");
                editTextReps.setText("");
                editTextSets.setText("");

                Toast.makeText(CreateExercise.this, "Exercise Added", Toast.LENGTH_SHORT).show();

                //TODO: save localy


                Intent Exercises = new Intent(CreateExercise.this,Exercises.class);
                startActivity(Exercises);
            }
        });
    }



}
