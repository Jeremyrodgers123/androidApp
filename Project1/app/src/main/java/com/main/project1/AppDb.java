package com.main.project1;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.time.LocalDateTime;

import static java.lang.System.exit;

@Database(entities = { UserProfile.class, FitnessProfile.class, StepData.class}, version = 2)
public abstract class AppDb extends RoomDatabase {

    public abstract UserProfileDao GetUserProfileDao();
    public abstract FitnessProfileDao GetFitnessProfileDao();
    public abstract StepDao GetStepDao();

    private static volatile AppDb INSTANCE;

    public static AppDb GetDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDb.class) {
                if (INSTANCE == null) {

                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDb.class, "app_database")
//                            .allowMainThreadQueries() // allow queries on the main thread.
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static class SeedDBAsync extends AsyncTask<Void,Void, Void>{

        @Override
        protected Void doInBackground(Void... v) {
            SeedDatabase();
            return null;
        }
        public static void SeedDatabase() {

            //get database and tables
//            AppDb appDb = GetDatabase(context);
            UserProfileDao userProfileDao = INSTANCE.GetUserProfileDao();
            FitnessProfileDao fitnessProfileDao = INSTANCE.GetFitnessProfileDao();
            StepDao stepDao = INSTANCE.GetStepDao();

            //clear all tables
            INSTANCE.clearAllTables();

            //seed user profiles
            UserProfile userProfile1 = new UserProfile("knownastron", "Jason", "Tran", 26, 0.01,
                        "Saskatoon", "Saskatchewan", "Canada", "C:\\");
            userProfileDao.Insert(userProfile1);

            String userName2 = "adamq";
            UserProfile userProfile2 = new UserProfile("adamq", "Adam", "Quintana", 34, 1,
                        "New Orleans", "Louisiana", "USA", "C:\\");
            userProfileDao.Insert(userProfile2);

            UserProfile userProfile3 = new UserProfile("jrodgers", "Jeremy", "Rodgers", 29, 0.99,
                        "Salt Lake City", "Utah", "USA", "C:\\");
            userProfileDao.Insert(userProfile3);

            //seed fitness profiles
            FitnessProfile fitnessProfile3 = new FitnessProfile(userProfile3, 6, 2, 185, FitnessProfile.FitnessLevel.Sedentary, FitnessProfile.WeightGoal.Lose,1);
            fitnessProfileDao.Insert(fitnessProfile3);

            // seed step data
            StepData stepData1 = new StepData();
            stepData1.TimeStamp = LocalDateTime.now().minusHours(5);
            stepData1.UserName = "knownastron";
            stepData1.StepCount = Float.valueOf(100);
            stepDao.Insert(stepData1);

            StepData stepData2 = new StepData();
            stepData2.TimeStamp = LocalDateTime.now().minusMinutes(1);
            stepData2.UserName = "knownastron";
            stepData2.StepCount = Float.valueOf(120);
            stepDao.Insert(stepData2);

        }

    }

    public static void SeedDBAsync(){
        try{
            new SeedDBAsync().execute().get();
            Log.d("Database", "database should be initialized");
        }catch (Exception e) {
            Log.d("Database", "Something went wrong seeding the database");
            exit(1);
        }
    }

}


