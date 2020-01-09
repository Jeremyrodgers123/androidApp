package com.main.project1;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.xw.repo.BubbleSeekBar;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;

public class FitnessProfileFragment extends Fragment {
    final int MAXBMI = 60;
    final int MAXBMR = 4000;
    final int MAXCALORIES = 6000;
    final int MAXWEIGHT = 600;
    final String feetOptionsDefaultTxt = "Feet";
    final String inchesOptionsDefaultTxt = "Inches";

    CircularProgressIndicator bmi_progress;
    CircularProgressIndicator bmr_progress;
    CircularProgressIndicator calories_progress;
    Spinner mFeetDropdown;
    Spinner mInchesDropdown;
    Spinner mGoalsDropdown;
    SeekBar mSeekBarWeight;
    BubbleSeekBar mSeekBarActivity;
    TextInputEditText mWeightGoalInput;
    MaterialButton mSubmitBtn;

    private AppViewModel mAppViewModel;
    FitnessProfile mCurrentFitnessProfile;
    UserHeight mCurrentUserHeight;
    ArrayList<String> mfeetOptions = new ArrayList<>(Arrays.asList(feetOptionsDefaultTxt,"1 foot","2 feet","3 feet","4 feet","5 feet","6 feet","7 feet","8 feet"));
    ArrayList<String> minchesOptions = new ArrayList<>(Arrays.asList(inchesOptionsDefaultTxt,"0 inches","1 inch","2 inches","3 inches","4 inches","5 inches","6 inches","7 inches","8 inches","9 inches","10 inches","11 inches"));
    ArrayList<String> mGoalOptions = new ArrayList<>(Arrays.asList("Activity Level"));

