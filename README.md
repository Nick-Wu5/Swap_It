# Swap_It 🚀

## Table of Contents 📚
- [Compiling and Running SwapIt](#compiling-and-running-swapit-)
- [Submissions](#submissions-)
- [Detailed Description of Classes](#detailed-description-of-classes-)

## Compiling and Running SwapIt 🖥️

Our project is designed to run by using the `AppGUI` and `Server` Java files. To begin, start the server to handle requests and then execute the `AppGUI` file.

## Submissions 📝

* Nick Wu - Submitted Vocareum workspace
* Divya Vemireddy - Submitted Project Report
* Divya Vemireddy - Submitted Presentation
* [GitHub Repository](https://github.com/Nick-Wu5/Swap_It)

## Detailed Description of Classes 📦

### UserProfile 🧑‍🤝‍🧑
**Functionality:** The `UserProfile` class is central to managing user data, such as username, password, email, friend lists, and blocked users. It provides methods for adding and removing friends, blocking users, and saving or retrieving data from files. This class is key to creating, storing, and maintaining individual user profiles, with the `toFileFormat()` method ensuring user data is stored in an easily retrievable format.

**Testing:** The `UserProfileTest` file evaluates this class through a series of test cases. These include validating friend management methods, blocking functionality, and the accuracy of file storage through mock user data. By simulating common user operations, the test ensures the class functions correctly in all expected scenarios.

### UserSearch 🔍
**Functionality:** The `UserSearch` class allows users to locate profiles based on their usernames. It reads user data from files, identifies matches, and returns `UserProfile` objects. This functionality is essential for features like finding friends or verifying users, with seamless interaction with the `UserProfile` class to maintain data consistency.

**Testing:** Using the `UserSearchTest` file, this class is tested for scenarios including finding existing users, handling nonexistent ones, and processing users with specific attributes like friends or blocked lists. Mock data ensures robust validation of the search logic.

### PasswordProtectedLogin 🔑
**Functionality:** This class handles secure user authentication by validating usernames and passwords against stored data. It ensures only authorized access, with users prompted to retry if credentials are incorrect. This reinforces application security and prevents unauthorized entry.

**Testing:** The `PasswordProtectedLoginTest` file examines the class's ability to load user data, authenticate credentials, and reject invalid attempts. Additional tests verify its resilience against file errors and invalid data formats.

### CreateNewUser 🆕
**Functionality:** The `CreateNewUser` class extends `UserProfile` to create unique accounts. It ensures no duplicate usernames, saves new profiles to a file, and provides methods for checking existing accounts. This is critical for managing new user registrations and maintaining data integrity.

**Testing:** The `CreateNewUserTest` file validates account creation, ensuring duplicates are prevented, data is saved accurately, and accounts can be retrieved as needed. Edge cases, such as missing or invalid data, are also tested for robustness.

### NewsComment 💬
**Functionality:** The `NewsComment` class facilitates interaction with comments on posts, allowing users to add, delete, upvote, or downvote comments. By storing all interactions in a file, it ensures persistence across sessions and supports dynamic engagement.

**Testing:** The `NewsCommentTest` file tests the methods for comment management and persistence. It ensures edge cases like duplicate or invalid input are handled gracefully, with results validated against mock data.

### NewsPost 📰
**Functionality:** The `NewsPost` class represents user-generated posts, including metadata like captions, image paths, and upvotes. It supports adding and managing comments, updating vote counts, and saving post data persistently. This class ensures posts are displayed and interacted with effectively.

**Testing:** The `NewsPostTest` file evaluates core functionality such as creating posts, adding comments, and updating votes. Test cases simulate real-world scenarios to ensure the class operates smoothly in all expected use cases.

### Client 🖧
**Functionality:** The `Client` class manages user interactions like login, searching profiles, and creating posts. It communicates with the server to process user actions and update data, providing a seamless interface for users to engage with the application.

**Testing:** The `ClientTest` file simulates user actions such as posting, searching, and friend management. Tests validate server-client communication, ensuring responses are accurate and timely.

### Server 🛠️
**Functionality:** The `Server` class processes requests from the client side, ensuring secure and efficient handling of data. It manages authentication, post and comment handling, and friend management, while preventing errors like duplicate usernames.

**Testing:** The `ServerTest` file validates the server's ability to handle client requests, ensuring thread safety and proper data processing. Edge cases like invalid input or disconnections are thoroughly tested.

### File I/O 🗂️
**Functionality:** File I/O underpins the application's data persistence, storing user profiles, posts, and comments in a structured format. This ensures that user data is always available and up-to-date, even after application restarts.

**Testing:** File I/O is indirectly tested through classes like `UserProfile` and `NewsPost`, ensuring file read/write operations are reliable and handle edge cases like corrupted files.

## GUI Classes 🎨

### AppGUI
**Functionality:** The `AppGUI` class serves as the main graphical interface for the Swap_It application. It organizes the application into various screens, like Sign-In, Home, and Profile, using a `CardLayout`. This ensures smooth transitions and a cohesive user experience, with dynamic server connectivity for real-time updates.

### ContentScreen
**Functionality:** The `ContentScreen` class allows users to create, delete, and manage posts or comments. It provides a visually engaging interface with dropdown menus and image previews, streamlining interaction with content.

### HomeScreen
**Functionality:** The `HomeScreen` class displays a personalized feed showcasing posts from the user's friends. Users can browse content and refresh the feed dynamically, ensuring an engaging and interactive experience.

### ProfileScreen
**Functionality:** The `ProfileScreen` class offers an overview of the user’s profile, including personal posts and key details like their friend count. Users can also view specific data, such as email and blocked lists, for complete profile management.

### SearchScreen
**Functionality:** The `SearchScreen` class enables users to find and interact with other profiles. By entering usernames, users can explore detailed profiles and engage with posts dynamically, fostering interaction within the application.
