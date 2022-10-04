package edu.ucdenver.hiitrunner;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.ListItemHolder> {
    private HistoryActivity historyActivity;
    private ArrayList<Workout> workoutList;

    public WorkoutAdapter (HistoryActivity historyActivity, ArrayList<Workout> workoutList){
        this.historyActivity = historyActivity;
        this.workoutList = workoutList;
    }

    @NonNull
    @Override
    public ListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent,false);
        return new ListItemHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutAdapter.ListItemHolder holder, int position) {
        Workout workout = workoutList.get(position);
        holder.textViewDate.setText(workout.getDate());
        holder.textViewDistance.setText(String.format(Locale.getDefault(), "%.2f km", workout.getTotalDistance()));

    }

    @Override
    public int getItemCount() {
        return workoutList.size();
    }

    public class ListItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textViewDate;
        private TextView textViewDistance;

        public ListItemHolder(View view){
            super(view);

            textViewDate = view.findViewById(R.id.textViewDate);
            textViewDistance = view.findViewById(R.id.textViewDistance);
            view.setClickable(true);
            view.setOnClickListener(this);
        }

        public void onClick (View view){

            Log.i("info", "OnClick method in WorkoutAdapter");
            historyActivity.showWorkout(getAdapterPosition());
        }
    }
}
