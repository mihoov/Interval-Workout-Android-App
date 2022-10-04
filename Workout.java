package edu.ucdenver.hiitrunner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Workout {

    private long startTime;
    private long totalTime;
    private double totalDistance;
    private double avgSpeed;
    private long totalHighTime;
    private long totalLowTime;

    public  Workout(long startTime, long totalTime, double totalDistance, double avgSpeed,
                    long totalHighTime, long totalLowTime){
        this.startTime = startTime;
        this.totalTime = totalTime;
        this.totalDistance = totalDistance;
        this.avgSpeed = avgSpeed;
        this.totalHighTime = totalHighTime;
        this.totalLowTime = totalLowTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public double getTotalDistance() {
        return (totalDistance/1000);
    }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public double getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(double avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public void setTotalHighTime(long totalHighTime) { this.totalHighTime = totalHighTime; }

    public long getTotalHighTime() { return totalHighTime; }

    public void setTotalLowTime(long totalLowTime) { this.totalLowTime = totalLowTime; }

    public long getTotalLowTime() { return totalLowTime; }

    public String getDate(){
        Calendar calendar = Calendar.getInstance();
        TimeZone timeZone = calendar.getTimeZone();

        //convert UTC to date and time
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        simpleDateFormat.setTimeZone(timeZone);

        return simpleDateFormat.format(new Date(startTime));
    }

}
