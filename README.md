# Interval-Workout-Android-App

The HIIT Runner app is an application that the user will use to track and record HIIT workouts. HIIT
stands for High-Intensity Interval Training. It consists of short periods of vigorous exercise to get the
heart-rate up interspersed with longer, low-level exercise for recovery. For instance, a workout could
consist of a 30 second sprint followed by a two minute jog. This would be repeated multiple times
throughout the training session.
The HIIT Runner app will allow the user to select the length of high-intensity intervals and low-intensity
intervals. It will also allow the user to choose how many times to repeat these intervals. Then, when the
user decides to start the training session the app will give notifications for when to begin an interval. So,
in practice, the app will give an audio cue for the user to begin a period of high-intensity running. It will
allow the selected time to pass and then it will give an audio cue when the user should transition from
high-intensity to low-intensity. It will repeat this process for the number of repetitions the user has
selected.
Since this is an app primarily for running, it will also track the user’s location during the run using Google
Maps. This can be emulated in Android Studio with the correct plugins. The app will track the path the
user took during the workout. It will then be able to calculate the total distance traveled during the
workout and the average speed of the runner during the workout.
The app will also have a functionality to store past workouts so that the user can access the data on a later
date. The app will calculate statistics for each workout, such as total distance ran, average pace, etc. The
user will be able to view a history of workouts, which will be tracked by the date the workout was
completed. Additionally the app will be able to calculate progress for the user, such as the average speed
of the runner over time so that the user can see progress over a long period of time, e.g. overall several
months of working out. The app will also provide statistics, such as the total distance ran during and the
average speed of the runner across all workouts.
Requirement Specifications
1. Main Menu
a. The activity will have three buttons. One to start a new workout, one to view workout
history, and one to view statistics.
b. Clicking each button will load the corresponding activity.
2. HIIT Workout Setup Activity
a. The Activity will have editTexts to take user input for length of high-intensity and
low-intensity intervals, and the number of rounds for the workout.
b. The inputs will allow the user to enter in the times as minutes and seconds.
c. It will have a button to start the workout.
3. HIIT workout timer activity
a. The Activity will be activated when the user hits the start button in the HIIT Workout
Setup Activity.
b. It will have a countdown timer to display the remaining time in the workout.
c. It will have a text view to display what type of interval the user should be performing at
the moment (high or low-intensity.)
d. It will play audio to indicate when the workout is transitioning from one type of interval
to the other.
e. It will have buttons to pause the workout or to cancel the workout early.
f. It will track the location of the user during the workout and save the information to a
database to be accessed later.
g. It will save completed workouts to a database so the app can calculate statistics for the
user.
4. Workout History Activity
a. The Activity will display a list of the user’s past workouts in a RecyclerView.
b. Each workout will have an onClick method to display the individual workout details.
c. It will display the workout time, distance, average speed, total time of high-intensity
intervals, and total time of low-intensity intervals in the individual workout details.
d. It will provide a button to delete workouts from the list.
e. The Activity will have a button to return to the main menu.
5. Workout Statistics Activity
a. The Activity will access the database of past workouts to aggregate statistics for the user.
b. It will calculate total time for all workouts, high-intensity intervals, low-intensity
intervals, total distance ran, average distance ran in workout, and average speed.
c. It will have textviews to display the statistics.
Technical Description
Main Activity
MainActivity implements a simple menu screen for the user. There are three buttons and for each button
has the setOnClickListener method implemented. The buttons allow the user to navigate to the different
parts of the app. Each button calls a function that creates a new Intent and then calls startActivity on the
intent that causes the app to run the corresponding activity.
NewWorkoutActivity
The NewWorkoutActivity allows the user to enter the time for high and low intervals, set the number of
loops, and then a button to start the workout timer. There is also a button to cancel the new workout which
returns the user to the main menu.
The buttons have setOnCickListener methods. When the Start Workout button is clicked, the java file
takes data from EditTexts. Each interval has EditTexts for minutes and seconds of the workout. Error
handling is implemented when gathering the inputs. If the user leaves the EditTexts blank or tries to input
something other than a number, then the value is set to 0 by default. The number of loops is set to 1 by
default. Although the user will input the time in minutes in seconds, the program converts the time to
milliseconds. Finally, the last action that Start Button initiates is to call the intent for the TimerActivity.
StartWorkoutActivity uses the Inent.putExtra method to pass the time, in milliseconds, and the number of
loops to the TimerActivity.
TimerActivity
The TimerActivity completes a number of different functions. The runTimer method manages the display
and tracking of the time remaining. It updates a TextView to display the type of interval. Additionally
there are two MediaPlayer objects to play a beeping sound when the type of interval is switched. It calls a
function to create and start a CountDownTimer each time a new interval is started. The
CountDownTimer.onTick method updates a TextView every second to display the correct time remaining.
Additionally the TimerActivity uses Android Location Services to track the location of the user during a
run. The activity requests the coordinates of the location every 5 seconds along with a time stamp. It
stores the time stamp, latitude, and longitude returned in an ArrayList. At the end of the workout the
ArrayList is used to calculate the total time of the workout, the distance of the workout, and the average
speed of the user during the workout.
When testing the app, for any distance or speed to register, the location of the device must be changed in
the emulator while the timer is running. Otherwise, the location coordinates will be exactly the same at
every timestamp and the app will not detect any movement. Thus, the distance and speed calculations will
return 0. There are two ways to easily simulate movement in the emulator. First, when the Android Virtual
Device is up and running, you must select the additional functions button and pull up location. Then,
while the timer is running you can either move the location of the device by clicking on different points
on the map, or a route can be simulated by uploading a gpx file with timestamps for each coordinate and
then clicking the run route button.
The TimerActivity uses an SQLite database to store information about the workout upon completion. It
uses the database through a datamanager.
DataManager
The DataManager class has a private SQLiteOpenHelper class to create a database. Upon initialization,
the helper class creates a table to store workouts. The DataManager is a singleton class to allow the
database to be shared across multiple activities. The main difference is that the constructor for
DataManger is private. Activities must call a getDataManagerInstance method to get a DataManager
object. The method checks if a DataManager instance has been created in another activity, and if so passes
that instance to the calling activity. Only if there is no existing DataManager instance does the method
call the constructor to create a new DataManager.
The DataManager has methods to insert new workouts and to delete workouts. The insertWorkout method
adds a row to the workout table. It also creates a new table, using the start time of the new workout as the
table title, to store the ArrayList of coordinate values for the workout. The delete method removes the
corresponding row of the workout table and deletes the table of coordinates from the database.
HistoryActivity
The HistoryActivity accesses the SQLite database to get the information for past workouts. It loads the
information with the use of a Cursor and stores it in an ArrayList. The HistoryActivity makes use of two
additional classes defined in separate java files, Workout and WorkoutAdapter. The Workout class is an
object that stores information about a individual workout with appropriate get and set methods. The
WorkoutAdapter class creates a RecyclerView to display the list of past workouts. WorkoutAdapter also
implements an onClick method that shows a fragment with details of the the individual workout. The
dialog fragment allows the user to delete the workout from history, in which case HistoryActivity uses the
DataManager class to delete the workout from the database and reloads the data into the ArrayList.
In the database all time variables are stored in milliseconds and all variables related to distance are in
meters. In order the display this information to the user in a way that is useful the HistoryActivity and
ViewWorkoutDialog converts these to units that are germane to human users. Milliseconds are converted
to minutes and seconds. Meters are converted to kilometers and formatted to one decimal point. Speed is
calculated in kilometers per hour and formatted to one decimal point.
StatisticsActivity
The StatisticsActivity aggregates data across all workouts. It also accesses the same DataManager
instance as HistoryActivity and TimerActivity. It loads the information from the workouts by use of a
cursor. It calculates total time of all workouts, total distance, average speed, and average distance of all
workouts. It displays the information on the screen using TextViews. The time variables from the database
are converted to minutes and seconds for output. The total distance and average distance is converted to
kilometers. The average speed is converted to kilometers per hour.
The StatisticsActivity includes a Main Menu button with an onClick method that will return the user to
the main menu.
