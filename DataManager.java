package edu.ucdenver.hiitrunner;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;

public class DataManager {
    private static DataManager dataManagerInstance;
    private SQLiteDatabase db;

    public static synchronized DataManager getDataManagerInstance(Context context){
        if(dataManagerInstance == null){
            dataManagerInstance = new DataManager(context.getApplicationContext());
       }

        return dataManagerInstance;
    }

    private DataManager(Context context) {
        dbHelper helper = new dbHelper(context);
        db = helper.getWritableDatabase();
    }

    public void insertNewWorkout(ArrayList<Coordinate> coordinateList, long highTime, long lowTime) {

        //variables for total time in high-intensity and low-intensity
        long totalHighTime = highTime;
        long totalLowTime = lowTime;
        //calculate start time and total time. Store as strings.
        String startTimeStr = Long.toString(coordinateList.get(0).getTime());
        String newTableName = "time" + startTimeStr;

        Long totalTime = coordinateList.get(coordinateList.size() - 1).getTime() - coordinateList.get(0).getTime()  ;
        String totalTimeStr = Long.toString(totalTime);

        //calculate total distance
        float totalDistance = 0;
        float distanceResults[];

        distanceResults = new float[1];

        for (int i = 1; i < coordinateList.size(); i++) {

            Location.distanceBetween(
                    coordinateList.get(i - 1).getLatitude(),
                    coordinateList.get(i - 1).getLongitude(),
                    coordinateList.get(i).getLatitude(),
                    coordinateList.get(i).getLongitude(),
                    distanceResults);

            totalDistance += distanceResults[0];
        }

        String totalDistanceStr = Float.toString(totalDistance);

        //calculate avgSpeed

        float avgSpeed = (float) ((3600 * totalDistance) / totalTime); //Average speed in km/h
        String avgSpeedStr = Float.toString(avgSpeed);

        //insert new workout into workouts table of database
        String query = "insert into workouts "
                + "(start_time, total_time, total_distance, avg_speed, total_high_time, total_low_time) values "
                + "(" + startTimeStr +
                ", " + totalTimeStr +
                ", " + totalDistanceStr +
                ", " + avgSpeedStr +
                ", " + totalHighTime +
                ", " + totalLowTime + ")";

        //try inserting workout into table row
        try {
            db.execSQL(query);
        } catch (SQLException e) {
            Log.i("info", "In DataManager insert Method trying to add row to workouts.");
            Log.i("info", e.getMessage());
        }

        //create new table in database to store coordinates of the workout
        String newTable = "create table " + newTableName + " ("
                + "time_stamp bigint primary key not null, "
                + "lattitude real, "
                + "longitude real)";

        try{
            db.execSQL(newTable);
        }
        catch (SQLException e){
            Log.i("info", "In DatabaseManager insert method create new coordinate table.");
            Log.i("info", e.getMessage());
        }

        //populate table with list of coordinates
        String timeStampStr;
        String lattitudeStr;
        String longitudeStr;

        //for loop inserts each coordinate pair into table as a row
        for (int i = 0; i < coordinateList.size(); i++){

            timeStampStr = Long.toString(coordinateList.get(i).getTime());
            lattitudeStr = Double.toString(coordinateList.get(i).getLatitude());
            longitudeStr = Double.toString(coordinateList.get(i).getLongitude());

            query = "insert into " + newTableName
                    + " (time_stamp, lattitude, longitude) values "
                    + "(" + timeStampStr +
                    ", " + lattitudeStr +
                    ", " + longitudeStr + " )";

            //try inserting coordinates into table row
            try{
                db.execSQL(query);
            }
            catch (SQLException e){
                Log.i("info", "In DatabaseManager insert method attempt adding row to coordinate table");
                Log.i("info", e.getMessage());
            }

        }// end for loop
    }// end insert Method


    public Cursor selectAllWorkouts(){
        Cursor cursor = null;
        String query = "select * from workouts order by start_time";

        try{
            cursor = db.rawQuery(query, null);
        }
        catch (SQLException e){
            Log.i("info", "In DatabaseManger selectAllWorkouts Method.");
            Log.i("info", e.getMessage());
        }

        return cursor;
    }


    public Cursor selectAllCoordinates(String tableName){
        Cursor cursor = null;
        String query = "select * from " + tableName + " order by time_stamp";

        try{
            cursor = db.rawQuery(query, null);
        }
        catch(SQLException e){
            Log.i("info", "In DatabaseManager selectAllCoordinates Method");
            Log.i("info", e.getMessage());
        }

        return cursor;
    }

    public void delete(long id){

        String query = "delete from workouts where start_time = " + Long.toString(id);

        try{
            db.execSQL(query);
        }
        catch (SQLException e){
            Log.i("info", "In DataManager delete method");
            Log.i("info", e.getMessage());
        }
    } // end delete method

    private class dbHelper extends SQLiteOpenHelper {
        private Context mContext;

        public dbHelper (Context context) {
            super(context, "HIIT_Workout_database", null, 1);
            mContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String newTable = "create table workouts ("
                    + "start_time bigint primary key not null, "
                    + "total_time bigint,"
                    + "total_distance real, "
                    + "avg_speed real, "
                    + "total_high_time int, "
                    + "total_low_time int)";

            try{
                db.execSQL(newTable);
            }
            catch(SQLException e){
                Log.i("info", "In dbHelper onCreate method");
                Log.i("info", e.getMessage());

            }


        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //Intentionally left blank
        }
    }
}
