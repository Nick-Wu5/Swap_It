import java.io.*;
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
public class NewsComment implements NewsFeed, Serializable {

    private int upvotes;  //number of upvotes per post
    private String captionOfPost; //title of post the comment is about
    private int downvotes;  //number of downvotes per post
    private String content;  //text content of post
    private String author;  //author that published post

    public NewsComment(String content, String author, String captionOfPost, int upvotes, int downvotes) {
        this.upvotes = 0;
        this.downvotes = 0;
        this.content = content;
        this.author = author;
        this.captionOfPost = captionOfPost;
    }

    public NewsComment(String content, String author, String captionOfPost) {
        this.upvotes = 0;
        this.downvotes = 0;
        this.content = content;
        this.author = author;
        this.captionOfPost = captionOfPost;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public int getDownvotes() {
        return downvotes;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public void incrementUpvotes() {
        upvotes++;
    }

    public void incrementDownvotes() {
        downvotes++;
    }

    public static void deleteComment(String content) {
        // Temporary storage for lines not matching the content to be deleted
        ArrayList<String> remainingLines = new ArrayList<>();

        // Read the original file and filter out the lines to delete
        try (BufferedReader readComments = new BufferedReader(new FileReader("newsComments.txt"))) {
            String line;
            while ((line = readComments.readLine()) != null) {
                if (!line.contains(content)) { // Keep lines that do not match the content
                    remainingLines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return; // Exit if an error occurs during reading
        }

        // Write the filtered lines back to the file
        try (PrintWriter writeComments = new PrintWriter(new FileWriter("newsComments.txt", false))) {
            for (String line : remainingLines) {
                writeComments.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public synchronized void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("newsComments.txt", true))) {
            writer.println(content + "," + author + "," + captionOfPost + "," + upvotes + "," + downvotes);
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }

    public String toString() {

        StringBuilder printThis = new StringBuilder("");

        printThis.append(author).append(": ").append(content);
        printThis.append(String.format(" - (%d, %d)", upvotes, downvotes));

        return printThis.toString();
    }
}
//push