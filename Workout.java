package tobemo.yourworkout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

public class Workout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
    }

    protected String name;
    protected List<Exercise> listExercises;
}
