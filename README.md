## Name
Quest Board - Gamified Task Tracking App

## Description
The purpose of this app is to make doing real-life chores more exciting by putting a video game spin on them.
Users create "quests" (i.e., tasks that need to be completed) to be tracked within the app. Each quest has a certain amount of experience points associated with it. When a user compeltes a quest, they are awarded experiance points; if they've received enough experience points after completing a quest, then they will level up.

Currently, it is in a bare-bones state; the business logic is all there, and users will receive a little reward for leveling up (popup and music), but there are plans to expand upon ways to incentivize users to compelte quests.

## Installation
For use of the application, the only requirements are:
- Latest version of Java JDK is installed
- Downloaded the latest JAR and Database (DB) file from the "Releases" page of this project

## Developement Environment
To develope the application, the the following software is required:
- Latest OpenJDK version
- Maven
- Git

To run the application locally, perform the following:
- Open a terminal
- Navigate to the 'quest-board' project directory
- Execute the following command: mvn clean javafx:run

**NOTE:** Uncomment the 'ConnectH2.initDatabase();' line of code in the App.java file to initially generate a DB file.

## Support
N/A

## Authors and acknowledgment
Show your appreciation to those who have contributed to the project.

## License
For open source projects, say how it is licensed.

## Project status
The app is in a relatively stable state; while additional features/fixes may come along in the future, the project itself is mostly done with planned development.
