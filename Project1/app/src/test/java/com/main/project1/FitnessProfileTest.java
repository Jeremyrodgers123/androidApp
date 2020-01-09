package com.main.project1;

import androidx.core.util.Pair;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FitnessProfileTest {

    private UserProfile _userProfile;

    @Before
    public void setup() {
        _userProfile = new UserProfile("JD1000", "John", "Doe", 30, 1,
                "Salt Lake City", "Utah", "USA", "C:\\");
    }

    @Test
    public void ValidConstructur() {
        //arrange
        UserProfile userProfile = new UserProfile("JD1001", "Jane", "Doe", 30, 0,
                "Salt Lake City", "Utah", "USA", "C:\\");

        //act
        FitnessProfile profile = new FitnessProfile(userProfile, 5, 7, 150, FitnessProfile.FitnessLevel.Sedentary, FitnessProfile.WeightGoal.Gain, 5);

        //assert
        assertEquals("JD1001", profile.UserName);
        assertEquals(FitnessProfile.WeightGoal.Gain, profile.Goal);
        assertEquals(FitnessProfile.FitnessLevel.Sedentary, profile.Level);
        assertEquals(5, profile.WeightChangePerWeek_lb, 0);
        assertEquals(23.5, profile.BodyMassIndex, 0.05);
        assertEquals(1433, profile.BasalMetabolicRate);
        assertEquals(1719, profile.DailyCaloricRequirementToMaintainWeight);
        assertEquals(4219, profile.DailyCaloricRequirementToMeetWeightGoal);
    }

    @Test(expected = IllegalArgumentException.class)
    public void HeightFeetIsTooLow() {
        //act
        FitnessProfile profile = new FitnessProfile(_userProfile, FitnessProfile.MINIMUM_HEIGHT_FT - 1, 7, 150, FitnessProfile.FitnessLevel.Sedentary, FitnessProfile.WeightGoal.Maintain, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void HeightFeetIsTooHigh() {
        //act
        FitnessProfile profile = new FitnessProfile(_userProfile, FitnessProfile.MAXIMUM_HEIGHT_FT + 1, 7, 150, FitnessProfile.FitnessLevel.Sedentary, FitnessProfile.WeightGoal.Maintain, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void HeightInchesIsTooLow() {
        //act
        FitnessProfile profile = new FitnessProfile(_userProfile, 5, FitnessProfile.MINIMUM_HEIGHT_IN - 1, 150, FitnessProfile.FitnessLevel.Sedentary, FitnessProfile.WeightGoal.Maintain, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void HeightInchesIsTooHigh() {
        //act
        FitnessProfile profile = new FitnessProfile(_userProfile, 5, FitnessProfile.MAXIMUM_HEIGHT_IN + 1, 150, FitnessProfile.FitnessLevel.Sedentary, FitnessProfile.WeightGoal.Maintain, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void WeightIsTooLow() {
        //act
        FitnessProfile profile = new FitnessProfile(_userProfile, 5, 7, FitnessProfile.MINIMUM_WEIGHT_LB - 1, FitnessProfile.FitnessLevel.Sedentary, FitnessProfile.WeightGoal.Maintain, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void WeightIsTooHigh() {
        //act
        FitnessProfile profile = new FitnessProfile(_userProfile, 5, 7, FitnessProfile.MAXIMUM_WEIGHT_LB + 1, FitnessProfile.FitnessLevel.Sedentary, FitnessProfile.WeightGoal.Maintain, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void FitnessLevelIsNull() {
        //act
        FitnessProfile profile = new FitnessProfile(_userProfile, 5, 7, 150, null, FitnessProfile.WeightGoal.Maintain, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void WeightGoalIsNull() {
        //act
        FitnessProfile profile = new FitnessProfile(_userProfile, 5, 7, 150, FitnessProfile.FitnessLevel.Sedentary, null, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void WeightChangePerWeekIsTooLow() {
        //act
        FitnessProfile profile = new FitnessProfile(_userProfile, 5, 7, 150, FitnessProfile.FitnessLevel.Sedentary, FitnessProfile.WeightGoal.Maintain, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void WeightChangePerWeekIsTooHigh() {
        //act
        FitnessProfile profile = new FitnessProfile(_userProfile, 5, 7, 150, FitnessProfile.FitnessLevel.Sedentary, FitnessProfile.WeightGoal.Maintain, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void WeightChangePerWeekIsTooLowForGainGoal() {
        //act
        FitnessProfile profile = new FitnessProfile(_userProfile, 5, 7, 150, FitnessProfile.FitnessLevel.Sedentary, FitnessProfile.WeightGoal.Gain, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void WeightChangePerWeekIsTooLowForLoseGoal() {
        //act
        FitnessProfile profile = new FitnessProfile(_userProfile, 5, 7, 150, FitnessProfile.FitnessLevel.Sedentary, FitnessProfile.WeightGoal.Lose, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void WeightChangePerWeekMakesWeightLossInvalid() {
        //act
        FitnessProfile profile = new FitnessProfile(_userProfile, 5, 7, FitnessProfile.MINIMUM_WEIGHT_LB + 10, FitnessProfile.FitnessLevel.Sedentary, FitnessProfile.WeightGoal.Lose, 11);
    }

    @Test(expected = IllegalArgumentException.class)
    public void WeightChangePerWeekMakesWeightGainInvalid() {
        //act
        FitnessProfile profile = new FitnessProfile(_userProfile, 5, 7, FitnessProfile.MAXIMUM_WEIGHT_LB - 10, FitnessProfile.FitnessLevel.Sedentary, FitnessProfile.WeightGoal.Gain, 11);
    }

    @Test
    public void GetFeetAndInches() {
        //arrange
        FitnessProfile profile = new FitnessProfile(_userProfile, 5, 1, 150, FitnessProfile.FitnessLevel.Sedentary, FitnessProfile.WeightGoal.Maintain, 0);

        //act
        Pair<Integer, Integer> pair = profile.GetHeightInFeetAndInches();

        //assert
        assertEquals(5, pair.first.intValue());
        assertEquals(1, pair.second.intValue());
    }

    @Test
    public void WeightGainGoalIsUnhealthy() {
        //arrange
        FitnessProfile profile = new FitnessProfile(_userProfile, 5, 1, 150, FitnessProfile.FitnessLevel.Sedentary, FitnessProfile.WeightGoal.Gain, 3);

        //act
        boolean isWeightGoalHealthy = profile.IsWeightGoalHealthy();

        //assert
        assertFalse(isWeightGoalHealthy);
    }

    @Test
    public void WeightGainGoalIsHealthy() {
        //arrange
        FitnessProfile profile = new FitnessProfile(_userProfile, 5, 1, 150, FitnessProfile.FitnessLevel.Sedentary, FitnessProfile.WeightGoal.Gain, 1);

        //act
        boolean isWeightGoalHealthy = profile.IsWeightGoalHealthy();

        //assert
        assertTrue(isWeightGoalHealthy);
    }

    @Test
    public void WeightLossGoalIsUnhealthy() {
        //arrange
        FitnessProfile profile = new FitnessProfile(_userProfile, 5, 1, 150, FitnessProfile.FitnessLevel.Sedentary, FitnessProfile.WeightGoal.Lose, 3);

        //act
        boolean isWeightGoalHealthy = profile.IsWeightGoalHealthy();

        //assert
        assertFalse(isWeightGoalHealthy);
    }

    @Test
    public void WeightLossGoalIsHealthy() {
        //arrange
        FitnessProfile profile = new FitnessProfile(_userProfile, 5, 1, 150, FitnessProfile.FitnessLevel.Sedentary, FitnessProfile.WeightGoal.Lose, 1);

        //act
        boolean isWeightGoalHealthy = profile.IsWeightGoalHealthy();

        //assert
        assertTrue(isWeightGoalHealthy);
    }

    @Test
    public void WeightMaintainGoalIsHealthy() {
        //arrange
        FitnessProfile profile = new FitnessProfile(_userProfile, 5, 1, 150, FitnessProfile.FitnessLevel.Sedentary, FitnessProfile.WeightGoal.Maintain, 0);

        //act
        boolean isWeightGoalHealthy = profile.IsWeightGoalHealthy();

        //assert
        assertTrue(isWeightGoalHealthy);
    }

    @Test
    public void DailyCaloricRequirementIsUnhealthy() {
        //arrange
        UserProfile userProfile = new UserProfile("JD1000", "John", "Doe", 80, 1,
                "Salt Lake City", "Utah", "USA", "C:\\");
        FitnessProfile profile = new FitnessProfile(userProfile, 5, 0, 100, FitnessProfile.FitnessLevel.Sedentary, FitnessProfile.WeightGoal.Lose, 10);

        //act
        boolean IsDailyCaloricRequirementHealthy = profile.IsDailyCaloricRequirementToMeetGoalHealthy(userProfile);

        //assert
        assertFalse(IsDailyCaloricRequirementHealthy);
    }

    @Test
    public void DailyCaloricRequirementIsHealthy() {
        //arrange
        UserProfile userProfile = new UserProfile("JD1000", "John", "Doe", 80, 1,
                "Salt Lake City", "Utah", "USA", "C:\\");
        FitnessProfile profile = new FitnessProfile(userProfile, 5, 0, 200, FitnessProfile.FitnessLevel.Sedentary, FitnessProfile.WeightGoal.Lose, 1);

        //act
        boolean IsDailyCaloricRequirementHealthy = profile.IsDailyCaloricRequirementToMeetGoalHealthy(userProfile);

        //assert
        assertTrue(IsDailyCaloricRequirementHealthy);
    }

}