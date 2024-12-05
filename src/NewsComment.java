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
        File originalFile = new File("newsComments.txt");
        File tempFile = new File("tempComments.txt");

        // Read the original file and write the filtered lines to a temporary file
        try (BufferedReader readComments = new BufferedReader(new FileReader(originalFile));
             PrintWriter writeTemp = new PrintWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = readComments.readLine()) != null) {
                String[] fields = line.split(","); // Assuming a CSV format
                if (fields.length > 1 && !fields[0].equals(content)) { // Match exact content in the second column
                    writeTemp.println(line); // Write the line to the temporary file
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error processing comments file.");
            return; // Exit if an error occurs
        }

        // Replace the original file with the temporary file
        if (!tempFile.renameTo(originalFile)) {
            System.err.println("Error replacing the original comments file.");
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