# TaskMaster

## Lab 26: Beginning TaskMaster

### Overview

- Today, you’ll start building an Android app that will be a main focus of the second half of the course: TaskMaster. While you’ll start small today, over time this will grow to be a fully-featured application.

### Setup

- To start, create a new directory and repo to hold this app. Name it taskmaster. Within that directory, use Android Studio to set up a new app, as discussed in class. Create a README file that includes, at minimum, a description of your app and a daily change log.

### Resources

- Android Buttons
- Android UI Events

### Feature Tasks

### Homepage

- The main page should be built out to match the wireframe. In particular, it should have a heading at the top of the page, an image to mock the “my tasks” view, and buttons at the bottom of the page to allow going to the “add tasks” and “all tasks” page.

![Home](/screenshots/lab26/main.PNG)

### Add a Task

- On the “Add a Task” page, allow users to type in details about a new task, specifically a title and a body. When users click the “submit” button, show a “submitted!” label on the page.

![Home](/screenshots/lab26/addTask.PNG)
![Home](/screenshots/lab26/submitted.PNG)

### All Tasks

- The all tasks page should just be an image with a back button; it needs no functionality.

![Home](/screenshots/lab26/allTasks.PNG)

### APK

[APK build file](apk_builds/apk_lab26/app-debug.apk)


# Lab 27: Data in TaskMaster

## Overview 

-Today, you’ll add the ability to send data among different activities in your application using SharedPreferences and Intents.

### Feature Tasks

#### Task Detail Page

- Create a Task Detail page.
- It should have a title at the top of the page, and a Lorem Ipsum description.

![Home](/screenshots/lab27/task1.png){:height="50px" width="50px" }
![Home](/screenshots/lab27/task2.png)
![Home](/screenshots/lab27/task3.png)

#### Settings Page

- create a settings page. It should allow users to enter their username and hit save.

![Home](/screenshots/lab27/settings1.png)
![Home](/screenshots/lab27/settings2.png)

#### HomePage

- The main page should be modified to contain three different buttons with hardcoded task titles.
- When a user taps one of the titles, it should go to the Task Detail page, and the title at the top of the page should match the task title that was tapped on the previous page.

![Home](/screenshots/lab27/main1.png)
![Home](/screenshots/lab27/main2.png)

### Documentation

- Replace your homepage screenshot and add a screenshot of your Task Detail page into your repo, and update your daily change log with today’s changes.

[APK build file](apk_builds/apk_lab27/app-debug.apk)
