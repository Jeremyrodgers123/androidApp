package com.main.project1;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class StepFragment extends Fragment {

    // UI Elements
    private TextView mStepTextView;
    private TextView mPrev1Date;
    private TextView mPrev2Date;
    private TextView mPrev3Date;
    private TextView mPrev1Count;
    private TextView mPrev2Count;
    private TextView mPrev3Count;
    private MaterialCardView mCardView1;
    private MaterialCardView mCardView2;
    private MaterialCardView mCardView3;


    // Sensors
    private SensorManager mSensorManager;
    private Sensor mStepCounter;
    private Sensor mLinearAccelerometer;

    // Other
    private AppViewModel mAppViewModel;
    private float mCurStepCount;
    private boolean mStepCounterOn;
    private String mUsername;

    private double last_x, last_y;
    private double now_x, now_y;
    private boolean mNotFirstTime;
    private final double mThreshold = 12.0;
    private long mLastTime;




    public StepFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Create the view model
        mAppViewModel = new ViewModelProvider(getActivity()).get(AppViewModel.class);

        //Set the observer
        mAppViewModel.getStepData().observe(getActivity(), stepProfileObserver);

        String userName = mAppViewModel.getUserProfile().getValue().getUserName();
        mAppViewModel.setStepData(userName);
    }

    private final Observer<ArrayList<StepData>> stepProfileObserver  = new Observer<ArrayList<StepData>>() {
        @Override
        public void onChanged(@Nullable final ArrayList<StepData> stepData) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        // Update the UI if this data variable changes
        mCardView1.setVisibility(View.INVISIBLE);
        mCardView2.setVisibility(View.INVISIBLE);
        mCardView3.setVisibility(View.INVISIBLE);

        if (stepData != null) {
            if (stepData.size() >= 1) {
                LocalDateTime time1 = stepData.get(0).TimeStamp;
                mPrev1Date.setText(time1.format(formatter));
                mPrev1Count.setText(Math.round(stepData.get(0).StepCount) + " steps");
                mCardView1.setVisibility(View.VISIBLE);
            }

            if (stepData.size() >= 2) {
                LocalDateTime time2 = stepData.get(1).TimeStamp;
                mPrev2Date.setText(time2.format(formatter));
                mPrev2Count.setText(Math.round(stepData.get(1).StepCount) + " steps");
                mCardView2.setVisibility(View.VISIBLE);
            }

            if (stepData.size() == 3) {
                LocalDateTime time3 = stepData.get(2).TimeStamp;
                mPrev3Date.setText(time3.format(formatter));
                mPrev3Count.setText(Math.round(stepData.get(2).StepCount) + " steps");
                mCardView3.setVisibility(View.VISIBLE);
            }
        }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_step, container, false);

        // Get UI elements
        mStepTextView = view.findViewById(R.id.textViewStepCount);
        mPrev1Date = view.findViewById(R.id.textViewPrev1Date);
        mPrev2Date = view.findViewById(R.id.textViewPrev2Date);
        mPrev3Date = view.findViewById(R.id.textViewPrev3Date);
        mPrev1Count = view.findViewById(R.id.textViewPrev1Count);
        mPrev2Count = view.findViewById(R.id.textViewPrev2Count);
        mPrev3Count = view.findViewById(R.id.textViewPrev3Count);
        mCardView1 = view.findViewById(R.id.cardViewPrev1);
        mCardView2 = view.findViewById(R.id.cardViewPrev2);
        mCardView3 = view.findViewById(R.id.cardViewPrev3);

        mStepTextView.setText(String.valueOf((int) mCurStepCount) + " steps");
        mUsername = mAppViewModel.getUserProfile().getValue().getUserName();

        // Inflate the layout for this fragment
        return view;
    }

    private SensorEventListener mShakeListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            //Get the acceleration rates along the y and z axes
            now_x = sensorEvent.values[0];
            now_y = sensorEvent.values[1];

            if (mNotFirstTime) {
                double dx = Math.abs(last_x - now_x);
                double dy = Math.abs(last_y - now_y);

                long curTime = System.currentTimeMillis();
                long timeDiff = curTime - mLastTime;
                if (!mStepCounterOn && timeDiff > 1000 && dy > mThreshold) { // first gesture to start step counter
                    mLastTime = curTime;

                    Toast.makeText(getContext(), "STEP COUNTER ON", Toast.LENGTH_SHORT).show();
                    mStepCounterOn = true;
                    mCurStepCount = 0;
                    mStepTextView.setText(String.valueOf((int) mCurStepCount));
                } else if (mStepCounterOn && timeDiff > 1000 && dx > mThreshold) { // second gesture to stop step counter
                    mLastTime = curTime;

                    Toast.makeText(getContext(), "STEP COUNTER OFF", Toast.LENGTH_SHORT).show();
                    StepData stepData = new StepData();
                    stepData.TimeStamp = LocalDateTime.now();
                    stepData.UserName = mUsername;
                    stepData.StepCount = mCurStepCount;
                    mAppViewModel.insertStepData(stepData);
                    mStepCounterOn = false;
                }
            }
            last_x = now_x;
            last_y = now_y;
            mNotFirstTime = true;

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    };

    private SensorEventListener mStepListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (mStepCounterOn) {
                mCurStepCount += 1;
                mStepTextView.setText(String.valueOf((int) mCurStepCount) + " steps");
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putFloat("CURRENT_STEP_COUNT", mCurStepCount);

    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mCurStepCount = savedInstanceState.getFloat("CURRENT_STEP_COUNT");
            mStepTextView.setText(String.valueOf((int) mCurStepCount + " steps"));
        }
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Sensor and step counting
        mSensorManager = (SensorManager) this.getContext().getSystemService(Context.SENSOR_SERVICE);
        mStepCounter = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        mLinearAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mCurStepCount = 0;

        mStepCounterOn = true;

        if (mStepCounter != null) {
            mSensorManager.registerListener(mStepListener, mStepCounter, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if(mLinearAccelerometer != null){
            mSensorManager.registerListener(mShakeListener, mLinearAccelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mStepCounter!=null || mLinearAccelerometer != null){
            mSensorManager.unregisterListener(mShakeListener);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mShakeListener = null;
    }
}
