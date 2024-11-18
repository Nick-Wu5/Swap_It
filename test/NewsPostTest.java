import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
        NewsPost post = new NewsPost("taylorswift246", "Test Post Caption", "/path/to/image.jpg", "2024-11-03");

        assertEquals("The date should match the post object", "2024-11-03", post.getDate());
        assertEquals("The upvotes should equal 0", 0, post.getUpvotes());

        post.incrementUpvotes();
        post.incrementUpvotes();
        assertEquals("The upvotes should equal 2", 2, post.getUpvotes());

        post.incrementDownvotes();
        assertEquals("The upvotes should equal 1 after decrementing", 1, post.getUpvotes());
    }


    @Test
    public void testToString() {
        NewsPost post = new NewsPost("author", "Test Title", "/path/to/image.jpg", "2024-11-03");
        String expected = "---------------\n" +
                "author: author\n" +
                "caption: Test Title\n" +
                "imagePath: /path/to/image.jpg\n" +
                "date: 2024-11-03\n" +
                "upvotes: 0\n" +
                "downvotes: 0\n" +
                "Comments: 0";
        assertEquals("The toString method should return the correct string", expected, post.toString());
    }

    @Test
    public void testFindCommentsForUser() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("newsComments.txt", true))) {
            writer.println("user1,Great post!,2024-11-03,0,0");
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<NewsComment> userComments = NewsPost.findCommentsForUser("user1");
        assertFalse("The user comments list should contain the comment by user1", userComments.isEmpty());
    }
}
