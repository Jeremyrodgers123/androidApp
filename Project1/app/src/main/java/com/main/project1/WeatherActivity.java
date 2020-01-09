package com.main.project1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class WeatherActivity extends AppCompatActivity {

    String mUserName;
    String mProfilePicturePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        Intent receivedIntent = getIntent();
        mUserName = receivedIntent.getStringExtra("USER_NAME");
        mProfilePicturePath = receivedIntent.getStringExtra("PROFILE_PICTURE_PATH");

        //Create fragments
        if (savedInstanceState == null) {

            //Create header fragment
            HeaderFragment headerFragment = new HeaderFragment();
            Bundle headerBundle = new Bundle();
            headerBundle.putString("USER_NAME", mUserName);
            headerBundle.putString("PROFILE_PICTURE_PATH", mProfilePicturePath);
            headerFragment.setArguments(headerBundle);

            //Create weather data fragment
            WeatherDataFragment weatherDataFragment = new WeatherDataFragment();
            Bundle data = new Bundle();
            data.putString("USER_NAME", mUserName);
            weatherDataFragment.setArguments(data);

            FragmentTransaction ftrans = getSupportFragmentManager().beginTransaction();
            ftrans.replace(R.id.header_content, headerFragment);
            ftrans.replace(R.id.weather_content_ph, weatherDataFragment, "WeatherDataFrag");
            ftrans.commit();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    Log.d("focus", "touchevent");
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}
