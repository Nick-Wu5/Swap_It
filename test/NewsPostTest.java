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
public class NewsPostTest {

    public static void main(String[] args) {
        // Assuming UserProfile and NewsComment classes are already defined
        UserProfile author = new UserProfile("johndoe,,,johndoe@example.com,password123");

        // Create a NewsPost instance
        NewsPost post = new NewsPost(author, "Test Post Title", "/path/to/image.jpg", "2024-11-03");

        // Test post details
        System.out.println("Post Author: " + post.getAuthor().getUsername());
        System.out.println("Post Title: " + post.getTitle());
        System.out.println("Post Image Path: " + post.getImagePath());
        System.out.println("Post Date: " + post.getDate());
        System.out.println("Initial Upvotes: " + post.getUpvotes()); // Expected: 0

        // Test upvoting and downvoting
        post.incrementUpvotes();
        post.incrementUpvotes();
        post.incrementDownvotes();
        System.out.println("Upvotes after voting: " + post.getUpvotes()); // Expected: 1

        // Test adding comments
        UserProfile commenter = new UserProfile("commenter");
        NewsComment comment1 = new NewsComment("Great post!", commenter);
        NewsComment comment2 = new NewsComment("Thanks for sharing!", commenter);

        post.addComment(comment1);
        post.addComment(comment2);

        // Verify comments were added
        System.out.println("Number of comments: " + post.getComments().size()); // Expected: 2

        // Display each comment's details
        for (NewsComment comment : post.getComments()) {
            System.out.println("Comment Author: " + comment.getAuthor().getUsername());
            System.out.println("Comment Content: " + comment.getContent());
            System.out.println("Comment Upvotes: " + comment.getUpvotes());
        }
    }
}
