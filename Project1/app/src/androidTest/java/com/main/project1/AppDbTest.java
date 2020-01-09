package com.main.project1;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AppDbTest {

    private AppDb _db;
    private UserProfileDao _userProfileDao;
    private FitnessProfileDao _fitnessProfileDao;

    @Before
    public void CreateDb() {
        Context context = ApplicationProvider.getApplicationContext(); ;
        _db = Room.inMemoryDatabaseBuilder(context, AppDb.class).build();
        _userProfileDao = _db.GetUserProfileDao();
        _fitnessProfileDao = _db.GetFitnessProfileDao();
    }

    @After
    public void CloseDb() {
        _db.close();
    }

    @Test
    public void InsertAndGetUserProfileSuccess() {

        //arrange
        UserProfile userProfile = new UserProfile("JD1000", "John", "Doe", 30, 1,
                "Salt Lake City", "Utah", "USA", "C:\\");

        //act
        _userProfileDao.Insert(userProfile);
        UserProfile userProfileFromDatabase = _userProfileDao.Get(userProfile.getUserName());

        //assert
        assertEquals(userProfile.getUserName(), userProfileFromDatabase.getUserName());
    }

    @Test
    public void GetUserProfileFailure() {

        //arrange
        UserProfile userProfile = new UserProfile("JD1000", "John", "Doe", 30, 1,
                "Salt Lake City", "Utah", "USA", "C:\\");

        //act
        _userProfileDao.Insert(userProfile);
        UserProfile userProfileFromDatabase = _userProfileDao.Get("not_a_valid_username");

        //assert
        assertNull(userProfileFromDatabase);
    }

    @Test
    public void UpdateUserProfile() {

        //arrange
        UserProfile userProfile = new UserProfile("JD1000", "John", "Doe", 30, 1,
                "Salt Lake City", "Utah", "USA", "C:\\");
        _userProfileDao.Insert(userProfile);

        //act
        userProfile.setAge(25);;
        _userProfileDao.Update(userProfile);
        UserProfile userProfileFromDatabase = _userProfileDao.Get("JD1000");

        //assert
        assertEquals(25, userProfileFromDatabase.getAge());
    }

    @Test
    public void InsertAndGetFitnessProfileSuccess() {

        //arrange
        UserProfile userProfile = new UserProfile("JD1000", "John", "Doe", 30, 1,
                "Salt Lake City", "Utah", "USA", "C:\\");
        FitnessProfile fitnessProfile = new FitnessProfile(userProfile, 5, 7, 150, FitnessProfile.FitnessLevel.Sedentary, FitnessProfile.WeightGoal.Gain, 5);

        //act
        _fitnessProfileDao.Insert(fitnessProfile);
        FitnessProfile fitnessProfileFromDatabase = _fitnessProfileDao.Get(userProfile.getUserName());

        //assert
        assertEquals(fitnessProfile.UserName, fitnessProfileFromDatabase.UserName);
    }

    @Test
    public void GetFitnessProfileFailure() {

        //arrange
        UserProfile userProfile = new UserProfile("JD1000", "John", "Doe", 30, 1,
                "Salt Lake City", "Utah", "USA", "C:\\");
        _userProfileDao.Insert(userProfile);
        FitnessProfile fitnessProfile = new FitnessProfile(userProfile, 5, 7, 150, FitnessProfile.FitnessLevel.Sedentary, FitnessProfile.WeightGoal.Gain, 5);
        _fitnessProfileDao.Insert(fitnessProfile);

        //act
        FitnessProfile fitnessProfileFromDatabase = _fitnessProfileDao.Get("not_a_valid_username");

        //assert
        assertNull(fitnessProfileFromDatabase);
    }

    @Test
    public void UpdateFitnessProfile() {

        //arrange
        UserProfile userProfile = new UserProfile("JD1000", "John", "Doe", 30, 1,
                "Salt Lake City", "Utah", "USA", "C:\\");
        _userProfileDao.Insert(userProfile);
        FitnessProfile fitnessProfile = new FitnessProfile(userProfile, 5, 7, 150, FitnessProfile.FitnessLevel.Sedentary, FitnessProfile.WeightGoal.Gain, 5);
        _fitnessProfileDao.Insert(fitnessProfile);

        //act
        fitnessProfile.Weight_lb = 200;
        _fitnessProfileDao.Update(fitnessProfile);
        FitnessProfile fitnessProfileFromDatabase = _fitnessProfileDao.Get("JD1000");

        //assert
        assertEquals(200, fitnessProfileFromDatabase.Weight_lb);
    }

}
