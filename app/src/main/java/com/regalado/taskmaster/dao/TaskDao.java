package com.regalado.taskmaster.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.regalado.taskmaster.model.Task;

import java.util.List;

@Dao  // This is like a Spring JPA repository, except it implements nothing for us
public interface TaskDao
{
    @Insert
    public void insertTask(Task task);

    @Query("SELECT * FROM Task")
    public List<Task> findAll();

}
