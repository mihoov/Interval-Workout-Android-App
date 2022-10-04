package edu.ucdenver.hiitrunner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class NewWorkoutActivity extends AppCompatActivity {

    //create variables for text Inputs
    private EditText inputHighIntervalMinutes;
    private EditText inputHighIntervalSeconds;
    private EditText inputLowIntervalMinutes;
    private EditText inputLowIntervalSeconds;
    private EditText inputLoops;

    // create variables for buttons
    private Button buttonCancel;
    private Button buttonStartWorkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_workout);

        //get references for EditTexts
        inputHighIntervalMinutes = findViewById(R.id.editTextHighIntervalMinutes);
        inputHighIntervalSeconds = findViewById(R.id.editTextHighIntervalSeconds);
        inputLowIntervalMinutes = findViewById(R.id.editLowHighIntervalMinutes);
        inputLowIntervalSeconds = findViewById(R.id.editTextLowIntervalSeconds);
        inputLoops = findViewById(R.id.editTextLoops);

        //set references for buttons
        buttonCancel = findViewById(R.id.buttonCancel);
        buttonStartWorkout = findViewById(R.id.buttonStartWorkout);

        //set OnClickListeners for Buttons
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonStartWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToTimerActivity();
                finish();
            }
        });


    }

    private void changeToTimerActivity(){
        // create variables for user input
        int highMinutes, highSeconds, lowMinutes, lowSeconds, numLoops;
        int highMillis, lowMillis;

        //get minutes and seconds for high-intensity interval
        try{
            highMinutes = Integer.parseInt(inputHighIntervalMinutes.getText().toString());
        }
        catch (NumberFormatException e){
            highMinutes = 0;
        }

        try{
            highSeconds = Integer.parseInt(inputHighIntervalSeconds.getText().toString());
        }
        catch (NumberFormatException e){
            highSeconds = 0;
        }

        //get minutes and seconds for low-intensity interval
        try{
            lowMinutes = Integer.parseInt(inputLowIntervalMinutes.getText().toString());
        }
        catch (NumberFormatException e){
            lowMinutes = 0;
        }

        try{
            lowSeconds = Integer.parseInt(inputLowIntervalSeconds.getText().toString());
        }
        catch (NumberFormatException e){
            lowSeconds = 0;
        }

        //calculate the number of total milliseconds in each interval
        highMillis = (highMinutes * 60000) + (highSeconds * 1000);
        lowMillis = (lowMinutes * 60000) + (lowSeconds * 1000);

        //get number of loops
        try {
             numLoops = Integer.parseInt(inputLoops.getText().toString());
        }
        catch (NumberFormatException e){
            numLoops = 1;
        }

        Intent intent = new Intent(getApplicationContext(), TimerActivity.class);
        intent.putExtra("num_loops", numLoops);
        intent.putExtra("high_millis", highMillis);
        intent.putExtra("low_millis", lowMillis);
        startActivity(intent);
    }

}
