import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

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
public class NewsComment implements NewsFeed {

    private int upvotes;  //number of upvotes per post
    private int downvotes;  //number of downvotes per post
    private String content;  //text content of post
    private UserProfile author;  //author that published post

    public NewsComment(String content, UserProfile author) {
        this.upvotes = 0;
        this.downvotes = 0;
        this.content = content;
        this.author = author;

        try (PrintWriter writer = new PrintWriter(new FileWriter("newsComments.txt", true))) {
            writer.println("Author: " + author.getUsername());
            writer.println("Content: " + content);
            writer.println("Upvotes: " + upvotes);
            writer.println("Downvotes: " + downvotes);
            writer.println("-----");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }

    public int getUpvotes() {
        return upvotes - downvotes;
    }

    public String getContent() {
        return content;
    }

    public UserProfile getAuthor() {
        return author;
    }

    public void incrementUpvotes() {
        upvotes++;
    }

    public void incrementDownvotes() {
        downvotes++;
    }
}
