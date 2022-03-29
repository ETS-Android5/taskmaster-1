package com.regalado.taskmaster.model;

public class Task
{
    String name;
    String body;
    State state;

    public Task(String name, String body, State state)
    {
        this.name = name;
        this.body = body;
        this.state = state;
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
}
