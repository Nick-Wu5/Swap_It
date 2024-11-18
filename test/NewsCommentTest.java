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

public class NewsCommentTest {

    public void testComments() {
        // Assuming UserProfile and NewsComment classes are already defined
        UserProfile author = new UserProfile("taylorswift246", "taylor.swift@gmail.com", "7685958484");

        // Create a NewsPost instance
        NewsPost post = new NewsPost(author, "Test Post Title", "/path/to/image.jpg", "2024-11-03");

        // Test adding comments
        UserProfile commenter = new UserProfile("ryangosling", "ryan.gosling@gmail.com", "emmastone");
        NewsComment comment1 = new NewsComment("Great post!", commenter);
        NewsComment comment2 = new NewsComment("Thanks for sharing!", commenter);

        post.addComment(comment1);
        post.addComment(comment2);
        comment1.incrementUpvotes();
        comment1.incrementDownvotes();

        // Verify comments were added
        assertEquals("The number of comments made should equal 2" + post.getComments().size(), "2");
        assertEquals("The author of the comment should match" + comment1.getAuthor(), "ryangosling");
        assertEquals("The contents of the comment should match" + comment2.getContent(), "Thanks for sharing!");
        assertEquals("The UpVotes should equal 0" + comment1.getUpvotes(), "0");
    }
}
