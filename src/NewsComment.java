import java.io.*;

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

        try (PrintWriter writer = new PrintWriter(new FileWriter("newsComments.txt", true))) {
            writer.println(content + "," + author + "," + captionOfPost + "," + upvotes + "," + downvotes);
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }

    public NewsComment(String content, String author, String captionOfPost) {
        this.upvotes = 0;
        this.downvotes = 0;
        this.content = content;
        this.author = author;
        this.captionOfPost = captionOfPost;

        try (PrintWriter writer = new PrintWriter(new FileWriter("newsComments.txt", true))) {
            writer.println(content + "," + author + "," + captionOfPost + "," + upvotes + "," + downvotes);
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

    public String getAuthor() {
        return author;
    }

    public void incrementUpvotes() {
        upvotes++;
    }

    public void incrementDownvotes() {
        downvotes++;
    }

    public void deleteComment(String content) {

        try (PrintWriter tempWrite = new PrintWriter(new FileWriter("tempComments.txt"));
             PrintWriter writeComments = new PrintWriter(new FileWriter("newsComments.txt", false));
             BufferedReader tempRead = new BufferedReader(new FileReader("tempComments.txt"));
             BufferedReader readComments = new BufferedReader(new FileReader("newsComments.txt"))) {
            String line;
            while ((line = readComments.readLine()) != null) {
                if (line.contains(content)) {
                    continue;
                }
                tempWrite.println(line);
            }
            while ((line = tempRead.readLine()) != null) {
                writeComments.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
//push