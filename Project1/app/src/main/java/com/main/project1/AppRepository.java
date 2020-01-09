package com.main.project1;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.main.MyAppContext;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.io.File;

import static java.lang.System.exit;


public class AppRepository {

    private static volatile AppRepository INSTANCE;

    private Application mApplication;
    private AppDb mAppDb;
    private RequestQueue mRequestQueue;
    private MutableLiveData<UserProfile> mUserProfile;
    private MutableLiveData<FitnessProfile> mFitnessProfile;
    private MutableLiveData<FitnessProfile> mCurrentFitnessProfile;
    private MutableLiveData<WeatherData> mWeatherData;
    private MutableLiveData<ArrayList<StepData>> mStepData;

    private String mUsername;

    private AppRepository(final Application application){
        mApplication = application;
        mAppDb = AppDb.GetDatabase(application);
        mRequestQueue = VolleySingleton.getInstance(application).getRequestQueue();
        Init();
    }

    public static synchronized AppRepository getInstance(final Application application) {
        if (INSTANCE == null) {
            INSTANCE = new AppRepository(application);
            AppDb.SeedDBAsync();
        }
        return INSTANCE;
    }

    public void Init() {
        mUserProfile = new MutableLiveData<>();
        mFitnessProfile = new MutableLiveData<>();
        mCurrentFitnessProfile = new MutableLiveData<>();
        mWeatherData = new MutableLiveData<>();
        mStepData = new MutableLiveData<>();
    }

    //User Profile

    private class getUserProfileAsync extends AsyncTask<String, Void, UserProfile> {
        @Override
        protected UserProfile doInBackground(final String... params) {
            String username = params[0];
            UserProfile userProfile = mAppDb.GetUserProfileDao().Get(username);
            return userProfile;
        }

        @Override
        protected void onPostExecute(UserProfile _userProfile) {
            UserProfile userProfile = _userProfile;
            mUserProfile.setValue(userProfile);
            Log.d("UserProfile", "userProfile set");
        }
    }

    private class insertUserProfileAsync extends AsyncTask<UserProfile, Void, Void> {
        private UserProfile userProfile;

        @Override
        protected Void doInBackground(final UserProfile... params) {
            userProfile = params[0];
            mAppDb.GetUserProfileDao().Insert(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void v){
            mUserProfile.setValue(userProfile);
            backupDB();
        }
    }

    private class updateUserProfileAsync extends AsyncTask<UserProfile, Void, Void> {
        private UserProfile userProfile;
        @Override
        protected Void doInBackground(final UserProfile... params) {
            userProfile = params[0];
            mAppDb.GetUserProfileDao().Update(params[0]);
            try{
                backupDB();
            } catch (Exception e) {
                Log.d("AWS", "Unable to backup db");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void v){
            mUserProfile.setValue(userProfile);
            try{
                backupDB();
            } catch (Exception e) {
                Log.d("AWS", "Unable to backup db");
            }
        }
    }

    public MutableLiveData<UserProfile> getUserProfile() {
        return mUserProfile;
    }




    public MutableLiveData<UserProfile> setUserProfile(String userName) {
        try{
            new getUserProfileAsync().execute(userName).get();
        } catch(Exception e){
            Log.d("UserProfile", "User profile not available error");
            exit(1);
        }
        return mUserProfile;
    }

    public void insertUserProfile(UserProfile userProfile) {
        new insertUserProfileAsync().execute(userProfile);
    }


    public void updateUserProfile(UserProfile userProfile) {
        new updateUserProfileAsync().execute(userProfile);
    }

    public Boolean doesUserProfileExist(String userName) {
        UserProfile userProfile;
        try{
           userProfile = new getUserProfileAsync().execute(userName).get();
            Log.d("UserProfile", "finished asynce task");
        } catch(Exception e){
            Log.d("UserProfile", "User profile not available error");
            exit(1);
            return null;
        }

        Log.d("UserProfile", "userProfile getting ready to return");
        return userProfile != null;
    }

    //Fitness Profile

    private class getFitnessProfileAsync extends AsyncTask<String, Void, FitnessProfile> {
        @Override
        protected FitnessProfile doInBackground(final String... params) {
            String username = params[0];
            FitnessProfile fitnessProfile = mAppDb.GetFitnessProfileDao().Get(username);
            return fitnessProfile;
        }

        @Override
        protected void onPostExecute(FitnessProfile fitnessProfile) {
            FitnessProfile fitnessProfile1 = fitnessProfile;
            mFitnessProfile.setValue(fitnessProfile1);
        }
    }

    private class insertFitnessProfileAsync extends AsyncTask<FitnessProfile, Void, Void> {
        private FitnessProfile fitnessProfile;

        @Override
        protected Void doInBackground(final FitnessProfile... params) {
            fitnessProfile = params[0];
            mAppDb.GetFitnessProfileDao().Insert(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void v){
            mFitnessProfile.setValue(fitnessProfile);
//            backupDB();
        }
    }

    private class updateFitnessProfileAsync extends AsyncTask<FitnessProfile, Void, Void> {
        private FitnessProfile fitnessProfile;
        @Override
        protected Void doInBackground(final FitnessProfile... params) {
            fitnessProfile = params[0];
            mAppDb.GetFitnessProfileDao().Update(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void v){
            mFitnessProfile.setValue(fitnessProfile);
            try{
                backupDB();
            } catch (Exception e) {
                Log.d("AWS", "Unable to backup db");
            }
        }
    }

    public LiveData<FitnessProfile> getFitnessProfile() {
        return mFitnessProfile;
    }

