# TaskMaster

## Lab 38: Intent Filters

## Daily Change log 4/10

- added Intent filter to `AndroidManifext.xml` file
- added logic to `AddTaskActivity` to save image from intent filter to S3 database.

### Work Time

- 5 hours

### Collaboration

- Hambalieu Jallow & Josh McCluskey

## Feature Tasks

- Add an intent filter to your application such that a user can hit the “share” button on an image in another application, choose TaskMaster as the app to share that image with, and be taken directly to the Add a Task activity with that image pre-selected.

<img src="/screenshots/lab38/imageFromGoogle.png" width="450">
<img src="/screenshots/lab38/imageFromGoogle2.png" width="450">
<img src="/screenshots/lab38/taskDetail.png" width="450">

### APK Build File

[APK build file](apk_builds/apk_lab38/app-debug.apk)

## Lab 37: S3

## Daily Change log 4/6-4/7

-  implemented logic to upload files to S3
-  implemented logic to delete images on AddTaskActivity.
-  implemented logic to display task images on TaskDetailActivity and AddTaskActivity

## Daily Change Log 4/5/2022

- added S3 dependencies `com.apmplifyframework:aws-storage-s3:1.34.0`
- added AWS S3 Storage plugin to `TaskMasterAmplify`
- added `amplify add storage`


### Work Time

- 8-10 hours (lots of bugs!)

## Feature Tasks

### Uploads

- On the “Add a Task” activity, allow users to optionally select a file to attach to that task. 
- If a user attaches a file to a task, that file should be uploaded to S3, and associated with that task.

<img src="/screenshots/lab37/addTask.png" width="450">

### Displaying Files

- On the Task detail activity, if there is a file that is an image associated with a particular Task, that image should be displayed within that activity. (If the file is any other type, you should display a link to it.)

<img src="/screenshots/lab37/taskDetail.png" width="450">

### DynamoDB

<img src="/screenshots/lab37/dynamoDB.PNG" width="700">

### Collaboration

- Hambalieu Jallow 

### Resources

