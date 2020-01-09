package com.main.project1;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppViewModel extends AndroidViewModel {

    private static List<DashboardData.Item> DASHBOARD_ITEMS = Arrays.asList(
            new DashboardData().new Item("HIKES", R.drawable.hiking),
            new DashboardData().new Item("WEATHER", R.drawable.weather),
            new DashboardData().new Item("FITNESS PROFILE", R.drawable.fitnessicon),
            new DashboardData().new Item("STEP COUNTER", R.drawable.boots),
            new DashboardData().new Item("SWITCH USER", R.drawable.defaultdualusers)
    );

    private AppRepository mAppRepository;
    private MutableLiveData<UserProfile> mUserProfile;
    private LiveData<FitnessProfile> mFitnessProfile;
    private MutableLiveData<WeatherData> mWeatherData;
    private LiveData<ArrayList<StepData>> mStepDataAL;


    public AppViewModel(Application application){
        super(application);
        mAppRepository = AppRepository.getInstance(application);
        mUserProfile = mAppRepository.getUserProfile();
        mFitnessProfile = mAppRepository.getFitnessProfile();
        mWeatherData = mAppRepository.getWeatherData();
        mStepDataAL = mAppRepository.getStepData();
    }

    public void Init() {
        mAppRepository.Init();
        mUserProfile = mAppRepository.getUserProfile();
        mFitnessProfile = mAppRepository.getFitnessProfile();
        mWeatherData = mAppRepository.getWeatherData();
        mStepDataAL = mAppRepository.getStepData();
    }

    //User Profile methods
    public LiveData<UserProfile> setUserProfile(String userName) {
        return mAppRepository.setUserProfile(userName);
    }

    public void insertUserProfile(UserProfile userProfile) {
        mAppRepository.insertUserProfile(userProfile);
    }

    public void updateUserProfile(UserProfile userProfile) {
        mAppRepository.updateUserProfile(userProfile);
    }

    public List<DashboardData.Item> getDashboardItems() {
        return  DASHBOARD_ITEMS;
    }

    public LiveData<UserProfile> getUserProfile() {
        return mUserProfile;
    }

    public Boolean doesUserProfileExist(String userName) {
        return mAppRepository.doesUserProfileExist(userName);
    }


    //FITNESS PROFILE

    public LiveData<FitnessProfile> setFitnessProfile(String userName) {
        return mAppRepository.setFitnessProfile(userName);
    }

    public LiveData<FitnessProfile> getFitnessProfile() {
        return mAppRepository.getFitnessProfile();
    }

    public void insertFitnessProfile(FitnessProfile fitnessProfile) {
        mAppRepository.insertFitnessProfile(fitnessProfile);
    }

    public void updateFitnessProfile(FitnessProfile fitnessProfile) {
        mAppRepository.updateFitnessProfile(fitnessProfile);
    }

    public Boolean hasExistingFitnessProfile() {
        return mAppRepository.doesFitnessProfileExist();
    }

    public LiveData<FitnessProfile> setCurrentFitnessProfile(FitnessProfile fitnessProfile) {
        return mAppRepository.setCurrentFitnessProfile(fitnessProfile);
    }

    public LiveData<FitnessProfile> initCurrentFitnessProfile() {
        return mAppRepository.initCurrentFitnessProfile();
    }

    public LiveData<FitnessProfile> getCurrentFitnessProfile() {
        return mAppRepository.getCurrentFitnessProfile();
    }
//
//    public void updateCurrentFitnessProfile(FitnessProfile fitnessProfile) {
//        mAppRepository.updateCurrentFitnessProfile(fitnessProfile);
//    }

    // WEATHER DATA

    public LiveData<WeatherData> getWeatherData() {
        return mAppRepository.getWeatherData();
    }

    public void setLocation(String city, String countryCode) {
        mAppRepository.setWeatherData(city, countryCode);
    }

    // STEP DATA
    public LiveData<ArrayList<StepData>> setStepData(String username) {
        return mAppRepository.setStepData(username);
    }

    public LiveData<ArrayList<StepData>> getStepData() {
        return mAppRepository.getStepData();
    }

    public void insertStepData(StepData stepData) {
        mAppRepository.insertStepData(stepData);
    }

    public Boolean hasExistingStepData(String username) {
        return mAppRepository.doesStepDataExist(username);
    }

    public LiveData<ArrayList<StepData>> setCurrentStepData(String username) {
        return mAppRepository.setStepData(username);
    }

}
