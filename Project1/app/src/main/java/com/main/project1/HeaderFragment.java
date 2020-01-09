package com.main.project1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.material.textview.MaterialTextView;
import com.mikhaellopez.circularimageview.CircularImageView;
import java.io.File;


public class HeaderFragment extends Fragment {

    private ImageView mHomeImageView;
    private MaterialTextView mUserNameTextView;
    private CircularImageView mProfilePictureImageView;
    private AppViewModel mAppViewModel;

    // Required empty public constructor
    public HeaderFragment() {   }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Create the view model
        mAppViewModel = new ViewModelProvider(this).get(AppViewModel.class);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_header, container, false);

        // Apply user name
        mUserNameTextView = view.findViewById(R.id.header_text);
        String username = mAppViewModel.getUserProfile().getValue().getUserName();
        if (username != null) {
            mUserNameTextView.setText(username);
        }
        else {
            mUserNameTextView.setText("user name not found");
        }

        // Attach click listener to profile image
        mProfilePictureImageView = view.findViewById(R.id.header_profile_image);
        mProfilePictureImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UserProfileActivity.class);
                startActivity(intent);
            }
        });

        // Apply profile picture
        String profileImagePath = mAppViewModel.getUserProfile().getValue().getProfileImageFilePath();
        if (profileImagePath != null) {
            File file = new File(profileImagePath);
            if (file.exists()) {
                Bitmap profilePictureBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                mProfilePictureImageView.setImageBitmap(profilePictureBitmap);
            }
        } else {
            mProfilePictureImageView.setImageResource(R.drawable.defaultuser);
        }

        // Attach click listener to home image
        mHomeImageView = view.findViewById(R.id.header_home_image);
        mHomeImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // If not already home
                if(!(getContext() instanceof DashboardActivity)) {
                    Intent intent = new Intent(getContext(), DashboardActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "You are already home!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
