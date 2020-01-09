package com.main.project1;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class UserProfileFragment extends Fragment implements View.OnClickListener {

    //static elements
    private static ArrayList<String> mUsaStates = new ArrayList<String>( Arrays.asList("Alabama","Alaska","Arizona","Arkansas","California","Colorado","Connecticut","Delaware","Florida","Georgia","Hawaii","Idaho","Illinois","Indiana","Iowa","Kansas","Kentucky","Louisiana","Maine","Maryland","Massachusetts","Michigan","Minnesota","Mississippi","Missouri","Montana","Nebraska","Nevada","New Hampshire","New Jersey","New Mexico","New York","North Carolina","North Dakota","Ohio","Oklahoma","Oregon","Pennsylvania","Rhode Island","South Carolina","South","Dakota","Tennessee","Texas","Utah","Vermont","Virginia","Washington","West Virginia","Wisconsin","Wyoming"));
    private static ArrayList<String> mCanadaProvinces = new ArrayList<String>(Arrays.asList("Alberta", "British Columbia", "Manitoba", "New Brunswick", "Newfoundland and Labrador", "Nova Scotia", "Ontario", "Price Edward Island", "Quebec", "Saskatchewan"));
    private static ArrayList<String> mCountries = new ArrayList<String>(Arrays.asList("United States", "Canada"));

    //UI elements
    ImageButton mImageButtonProfilePic;
    TextInputEditText mTextEditUsername;
    TextInputEditText mTextEditFirstName;
    TextInputEditText mTextEditLastName;
    TextInputEditText mTextEditCity;
    CircularImageView mImageViewProfilePic;
    SeekBar mSeekBarSex;
    SeekBar mSeekBarAge;
    MaterialButton mButtonSubmit;
    Spinner mSpinnerState;
    Spinner mSpinnerCountry;

    // Other elements
    Bitmap mProfileBitmap;
    ArrayAdapter<String> mCountryAdapter;
    ArrayAdapter<String> mStateAdapter;
    Boolean mUpdateStates = true;
    private AppViewModel mAppViewModel;

    //Define a request code for the camera
    static final int REQUEST_IMAGE_CAPTURE = 1;


    public UserProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Create the view model
        mAppViewModel = new ViewModelProvider(this).get(AppViewModel.class);

        //Set the observer
        mAppViewModel.getUserProfile().observe(this, userProfileObserver);
    }

    private final Observer<UserProfile> userProfileObserver  = new Observer<UserProfile>() {
        @Override
        public void onChanged(@Nullable final UserProfile userProfile) {
        // Update the UI if this data variable changes
        if(userProfile != null) {
            mSeekBarAge.setProgress(userProfile.getAge());
            mSeekBarSex.setProgress(getConvertedValueReverse(userProfile.getGender()));

            mTextEditUsername.setText(userProfile.getUserName());
            mTextEditFirstName.setText(userProfile.getFirstName());
            mTextEditLastName.setText(userProfile.getLastName());
            mTextEditCity.setText(userProfile.getCity());

            int countrySpinnerPosition = mCountryAdapter.getPosition(userProfile.getCountry());
            mUpdateStates = false; // so state doesn't get reset in the country spinner's onSelectedListener
            mSpinnerCountry.setSelection(countrySpinnerPosition, false);
            setStates(userProfile.getCountry());

            int stateSpinnerPosition  = mStateAdapter.getPosition(userProfile.getState());
            mSpinnerState.setSelection(stateSpinnerPosition);
            mProfileBitmap = retrieveImage(userProfile.getProfileImageFilePath());
            if (mProfileBitmap == null) {
                mImageViewProfilePic.setImageDrawable(getResources().getDrawable(R.drawable.defaultuser, null));
            } else {
                mImageViewProfilePic.setImageBitmap(mProfileBitmap);
            }
        }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        // get UI elements
        mImageButtonProfilePic = view.findViewById(R.id.imageButtonProfilePic);
        mTextEditUsername = view.findViewById(R.id.textEditUsername);
        mTextEditFirstName = view.findViewById(R.id.textEditFirstName);
        mTextEditLastName = view.findViewById(R.id.textEditLastName);
        mSeekBarAge = view.findViewById(R.id.seekBarAge);
        mSeekBarSex = view.findViewById(R.id.seekBarSex);
        mButtonSubmit = view.findViewById(R.id.buttonSubmitNewUser);
        mTextEditCity = view.findViewById(R.id.textEditCity);
        mSpinnerState = view.findViewById(R.id.spinnerState);
        mSpinnerCountry = view.findViewById(R.id.spinnerCountry);
        mImageViewProfilePic = view.findViewById(R.id.imageViewProfilePic);
        mSeekBarSex.setMax(100000000);

        // sets seekbars
        setSeekBars();

        //Set Country spinner
        setCountrySpinner();

        mButtonSubmit.setOnClickListener(this);
        mImageButtonProfilePic.setOnClickListener(this);

        //disable edit username if user profile exists
        UserProfile userProfile = mAppViewModel.getUserProfile().getValue();
        if (userProfile != null) {
            mTextEditUsername.setFocusable(false);
        }

        return view;

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("FIRST_NAME", mTextEditFirstName.getText().toString());
        savedInstanceState.putString("LAST_NAME", mTextEditLastName.getText().toString());
        savedInstanceState.putString("USER_NAME", mTextEditUsername.getText().toString());
        savedInstanceState.putInt("AGE", mSeekBarAge.getProgress());
        savedInstanceState.putDouble("SEX", getConvertedValue(mSeekBarSex.getProgress()));
        savedInstanceState.putString("CITY", mTextEditCity.getText().toString());
        savedInstanceState.putString("COUNTRY", (String) mSpinnerState.getSelectedItem());
        savedInstanceState.putString("STATE", (String) mSpinnerCountry.getSelectedItem());
        savedInstanceState.putParcelable("PROFILE_PICTURE", mProfileBitmap);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            String firstName = savedInstanceState.getString("FIRST_NAME");
            String lastName = savedInstanceState.getString("LAST_NAME");
            String userName = savedInstanceState.getString("USER_NAME");
            String city = savedInstanceState.getString("CITY");
            String state = savedInstanceState.getString("STATE");
            String country = savedInstanceState.getString("COUNTRY");
            int age = savedInstanceState.getInt("AGE");
            double sex = savedInstanceState.getDouble("SEX");
            Bitmap profilePictureBitmap = savedInstanceState.getParcelable("PROFILE_PICTURE");


            mTextEditUsername.setText(userName);
            mSeekBarAge.setProgress(age);
            mSeekBarSex.setProgress(getConvertedValueReverse(sex));
            if (firstName != null) {
                mTextEditFirstName.setText(firstName);
            }
            if (lastName != null) {
                mTextEditLastName.setText(lastName);
            }
            if (city != null) {
                mTextEditCity.setText(city);
            }
            if (country != null) {
                int countrySpinnerPosition = mCountryAdapter.getPosition(country);
                mUpdateStates = false; // so state doesn't get reset in the country spinner's onSelectedListener
                mSpinnerCountry.setSelection(countrySpinnerPosition, false);
                setStates(country);
            }
            if (state != null) {
                int stateSpinnerPosition  = mStateAdapter.getPosition(state);
                mSpinnerState.setSelection(stateSpinnerPosition);
            }
            if (profilePictureBitmap != null) {
                mImageViewProfilePic.setImageBitmap(profilePictureBitmap);
            } else {
                mImageViewProfilePic.setImageDrawable(getResources().getDrawable(R.drawable.defaultuser, null));
            }
            mProfileBitmap = profilePictureBitmap;
        }
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() { super.onDetach(); }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.buttonSubmitNewUser: {
                boolean isNewUser = mAppViewModel.getUserProfile().getValue() == null;

                String firstName = mTextEditFirstName.getText().toString();
                String lastName = mTextEditLastName.getText().toString();
                String username = mTextEditUsername.getText().toString();
                int age = mSeekBarAge.getProgress();
                double sex = getConvertedValue(mSeekBarSex.getProgress());
                String city = mTextEditCity.getText().toString();
                String state = (String) mSpinnerState.getSelectedItem();
                String country = (String) mSpinnerCountry.getSelectedItem();

                String path = null;
                if (mProfileBitmap != null) {
                    path = saveImage(mProfileBitmap, username);
                }

                if (isNewUser) {
                    // new user then create new user
                    try {
                        // check if user exists
                        if (mAppViewModel.doesUserProfileExist(username)) {
                            Toast.makeText(getContext(), "" + "username already exists", Toast.LENGTH_SHORT).show();

                        } else {

                            //save to db
                            UserProfile userProfile = new UserProfile(username, firstName, lastName, age, sex, city, state, country, path);
                            mAppViewModel.insertUserProfile(userProfile);

                            //open dashboard activity
                            Intent messageIntent = new Intent(getContext(), DashboardActivity.class);
                            this.startActivity(messageIntent);
                        }
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                 // existing user
                } else {
                    try {

                        // update the existing user's profile
                        UserProfile updatedUser = new UserProfile(username, firstName, lastName, age, sex, city, state, country, path);
                        mAppViewModel.updateUserProfile(updatedUser);

                        //open dashboard activity
                        Intent messageIntent = new Intent(getContext(), DashboardActivity.class);
                        this.startActivity(messageIntent);
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                break;
            }
            case R.id.imageButtonProfilePic: {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Context context = getContext();
                if (cameraIntent.resolveActivity(context.getPackageManager()) != null) {
                    startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                }
                break;
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            if (bitmap.getWidth() > bitmap.getHeight()) {
                Matrix matrix = new Matrix();
                matrix.postRotate(270);
                bitmap = Bitmap.createBitmap(bitmap , 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            }
            mProfileBitmap = bitmap;
            mImageViewProfilePic.setImageBitmap(mProfileBitmap);
        }
    }

    /**
     * for turning a int to a double for seek bar
     * @param intVal
     * @return
     */
    public double getConvertedValue(int intVal){
        return intVal * .00000001;
    }

    /**
     * for turning a double value into an int for seek bar
     * @param doubleVal
     * @return
     */
    public int getConvertedValueReverse(Double doubleVal) {
        Double newVal = doubleVal / .00000001;
        return newVal.intValue();
    }

    /**
     * Saves an image to internal storage
     *
     * @param finalBitmap
     * @param username
     * @return
     */
    private String saveImage(Bitmap finalBitmap, String username) {
        String filename = "profile_picture_" + username + ".jpg";
        ContextWrapper cw = new ContextWrapper(getContext());
        File directory = cw.getDir("ProfileImages", Context.MODE_PRIVATE);
        File mypath = new File(directory, filename);
        FileOutputStream out = null;

        try {
            out = new FileOutputStream(mypath);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.close();
            return mypath.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets the bitmap when given a path to external storage
     *
     * @param path
     * @return
     */
    private Bitmap retrieveImage(String path) {
        try {
            File f= new File(path);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            return b;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Sets the drop down menu for the states depending on the selected country
     *
     * @param selected a country (USA or Canada)
     */
    private void setStates(String selected) {
        List<String> spinnerArray;
        spinnerArray = mUsaStates; // default is USA!

        if (selected.equals("Canada")) {
            spinnerArray = mCanadaProvinces;
        }
        mStateAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, spinnerArray);
        mStateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerState.setAdapter(mStateAdapter);

    }

    /**
     * Sets the drop down menu for the countries
     * (currently supports USA and Canada)
     *
     */
    private void setCountrySpinner() {
        List<String> spinnerArray;
        spinnerArray = mCountries;

        mCountryAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, spinnerArray);
        mCountryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCountry.setAdapter(mCountryAdapter);
        mSpinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String curCountry = mCountryAdapter.getItem(i);
                if (mUpdateStates) {
                    setStates(curCountry);
                }
                mUpdateStates = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

    }

    /**
     * Sets up listeners for the sex and age seek bars
     *
     */
    private void setSeekBars() {
        mSeekBarSex.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                DecimalFormat f = new DecimalFormat("0.00000000");
                Toast.makeText(getContext(), "" + f.format(getConvertedValue(progress)), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        mSeekBarAge.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                Toast.makeText(getContext(),"" + "Age: " +  progress, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }
}
