package com.main.project1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import com.amazonaws.mobile.client.AWSMobileClient;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button mExistingUserLoginBtn;
    private Button mNewUserLoginBtn;
    private Button mEditUserBtn;
    private Button mFitnessBtn;
    private Button mDashboardBtn;
    private Button mWeatherBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mExistingUserLoginBtn = findViewById(R.id.main_ex_usr_btn);
        mNewUserLoginBtn = findViewById(R.id.main_ex_usr_btn);
        mEditUserBtn = findViewById(R.id.main_profile_form_btn);
        mFitnessBtn = findViewById(R.id.main_fitness_btn);
        mDashboardBtn = findViewById(R.id.main_dashboard_btn);
        mWeatherBtn = findViewById(R.id.main_weather_btn);

        mExistingUserLoginBtn.setOnClickListener(this);
        mNewUserLoginBtn.setOnClickListener(this);
        mEditUserBtn.setOnClickListener(this);
        mFitnessBtn.setOnClickListener(this);
        mDashboardBtn.setOnClickListener(this);
        mWeatherBtn.setOnClickListener(this);

        //seed database
//        AppDb.SeedDatabase(this);
    }

    @Override
    public void onClick(View view) {
        AppDb appDb = AppDb.GetDatabase(this);
        UserProfileDao userProfileDao = appDb.GetUserProfileDao();
        UserProfile userProfile = userProfileDao.Get("jrodgers");

        Intent i = null;
        switch (view.getId()) {
            case R.id.main_ex_usr_btn:{
                i = new Intent(this, LoginActivity.class);
                break;
            }
            case R.id.main_new_usr_login_btn:{
                i = new Intent(this, LoginActivity.class);
                break;
            }
            case R.id.main_profile_form_btn:{
                i = new Intent(this, UserProfileActivity.class);
                break;
            }
            case R.id.main_fitness_btn:{
                i = new Intent(this, FitnessActivity.class);
                i.putExtra("USER_NAME", userProfile.getUserName());
                i.putExtra("PROFILE_PICTURE_PATH", userProfile.getProfileImageFilePath());
                break;
            }
            case R.id.main_dashboard_btn:{
                i = new Intent(this, DashboardActivity.class);
                i.putExtra("USER_NAME", userProfile.getUserName());
                i.putExtra("PROFILE_PICTURE_PATH", userProfile.getProfileImageFilePath());
                break;
            }
            case R.id.main_weather_btn:{
                i = new Intent(this, WeatherActivity.class);
                i.putExtra("USER_NAME", userProfile.getUserName());
                i.putExtra("PROFILE_PICTURE_PATH", userProfile.getProfileImageFilePath());
                break;
            }
        }
        if (i != null) {
            startActivity(i);
        } else {
            Toast.makeText(this,"Could not find activity", Toast.LENGTH_SHORT).show();
        }
    }
}
