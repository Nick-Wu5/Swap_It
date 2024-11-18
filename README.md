# Swap_It

## Table of Contents
- [Compiling and Running SwapIt](#compiling-and-running-swapit) 
- [Submissions](#submissions) 
- [Detailed Description of Classes](#detailed-description-of-classes)

## Compiling and Running SwapIt

Our project is compiled and run using the Main.java file.  This file essentially contains a demo of how the program (without GUIs) can be used by a user including how an account can be created, how friends can be added and removed, user search, making posts and comments, and user authentication.

## Submissions

* Ramya Prasanna - Submitted Vocareum workspace

## Detailed Description of Classes

### UserProfile
**Functionality:** This class stores data for each user account, including the username, password, email, list of friends, and list of blocked friends. In addition, the class includes methods for getting and setting all fields, as well as functionality to add and remove friends and block users. Finally, this class contains the methods toFileFormat() and saveToFile(), which convert user data to a string and save it to users.txt for later access. This class serves as the foundation for individual account creation and data.

**Testing:** This functionality of this class is evaluated by the UserProfileTest test case file. The setUp() method initializes three UserProfile instances (user1, user2, user3) before each test to ensure a consistent environment for testing. The methods testAddFriend(), testRemoveFriend, and testBlockUser() are tested with assertTrue and assertFalse to ensure their functionality behaves as expected. Next, the toFileFormat() method is validated for correct string output. Finally, the findUserByUsername() is tested for both existing and non-existing users to ensure correct behavior when reading from a file.

### UserSearch
**Functionality:** This class provides methods for user search functionality. Specifically, it reads all usernames stored in users.txt and compares them to the given username. If a match is found, it returns the corresponding user as an object otherwise, it returns null. To create a UserProfile object when a matching username is found, the class uses the getSearchedUser()method, which utilizes a no-argument constructor and creates the object with data from users.txt. This class directly interacts with the UserProfile class by reading and using data generated by that class.

**Testing:** The class is tested using the UserSearchTest file. First, the setUp() method creates a users.txt file with mock data. Three test cases were written to ensure the findUserByUsername() method behaves as expected: when the user exists, does not exist, and when the user has friends/blocked users. Finally, the getSearchedUser() method is tested to make sure it processes the user data in the right order to successfully create a UserProfile object. 

### PasswordProtectedLogin
**Functionality:** This class loads the users and checks if the username and password are valid. The loadUsersFromFile method reads user information from a file and loads it into two lists: users (for usernames) and passes (for passwords). The login method prompts the person to enter a username and password which is then authenticated by the authenticate method. If the login information is valid then the authenticate method returns true. If the login information is incorrect then the authenticate method returns false and prompts the person to try again.

**Testing:** This class is tested using the PasswordProtectedLoginTest file. The testLoadUsersFromFile method checks if the class correctly loads users from the users.txt file. The testAuthenticateValidCredentials method and testAuthenticateInvalidCredentials methods check if the correct information validates and incorrect information invalidates.

### CreateNewUser
**Functionality:** This class extends the UserProfile class and creates a new user including a username, password, a boolean called alreadyRegistered that checks whether the inputted username already exists in the system, a list of user profiles, and other fields and methods inherited from its parent class.  In addition, CreateNewUser contains a field storing a the name of file which contains each user’s information and new users are saved to this file using the saveUserToFile() method and users can be loaded from the file to the list of user profiles using the loadUsersFromFile() method. Finally, this class contains the method checkIfUserExists() to check whether an inputted username is already associated with a user profile and then returns the boolean value to alreadyRegistered, as well as a main method which contains a Scanner object that asks the user to input an email address, password, and also checks to see if this email address is already existing.

**Testing:** This class is tested using the CreateNewUserTest file.  This class specifically creates a new user, no duplicate usernames, and persistence with save and load.  The test first  uses a method set up by clearing the list of user profiles and the file that stores them.  Then, creating new users is tested by creating a new user and failing the test if the username already exists as well as retrieving the profile of the newly created user and failing the test if the profile is null (suggesting no profile was made) or if the username doesn’t equal the name given when instantiating a user object.  Next, duplicate usernames are tested by instantiating two user objects of the same username and then failing the test if both still exist in the list of user profiles.  Finally, the persistence of saving and loading the file’s contents is tested by creating a new user, saving their information onto the user file, and then loading their info back onto the list of user profiles, where the test fails if the size of the user profiles list is only one and if the username of the one user profile is not matching the user object.

### NewsComment
**Functionality:** This class implements the NewsFeed interface and allows users to downvote,upvote, delete, and comment on posts. In the file output the content and author is logged along with the comment controls like upvote and downvote initialized to 0. This class took into account the readability where “----” was used to separate comments in the file and each comment is written line by line. The PrintWriter is used with the FileWriter to write to newComments.txt. This was written so when the machine is shut down the file that stores all the comments will be able to be read when the machine is started back up, taking that into account the IOException was implemented to address any errors during file writing. Both upvotes and downvotes are incremented to go up by one, and then the class uses the getUpvote() method to return the difference of upvotes and downvotes. 

**Testing:** This class is tested using the NewsCommentTest file. This test case implements UserProfile instances that can stand in place of actual users. This will simulate a conversation in the comments and the way that they interact with the other users comments such as downvoting or upvoting the comment. The testing process is first initializing a comment with the content and author and then it is retrieved and prints the details such as the username, content, and the comment upvotes. Then the test has the upvote and downvote test that will increment to two upvotes and then one downvote which is then printed out and returns the calculated upvote count.  

### NewsPost
**Functionality:** This class implements the NewsFeed interface that displays a brief headline for the post, the username, date posted, and a counter to upvote and downvote the post. And for the post there is an imagePath which is the file path associated with the image in a post. The ArrayList<NewsComment> comment holds the comments for the users post, each comment is an instance which is found in the class NewsComment which includes information about the user and each comment. The comments are initialized in the constructor as empty so that it could store other users comments, along with the upvotes and downvotes initialized to 0. And after all the attributes are initialized it is written to the file using the printWriter object to the newPosts.txt file. 

**Testing:** This class is tested using the NewsPostTest file, UserProfile instances are implemented to simulate a user posting to the newsfeed and then it is tested to see if the values of title, date, and imagePath are returned and match the expected values if even one of them fails to return the test fails. The post upvotes and downvotes are also incremented to test if the getUpvotes() will return the correct calculated upvotes to the post. Checks if the getComments() will return the correct number of comments entered along with the addComment() if any comments could be added to the post, test fails if any of the comment details are not returned. 

### Client
**Functionality:** This class holds all the code for the user to be able to login or register. If the user decides to register, the server connects to the client to see if the user’s information already exists in the file, if it exists the user will be prompted to login instead. After the user has logged in they will be prompted to the main menu which consists of 5 functions: Search, Post, Friends, Account, and Exit. The “Search” function will allow users to find other users or the user can use the “Friends” function to look at the mutuals they already have or block mutuals. The “Account” function will allow the user to see their own profile along with the posts they have created or click a separate function where they can see just their account info. With the “Post” function the users will be able to either create or delete a post, with creating a post the user will be allowed to add a caption and image, and with deleting a post the user will have to enter the caption of the post they wish to be deleted. 

**Testing:**  This class is tested using the ClientTest file, the test simulates a user trying to login, search, register, and post. The test case first checks if the user is able to login and if when registering the program is able to detect that the user already exists. With testing if the post function works, this test case sees if the user is able to make a post along with creating a caption and posting an image, after making a post the case tests if it is able to delete a post using a caption that was created. The search function is examined to see if the user is able to search for friends, and when the friend user is found the program must display the username, posts, and friends. The friend function is tested to see if the program can allow the user to add new friends by searching up their username and if they can block certain users too. The test case also evaluates if the program is able to pull up its own users' posts and if the user is able to see them, or if the user can pull up just their account details. 

### Server
**Functionality:** This class extends the PasswordProtectedLogin class as well as implementing the Runnable interface. The Server class will carry out all the functions that the user carries out from the client side and reads the file. When the user prompts the program to look for a user, the server class will look through the users.txt file to examine if there are any identical usernames so that the program can avoid duplicate usernames or if someone has already been registered. For the functions of logging in and registering, there was a do-while loop implemented that ends when the Server is able to receive the password and username. The server class connects with the client in order to delete or create a post, and add or block friends. 

**Testing:** The Server class is tested using the ServerTest file, in this file contains content that makes sure that the Server class is thread safe also testing if it is connection safe and if there is an invalid port. The ServerTest file also looks for the system outputs, such as the comments or captions to make sure that the posts that are uploaded are separated and also display the users inputted information. It tests if the client and server are connected and that the socket connection has not timed out. 

### File I/O
**Functionality/Testing:** The file IO is used to store all the information that the user inputs on the client side, Creating users will be added to the users.txt file where the program will read when the user is registering, if there is any duplicate information found in the users.txt file it will prompt the user to login instead. This file is also used with the terminal prompts found on the main menu such as the “Search” and the “Friends” function which will refer back to user.txt in case the user wants to search for friends, add, or block them. The newsComments.txt file is used to contain all the comments that are under the user’s or friends post. The newsPost.txt file is used to contain all the information such as the user, caption, and the imagepath, if a post wants to be deleted the program will refer to the newsPost.txt to track the caption associated with the post that the user wants to delete.
