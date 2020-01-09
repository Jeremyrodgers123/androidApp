package com.main.project1;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserProfileDao {

    @Insert
    void Insert(UserProfile userProfile);

    @Update
    void Update(UserProfile userProfile);

    @Query("SELECT * from user_profile_table WHERE user_name == :userName")
    UserProfile Get(String userName);

}