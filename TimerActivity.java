package edu.ucdenver.hiitrunner;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class TimerActivity extends AppCompatActivity {

    //create dataManager
    public  DataManager dataManager;
    //create variables for locations tracking
    //Most of the code for the location tracking is based off of
    //the code provided in the class textbook:
    // Zybooks CSCI 4211/5211: Mobile Computing and Programming
    private final String TAG = "TimerActivity";
    private final int REQUEST_LOCATION_PERMISSIONS = 0;

    private FusedLocationProviderClient mClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;

    private ArrayList<Coordinate> coordinateList;

    //create mediaplayers for audio
    MediaPlayer highMP;
    MediaPlayer lowMP;

    //create timer
    private CountDownTimer countDownTimer;

    //create variables for timer
    private long timeRemaining = 10000;
    private boolean timerIsRunning;
    private int numLoops;
    private int numHighRemaining;
    private int numLowRemaining;
    private long highIntensityTime;
    private long lowIntensityTime;
    private long totalHighTime;
    private long totalLowTime;

    //create variable for textView
    private TextView displayIntervalType;
    private TextView displayTimeRemaining;

    //create variables for buttons
    private Button buttonPauseResume;
    private Button buttonCancelTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        //create dataManager
        dataManager = DataManager.getDataManagerInstance(getApplicationContext());

        //create mediaPlayers
        highMP = MediaPlayer.create(getApplicationContext(), R.raw.start_high_beep);
        lowMP = MediaPlayer.create(getApplicationContext(), R.raw.start_low_beep);

        if (hasLocationPermission()) {
            trackLocation();
        }

        //set variables for tracking list of coordinates
        coordinateList = new ArrayList<Coordinate>();

        //get extras passed to intent
        Bundle extras = getIntent().getExtras();

        //set variables for timer with extras
        if (extras != null){
            numLoops = extras.getInt("num_loops");
            highIntensityTime = (long) (extras.getInt("high_millis") + 1000);
            lowIntensityTime = (long) (extras.getInt("low_millis") + 1000);
        }

        //set variables for timer loop
        numHighRemaining = numLoops;
        numLowRemaining = numLoops;
        totalHighTime = highIntensityTime * numLoops;
        totalLowTime = lowIntensityTime * numLoops;

        //set references for textView
        displayIntervalType = findViewById(R.id.textViewIntervalType);
        displayTimeRemaining = findViewById(R.id.textViewTimeRemaining);

        //set references for buttons
        buttonCancelTimer = findViewById(R.id.buttonCancelTimer);
        buttonPauseResume = findViewById(R.id.buttonPauseResume);

        //set onClickListeners for buttons
        buttonPauseResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timerIsRunning){
                    pauseTimer();
                }
                else{
                    startTimer();
                }
            }
        });

        buttonCancelTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }// end onCreate

    @Override
    protected void onStart() {
        super.onStart();
        runTimer();

    }

    //The code for trackLocation, location callback, onPause, onResume, hasLocationPermission,
    // and onRequestPermissionsResult is borrowed from the online course textbook in Zybooks:
    // CSCI 4211/5211: Mobile Computing and Programming
    @SuppressLint("MissingPermission")
    private void trackLocation() {
        // Create location request
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(3000);
        mLocationRequest.setFastestInterval(3000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Create location callback
        //get location coordinates and time, then add the info to ArrayList
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    for (Location location : locationResult.getLocations()) {
                        Coordinate coordinateToAdd = new Coordinate(
                                location.getLatitude(),
                                location.getLongitude(),
                                location.getTime());

                        addCoordinateToList(coordinateToAdd);

                    }
                }
            }
        };

        mClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mClient.removeLocationUpdates(mLocationCallback);
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onResume() {
        super.onResume();

        if (hasLocationPermission()) {
            mClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        }
    }

    private boolean hasLocationPermission() {
        // Request fine location permission if not already granted
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSIONS);

            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Find the location when permission is granted
        if (requestCode == REQUEST_LOCATION_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                trackLocation();
            }
        }
    }

    private void startTimer(){
        timerIsRunning = true;
        buttonPauseResume.setText(R.string.pauseString);

        countDownTimer = new CountDownTimer(timeRemaining, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemaining = millisUntilFinished;
                updateDisplayTimeRemaining();


            }

            @Override
            public void onFinish() {
                countDownTimer.cancel();
                timerIsRunning = false;
                runTimer();
            }
        }.start();
    }

    private void pauseTimer(){
        countDownTimer.cancel();
        timerIsRunning = false;
        buttonPauseResume.setText(R.string.resumeString);
    }

    private void updateDisplayTimeRemaining(){
        int minutesRemaining =  (int) TimeUnit.MILLISECONDS.toMinutes(timeRemaining);
        int secondsRemaining = (int) (TimeUnit.MILLISECONDS.toSeconds(timeRemaining) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeRemaining)));

        displayTimeRemaining.setText(String.format(Locale.getDefault(),"%02d:%02d", minutesRemaining, secondsRemaining));
    }

    private void runTimer(){

        if((numHighRemaining > 0) && (numHighRemaining >= numLowRemaining)){
            //change display of interval type
            displayIntervalType.setText(R.string.displayHighIntensityString);
            displayIntervalType.setTextColor(Color.RED);
            highMP.start();

            //start timer for interval
            timeRemaining = highIntensityTime;
            startTimer();
            numHighRemaining -= 1;
        }
        else if(numLowRemaining > 0){
            //change display for interval type
            displayIntervalType.setText(R.string.displayLowIntensityString);
            displayIntervalType.setTextColor(Color.GREEN);
            lowMP.start();

            //start timer for interval
            timeRemaining = lowIntensityTime;
            startTimer();
            numLowRemaining -= 1;
        }
        else {
            endWorkoutSession();
        }
    }

    //function to add coordinate to ArrayList
    private void addCoordinateToList(Coordinate coordinate){
        coordinateList.add(coordinate);
    }

    private void endWorkoutSession(){
        //remove location services
        mClient.removeLocationUpdates(mLocationCallback);

        /*for (int i = 0; i < coordinateList.size(); i++){
            lattitude = coordinateList.get(i).getLatitude();
            longitude = coordinateList.get(i).getLongitude();
            time = coordinateList.get(i).getTime();

            Log.i(TAG, "Lattitude: " + lattitude + " Longitude: " + longitude + " Time: " + time);
        }
         */

        //insert the complete workout into the database
        dataManager.insertNewWorkout(coordinateList, totalHighTime, totalLowTime);
        finish();
    }
}
