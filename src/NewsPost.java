import javax.xml.stream.events.Comment;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

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
public class NewsPost implements NewsFeed {
    private UserProfile author;  //author profile of post
    private String title;  //title of post
    private String imagePath;  //path to image of post
    private String date;  //date of published post
    private int upvotes;  //number of upvotes per post
    private int downvotes;  //number of downvotes per post
    private ArrayList<NewsComment> comments;  //number of comments per post

    public NewsPost(UserProfile author, String title, String imagePath, String date) {
        this.author = author;
        this.title = title;
        this.imagePath = imagePath;
        this.date = date;
        this.upvotes = 0;
        this.downvotes = 0;
        this.comments = new ArrayList<>();


        try (PrintWriter writer = new PrintWriter(new FileWriter("newsPosts.txt", true))) {
            writer.println("Author: " + author.getUsername());
            writer.println("Title: " + title);
            writer.println("Image Path: " + imagePath);
            writer.println("Date: " + date);
            writer.println("Upvotes: " + upvotes);
            writer.println("Downvotes: " + downvotes);
            writer.println("Comments: " + comments.size() + " comments");
            writer.println("-----");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }

    public void incrementUpvotes() {
        upvotes++;
    }

    public void incrementDownvotes() {
        downvotes++;
    }

    public UserProfile getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getDate() {
        return date;
    }

    public int getUpvotes() {
        return upvotes - downvotes;
    }

    public ArrayList<NewsComment> getComments() {
        return comments;
    }

    public void addComment(NewsComment comment) {
        comments.add(comment);
    }
}
