import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class NewsComment implements NewsFeed {
    private int upvotes;
    private int downvotes;
    private String content;
    private UserProfile author;

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

