-- passwords are saved in their hashed state, auto-populated unhashed passwords are listed below for demonstration purposes
-- password for user 1 is Password123*
-- password for user 2 is 404PasswordNotFound
-- password for user 3 is ExamplePassword
-- password for user 4 is YoThisIsMyPassword
-- password for user 5 is MyPassword69420

INSERT INTO ers_users (
	ers_userID, 
	ers_username, 
	ers_password, 
	user_firstname,
	user_lastname, 
	user_email,
	user_roleID
) VALUES (
	1,
	"Username",
	"144656446c77a6b15e7d2be7cfc4ae78",
	"George",
	"Lucas",
	"email",
	1
), (
	2,
	"Username2",
	"a8ddae08ada0a7a067f1e61a26ff154a",
	"Johnny",
	"Depp",
	"email",
	2
), (
	3,
	"Username3",
	"21c65cd155effb376a6dd8e68db58840",
	"Owen",
	"Wilson",
	"email",
	2 
), (
	4,
	"Username4",
	"7c89d143715e59c1967e4e3040a6f9e1",
	"Nicholas",
	"Cage",
	"email",
	1 
), (
	5,
	"Username5",
	"051fb13f3e494676fe8a769133ffb348",
	"Tommy",
	"Wiseau",
	"email",
	2
);