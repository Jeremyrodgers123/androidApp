package com.main.project1;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface FitnessProfileDao {
    @Insert
    void Insert(FitnessProfile fitnessProfile);

    @Update
    void Update(FitnessProfile fitnessProfile);

    @Query("SELECT * from fitness_profile_table WHERE user_name == :userName")
    FitnessProfile Get(String userName);
}
