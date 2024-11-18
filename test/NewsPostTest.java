import org.junit.Test;
import java.util.ArrayList;

import static org.junit.Assert.*;

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

public class NewsPostTest {

    @Test
    public void testPost() {
        // Assuming UserProfile and NewsComment classes are already defined
        UserProfile author = new UserProfile("taylorswift246", "taylor.swift@gmail.com", "7685958484");

        // Create a NewsPost instance
        NewsPost post = new NewsPost("taylorswift246", "Test Post Caption", "/path/to/image.jpg", "2024-11-03");

        // Test post details
        assertEquals("The usernames should match the user object", post.getAuthor(), "taylorswift246");
        assertEquals("The title should match the post object", post.getCaption(), "Test Post Caption");
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

    @Test
    public void testToString() {
        NewsPost post = new NewsPost("author", "Test Title", "/path/to/image.jpg", "2024-11-03");
        String expected = "author: author\n" +
                "caption: Test Title\n" +
                "imagePath: /path/to/image.jpg\n" +
                "date: 2024-11-03\n" +
                "upvotes: 0\n" +
                "downvotes: 0\n" +
                "comments: \n";
        assertEquals("The toString method should return the correct string", expected, post.toString());
    }

    @Test
    public void testFindComments() {
        NewsPost post = new NewsPost("author", "Test Title", "/path/to/image.jpg", "2024-11-03");
        NewsComment comment1 = new NewsComment("user1", "Great post!", "2024-11-03", 0, 0);
        post.addComment(comment1);

        ArrayList<NewsComment> comments = NewsPost.findComments("Test Title");
        assertTrue("The comments list should contain the added comment", comments.contains(comment1));
    }

    @Test
    public void testFindCommentsForUser() {
        NewsPost post = new NewsPost("author", "Test Title", "/path/to/image.jpg", "2024-11-03");
        NewsComment comment1 = new NewsComment("user1", "Great post!", "2024-11-03", 0, 0);
        post.addComment(comment1);

        ArrayList<NewsComment> userComments = NewsPost.findCommentsForUser("user1");
        assertTrue("The user comments list should contain the comment by user1", userComments.contains(comment1));
    }
    @Test
    public void testDeleteComment() {
        // Create a NewsPost object
        NewsPost post = new NewsPost("author", "Test Title", "/path/to/image.jpg", "2024-11-03");

        // Add a comment to the post
        NewsComment comment = new NewsComment("user1", "Great post!", "2024-11-03", 0, 0);
        post.addComment(comment);

        // Verify that the comment has been added
        assertTrue("The post should contain the added comment", post.getComments().contains(comment));

        // Assuming you have implemented the deleteComment method correctly, delete the comment
        post.deleteComment("Great post!");

        // Verify that the comment is removed
        assertFalse("The post should no longer contain the deleted comment", post.getComments().contains(comment));
    }
}
// method