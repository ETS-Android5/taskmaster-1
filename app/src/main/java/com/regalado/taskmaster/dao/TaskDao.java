package com.regalado.taskmaster.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.regalado.taskmaster.model.Task;

@Dao  // This is like a Spring JPA repository, except it implements nothing for us
public interface TaskDao
{
    @Insert
    public void insertTask(Task task);


}
