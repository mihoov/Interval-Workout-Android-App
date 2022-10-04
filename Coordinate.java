package edu.ucdenver.hiitrunner;

public class Coordinate {
    //Coordinate class fields
    private double latitude;
    private double longitude;
    private long time;

    //Constructor
    public Coordinate (double latitude, double longitude, long time ) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = time;
    }

    //gets and sets for Coordinate class
    public double getLatitude(){ return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude;}

    public double getLongitude(){ return longitude;}
    public void setLongitude(double longitude) { this.longitude = longitude;}

    public long getTime() { return time; }
    public void setTime(long time) { this.time = time; }
}
