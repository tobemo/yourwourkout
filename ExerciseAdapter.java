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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ViewHolder>{

    private static final String TAG = "Adapter";

    private List<Exercise> listExercises = new ArrayList<>();
    private Context context;

    public ExerciseAdapter(Context context, List<Exercise> listExercises) {
        this.listExercises = listExercises;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

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

        holder.buttonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper databaseHelper = new DatabaseHelper(holder.buttonSelect.getContext());
                if( databaseHelper.isUniqueInCurrentWorkoutTable(exercise)) {
                    databaseHelper.addExerciseToCurrentWorkout(exercise);
                }   else {
                    Toast.makeText(context, "Exercise already selected.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper databaseHelper = new DatabaseHelper(holder.buttonSelect.getContext());
                databaseHelper.deleteExercise(
                        exercise.getID());


                if( !databaseHelper.isUniqueInCurrentWorkoutTable(exercise)) {
                    databaseHelper.deleteExerciseInCurrentWorkout(
                            exercise.getID()
                    );
                }

                context.startActivity(new Intent(context,Exercises.class));

            }
        });

        //TODO implement both buttons
//            holder.buttonSelect.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                }
//            });
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
        Button buttonSelect;
        Button buttonAdd;
        ImageView buttonDelete;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.tv_card_title);
            textViewSets = itemView.findViewById(R.id.tv_card_sets);
            textViewBreakDuration = itemView.findViewById(R.id.tv_card_break_duration);
            textViewRepsOrSecs = itemView.findViewById(R.id.tv_card_reps_or_secs);
            buttonSelect = itemView.findViewById(R.id.bt_card_select);
            buttonAdd = itemView.findViewById(R.id.bt_card_add);
            buttonDelete = itemView.findViewById(R.id.iv_delete);
        }
    }

}
