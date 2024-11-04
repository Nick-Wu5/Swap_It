/**
 * Team Project - Social Media App
 * <p>
 * This program provides a social networking system that allows users to create password-protected accounts and log in
 * securely. It includes features for searching and viewing other user profiles, as well as options to add, block, or
 * remove friends. The system also supports account and relationship management for an interactive user experience.
 *
 * @version November 17, 2024
 * @authors Nick Wu, Chris Brantley, Ramya Prasanna, and Divya Vemireddy
 */
public class NewsCommentTest {
    public static void main(String[] args) {
        // Assuming UserProfile class is already defined
        UserProfile commenter = new UserProfile("commenterUser");

        // Create a NewsComment instance
        NewsComment comment = new NewsComment("This is a test comment.", commenter);

        // Test comment details
        System.out.println("Comment Author: " + comment.getAuthor().getUsername());
        System.out.println("Comment Content: " + comment.getContent());
        System.out.println("Initial Upvotes: " + comment.getUpvotes()); // Expected: 0

        // Test upvoting and downvoting
        comment.incrementUpvotes();
        comment.incrementUpvotes();
        comment.incrementDownvotes();
        System.out.println("Upvotes after voting: " + comment.getUpvotes()); // Expected: 1 (2 upvotes - 1 downvote)

        // Add more votes to verify accuracy
        comment.incrementDownvotes();
        comment.incrementUpvotes();
        System.out.println("Final Upvotes after more voting: " + comment.getUpvotes()); // Expected: 1 (3 upvotes - 2 downvotes)
    }
}
