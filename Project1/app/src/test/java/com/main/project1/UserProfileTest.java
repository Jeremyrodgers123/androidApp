package com.main.project1;

import org.junit.Test;
import static org.junit.Assert.*;

public class UserProfileTest {

    @Test
    public void ValidConstructor() {
        //arrange

        //act
        UserProfile profile = new UserProfile("JD1000", "John", "Doe", 30, 1,
                "Salt Lake City", "Utah", "USA", "C:\\");

        //assert
        assertEquals("JD1000", profile.getUserName());
        assertEquals("John", profile.getFirstName());
        assertEquals("Doe", profile.getLastName());
        assertEquals(30, profile.getAge());
        assertEquals(1, profile.getGender(), 0);
        assertEquals("Salt Lake City", profile.getCity());
        assertEquals("USA", profile.getCountry());
        assertEquals("C:\\", profile.getProfileImageFilePath());
    }

    @Test(expected = IllegalArgumentException.class)
    public void UserNameIsNull() {
        //act
        UserProfile profile = new UserProfile(null, "John", "Doe", 30, 1,
                "Salt Lake City", "Utah", "USA", "C:\\");
    }

    @Test(expected = IllegalArgumentException.class)
    public void UserNameIsTooShort() {
        //act
        UserProfile profile = new UserProfile("1234", "John", "Doe", 30, 1,
                "Salt Lake City", "Utah", "USA", "C:\\");
    }

    @Test(expected = IllegalArgumentException.class)
    public void UserNameIsTooLong() {
        //act
        UserProfile profile = new UserProfile("123456789012345678901", "John", "Doe", 30, 1,
                "Salt Lake City", "Utah", "USA", "C:\\");
    }

    @Test(expected = IllegalArgumentException.class)
    public void FirstNameIsNull() {
        //act
        UserProfile profile = new UserProfile("JD1000", null, "Doe", 30, 1,
                "Salt Lake City", "Utah", "USA", "C:\\");
    }

    @Test(expected = IllegalArgumentException.class)
    public void FirstNameIsTooShort() {
        //act
        UserProfile profile = new UserProfile("1", "", "Doe", 30, 1,
                "Salt Lake City", "Utah", "USA", "C:\\");
    }

    @Test(expected = IllegalArgumentException.class)
    public void LastNameIsNull() {
        //act
        UserProfile profile = new UserProfile("JD1000", "John", null, 30, 1,
                "Salt Lake City", "Utah", "USA", "C:\\");
    }

    @Test(expected = IllegalArgumentException.class)
    public void LastNameIsTooShort() {
        //act
        UserProfile profile = new UserProfile("1", "John", "", 30, 1,
                "Salt Lake City", "Utah", "USA", "C:\\");
    }

    @Test(expected = IllegalArgumentException.class)
    public void AgeIsTooLow() {
        //act
        UserProfile profile = new UserProfile("JD1000", "John", "Doe", 0, 1,
                "Salt Lake City", "Utah", "USA", "C:\\");
    }

    @Test(expected = IllegalArgumentException.class)
    public void AgeIsTooHigh() {
        //act
        UserProfile profile = new UserProfile("JD1000", "John", "Doe", 151, 1,
                "Salt Lake City", "Utah", "USA", "C:\\");
    }

    @Test(expected = IllegalArgumentException.class)
    public void CityIsNull() {
        //act
        UserProfile profile = new UserProfile("JD1000", "John", "Doe", 30, 1,
                null, "Utah", "USA", "C:\\");
    }

    @Test(expected = IllegalArgumentException.class)
    public void CityIsTooShort() {
        //act
        UserProfile profile = new UserProfile("1", "John", "", 30, 1,
                "", "Utah", "USA", "C:\\");
    }

    @Test(expected = IllegalArgumentException.class)
    public void StateIsNull() {
        //act
        UserProfile profile = new UserProfile("JD1000", "John", "Doe", 30, 1,
                "Salt Lake City", null, "USA", "C:\\");
    }

    @Test(expected = IllegalArgumentException.class)
    public void StateIsTooShort() {
        //act
        UserProfile profile = new UserProfile("1", "John", "", 30, 1,
                "Salt Lake City", "", "USA", "C:\\");
    }

    @Test(expected = IllegalArgumentException.class)
    public void CountryIsNull() {
        //act
        UserProfile profile = new UserProfile("JD1000", "John", "Doe", 30, 1,
                "Salt Lake City", "Utah", null, "C:\\");
    }

    @Test(expected = IllegalArgumentException.class)
    public void CountryIsTooShort() {
        //act
        UserProfile profile = new UserProfile("1", "John", "", 30, 1,
                "Salt Lake City", "Utah", "", "C:\\");
    }

    @Test(expected = IllegalArgumentException.class)
    public void ProfileImagePathIsNull() {
        //act
        UserProfile profile = new UserProfile("JD1000", "John", "Doe", 30, 1,
                "Salt Lake City", "Utah", "USA", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ProfileImagePathIsTooShort() {
        //act
        UserProfile profile = new UserProfile("1", "John", "", 30, 1,
                "Salt Lake City", "Utah", "USA", "");
    }
}