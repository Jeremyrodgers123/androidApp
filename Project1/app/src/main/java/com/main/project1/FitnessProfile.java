package com.main.project1;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Entity(tableName = "fitness_profile_table")
public class FitnessProfile implements Serializable {

    public enum FitnessLevel {
        Sedentary(0),
        LightlyActive(1),
        ModeratelyActive(2),
        VeryActive(3),
        ExtraActive(4);

        private int level;

        private static Map<Integer, FitnessLevel> map = new HashMap();

        static {
            for (FitnessLevel levelEnum : FitnessLevel.values()) {
                map.put(levelEnum.level, levelEnum);
            }
        }

        FitnessLevel(final int level) { this.level = level; }

        @TypeConverter
        public static FitnessLevel GetFitnessLevel(int level) {
            return map.get(level);
        }

        @TypeConverter
        public static int GetFitnessLevelInt(FitnessLevel fitnessLevel){
            return fitnessLevel.level;
        }

     }

    public enum WeightGoal {
        Lose(-1),
        Maintain(0),
        Gain (1);

        private int goal;

        private static Map<Integer, WeightGoal> map = new HashMap();

        static {
            for (WeightGoal goalEnum : WeightGoal.values()) {
                map.put(goalEnum.goal, goalEnum);
            }
        }

        WeightGoal(final int goal) { this.goal = goal; }

        @TypeConverter
        public static WeightGoal GetWeightGoal(int goal) {
            return map.get(goal);
        }

        @TypeConverter
        public static int GetWeightGoalInt(WeightGoal weightGoal){
            return weightGoal.goal;
        }
    }

    public static final int MINIMUM_WEIGHT_LB = 1;
    public static final int MAXIMUM_WEIGHT_LB = 1000;
    public static final int MINIMUM_HEIGHT_FT = 0;
    public static final int MAXIMUM_HEIGHT_FT = 12;
    public static final int MINIMUM_HEIGHT_IN = 0;
    public static final int MAXIMUM_HEIGHT_IN= 11;
    public static final int MINIMUM_WEIGHT_CHANGE_PER_WEEK_LB = 0;
    public static final int MAXIMUM_WEIGHT_CHANGE_PER_WEEK_LB = 10;

