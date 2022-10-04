package edu.ucdenver.hiitrunner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ViewWorkoutDialog extends DialogFragment {

    //Variables to hold references to editText
    private TextView textViewDate;
    private TextView textViewDistance;
    private TextView textViewTime;
    private TextView textViewAvgSpeed;
    private TextView textViewHighTime;
    private TextView textViewLowTime;

    //Variables to hold button references
    private Button buttonWorkoutHistory;
    private Button buttonDelete;

    //Variable to hold workout reference
    private Workout workout;

    public ViewWorkoutDialog(){
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_view_workout, null);

        //get references
        textViewDate = dialogView.findViewById(R.id.textViewWorkoutDate);
        textViewDistance = dialogView.findViewById(R.id.textViewWorkoutDistance);
        textViewTime = dialogView.findViewById(R.id.textViewWorkoutTime);
        textViewAvgSpeed = dialogView.findViewById(R.id.textViewWorkoutAvgSpeed);
        textViewHighTime = dialogView.findViewById(R.id.textViewHighTime);
        textViewLowTime = dialogView.findViewById(R.id.textViewLowTime);

        buttonWorkoutHistory = dialogView.findViewById(R.id.buttonReturnHistory);
        buttonDelete = dialogView.findViewById(R.id.buttonDelete);

        textViewDate.setText(workout.getDate());
        textViewDistance.setText(String.format(Locale.getDefault(), "%.2f km", workout.getTotalDistance()));

        //calculate and display total time of workout
        int minutesTotal =  (int) TimeUnit.MILLISECONDS.toMinutes(workout.getTotalTime());
        int secondsTotal = (int) (TimeUnit.MILLISECONDS.toSeconds(workout.getTotalTime()) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(workout.getTotalTime())));

        textViewTime.setText(String.format(Locale.getDefault(),"%02d:%02d", minutesTotal, secondsTotal));

        textViewAvgSpeed.setText(String.format(Locale.getDefault(), "%.1f km/h", workout.getAvgSpeed()));

        int minutesHigh =  (int) TimeUnit.MILLISECONDS.toMinutes(workout.getTotalHighTime());
        int secondsHigh = (int) (TimeUnit.MILLISECONDS.toSeconds(workout.getTotalHighTime()) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(workout.getTotalHighTime())));

        textViewHighTime.setText(String.format(Locale.getDefault(),"%02d:%02d", minutesHigh, secondsHigh));

        int minutesLow =  (int) TimeUnit.MILLISECONDS.toMinutes(workout.getTotalLowTime());
        int secondsLow = (int) (TimeUnit.MILLISECONDS.toSeconds(workout.getTotalLowTime()) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(workout.getTotalLowTime())));

        textViewLowTime.setText(String.format(Locale.getDefault(),"%02d:%02d", minutesLow, secondsLow));

        buttonWorkoutHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HistoryActivity callingActivity =(HistoryActivity) getActivity();
                callingActivity.deleteWorkout(workout);
                dismiss();
            }
        });


        builder.setView(dialogView).setMessage(" ");
        return builder.create();
    }

    public void sendWorkout(Workout workout) {this.workout = workout;}


}