    public FitnessProfileFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAppViewModel = new ViewModelProvider(this).get(AppViewModel.class);
        ArrayList<FitnessProfile.WeightGoal> activityLevelOptions = new ArrayList<>(EnumSet.allOf(FitnessProfile.WeightGoal.class));
        for (FitnessProfile.WeightGoal option : activityLevelOptions) {
            String revisedText = option.toString().replaceAll("([^_])([A-Z])", "$1 $2");
            revisedText += " Weight";
            mGoalOptions.add(revisedText);
        }
        initCurrentFitnessProfile();
        String userName = mAppViewModel.getUserProfile().getValue().getUserName();
        mAppViewModel.setFitnessProfile(userName);
        mAppViewModel.getCurrentFitnessProfile().observe(this, currentFitnessProfileObserver);
        mAppViewModel.getFitnessProfile().observe(this, savedFitnessProfileObserver);
    }


    private final Observer<FitnessProfile> currentFitnessProfileObserver  = new Observer<FitnessProfile>() {
        @Override
        public void onChanged(@Nullable final FitnessProfile fitnessProfile) {
            if(fitnessProfile != null) {
                mCurrentFitnessProfile = fitnessProfile;
                mCurrentUserHeight = getFeetAndInches(fitnessProfile.Height_in);
                initGuiOptions();
            }
        }
    };

    private final Observer<FitnessProfile> savedFitnessProfileObserver  = new Observer<FitnessProfile>() {
        @Override
        public void onChanged(@Nullable final FitnessProfile fitnessProfile) {
            if(fitnessProfile != null) {
                mAppViewModel.setCurrentFitnessProfile(mAppViewModel.getFitnessProfile().getValue());
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_fitness_profile, container, false);

        mFeetDropdown = view.findViewById(R.id.spinnerFeet);
        mInchesDropdown = view.findViewById(R.id.spinnerInches);
        mGoalsDropdown = view.findViewById(R.id.GoalSpinner);
        mWeightGoalInput = view.findViewById(R.id.textEditWeightGainLoss);
        mSubmitBtn = view.findViewById(R.id.submit_fitness_profile_btn);
        addTextChangeListener(mWeightGoalInput);

        mSeekBarWeight = view.findViewById(R.id.fitnessWeightSeekBar);
        mSeekBarActivity = view.findViewById(R.id.fitnessActivitySeekBar);
        mSeekBarWeight.setMax(MAXWEIGHT);

        mSeekBarActivity.setCustomSectionTextArray(new BubbleSeekBar.CustomSectionTextArray() {
            @NonNull
            @Override
            public SparseArray<String> onCustomize(int sectionCount, @NonNull SparseArray<String> array) {
                array.clear();
                array.put(0, "Lazy");
                array.put(1, "Moderate");
                array.put(2, "Active");
                array.put(3, "Very Active");
                array.put(4, "Freak");
                return array;
            }
        });
        setSeekBars();

        createSpinnerOptions(mfeetOptions, mFeetDropdown, new HeightOptionsHandler());
        createSpinnerOptions(minchesOptions, mInchesDropdown, new HeightOptionsHandler());
        createSpinnerOptions(mGoalOptions, mGoalsDropdown, new GoalOptionsHandler());

        bmi_progress = view.findViewById(R.id.bmi_progress);
        bmr_progress = view.findViewById(R.id.bmr_progress);
        calories_progress = view.findViewById(R.id.calories_progress);
        initGuiOptions();
        setSubmitListener();
        view.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) { new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                    mSeekBarActivity.correctOffsetWhenContainerOnScrolling();
                }
            };
            mSeekBarActivity.correctOffsetWhenContainerOnScrolling();
            }
        });
        return view;
    }

    void initGuiOptions() {
        if(mAppViewModel.hasExistingFitnessProfile()) {
            mSeekBarWeight.setProgress(mCurrentFitnessProfile.Weight_lb);
            mSeekBarActivity.setProgress(fitnessLevelToSlidebarVal());
            mGoalsDropdown.setSelection(mCurrentFitnessProfile.Goal.ordinal() + 1);
            mFeetDropdown.setSelection(mCurrentUserHeight.feet);
            mInchesDropdown.setSelection(mCurrentUserHeight.inches + 1);
            mWeightGoalInput.setText(Double.toString(mCurrentFitnessProfile.WeightChangePerWeek_lb));
            setCircularProgressValue();
        }
    }

    UserHeight getFeetAndInches(double _height) {
        int feet = (int) _height / 12;
        int inches = (int) _height % 12;
        return new UserHeight(feet, inches);
    }

    private int fitnessLevelToSlidebarVal() {
        int ret =  (mCurrentFitnessProfile.Level.ordinal()) * (100 /(getNumFitnessLevelOptions() -1));
        return ret;
    }

    void setCircularProgressValue() {
        if (!mAppViewModel.hasExistingFitnessProfile()) return;
        bmi_progress.setProgress(mCurrentFitnessProfile.BodyMassIndex, MAXBMI);
        bmr_progress.setProgress(mCurrentFitnessProfile.BasalMetabolicRate, MAXBMR);
        if (mCurrentFitnessProfile.Goal == FitnessProfile.WeightGoal.Gain
                || mCurrentFitnessProfile.Goal == FitnessProfile.WeightGoal.Lose) {
            calories_progress.setProgress(mCurrentFitnessProfile.DailyCaloricRequirementToMeetWeightGoal, MAXCALORIES);
        } else {
            calories_progress.setProgress(mCurrentFitnessProfile.DailyCaloricRequirementToMaintainWeight, MAXCALORIES);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    interface OnSelect{
        void select(ArrayAdapter<String> adapter, int itemPos);
    }

    private class HeightOptionsHandler implements OnSelect{

        @Override
        public void select(ArrayAdapter<String> adapter, int itemPos) {
            String item = adapter.getItem(itemPos);
            if(itemPos == 0){
                return;
            }
            String[] split = item.split("\\s+");
            if (adapter.getItem(0) == feetOptionsDefaultTxt) {
                int feet = Integer.parseInt(split[0]);
                if(feet == mCurrentUserHeight.feet) return;
                mCurrentUserHeight.feet = feet;
            } else {
                int inches =  Integer.parseInt(split[0]);
                if(inches == mCurrentUserHeight.inches) return;
                mCurrentUserHeight.inches = inches;
            }
            try {
                mCurrentFitnessProfile.Height_in = mCurrentUserHeight.calcHeightInInches();
                updateGui();

            } catch (Exception e) {
                Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class GoalOptionsHandler implements OnSelect{

        @Override
        public void select(ArrayAdapter<String> adapter, int itemPos) {
            String item = adapter.getItem(itemPos);
            if(itemPos == 0){
                return;
            }
            switch(item){
                case "Gain Weight":
                    if(mCurrentFitnessProfile.Goal == FitnessProfile.WeightGoal.Gain) return;
                    mCurrentFitnessProfile.Goal = FitnessProfile.WeightGoal.Gain;
                    break;
                case "Maintain Weight":
                    if(mCurrentFitnessProfile.Goal == FitnessProfile.WeightGoal.Maintain) return;
                    mCurrentFitnessProfile.Goal = FitnessProfile.WeightGoal.Maintain;
                    mCurrentFitnessProfile.WeightChangePerWeek_lb = 0;
                    mWeightGoalInput.setText("0");
                    break;
                case "Lose Weight":
                    if(mCurrentFitnessProfile.Goal == FitnessProfile.WeightGoal.Lose) return;
                    mCurrentFitnessProfile.Goal = FitnessProfile.WeightGoal.Lose;
                    break;
            }
            try {
                updateGui();
            } catch (Exception e) {
                Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    void createSpinnerOptions(ArrayList<String> options, Spinner spinner, final OnSelect handler) {
        final ArrayAdapter<String> adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, options) {
            @Override
            public boolean isEnabled(int position) {
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setGravity(Gravity.CENTER);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                handler.select(adapter, i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }


    void updateGui() {
        if(mAppViewModel.hasExistingFitnessProfile()) {
            mCurrentFitnessProfile.Update(mAppViewModel.getUserProfile().getValue(), mCurrentUserHeight.feet, mCurrentUserHeight.inches,mCurrentFitnessProfile.Weight_lb, mCurrentFitnessProfile.Level, mCurrentFitnessProfile.Goal, mCurrentFitnessProfile.WeightChangePerWeek_lb);
            setCircularProgressValue();
            if(!mCurrentFitnessProfile.IsDailyCaloricRequirementToMeetGoalHealthy(mAppViewModel.getUserProfile().getValue())) {
                Toast.makeText(getContext(), "Warning! Unhealthy weight change detected.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void initCurrentFitnessProfile() {
        mCurrentFitnessProfile = new FitnessProfile();
        mAppViewModel.initCurrentFitnessProfile();
        mCurrentFitnessProfile = mAppViewModel.getCurrentFitnessProfile().getValue();
        mCurrentFitnessProfile.Level = FitnessProfile.FitnessLevel.Sedentary;
        mCurrentFitnessProfile.Goal = FitnessProfile.WeightGoal.Maintain;
        mCurrentUserHeight = getFeetAndInches(mCurrentFitnessProfile.Height_in);
        mCurrentFitnessProfile.UserName = mAppViewModel.getUserProfile().getValue().getUserName();
    }

    void setSeekBars() {
        mSeekBarWeight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                DecimalFormat f = new DecimalFormat("0.0");
                Toast.makeText(getContext(), "" + f.format(progress) + " lbs", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = Math.round(seekBar.getProgress());
                if(progress == mCurrentFitnessProfile.Weight_lb) return;
                mCurrentFitnessProfile.Weight_lb = progress;
                try {
                    updateGui();
                } catch (Exception e) {
                    Toast.makeText(getContext(), "a problem occured setting seekbar" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        mSeekBarActivity.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                mSeekBarActivity.correctOffsetWhenContainerOnScrolling();
            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {}

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                int range = 100 / getNumFitnessLevelOptions();
                int slideVal = progress / range;
                if(slideVal > getNumFitnessLevelOptions() - 1) {
                    slideVal = getNumFitnessLevelOptions() - 1;
                }
                if(FitnessProfile.FitnessLevel.values()[slideVal] == mCurrentFitnessProfile.Level) return;
                mCurrentFitnessProfile.Level = FitnessProfile.FitnessLevel.values()[slideVal];
//                mAppViewModel.updateCurrentFitnessProfile(mCurrentFitnessProfile);
                try {
                    updateGui();
                } catch (Exception e) {
                    Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void addTextChangeListener(final TextInputEditText textView) {
        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {}
        });

        textView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
            if(!hasFocus) {
                mSeekBarActivity.correctOffsetWhenContainerOnScrolling();
                if(textView.getText() == null || textView.getText().toString().equals("")) return;
                try {
                    double weeklyWeightChangeGoal = new Double (textView.getText().toString());
                    mCurrentFitnessProfile.WeightChangePerWeek_lb = weeklyWeightChangeGoal;
                    updateGui();
                } catch (Exception e) {
                    textView.setError(e.getMessage());
                }
            }
            }
        });
    }

    private class UserHeight{
        int feet = 0;
        int inches = 0;
        UserHeight( int _feet, int _inches) {
            feet = _feet;
            inches = _inches;
        };
        int calcHeightInInches() {
         return (feet * 12) + inches;
        }
    }

    private int getNumFitnessLevelOptions() {
        return FitnessProfile.FitnessLevel.values().length;
    }

    void setSubmitListener() {
        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });
    }

    void save() {
        if(!mAppViewModel.hasExistingFitnessProfile()) {
            try {
                mCurrentFitnessProfile.Update(mAppViewModel.getUserProfile().getValue(), mCurrentUserHeight.feet, mCurrentUserHeight.inches,mCurrentFitnessProfile.Weight_lb, mCurrentFitnessProfile.Level, mCurrentFitnessProfile.Goal, mCurrentFitnessProfile.WeightChangePerWeek_lb);
                mAppViewModel.insertFitnessProfile(mCurrentFitnessProfile);
                updateGui();
                Toast.makeText(getContext(), "Profile Saved", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }else {
            try {
                mCurrentFitnessProfile.Update(mAppViewModel.getUserProfile().getValue(), mCurrentUserHeight.feet, mCurrentUserHeight.inches,mCurrentFitnessProfile.Weight_lb, mCurrentFitnessProfile.Level, mCurrentFitnessProfile.Goal, mCurrentFitnessProfile.WeightChangePerWeek_lb);
                mAppViewModel.updateFitnessProfile(mCurrentFitnessProfile);
                Toast.makeText(getContext(), "Profile Saved", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("FeetDropdownPos", mFeetDropdown.getSelectedItemPosition());
        savedInstanceState.putInt("InchesDropdownPos", mInchesDropdown.getSelectedItemPosition());
        savedInstanceState.putInt("GoalsDropdownPos", mGoalsDropdown.getSelectedItemPosition());
        savedInstanceState.putInt("weightSeekbarPos", mSeekBarWeight.getProgress());
        savedInstanceState.putInt("activitySeekbarPos", mSeekBarActivity.getProgress());
        savedInstanceState.putString("weightGoalInput", mWeightGoalInput.getText().toString());
        savedInstanceState.putDouble("BMI", mCurrentFitnessProfile.BodyMassIndex);
        savedInstanceState.putInt("BMR", mCurrentFitnessProfile.BasalMetabolicRate);
        savedInstanceState.putInt("MaintainCalories", mCurrentFitnessProfile.DailyCaloricRequirementToMaintainWeight);
        savedInstanceState.putInt("MeetGoalCalories", mCurrentFitnessProfile.DailyCaloricRequirementToMeetWeightGoal);
        savedInstanceState.putDouble("heightIn", mCurrentFitnessProfile.Height_in);
        savedInstanceState.putInt("weight", mCurrentFitnessProfile.Weight_lb);
        savedInstanceState.putInt("activity", mCurrentFitnessProfile.Level.ordinal());
        savedInstanceState.putInt("weightGoal", mCurrentFitnessProfile.Goal.ordinal());
        savedInstanceState.putDouble("weightChange", mCurrentFitnessProfile.WeightChangePerWeek_lb);
        savedInstanceState.putInt("userHeightFeet", mCurrentUserHeight.feet);
        savedInstanceState.putInt("userHeightInches", mCurrentUserHeight.inches);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            int feetDropdownPos = savedInstanceState.getInt("FeetDropdownPos");
             int InchesDropdownPos = savedInstanceState.getInt("InchesDropdownPos");
             int GoalsDropdownPos = savedInstanceState.getInt("GoalsDropdownPos");
             int weightSeekbarPos = savedInstanceState.getInt("weightSeekbarPos");
             int activitySeekbarPos = savedInstanceState.getInt("activitySeekbarPos");
            String weightGoalInput = savedInstanceState.getString("weightGoalInput");
            Double BMI = savedInstanceState.getDouble("BMI");
            int BMR = savedInstanceState.getInt("BMR");
            int MaintainCalories = savedInstanceState.getInt("MaintainCalories");
            int MeetGoalCalories =  savedInstanceState.getInt("MeetGoalCalories");
            Double heightIn = savedInstanceState.getDouble("heightIn");
            int weight = savedInstanceState.getInt("weight");
            int activity = savedInstanceState.getInt("activity");
            int weightGoal = savedInstanceState.getInt("weightGoal");
            Double weightChange = savedInstanceState.getDouble("weightChange");
            int userHeightFeet  = savedInstanceState.getInt("userHeightFeet");
            int userHeightInches = savedInstanceState.getInt("userHeightInches");

            mFeetDropdown.setSelection(feetDropdownPos);
            mInchesDropdown.setSelection(InchesDropdownPos);
            mGoalsDropdown.setSelection(GoalsDropdownPos);
            mSeekBarWeight.setProgress(weightSeekbarPos);
            mSeekBarActivity.setProgress(activitySeekbarPos);
            mWeightGoalInput.setText(weightGoalInput);
            mCurrentFitnessProfile.BodyMassIndex = BMI;
            mCurrentFitnessProfile.BasalMetabolicRate = BMR;
            mCurrentFitnessProfile.DailyCaloricRequirementToMaintainWeight = MaintainCalories;
            mCurrentFitnessProfile.DailyCaloricRequirementToMeetWeightGoal = MeetGoalCalories;
            mCurrentFitnessProfile.Height_in = heightIn;
            mCurrentFitnessProfile.Weight_lb = weight;
            mCurrentFitnessProfile.Level = mCurrentFitnessProfile.Level.values()[activity];
            mCurrentFitnessProfile.Goal = mCurrentFitnessProfile.Goal.values()[weightGoal];
            mCurrentFitnessProfile.WeightChangePerWeek_lb = weightChange;
            mCurrentUserHeight.feet = userHeightFeet;
            mCurrentUserHeight.inches = userHeightInches;
            mSeekBarActivity.correctOffsetWhenContainerOnScrolling();

        }
    }
}
