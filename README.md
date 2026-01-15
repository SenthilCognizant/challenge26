Refer to GDSChallengeApp_Readme.doc file in the repo.
Session Application – GDS Challenge – ReadMe
The Session Application allows users to participate in a restaurant voting session.
Users are loaded via Batch (CSV import), and a session can be initiated either via CLI or managed through REST APIs.
The application supports:
•	User import using Spring Batch
•	Session management (invite, vote, end session)
•	Random restaurant selection
•	REST APIs with Swagger documentation
•	CLI-based interaction for session initiation.
How to Run the application
Command to Run the application - mvn spring-boot:run
On startup:
1.	The application prompts for a session initiator name via CLI.
2.	If authorized, a session starts and menu options are displayed.
3.	REST APIs are also available for session operations.
File name – resources/users.csv
 
Rest API’s:
1.	Batch Job upload 
POST API /batch/upload
Which will update H2 Embedded Database table ‘users’
 

2.	Get all users GET 
API  /users

3.	Session Management Invite Participants 
POST API /sessions/invite?participant=Ben

4.	Session Management Submit Restaurant Choice
POST API /sessions/submit?participant=Bob&restaurant=ChickenRice

5.	Session Management End Session
GET API /sessions/end?initiator=Amy

6.	Session Management Randomly pick a restaurant
Get API /sessions/pick

7.	Session Management view Participants & Choices
Get API /sessions/participants

Note: In my Local machine setup, Lombok package been included, but it’s not working as expected. So, I have included manually Getter and Setter. 

CLI Session Menu:
1.	Invite participant
2.	Submit response
3.	Choice of Restaurant made
4.	End session
5.	Randomly pick answer
6.	Exit
   
Core Components

SessionManager
•	CLI-based session handling
•	Menu-driven user interaction
•	Delegates business logic to SessionService

SessionService
•	Maintains session state
•	Handles participants, responses, and session lifecycle
•	Randomly select restaurant after session ends

Spring Batch
•	Reads users from CSV
•	Converts UserDto → User
•	Persists users to database

Testing:
•	Pure unit tests for controllers and services
•	Mockito-based mocking
•	No Spring context required for controller unit tests

Project Links
•	H2 Console - http://localhost:8080/h2-console/
•	Swagger Documentation - http://localhost:8080/swagger-ui/index.html
