package com.main.project1;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface StepDao {

    @Insert
    void Insert(StepData stepData);

    @Query("SELECT * from step_table WHERE user_name == :userName ORDER BY time_stamp DESC limit 3")
    List<StepData> Get(String userName);


}