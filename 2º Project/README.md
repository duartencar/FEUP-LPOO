# Swimming Coach

## Architecture Design

### UML
![UML]

### JAVADOC
https://github.com/duartencar/FEUP-LPOO/tree/master/2%C2%BA%20Project/docs

### EXECUTABLE FILES
https://github.com/duartencar/FEUP-LPOO/tree/master/2%C2%BA%20Project/exec

### Design Patterns
  * Singleton - in main container

## GUI Design

### Main functionalities

 1. Store athletes information:
 	* Name, birthday, picture and other personal info.
 	* Contests.
 2. Timer:
 	* Register partial and total times of contests.

### GUI Walkthrough

#### Disclaimer
This application was designed to a smartphone usage, but after talking with the class professor I decided to use Java SWING, replicating an SmartPhone screen size and interaction. In future, I will try to produce a real Android App.

When program starts we have this interface where we can log in. note that all menu options are locked except "Exit" option.
Use "cam" as username and paswword is "123" to go to next step.

![BeforeLogin]

Once you log in all menu option are unlocked and you have a welcome message. Username field and password field become locked. They just unlock once you logg off.

![AfterLogin]

If you click on swimmers panel it is displayed all swimmers that a coach has access to. If user is a pool master it has access to all swimmers of his pool, if not it only has access to his swimmers. You can see that a blue button ("ADD Swimmer") is present. if you click it, a series of message boxes will allow you to add a swimmer to the database.

![BeforeSwimmers]

If you click in a swimmer area, that swimmer will be highlighted and three more buttons appear. If you click on "DELETE Swimmer", the selected swimmer will be deleted from database. If you click on "SEE Contests" you will be taken to contests panel were it will be shown contests of the selected swimmer, more on that later. If you click on "ADD Contest" button a message box will appear, asking if you want to add a new contest, or if you want to add a previous contest. If you click on the second option a series of message boxes will appear to allow you to add an older contest. IF you click on the first you will be taken to timmer panel.

![AfterSwimmers] ![OldOrNewMessage] 

In the timer you can select contest distance, contest style, pool size and you have a check box. If you check that option, you will have to click on the button in the lowest part of the screen every time a swimmer touches the wall during contest, so you can count the partials, and the program will check if you have previous records doing that type of contest (same distance, style and pool size). If you have it will display improvements or worst partials differentials. Below are the various states of the timer:

![initialTimer] ![midTimer] ![lastPartial] ![finalPartial]

Return now to the swimmers panel. Select a swimmer (select me) and click "SEE Contests". You will be taken to contests panel. Right away you can notice that only the contests of the swimmer you selected are showing up. IFin the swimmer combo box you select all, contests of all swimmers appear. So that is a filter. You have three more filters, so you can see only the contests you want.

![contestsBefore] ![contestsAfter]

That's it. Walkthrough done. If you click "Log Off" evertything becames locked again if you click "Exit" the application closes. 

## Difficulties and conclusions
The highest difficulties were interact with the database, be constantly updating panel and timmer panel was a big investment of time. This last feature I thought it would be easier. A feature that I wasn't cappable of doing was to load profile pictures to the database, and I lost a lot of time trying to do it. The fact that I'm solo, had no team, made things even more difficult. But in the end I think I used all the knowledge that LPOO had to offer.

## Test Design

 I expect to test this application by:
 
 1. Performing JUnit tests, testing:
	- Sorting correctly contests time.
	- Having a test database, check if the the queried objects are correct.
	- Having a test database, check if the the insert, delete and update queries take effect.

## Setup/Installation
To run the project you just need to go to /exec folder and double click on "Swimming Coach App.jar".
Jar dependencies are in "Swimming Coach App_lib" folder.
"database" folder contains a SQLite3 database wich the application needs to work. Media is where swimmers profile pictures are stored.
You must mantain this tree hieararchy so that everything runs smothly.

[UML]: ./media/UML.png
[BeforeLogin]: ./media/beforeLogin.png
[AfterLogin]: ./media/afterLogin.png
[BeforeSwimmers]: ./media/swimmersPanelBeforeSelected.png
[AfterSwimmers]: ./media/swimmersPanelAfterSelected.png
[OldOrNewMessage]: ./media/oldOrNew.png
[initialTimer]: ./media/timerInitialState.png
[midTimer]: ./media/timerWithComparisonMid.png
[lastPartial]: ./media/timerLastPartialState.png
[finalPartial]: ./media/timerFinalState.png
[contestsBefore]: ./media/contestsPanelBeforeFilter.png
[contestsAfter]: ./media/contestsPanelAfterFilter.png