[Amplify](https://docs.amplify.aws/lib/storage/getting-started/)  
[Android File Picker](https://developer.android.com/guide/topics/providers/document-provider)  

### APK Build File

[APK build file](apk_builds/apk_lab37/app-debug.apk)


## Lab 36: Cognito

## Daily Change Log 4/4/2022

- added AWS Cognito dependency to project level `build.gradle` file.
- added login, logout and signup activities
- add appropriate UI elements to display when user is logged in or logged out
- displays current logged in username on main activity

### Work Time

- 5 hours

## Feature Tasks

### User Login and Sign up

- Add Cognito to your Amplify setup. 
  Add in user login and sign up flows to your application, using Cognito’s pre-built UI as appropriate. Display the logged in user’s username somewhere relevant in your app.

<img src="/screenshots/lab36/login.png" width="450">
<img src="/screenshots/lab36/verify.png" width="450">

### User Logout

- Allow users to log out of your application.
- Displays current logged in user on main activity and renders logout button

<img src="/screenshots/lab36/logout.png" width="450">
<img src="/screenshots/lab36/loggedoutMain.png" width="450">

[APK build file](apk_builds/apk_lab36/app-debug.apk)

## Lab 33: Another Day, Another Model

## Daily Log 4/2/2022 - 4/3/2022

### Documentation & Updates

- added teams to DB, created DB relationships, updated UI elements
- manually created 3 teams running a mutation.
- added a spinner on the add task activity to select which team a task belongs to.
- added a spinner on the settings activity that filters out what is displayed on the main activity page based on which team is selected.

### Work Time

- 8-10 hours

## Overview

- Today, your app will add a second resource on your backend, consumed by your frontend.

### Resources

- [Amplify Getting Started](https://aws-amplify.github.io/docs/)

## Feature Tasks

#### Tasks Are Owned By Teams

- Create a second entity for a team, which has a name and a list of tasks. Update your tasks to be owned by a team.

- Manually create three teams by running a mutation exactly three times in your code. (You do NOT need to allow the user to create new teams.)

### Add Task Form

- Modify your Add Task form to include either a Spinner or Radio Buttons for which team that task belongs to.

<img src="/screenshots/lab33/teamList.png" width="450">

### Settings Page

- In addition to a username, allow the user to choose their team on the Settings page. Use that Team to display only that team’s tasks on the homepage.

<img src="/screenshots/lab33/settingJediGrayTask.png" width="450">
<img src="/screenshots/lab33/mainJediGrayTask.png" width="450">

### DynamoDB

<img src="/screenshots/lab33/dynamoDbTasks.png" width="700">
<img src="/screenshots/lab33/dynamoDbTeams.png" width="700">

[APK build file](apk_builds/apk_lab33/app-debug.apk)  

## Lab 32: Integrating AWS for Cloud Data Storage

## Overview

- Today, your app will gain a scalable backend by using AWS Amplify. We’ll continue to work with Amplify to add more cloud functionality for the rest of the course.

### Setup

- Continue working in your taskmaster repository. Ensure that you have created an AWS account and installed the Amplify CLI, and follow the Amplify Getting Started directions to set up your application with Amplify.

### Resources

- Amplify Getting Started

## Feature Tasks

### Tasks Are Cloudy

- Using the `amplify add api` command, create a Task resource that replicates our existing Task schema. Update all references to the Task data to instead use AWS Amplify to access your data in DynamoDB instead of in Room.

### Add Task Form

- Modify your Add Task form to save the data entered in as a Task to DynamoDB.

### Homepage

- Refactor your homepage’s RecyclerView to display all Task entities in DynamoDB.

### Documentation

- Removed room database, updated imports and refactored necessary code, added GraphQL queries.

### Testing

- Ensure that all Espresso tests are still passing (since we haven’t changed anything about the UI today, no new updates required).

[APK build file](apk_builds/apk_lab32/app-debug.apk)

## Lab 31: Testing Views with Espresso and Polishing TaskMaster

## Overview

- Today, you’ll add UI tests to your application, and polish any remaining feature tasks from previous labs.

### Setup

- Continue working in your taskmaster repository.

### Resources

- [Espresso Testing](https://developer.android.com/training/testing/espresso)

## Feature Tasks

### Espresso Testing

- Add Espresso to your application, and use it to test basic functionality of the main components of your application. For example:
  - assert that important UI elements are displayed on the page
  - tap on a task, and assert that the resulting activity displays the name of that task
  - edit the user’s username, and assert that it says the correct thing on the homepage

### Polish

- Complete any remaining feature tasks from previous days’ labs.

[APK build file](apk_builds/apk_lab31/app-debug.apk)

## Lab 29: Room

##  Overview

- Today, you’ll refactor your model layer to store Task data in a local database.

### Setup

- Continue working in your taskmaster repository.

## Resources

- [Save data in local database using Room](https://developer.android.com/training/data-storage/room)
- [Meet Android Studio](https://developer.android.com/studio/intro)

## Feature Tasks

### Task Model and Room

- Following the directions provided in the Android documentation, set up Room in your application, and modify your Task class to be an Entity.

### Add Task Form

- Modify your Add Task form to save the data entered in as a Task in your local database.

<img src="/screenshots/lab29/main.png" width="450">
<img src="/screenshots/lab28/task.png" width="450">

### Homepage

- Refactor your homepage’s RecyclerView to display all Task entities in your database.

### Detail Page

- Ensure that the description and status of a tapped task are also displayed on the detail page, in addition to the title. (Note that you can accomplish this by passing along the entire Task entity, or by passing along only its ID in the intent.)

### Documentation

- Replace your homepage screenshot, and update your daily change log with today’s changes.

### Collaboration

- Hambalieu Jallow

[APK build file](apk_builds/apk_lab29/app-debug.apk)

## Lab 28: RecyclerViews for Displaying Lists of Data

##  Overview

- Today, you’ll refactor your homepage to look snazzy, with a RecyclerView full of Task data.

### Setup

- Continue working in your taskmaster repository.

## Resources

- RecyclerView
- Android Studio user guide

## Feature Tasks

### Task Model

- Create a Task class. A Task should have a title, a body, and a state. The state should be one of “new”, “assigned”, “in progress”, or “complete”.

### Homepage

- Refactor your homepage to use a RecyclerView for displaying Task data. This should have hardcoded Task data for now.

<img src="/screenshots/lab28/main.png" width="450">
<img src="/screenshots/lab28/task.png" width="450">

Some steps you will likely want to take to accomplish this:
  - Create a ViewAdapter class that displays data from a list of Tasks.
  - In your MainActivity, create at least three hardcoded Task instances and use those to populate your RecyclerView/ViewAdapter.
  - Ensure that you can tap on any one of the Tasks in the RecyclerView, and it will appropriately launch the detail page with the correct Task title displayed.

### Documentation

- Replace your homepage screenshot, and update your daily change log with today’s changes.

### Collaboration

- Hambalieu Jallow 

[APK build file](apk_builds/apk_lab28/app-debug.apk)

## Lab 27: Data in TaskMaster

## Overview 

-Today, you’ll add the ability to send data among different activities in your application using SharedPreferences and Intents.

### Feature Tasks

#### Task Detail Page

- Create a Task Detail page.
- It should have a title at the top of the page, and a Lorem Ipsum description.

<img src="/screenshots/lab27/task1.png" width="450">
<img src="/screenshots/lab27/task2.png" width="450">
<img src="/screenshots/lab27/task3.png" width="450">

#### Settings Page

- create a settings page. It should allow users to enter their username and hit save.

<img src="/screenshots/lab27/settings1.png" width="450">
<img src="/screenshots/lab27/settings2.png" width="450">

#### HomePage

- The main page should be modified to contain three different buttons with hardcoded task titles.
- When a user taps one of the titles, it should go to the Task Detail page, and the title at the top of the page should match the task title that was tapped on the previous page.

<img src="/screenshots/lab27/main1.png" width="450">
<img src="/screenshots/lab27/main2.png" width="450">

### Documentation

- Replace your homepage screenshot and add a screenshot of your Task Detail page into your repo, and update your daily change log with today’s changes.

[APK build file](apk_builds/apk_lab27/app-debug.apk)


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
