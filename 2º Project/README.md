# Swimming Coach

## Architecture Design

### UML
![UML]

### JAVADOC
https://duartencar.github.io/LPOO1718_T3G14/

### Design Patterns
  * Singleton - to have a single instance of certain objects, like JPanel to draw things.

## GUI Design

### Main functionalities

 1. Store athletes information:
 	* Name, birthday, picture and other personal info.
 	* Contests and practices.
 2. Timer:
 	* Register partial and total times of contests.
 3. Practices:
 	* Create practice plans and assign them to swimmers.
	* Share them with swimmers.
 4. Contests:
 	* Select athletes to race against each other and store it's perfromance.
	* Determine winner and provide race statistics from selected swimmers.
 5. Data processing:
 	* Create statistics.
	* Render graphics.
	* Share them with swimmers by Facebook.

### GUI Walkthrough

When program starts we have this interface where we can log in. note that all menu options are locked except "Exit" option.
Use "cam" as username and paswword is "123" to go to next step.

![BeforeLogin]

Once you log in all menu option are unlocked and you have a welcome message. Username field and password field become locked. They just unlock once you logg off.

![AfterLogin]


## Test Design

 I expect to test this application by:
 
 1. Performing JUnit tests, testing:
	- Sorting correctly contests time.
	- Having a test database, check if the the queried objects are correct.
	- Having a test database, check if the the insert, delete and update queries take effect.


[UML]: ./media/UML.png
[BeforeLogin]: ./media/beforeLogin.png
[AfterLogin]: ./media/afterLogin.png
