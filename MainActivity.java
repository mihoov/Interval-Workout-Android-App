package edu.ucdenver.hiitrunner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //variable to for button references
    private Button buttonNewWorkout;
    private Button buttonHistory;
    private Button buttonStatistics;

    private DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get Button IDs
        buttonNewWorkout = findViewById(R.id.newWorkoutButton);
        buttonHistory = findViewById(R.id.historyButton);
        buttonStatistics = findViewById(R.id.statButton);

        //Set onclickListeners for buttons
        buttonNewWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                changeToNewWorkoutActivity();
            }
        });


        buttonHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToHistoryActivity();
            }
        });

        buttonStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToStatisticsActivity();
            }
        });

    }

    private void changeToNewWorkoutActivity(){
        Intent intent = new Intent(this, NewWorkoutActivity.class);
        startActivity(intent);
    }

    private void changeToHistoryActivity(){
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }

    private void changeToStatisticsActivity(){
        Intent intent = new Intent(this, StatisticsActivity.class);
        startActivity(intent);
    }
}