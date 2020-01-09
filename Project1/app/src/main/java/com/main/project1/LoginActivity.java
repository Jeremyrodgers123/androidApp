package com.main.project1;

import android.content.Intent;
import android.os.Bundle;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private MaterialButton mButtonLogin;
    private MaterialButton mButtonCreateAccount;
    private TextInputEditText mTextInputUserName;
    private AppViewModel mAppViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AWSMobileClient.getInstance().initialize(this, new AWSStartupHandler() {
            @Override
            public void onComplete(AWSStartupResult awsStartupResult) {
                Log.d("YourMainActivity", "AWSMobileClient is instantiated and you are connected to AWS!");
            }
        }).execute();
        mButtonLogin = findViewById(R.id.buttonLogin);
        mButtonCreateAccount = findViewById(R.id.buttonCreateAccount);
        mTextInputUserName = findViewById(R.id.textInputEditUsername);

        mButtonLogin.setOnClickListener(this);
        mButtonCreateAccount.setOnClickListener(this);

        //Create the view model and initialize it
        mAppViewModel = new ViewModelProvider(this).get(AppViewModel.class);
        mAppViewModel.Init();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {

            case R.id.buttonLogin: {
                String username = mTextInputUserName.getText().toString();
                if (username.matches("")) {
                    Toast.makeText(LoginActivity.this, "Please enter username", Toast.LENGTH_SHORT).show();
                } else {

                    username = username.replaceAll("^\\s+","");
                    username = username.trim();
                    String[] splitUsername = username.split("\\s+");
                    if (splitUsername.length > 1) {
                        Toast.makeText(LoginActivity.this, "Username cannot have spaces", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!mAppViewModel.doesUserProfileExist(username)) {
                            Toast.makeText(LoginActivity.this, "Username does not exist", Toast.LENGTH_SHORT).show();
                        } else {
                            mAppViewModel.setUserProfile(username);
                            Intent existingUserIntent = new Intent(this, DashboardActivity.class);
                            this.startActivity(existingUserIntent);
                        }

                    }

                }
                break;
            }

            case R.id.buttonCreateAccount: {
                Intent newUserIntent = new Intent(this, UserProfileActivity.class);
                this.startActivity(newUserIntent);
            }
        }
    }
}
