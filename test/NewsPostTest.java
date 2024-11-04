import static org.junit.Assert.*;

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

public class NewsPostTest {

    public void testPost() {
        // Assuming UserProfile and NewsComment classes are already defined
        UserProfile author = new UserProfile("taylorswift246", "taylor.swift@gmail.com", "7685958484");

        // Create a NewsPost instance
        NewsPost post = new NewsPost(author, "Test Post Title", "/path/to/image.jpg", "2024-11-03");

        // Test post details
        assertEquals("The usernames should match the user object", post.getAuthor().getUsername(), "taylorswift246");
        assertEquals("The title should match the post object", post.getTitle(), "Test Post Title");
        assertEquals("The image path should match the post object", post.getImagePath(), "/path/to/image.jpg");
        assertEquals("The date should match the post object" + post.getDate(), "2024-11-03");
        assertEquals("The upvotes should equal 0" + post.getUpvotes(), "0");

        // Test upvoting and downvoting
        post.incrementUpvotes();
        post.incrementUpvotes();
        assertEquals("The upvotes should equal 2" + post.getUpvotes(), "2");

        post.incrementDownvotes();
        assertEquals("The upvotes should equal 1 after being decremented" + post.getUpvotes(), "1");
    }
}
