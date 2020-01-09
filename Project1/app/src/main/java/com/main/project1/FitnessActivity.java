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

public class FitnessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness);

        Intent receivedIntent = getIntent();
        String userName = receivedIntent.getStringExtra("USER_NAME");
        String mProfilePicturePath = receivedIntent.getStringExtra("PROFILE_PICTURE_PATH");
        if(savedInstanceState == null) {

            //Create header fragment
            HeaderFragment headerFragment = new HeaderFragment();
            Bundle headerBundle = new Bundle();
            headerBundle.putString("USER_NAME", userName);
            headerBundle.putString("PROFILE_PICTURE_PATH", mProfilePicturePath);
            headerFragment.setArguments(headerBundle);



            FitnessProfileFragment fitnessProfileFragment = new FitnessProfileFragment();
            Bundle data = new Bundle();
            data.putString("USER_NAME", userName);
            fitnessProfileFragment.setArguments(data);

            FragmentTransaction ftrans = getSupportFragmentManager().beginTransaction();
            ftrans.replace(R.id.header_content, headerFragment);
            ftrans.replace(R.id.fitness_content_ph, fitnessProfileFragment, "FitnessProfileFrag");
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
