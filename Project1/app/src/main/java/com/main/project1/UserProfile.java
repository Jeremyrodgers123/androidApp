package com.main.project1;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "user_profile_table")
public class UserProfile implements Serializable {

    public static final int USER_NAME_MINIMUM_CHARACTER_LIMIT = 5;
    public static final int USER_NAME_MAXIMUM_CHARACTER_LIMIT = 20;
    public static final int STRING_MINIMUM_CHARACTER_LIMIT = 1;
    public static final int MINIMUM_AGE = 1;
    public static final int MAXIMUM_AGE = 150;
    public static final int MINIMUM_GENDER = 0;
    public static final int MAXIMUM_GENDER = 1;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "user_name")
    private String UserName;

    @ColumnInfo(name = "first_name")
    private String FirstName;

    @ColumnInfo(name = "last_name")
    private String LastName;

    @ColumnInfo(name = "gender")
    private double Gender; //0 = full female, 1 = full male



    @ColumnInfo(name = "age")
    private int Age;

    @ColumnInfo(name = "city")
    private String City;

    @ColumnInfo(name = "state")
    private String State;

    @ColumnInfo(name = "country")
    private String Country;

    @ColumnInfo(name = "profile_image_file_path")
    private String ProfileImageFilePath;

    /**
     * default constructor
     */
    public UserProfile() { }

    /**
     * @param userName user's unique id
     * @param firstName user's first name
     * @param lastName user's last name
     * @param age user's age
     * @param gender user's gender
     * @param city user's city
     * @param state user's state
     * @param country user's country
     * @param profileImageFilePath user's profile picture file path
     */
    public UserProfile(String userName, String firstName, String lastName, int age, double gender, String city, String state, String country, String profileImageFilePath) {

        Update(userName, firstName, lastName, age, gender, city, state, country, profileImageFilePath);
    }

    /**
     * @param userName user's unique id
     * @param firstName user's first name
     * @param lastName user's last name
     * @param age user's age
     * @param gender user's gender
     * @param city user's city
     * @param state user's state
     * @param country user's country
     * @param profileImageFilePath user's profile picture file path
     */
    public void Update(String userName, String firstName, String lastName, int age, double gender, String city, String state, String country, String profileImageFilePath) {

        //validate user name
        if (userName == null || userName.length() < USER_NAME_MINIMUM_CHARACTER_LIMIT || userName.length() > USER_NAME_MAXIMUM_CHARACTER_LIMIT) {
            throw new IllegalArgumentException("user name length is out of range");
        }
        UserName = userName;

        //validate first name
        if (firstName == null || firstName.length() < STRING_MINIMUM_CHARACTER_LIMIT) {
            throw new IllegalArgumentException("first name is required");
        }
        FirstName = firstName;

        //validate last name
        if (lastName == null || lastName.length() < STRING_MINIMUM_CHARACTER_LIMIT) {
            throw new IllegalArgumentException("last name is required");
        }
        LastName = lastName;

        //validate city
        if (city == null || city.length() < STRING_MINIMUM_CHARACTER_LIMIT) {
            throw new IllegalArgumentException("city is required");
        }
        City = city;

        //validate state
        if (state == null || state.length() < STRING_MINIMUM_CHARACTER_LIMIT) {
            throw new IllegalArgumentException("state is required");
        }
        State = state;

        //validate country
        if (country == null || country.length() < STRING_MINIMUM_CHARACTER_LIMIT) {
            throw new IllegalArgumentException("country is required");
        }
        Country = country;

        //validate age
        if (age < MINIMUM_AGE || age > MAXIMUM_AGE) {
            throw new IllegalArgumentException("age is out of range");
        }
        Age = age;

        //validate gender
        if (gender < MINIMUM_GENDER || gender > MAXIMUM_GENDER) {
            throw new IllegalArgumentException("gender is out of range");
        }
        Gender = gender;

        //validate image file path
        if (profileImageFilePath == null || profileImageFilePath.length() < STRING_MINIMUM_CHARACTER_LIMIT) {
            throw new IllegalArgumentException("profile picture is required");
        }
        ProfileImageFilePath = profileImageFilePath;
    }

    @NonNull
    public String getUserName() {
        return UserName;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public double getGender() {
        return Gender;
    }

    public int getAge() {
        return Age;
    }

    public String getCity() {
        return City;
    }

    public String getState() {
        return State;
    }

    public String getCountry() {
        return Country;
    }

    public String getProfileImageFilePath() {
        return ProfileImageFilePath;
    }

    public void setUserName(@NonNull String userName) {
        UserName = userName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public void setGender(double gender) {
        Gender = gender;
    }

    public void setAge(int age) {
        Age = age;
    }

    public void setCity(String city) {
        City = city;
    }

    public void setState(String state) {
        State = state;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public void setProfileImageFilePath(String profileImageFilePath) {
        ProfileImageFilePath = profileImageFilePath;
    }
}