    private static final double KILOGRAMS_PER_POUND = 0.453592;
    private static final double CENTIMETERS_PER_INCH = 2.54;
    private static final int BMR_GENDER_SPAN = 166;
    private static final int CALORIC_REQUIREMENT_GENDER_SPAN = 200;
    private static final int MINIMUM_HEALTHY_WEIGHT_CHANGE_PER_WEEK_LB = 0;
    private static final int MAXIMUM_HEALTHY_WEIGHT_CHANGE_PER_WEEK_LB = 2;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "user_name")
    public String UserName;

    @TypeConverters(FitnessLevel.class)
    @ColumnInfo(name = "level")
    public FitnessLevel Level;

    @ColumnInfo(name = "height_in")
    public double Height_in;

    @ColumnInfo(name = "weight_lb")
    public int Weight_lb;

    @TypeConverters(WeightGoal.class)
    @ColumnInfo(name = "goal")
    public WeightGoal Goal;

    @ColumnInfo(name = "weight_change_per_week_lb")
    public double WeightChangePerWeek_lb;

    @ColumnInfo(name = "body_mass_index")
    public double BodyMassIndex;

    @ColumnInfo(name = "basal_metabolic_rate")
    public int BasalMetabolicRate;

    @ColumnInfo(name = "daily_caloric_requirement_to_maintain_weight")
    public int DailyCaloricRequirementToMaintainWeight;

    @ColumnInfo(name = "daily_caloric_requirement_to_meet_weight_goal")
    public int DailyCaloricRequirementToMeetWeightGoal;


    /**
     * Default Constructor
     */
    public FitnessProfile(){}

    /**
     * Constructor
     * @param userProfile user profile object
     * @param height_ft feet component of the user's height
     * @param height_in inches component of the user's height
     * @param weight_lb user's weight in pounds
     * @param fitnessLevel user's fitness level
     * @param weightGoal user's weight goal
     * @param weightChangePerWeek_lb user's desired weight change per week in pounds
     * @throws IllegalArgumentException
     */
    public FitnessProfile(UserProfile userProfile, int height_ft, int height_in, int weight_lb, FitnessLevel fitnessLevel, WeightGoal weightGoal, int weightChangePerWeek_lb) throws IllegalArgumentException {
        Update(userProfile, height_ft, height_in, weight_lb, fitnessLevel, weightGoal, weightChangePerWeek_lb);
    }

    /**
     * Performs validation and updates all member variables
     * @param userProfile user profile object
     * @param height_ft feet component of the user's height
     * @param height_in inches component of the user's height
     * @param weight_lb user's weight in pounds
     * @param fitnessLevel user's fitness level
     * @param weightGoal user's weight goal
     * @param weightChangePerWeek_lb user's desired weight change per week in pounds
     * @throws IllegalArgumentException
     */
    public void Update(UserProfile userProfile, int height_ft, int height_in, int weight_lb, FitnessLevel fitnessLevel, WeightGoal weightGoal, double weightChangePerWeek_lb) throws IllegalArgumentException {

        UserName = userProfile.getUserName();

        //validate weight
        if (weight_lb < MINIMUM_WEIGHT_LB || weight_lb > MAXIMUM_WEIGHT_LB) {
            throw new IllegalArgumentException("weight is out of range");
        }
        Weight_lb = weight_lb;

        //validate height
        if (height_ft < MINIMUM_HEIGHT_FT || height_ft > MAXIMUM_HEIGHT_FT) {
            throw new IllegalArgumentException("height is out of range");
        }
        if (height_in < MINIMUM_HEIGHT_IN || height_in > MAXIMUM_HEIGHT_IN) {
            throw new IllegalArgumentException("height is out of range");
        }
        Height_in = FeetAndInchesToTotalInches(height_ft, height_in);

        //validate fitness level
        if (fitnessLevel == null) {
            throw new IllegalArgumentException("fitness level is out of range");
        }
        Level = fitnessLevel;

        //validate weight goal
        if (weightGoal == null) {
            throw new IllegalArgumentException("weight goal is out of range");
        }
        if (weightChangePerWeek_lb < MINIMUM_WEIGHT_CHANGE_PER_WEEK_LB || weightChangePerWeek_lb > MAXIMUM_WEIGHT_CHANGE_PER_WEEK_LB) {
            throw new IllegalArgumentException("weight change is out of range");
        }
        switch (weightGoal) {
            case Lose:
                if (weightChangePerWeek_lb <= 0) {
                    throw new IllegalArgumentException("weight change must be greater than zero");
                }
                if (weight_lb - weightChangePerWeek_lb < MINIMUM_WEIGHT_LB) {
                    throw new IllegalArgumentException("weight change is out of range");
                }
                break;
            case Maintain:
                if (weightChangePerWeek_lb != 0) {
                    throw new IllegalArgumentException("weight change should be zero");
                }
                break;
            case Gain:
                if (weightChangePerWeek_lb <= 0) {
                    throw new IllegalArgumentException("weight change must be greater than zero");
                }
                if (weight_lb + weightChangePerWeek_lb > MAXIMUM_WEIGHT_LB) {
                    throw new IllegalArgumentException("weight change is out of range");
                }
                break;
        }
        Goal = weightGoal;
        WeightChangePerWeek_lb = weightChangePerWeek_lb;

        //calculate bmi, bmr, daily caloric requirement to maintain weight and daily caloric requirement to meet weight goal
        BodyMassIndex = CalculateBodyMassIndex();
        BasalMetabolicRate = CalculateBasalMetobolicRate(userProfile.getAge(), userProfile.getGender(), Weight_lb);
        DailyCaloricRequirementToMaintainWeight = CalculateDailyCaloricRequirement(BasalMetabolicRate);
        DailyCaloricRequirementToMeetWeightGoal = CalculateDailyCaloricRequirement(DailyCaloricRequirementToMaintainWeight, WeightChangePerWeek_lb);
    }

    /**
     * Returns height as a tuple of feet and inches
     * @return height in feet and inches
     */
    public Pair<Integer, Integer> GetHeightInFeetAndInches() {
        int feet = (int)(Height_in / 12);
        int inches = (int)(Height_in % 12);
        return new Pair(feet, inches);
    }

    /**
     * Determines if weight goal is healthy or not
     * @return true if healthy, otherwise false
     */
    public boolean IsWeightGoalHealthy() {
        if (Goal == WeightGoal.Gain || Goal == WeightGoal.Lose) {
            return WeightChangePerWeek_lb > MINIMUM_HEALTHY_WEIGHT_CHANGE_PER_WEEK_LB && WeightChangePerWeek_lb < MAXIMUM_HEALTHY_WEIGHT_CHANGE_PER_WEEK_LB;
        }
        return true;
    }

    /**
     * Determines if the daily requirement to meet the user's weight goal is healthy or not
     * @param userProfile user's profile object
     * @return true if healthy, otherwise false
     */
    public boolean IsDailyCaloricRequirementToMeetGoalHealthy(UserProfile userProfile) {
        int femaleMinimumCaloricRequirement = 1000;

        //calculate scale factor given gender from 0 -> 1
        //if gender = 0 (female), genderScaleFactor = 0
        //if gender = 1 (male), genderScaleFactor = CALORIC_REQUIREMENT_GENDER_SPAN (200)
        double genderScaleFactor = CALORIC_REQUIREMENT_GENDER_SPAN * userProfile.getGender();

        //apply gender scale factor and return result of comparison
        return DailyCaloricRequirementToMeetWeightGoal > femaleMinimumCaloricRequirement + genderScaleFactor;
    }

    /**
     * Converts feet and inches to total inches
     * @param feet feet component of user's height
     * @param inches inches component of user's height
     * @return user's height in total inches
     */
    private double FeetAndInchesToTotalInches(int feet, int inches) {
        return ((float)feet * 12) + inches;
    }

    /**
     * Calculates body mass index
     * Uses weightm and height member variables
     * @return body mass index
     */
    private double CalculateBodyMassIndex() {
        return (703 * Weight_lb) / (Height_in * Height_in);
    }

    /**
     * Calculates the basal metobolic rate using the Mifflin-St Jeor equation
     * Also uses height member variable
     * https://www.calculator.net/bmr-calculator.html
     * @param age user's age in years
     * @param gender user's gender
     * @param targetWeight_lb user's target weight in pounds
     * @return basal metabolic rate
     */
    private int CalculateBasalMetobolicRate(int age, double gender, double targetWeight_lb) {

        //convert from lb to kg
        double weight_kg = targetWeight_lb * KILOGRAMS_PER_POUND;

        //convert from in to cm
        double height_cm = Height_in * CENTIMETERS_PER_INCH;

        //calculate male bmr
        int maleBmr = (int)(10 * weight_kg + 6.25 * height_cm - 5 * age  + 5);

        //calculate scale factor given gender from 0 -> 1
        double genderScaleFactor = BMR_GENDER_SPAN * gender;

        //apply gender scale factor
        return (int)(maleBmr - BMR_GENDER_SPAN + genderScaleFactor);
    }

    /**
     * Calculates new basal metobolic rate using age and gender.
     * Then calculates the number of calories required for the given basl metobolic rate.
     * Uses the fitness level member variable
     * http://www.checkyourhealth.org/eat-healthy/cal_calculator.php
     * @param dailyCaloriesToMaintainWeight user's daily calories to maintain weight
     * @param weightChangeLbsPerWeek user's desired weight change per week
     * @return daily caloric requirement
     */
    //http://www.checkyourhealth.org/eat-healthy/cal_calculator.php
    private int CalculateDailyCaloricRequirement(int dailyCaloriesToMaintainWeight, double weightChangeLbsPerWeek) {

        //calculate target weight
        final int caloriesPerLb = 3500;
        int dailyCalories = 0;
        double weightChangeCaloriesPerDay = (caloriesPerLb * weightChangeLbsPerWeek)/ 7;

        switch (Goal) {
            case Lose:
                dailyCalories =(int)(dailyCaloriesToMaintainWeight - Math.round(weightChangeCaloriesPerDay));
                break;
            case Maintain:
                dailyCalories = dailyCaloriesToMaintainWeight;
                break;
            case Gain:
                dailyCalories = (int)(dailyCaloriesToMaintainWeight + Math.round(weightChangeCaloriesPerDay));
                break;
        }

        return dailyCalories;
    }


    /**
     * Calculates the number of calories required for the given basl metobolic rate.
     * Uses the fitness level member variable
     * http://www.checkyourhealth.org/eat-healthy/cal_calculator.php
     * @param basalMetabolicRate user's basal metabolic rate
     * @return daily caloric requirement
     */
    private int CalculateDailyCaloricRequirement(int basalMetabolicRate) {

        switch (Level) {
            case Sedentary:
                return (int)(basalMetabolicRate * 1.2);
            case LightlyActive:
                return (int)(basalMetabolicRate * 1.375);
            case ModeratelyActive:
                return (int)(basalMetabolicRate * 1.55);
            case VeryActive:
                return (int)(basalMetabolicRate * 1.725);
            case ExtraActive:
                return (int)(basalMetabolicRate * 1.9);
        }

        //should never get here
        throw new AssertionError();
    }
}
