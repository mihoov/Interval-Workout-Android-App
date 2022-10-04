package edu.ucdenver.hiitrunner;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    private ArrayList<Workout> workoutList;
    RecyclerView recyclerView;
    WorkoutAdapter workoutAdapter;
    private DataManager dataManager;
    private Button buttonMainMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        dataManager = DataManager.getDataManagerInstance(getApplicationContext());

        workoutList = new ArrayList<Workout>();
        recyclerView = findViewById(R.id.recyclerView);
        workoutAdapter = new WorkoutAdapter(this, workoutList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(workoutAdapter);

        buttonMainMenu = findViewById(R.id.buttonMainMenuFromHist);
        buttonMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadWorkouts();
    }

    public void loadWorkouts(){
        Cursor cursor =dataManager.selectAllWorkouts();
        int workoutCount = cursor.getCount();

        if(workoutCount > 0){
            workoutList.clear();

            while(cursor.moveToNext()){
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

        workoutAdapter.notifyDataSetChanged();
    }

    public void showWorkout(int workoutToShow){
        ViewWorkoutDialog viewWorkoutDialog = new ViewWorkoutDialog();
        viewWorkoutDialog.sendWorkout(workoutList.get(workoutToShow));
        viewWorkoutDialog.show(getSupportFragmentManager(), " ");


    }

    public void deleteWorkout(Workout workout){
        long id = workout.getStartTime();

        dataManager.delete(id);
        loadWorkouts();
    }
}