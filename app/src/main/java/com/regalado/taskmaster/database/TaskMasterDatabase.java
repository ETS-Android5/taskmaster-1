package com.regalado.taskmaster.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.regalado.taskmaster.dao.TaskDao;
import com.regalado.taskmaster.model.Task;

@Database(entities = {Task.class}, version = 1)
@TypeConverters({TaskMasterDatabaseConverters.class})
public abstract class TaskMasterDatabase extends RoomDatabase
{
    public abstract TaskDao taskDao();
}
