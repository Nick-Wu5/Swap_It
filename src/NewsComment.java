import java.io.*;

/**
 * NewsComment
 * <p>
 * Represents a comment in the social media application. Each comment is associated with
 * a specific post and includes details such as the author, content, upvotes, and downvotes.
 * Provides methods for managing comments, including saving to a file and deleting specific comments.
 *
 * @version December 7, 2024
 * @author Nick Wu, Chris Brantley, Ramya Prasanna, and Divya Vemireddy
 */
public class NewsComment implements NewsCommentInterface, Serializable {

    private int upvotes;  // Number of upvotes for the comment
    private String captionOfPost;  // Title of the post the comment is associated with
    private int downvotes;  // Number of downvotes for the comment
    private String content;  // The text content of the comment
    private String author;  // The author who wrote the comment

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

    /**
     * Retrieves the number of upvotes for the comment.
     *
     * @return the number of upvotes
     */
    public int getUpvotes() {
        return upvotes;
    }

    /**
     * Retrieves the number of downvotes for the comment.
     *
     * @return the number of downvotes
     */
    public int getDownvotes() {
        return downvotes;
    }

    /**
     * Retrieves the text content of the comment.
     *
     * @return the content of the comment
     */
    public String getContent() {
        return content;
    }

    /**
     * Retrieves the author of the comment.
     *
     * @return the author of the comment
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Increments the upvote count for the comment by 1.
     */
    public void incrementUpvotes() {
        upvotes++;
    }

    /**
     * Increments the downvote count for the comment by 1.
     */
    public void incrementDownvotes() {
        downvotes++;
    }

    /**
     * Deletes a comment from the `newsComments.txt` file by its content.
     * Reads the original file and writes all other comments to a temporary file,
     * then replaces the original file with the temporary file.
     *
     * @param content the content of the comment to delete
     */
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

    /**
     * Saves the comment to the `newsComments.txt` file in a comma-separated format.
     * Appends the comment to the file, ensuring no existing comments are overwritten.
     */
    public synchronized void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("newsComments.txt", true))) {
            writer.println(content + "," + author + "," + captionOfPost + "," + upvotes + "," + downvotes);
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }

    /**
     * Returns a string representation of the comment, including the author, content,
     * and the count of upvotes and downvotes.
     *
     * @return a string representation of the comment
     */
    public String toString() {

        StringBuilder printThis = new StringBuilder("");

        printThis.append(author).append(": ").append(content);
        printThis.append(String.format(" - (%d, %d)", upvotes, downvotes));

        return printThis.toString();
    }
}