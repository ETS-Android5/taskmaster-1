package com.regalado.taskmaster.model;

import java.util.Date;

public class Task
{

    public Long id;
    String name;
    String body;
    java.util.Date dateCreated;
    State state;

    public Task(String name, String body, Date dateCreated, State state)
    {
        this.name = name;
        this.body = body;
        this.dateCreated = dateCreated;
        this.state = state;
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
