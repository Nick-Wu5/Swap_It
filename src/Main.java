/**
 * Team Project - Social Media App
 * <p>
 * This program provides a social networking system that allows users to create password-protected accounts and log in
 * securely. It includes features for searching and viewing other user profiles, as well as options to add, block, or
 * remove friends. The system also supports account and relationship management for an interactive user experience.
 *
 * @version November 3, 2024
 * @author Nick Wu, Chris Brantley, Ramya Prasanna, and Divya Vemireddy
 */
public class Main {

    public static void main(String[] args) {

        // Creating user profiles
        UserProfile user1 = new UserProfile("taylorswift246", "taylor.swift@gmail.com", "7685958484");
        UserProfile user2 = new UserProfile("ryangosling", "ryan.gosling@gmail.com", "emmastone");
        UserProfile user3 = new UserProfile("travisscott21", "travis.scott@gmail.com", "123456789");

        // Display initial profiles
        System.out.println("Initial Profiles:");
        System.out.println(user1.toFileFormat());
        System.out.println(user2.toFileFormat());
        System.out.println(user3.toFileFormat());

        // Adding friends
        System.out.println("\nAdding Friends:");
        if (user1.addFriend(user2.getUsername())) {
            System.out.println(user1.getUsername() + " added " + user2.getUsername() + " as a friend.");
        }
        if (user1.addFriend(user3.getUsername())) {
            System.out.println(user1.getUsername() + " added " + user3.getUsername() + " as a friend.");
        }

        // Display user1's friend list
        System.out.println(user1.getUsername() + "'s friends: " + user1.getFriends());

        // Blocking a user
        System.out.println("\nBlocking a User:");
        user1.blockUser(user3.getUsername());
        System.out.println(user1.getUsername() + " blocked " + user3.getUsername() + ".");
        System.out.println(user1.getUsername() + "'s blocked friends: " + user1.getBlockedFriends());

        // Attempting to add a blocked user
        System.out.println("\nAttempting to add a blocked user:");
        if (!user1.addFriend(user3.getUsername())) {
            System.out.println("Cannot add " + user3.getUsername() + " as a friend. The user is blocked.");
        }

        // Removing a friend
        System.out.println("\nRemoving a Friend:");
        user1.removeFriend(user2.getUsername());
        System.out.println(user1.getUsername() + " removed " + user2.getUsername() + " from friends list.");
        System.out.println(user1.getUsername() + "'s updated friends list: " + user1.getFriends());

        // Creating and displaying a news post
        System.out.println("\nCreating and Displaying News Posts:");
        NewsPost post1 = new NewsPost(user1, "New Album Release", "/images/album.jpg", "2024-11-03");
        System.out.println("Post Author: " + post1.getAuthor().getUsername());
        System.out.println("Post Title: " + post1.getTitle());
        System.out.println("Post Image Path: " + post1.getImagePath());
        System.out.println("Post Date: " + post1.getDate());
        System.out.println("Initial Upvotes: " + post1.getUpvotes());

        // Upvoting and downvoting
        post1.incrementUpvotes();
        post1.incrementUpvotes();
        post1.incrementDownvotes();
        System.out.println("Upvotes after voting: " + post1.getUpvotes());

        // Adding comments
        NewsComment comment1 = new NewsComment("This is amazing!", user2);
        NewsComment comment2 = new NewsComment("Can't wait to hear it!", user3);
        post1.addComment(comment1);
        post1.addComment(comment2);

        // Upvoting comments
        comment1.incrementUpvotes();
        comment1.incrementUpvotes();
        comment2.incrementUpvotes();

        // Display comments
        System.out.println("\nComments on the post:");
        for (NewsComment comment : post1.getComments()) {
            System.out.println("Comment Author: " + comment.getAuthor().getUsername());
            System.out.println("Comment Content: " + comment.getContent());
            System.out.println("Comment Upvotes: " + comment.getUpvotes());
        }

        // Demonstrating secure account creation
        System.out.println("\nCreating New User Accounts:");
        CreateNewUser newUser1 = new CreateNewUser("johnsmith", "securePass123");
        CreateNewUser newUser2 = new CreateNewUser("janedoe", "password456");

        // Displaying user registration status
        if (!newUser1.isAlreadyRegistered()) {
            System.out.println("User 'johnsmith' registered successfully!");
        } else {
            System.out.println("User 'johnsmith' is already registered.");
        }
        if (!newUser2.isAlreadyRegistered()) {
            System.out.println("User 'janedoe' registered successfully!");
        } else {
            System.out.println("User 'janedoe' is already registered.");
        }

        // Demonstrating password-protected login
        System.out.println("\nPassword-Protected Login:");
        PasswordProtectedLogin loginSystem = new PasswordProtectedLogin();
        if (loginSystem.authenticate("johnsmith", "securePass123")) {
            System.out.println("Login successful for 'johnsmith'.");
        } else {
            System.out.println("Login failed for 'johnsmith'.");
        }

        if (loginSystem.authenticate("janedoe", "wrongPassword")) {
            System.out.println("Login successful for 'janedoe'.");
        } else {
            System.out.println("Login failed for 'janedoe'.");
        }
    }
}
