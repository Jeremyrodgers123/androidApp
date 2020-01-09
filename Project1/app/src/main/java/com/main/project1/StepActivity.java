package com.main.project1;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.view.View;

public class StepActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        // get the fragments
        StepFragment stepFragment = new StepFragment();
        HeaderFragment headerFragment = new HeaderFragment();

        FragmentTransaction ftrans = getSupportFragmentManager().beginTransaction();
        ftrans.replace(R.id.stepHeader, headerFragment);
        ftrans.replace(R.id.frameLayoutStep, stepFragment, "stepFrag");
        ftrans.commit();
    }

}
