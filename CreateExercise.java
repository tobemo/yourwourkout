package tobemo.yourworkout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateExercise extends AppCompatActivity {

    private static final String TAG = "CreateExercise";

    protected String name;

    protected int sets;

    protected long durationInMillis;    //TODO: implement reps

    protected long breakDurationInMillis;

    private EditText eNameInput;

    private EditText eHoursInput;
    private EditText eMinutesInput;
    private EditText eSecondsInput;
    private EditText eBreakMinutesInput;
    private EditText eBrealSecondsInput;

    private Button eButtonTimeSet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_exercise);

        eButtonTimeSet = findViewById(R.id.bt_ADD);

        eNameInput = findViewById(R.id.et_exercise_name_input);

        eHoursInput = findViewById(R.id.et_set_time_hours);
        eMinutesInput = findViewById(R.id.et_set_time_minutes);
        eSecondsInput = findViewById(R.id.et_set_time_seconds);
        eBreakMinutesInput = findViewById(R.id.et_set_rest_time_minutes);
        eBrealSecondsInput = findViewById(R.id.et_set_rest_time_seconds);


        eButtonTimeSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //save name
                name = eNameInput.getText().toString();

                //save sets

                //save duration
                long hours = 0;
                long minutes = 0;
                long seconds = 0;

                Log.d(TAG, "hours in chars= " + eHoursInput.getText().toString());

                if (eHoursInput.getText().toString().length() != 0) {
                    hours = Long.parseLong(eHoursInput.getText().toString())
                            * 60 * 60 * 1000;
                }
                if (eMinutesInput.getText().toString().length() != 0) {
                    minutes = Long.parseLong(eMinutesInput.getText().toString())
                            * 60 * 1000;
                }
                if (eSecondsInput.getText().toString().length() != 0) {
                    seconds = Long.parseLong(eSecondsInput.getText().toString())
                            * 1000;
                }

                long millisinput = hours + minutes + seconds;
                Log.d(TAG, "millis = " + millisinput);

                if (millisinput == 0) {
                    Toast.makeText(CreateExercise.this, "No time input", Toast.LENGTH_SHORT).show();
                    return;
                }

                durationInMillis = millisinput;

                //save break
                long minutesBreak = 0;
                long secondsBreak = 0;


                if (eBreakMinutesInput.getText().toString().length() != 0) {
                    minutesBreak = Long.parseLong(eBreakMinutesInput.getText().toString())
                            * 60 * 1000;
                }
                if (eBrealSecondsInput.getText().toString().length() != 0) {
                    secondsBreak = Long.parseLong(eBrealSecondsInput.getText().toString())
                            * 1000;
                }

                long millisinputBreak =  minutesBreak + secondsBreak;

                if (millisinputBreak == 0) {
                    Toast.makeText(CreateExercise.this, "No time input", Toast.LENGTH_SHORT).show();
                    return;
                }

                breakDurationInMillis = millisinputBreak;
            }
        });
    }
}
