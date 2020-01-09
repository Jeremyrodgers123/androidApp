package com.main.project1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.widget.Toast;
import java.util.Arrays;
import java.util.List;

public class DashboardActivity extends AppCompatActivity implements DashboardAdapter.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //Create header fragment
        HeaderFragment headerFragment = new HeaderFragment();

        //Create dashboard fragment
        DashboardFragment dashboardFragment = new DashboardFragment();

        //Replace fragment containers with dashboard fragment
        FragmentTransaction ftrans = getSupportFragmentManager().beginTransaction();
        ftrans.replace(R.id.header_content, headerFragment);
        ftrans.replace(R.id.dashboard_content, dashboardFragment);
        ftrans.commit();
    }

    @Override
    public void OnClick(int position) {
        Intent intent = null;
        Fragment fragment = null;
        switch(position) {

            case 0: //Hiking
                //Toast.makeText(mContext, "Hiking", Toast.LENGTH_SHORT).show();
                Uri uri = Uri.parse("geo:0,0?q=hikes");
                intent = new Intent(Intent.ACTION_VIEW, uri);
                break;

            case 1: //Weather
                //Toast.makeText(this, "Weather App place holder", Toast.LENGTH_SHORT).show();

                // for phone
                intent = new Intent(this, WeatherActivity.class);

                // for tablet
                fragment = new WeatherDataFragment();

                break;

            case 2: //Fitness Profile
                //Toast.makeText(mContext, "Fitness Profile", Toast.LENGTH_SHORT).show();

                // for phone
                intent = new Intent(this, FitnessActivity.class);

                // for tablet
                fragment = new FitnessProfileFragment();

                break;

            case 3: // Step counter
                Toast.makeText(this, "Step counter!", Toast.LENGTH_SHORT).show();

                // for phone
                intent = new Intent(this, StepActivity.class);

                // for tablet
                fragment = new StepFragment();
                
                break;

            case 4: //Switch Profile
                //Toast.makeText(mContext, "User Profile", Toast.LENGTH_SHORT).show();

                // for phone
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);


                break;


        }

        if (getResources().getBoolean(R.bool.isTablet)) {
            if (fragment != null) {
                FragmentTransaction ftrans = getSupportFragmentManager().beginTransaction();
                ftrans.replace(R.id.detail_content, fragment);
                ftrans.commit();
            } else if (intent != null) {
                startActivity(intent);
            }

        } else {
            if (intent != null) {
                startActivity(intent);
            }
        }
    }
}
