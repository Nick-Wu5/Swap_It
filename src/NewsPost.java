import javax.xml.stream.events.Comment;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class NewsPost implements NewsFeed {
    private UserProfile author;
    private String title;
    private String imagePath;
    private String date;
    private int upvotes;
    private int downvotes;
    private ArrayList<NewsComment> comments;

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
        return upvotes-downvotes;
    }
    public ArrayList<NewsComment> getComments() {
        return comments;
    }
    public void addComment(NewsComment comment) {
        comments.add(comment);
    }
}
