package edu.ucdenver.hiitrunner;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class StatisticsActivity extends AppCompatActivity {

    private ArrayList<Workout> workoutList;
    private DataManager dataManager;
    private Button buttonMainMenu;

    private TextView textViewTotalTime;
    private TextView textViewTotalDistance;
    private TextView textViewAvgSpeed;
    private TextView textViewAvgDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        workoutList = new ArrayList<Workout>();
        dataManager = DataManager.getDataManagerInstance(getApplicationContext());

        textViewTotalTime = findViewById(R.id.textViewStatTotalTime);
        textViewTotalDistance = findViewById(R.id.textViewStatDistance);
        textViewAvgSpeed = findViewById(R.id.textViewStatAvgSpeed);
        textViewAvgDistance =findViewById(R.id.textViewStatAvgDistance);

        buttonMainMenu = findViewById(R.id.buttonReturnMainFromStat);
        buttonMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public void loadWorkouts() {
        Cursor cursor = dataManager.selectAllWorkouts();
        int workoutCount = cursor.getCount();

        if (workoutCount > 0) {
            workoutList.clear();

            while (cursor.moveToNext()) {
                long startTime = cursor.getLong(0);
                long totalTime = cursor.getLong(1);
                double totalDistance = cursor.getDouble(2);
                double avgSpeed = cursor.getDouble(3);
                long totalHighTime = cursor.getLong(4);
                long totalLowTime = cursor.getLong(5);

                Log.i("info", Long.toString(startTime));
                Workout workout = new Workout(startTime, totalTime, totalDistance, avgSpeed,
                        totalHighTime, totalLowTime);

                workoutList.add(workout);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadWorkouts();

        int totalTime = 0;
        double totalDistance = 0;
        double avgSpeed = 0;
        double avgDistance = 0;

        for (int i = 0; i < workoutList.size(); i++) {
            totalTime += workoutList.get(i).getTotalTime();
            totalDistance += workoutList.get(i).getTotalDistance();
            avgSpeed += workoutList.get(i).getAvgSpeed();
        }

        int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(totalTime);
        int seconds = (int) (TimeUnit.MILLISECONDS.toSeconds(totalTime) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(totalTime)));

        avgDistance = totalDistance / workoutList.size();

        textViewTotalTime.setText(String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds));
        textViewTotalDistance.setText(String.format(Locale.getDefault(), "%.1f km", totalDistance));
        avgSpeed = avgSpeed/workoutList.size();
        textViewAvgSpeed.setText(String.format(Locale.getDefault(), "%.1f km/h", avgSpeed));
        textViewAvgDistance.setText(String.format(Locale.getDefault(), "%.1f km", avgDistance));

    }
}