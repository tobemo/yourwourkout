package tobemo.yourworkout;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class CurrentWorkoutAdapter extends RecyclerView.Adapter<CurrentWorkoutAdapter.ViewHolder>{

    private static final String TAG = "currentWorkoutAdapter";

    private List<Exercise> listExercises;
    private Context context;

    public CurrentWorkoutAdapter(Context context, List<Exercise> listExercises) {
        this.listExercises = listExercises;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.current_exercise_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        Log.d(TAG, "onBindViewholder");

        final Exercise exercise = listExercises.get(position);

        holder.textViewTitle.setText(exercise.getName());
        holder.textViewSets.setText(exercise.getIntSets() +"x");
        if(exercise.isBooleanTypeOfExerciseIsReps())    {
            int reps = exercise.getIntReps();
            holder.textViewRepsOrSecs.setText(reps + " reps");
        }   else if (!exercise.isBooleanTypeOfExerciseIsReps())  {
            long longTimeLeftInMillis = exercise.getLongDurationInMillis();
            long seconds = (longTimeLeftInMillis / 1000) % 60;
            long minutes = (longTimeLeftInMillis / (1000 * 60)) % 60;
            long hours = (longTimeLeftInMillis / (1000 * 60 * 60)) % 24;
            String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
            holder.textViewRepsOrSecs.setText(timeLeftFormatted);
        }   else    {
            Toast.makeText(context, "Error determining type of exercise.", Toast.LENGTH_SHORT).show();
        }
        long longBreakDurationInMillis = exercise.getLongBreakDurationInMillis();
        long seconds = (longBreakDurationInMillis / 1000) % 60;
        long minutes = (longBreakDurationInMillis / (1000 * 60)) % 60;
        String breakFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        holder.textViewBreakDuration.setText(breakFormatted);

        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper databaseHelper = new DatabaseHelper(holder.buttonDelete.getContext());
                databaseHelper.removeExercise(position);

                context.startActivity(new Intent(context,CurrentWorkout.class));

            }
        });

        holder.buttonAddBreak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

    }

    private void openDialog() {

    }

    @Override
    public int getItemCount() {
        return listExercises.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewSets;
        TextView textViewBreakDuration;
        TextView textViewRepsOrSecs;
        ImageView buttonDelete;
        Button buttonAddBreak;
        Button buttonSetBreak;
        EditText editTextMinutes;
        EditText editTextSeconds;


        public ViewHolder(View itemView)    {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.tv_card_title);
            textViewSets = itemView.findViewById(R.id.tv_card_sets);
            textViewBreakDuration = itemView.findViewById(R.id.tv_card_break_duration);
            textViewRepsOrSecs = itemView.findViewById(R.id.tv_card_reps_or_secs);
            buttonDelete = itemView.findViewById(R.id.iv_delete);
            buttonAddBreak = itemView.findViewById(R.id.bt_set_break_between_sets);
            buttonSetBreak = itemView.findViewById(R.id.bt_set_popup);
            editTextMinutes = itemView.findViewById(R.id.et_set_time_minutes_popup);
            editTextMinutes = itemView.findViewById(R.id.et_set_time_seconds_popup);

        }
    }
}
