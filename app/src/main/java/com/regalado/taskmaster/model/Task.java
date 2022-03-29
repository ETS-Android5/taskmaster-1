package com.regalado.taskmaster.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Task
{
    @PrimaryKey(autoGenerate = true)
    Long id;
    String name;
    String body;
    java.util.Date dateCreated;
    State state;

    public Task(String name, String body, Date dateCreated, State state)
    {
        this.name = name;
        this.body = body;
        this.state = state;
        this.dateCreated = dateCreated;
    }

    public Long getId()
    {
        return id;
    }

    public String getBody()
    {
        return body;
    }

    public void setBody(String body)
    {
        this.body = body;
    }

    public State getState()
    {
        return state;
    }

    public void setState(State state)
    {
        this.state = state;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Date getDateCreated()
    {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated)
    {
        this.dateCreated = dateCreated;
    }
}
