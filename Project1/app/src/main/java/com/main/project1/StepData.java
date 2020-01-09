package com.main.project1;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.time.LocalDateTime;

@Entity(tableName = "step_table", primaryKeys = {"time_stamp","user_name"})
public class StepData {

    @NonNull
    @ColumnInfo(name = "step_count")
    public Float StepCount;

    @NonNull
    @ColumnInfo(name = "time_stamp")
    @TypeConverters(StepData.class)
    public LocalDateTime TimeStamp;

    @NonNull
    @ColumnInfo(name = "user_name")
    public String UserName;

//    public static class LocalDateTimeConverters {}
    @TypeConverter
    public static LocalDateTime GetTimeStamp (String stringDateTime) {
        return LocalDateTime.parse(stringDateTime);
    }

    @TypeConverter
    public static String GetTimeStampString(LocalDateTime localDateTime){
        return localDateTime.toString();
    }
}
