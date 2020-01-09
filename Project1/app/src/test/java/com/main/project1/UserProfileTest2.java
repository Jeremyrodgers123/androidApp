package com.main.project1;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import org.junit.Test;
import static org.junit.Assert.*;


public class UserProfileTest2 {
//    private AppRepository mAppRepository;
//    private MutableLiveData<UserProfile> mUserProfile;
//    private LiveData<FitnessProfile> mFitnessProfile;
//    private MutableLiveData<WeatherData> mWeatherData;

    @Test
    public void ValidConstructor1() {
//        Application ya = new Application();
//        AppViewModel mAppViewModel = new AppViewModel(ya);

        AppRepository ya = AppRepository.getInstance(new Application());

    }
}
