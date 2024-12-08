import java.io.*;
import java.util.ArrayList;

/**
 * NewsPost
 * <p>
 * Represents a news post in the social media application. Each post contains details such as the author,
 * caption, image path, date, upvotes, downvotes, and associated comments. This class provides methods
 * to manage posts, including creating, deleting, and retrieving comments for a post.
 *
 * @author Nick Wu, Chris Brantley, Ramya Prasanna, and Divya Vemireddy
 * @version December 7, 2024
 */
public class NewsPost implements NewsFeed, Serializable {

    private String author;  // Author of the post
    private String caption;  // Caption of the post
    private String imagePath;  // File path to the image associated with the post
    private String date;  // Date the post was created
    private int upvotes;  // Number of upvotes the post has received
    private int downvotes;  // Number of downvotes the post has received
    private ArrayList<NewsComment> comments;  // List of comments associated with the post

    public NewsPost(String author, String caption, String imagePath, String date) {
        this.author = author;
        this.caption = caption;
        this.imagePath = imagePath;
        this.date = date;
        this.upvotes = 0;
        this.downvotes = 0;
        this.comments = new ArrayList<>();


        try (PrintWriter writer = new PrintWriter(new FileWriter("newsPosts.txt", true))) {
            writer.println(author + "," + caption + "," + imagePath + "," + date + "," + upvotes + "," + downvotes + ","
                    + comments.size());
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }

    public NewsPost() {
    }

    /**
     * Deletes a post from the "newsPosts.txt" file based on its caption.
     * The method creates a temporary file and copies all lines except the one
     * matching the specified caption. Replaces the original file with the temporary file.
     *
     * @param caption the caption of the post to delete
     */
    public static void deletePost(String caption) {
        System.out.println("Looking to delete the post: " + caption);

        // Temporary file to write updated content
        File originalFile = new File("newsPosts.txt");
        File tempFile = new File("tempFile.txt");

        try (
                BufferedReader readPost = new BufferedReader(new FileReader(originalFile));
                PrintWriter tempWrite = new PrintWriter(new FileWriter(tempFile))
        ) {
            String line;

            while ((line = readPost.readLine()) != null) {

                String[] parts = line.split(",");
                if (parts[1].equals(caption)) {
                    continue;
                }
                tempWrite.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Replace the original file with the temporary file
        if (!tempFile.renameTo(originalFile)) {
            System.out.println("Error replacing the original file.");
        }
    }

    /**
     * Finds and retrieves all comments associated with a specific post caption
     * from the "newsComments.txt" file. Reads the file and creates NewsComment
     * objects for each matching entry.
     *
     * @param captionOfPost the caption of the post whose comments are to be retrieved
     * @return an ArrayList of NewsComment objects associated with the post
     */
    public static ArrayList<NewsComment> findComments(String captionOfPost) {

        ArrayList<NewsComment> comments = new ArrayList<>();

        try (BufferedReader readComments = new BufferedReader(new FileReader("newsComments.txt"))) {
            String line;

            while ((line = readComments.readLine()) != null) {
                if (line.contains(captionOfPost)) {

                    String[] commentInfo = line.split(",");

                    comments.add(new NewsComment(commentInfo[0], commentInfo[1], commentInfo[2],
                            Integer.parseInt(commentInfo[3]), Integer.parseInt(commentInfo[4])));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return comments;
    }

    /**
     * Finds and retrieves all comments associated with a specific user.
     * Uses the `findComments` method to perform the search.
     *
     * @param user1 the username of the user whose comments are to be retrieved
     * @return an ArrayList of NewsComment objects associated with the user
     */
    public static ArrayList<NewsComment> findCommentsForUser(String user1) {
        return findComments(user1);
    }

    /**
     * Increments the upvote count for the post by 1.
     */
    public void incrementUpvotes() {
        upvotes++;
    }

    /**
     * Increments the downvote count for the post by 1.
     */
    public void incrementDownvotes() {
        downvotes++;
    }

    //Getters and setters
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCaption() {
        return caption;
    }
    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getImagePath() {
        return imagePath;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public int getUpvotes() {
        return upvotes - downvotes;
    }
    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }

    public int getDownvotes() {
        return downvotes;
    }
    public void setDownvotes(int downvotes) {
        this.downvotes = downvotes;
    }

    public ArrayList<NewsComment> getComments() {
        return comments;
    }
    public void addComment(NewsComment comment) {
        comments.add(comment);
    }
    public void setComments(ArrayList<NewsComment> comments) {
        this.comments = comments;
    }

    /**
     * Returns a string representation of the post, including its details and comments.
     * Displays information such as the author, caption, image path, date, upvotes,
     * downvotes, and comments in a formatted manner.
     *
     * @return a formatted string representation of the post
     */
    @Override
    public String toString() {

        StringBuilder postInfo = new StringBuilder();

        postInfo.append("---------------\n"); // Separator
        postInfo.append("author: " + author + "\n");
        postInfo.append("caption: " + caption + "\n");
        postInfo.append("imagePath: " + imagePath + "\n");
        postInfo.append("date: " + date + "\n");
        postInfo.append("upvotes: " + upvotes + "\n");
        postInfo.append("downvotes: " + downvotes + "\n");

        if (comments.size() == 0) {
            postInfo.append("Comments: 0");
        } else {
            postInfo.append("Comments: \n");
            for (NewsComment comment : comments) {
                postInfo.append(comment.toString() + "\n");
            }
        }
        return postInfo.toString();
    }
}