    public LiveData<FitnessProfile> setFitnessProfile(String userName) {
        new getFitnessProfileAsync().execute(userName);
        return mFitnessProfile;
    }

    public void insertFitnessProfile(FitnessProfile fitnessProfile) {
        new insertFitnessProfileAsync().execute(fitnessProfile);
    }

    public void updateFitnessProfile(FitnessProfile fitnessProfile) {
       new updateFitnessProfileAsync().execute(fitnessProfile);
    }

    public Boolean doesFitnessProfileExist() {
        try{
            FitnessProfile fitnessProfile = mFitnessProfile.getValue();
            if(fitnessProfile != null) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e){
            return false;
        }
    }

    public MutableLiveData<FitnessProfile> setCurrentFitnessProfile(FitnessProfile fitnessProfile) {
        mCurrentFitnessProfile.setValue(fitnessProfile);
        return mCurrentFitnessProfile;
    }

    public MutableLiveData<FitnessProfile> initCurrentFitnessProfile() {
        FitnessProfile fitnessProfile = new FitnessProfile();
        mCurrentFitnessProfile.setValue(fitnessProfile);
        return mCurrentFitnessProfile;
    }

    public MutableLiveData<FitnessProfile> getCurrentFitnessProfile() {
        return mCurrentFitnessProfile;
    }

//    public void updateCurrentFitnessProfile(FitnessProfile fitnessProfile) {
//        mAppDb.GetFitnessProfileDao().Update(fitnessProfile);
//        mCurrentFitnessProfile.setValue(fitnessProfile);
//    }

    //Weather Data
    public MutableLiveData<WeatherData> getWeatherData() {
        return mWeatherData;
    }

    public void setWeatherData(String city, String countryCode) {
        if(city == null || countryCode == null) {
            return;
        }
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.openweathermap.org")
                .appendPath("data")
                .appendPath("2.5")
                .appendPath("weather")
                .appendQueryParameter("q", city+","+countryCode)
                .appendQueryParameter("APPID", "e3f129fcf04e1e77a9b2c5aec2a7f393")
                .appendQueryParameter("units", "imperial");
        String url = builder.build().toString();
        Log.d("weather", "url: "+ url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("weather", "onResponse: "+ response.toString());
                        try{
                            WeatherData weatherData = JSONWeatherUtils.getWeatherData(response);
                            Log.d("weather", "weatherData: "+ weatherData.toString());
                            mWeatherData.setValue(weatherData);
                        } catch (JSONException e) {
                            Log.d("weather", "json parsing error: "+ e);
                        }
                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("weather", "onerror: "+ error);
                        mWeatherData.setValue(null);
                    }

                });
        mRequestQueue.add(jsonObjectRequest);
    }
  
    private class getStepDataAsync extends AsyncTask<String, Void, ArrayList<StepData>> {

        @Override
        protected ArrayList<StepData> doInBackground(final String... params) {
            String username = params[0];
            mUsername = params[0];
            ArrayList<StepData> steps = new ArrayList<>(mAppDb.GetStepDao().Get(username));
            return steps;
        }

        @Override
        protected void onPostExecute(ArrayList<StepData> _steps) {
            ArrayList<StepData> steps= _steps;
            mStepData.setValue(_steps);
            Log.d("StepData", "Step data set");
        }
    }

    private class insertStepDataAsync extends AsyncTask<StepData, Void, Void> {

        @Override
        protected Void doInBackground(final StepData... params) {
            mAppDb.GetStepDao().Insert(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void v){
            // may need to do something here....
        }
    }

    public MutableLiveData<ArrayList<StepData>> getStepData() {
        return mStepData;
    }


    public MutableLiveData<ArrayList<StepData>> setStepData(String userName) {
        try{
            new getStepDataAsync().execute(userName).get();
        } catch(Exception e){
            Log.d("UserProfile", "User profile not available error");
            exit(1);
        }
        return mStepData;
    }

    public void insertStepData(StepData stepData) {
        new insertStepDataAsync().execute(stepData);
    }

    public Boolean doesStepDataExist(String userName) {
        ArrayList<StepData> stepData;
        try{
            stepData = new getStepDataAsync().execute(userName).get();
            Log.d("StepData", "finished asynce task");
        } catch(Exception e){
            Log.d("StepData", "StepData not available error");
            exit(1);
            return null;
        }

        Log.d("StepData", "StepData getting ready to return");
        return stepData != null;
    }
  
    //Sync DB
    BasicAWSCredentials credentials = new BasicAWSCredentials(BuildConfig.ACCESS_KEY, BuildConfig.SECRET_KEY);
    AmazonS3Client s3Client = new AmazonS3Client(credentials);

    void backupDB () {
        final Context context = MyAppContext.getAppContext();
        String currentDBPath = context.getDatabasePath("app_database").getAbsolutePath();
        File tempDbFile = new File(currentDBPath);

        TransferUtility transferUtility =
                TransferUtility.builder()
                        .context(context)
                        .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                        .s3Client(s3Client)
                        .build();
        TransferObserver uploadObserver =
                transferUtility.upload("msdS3/" + "dbBackup", tempDbFile);

        uploadObserver.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED == state) {
//                    Toast.makeText(context, "Upload Completed!", Toast.LENGTH_SHORT).show();
                    Log.d("AWS", "UPLOAD SUCCESSFUL");
                } else if (TransferState.FAILED == state) {

                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                float percentDonef = ((float)bytesCurrent/(float)bytesTotal) * 100;
                int percentDone = (int)percentDonef;
            }

            @Override
            public void onError(int id, Exception ex) {
                // Handle errors
            }
        });

        }
}
