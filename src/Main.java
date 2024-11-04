/**
 * Team Project - Social Media App
 * <p>
 * This program provides a social networking system that allows users to create password-protected accounts and log in
 * securely. It includes features for searching and viewing other user profiles, as well as options to add, block, or
 * remove friends. The system also supports account and relationship management for an interactive user experience.
 *
 * @version November 3, 2024
 * @authors Nick Wu, Chris Brantley, Ramya Prasanna, and Divya Vemireddy
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

    }
}
