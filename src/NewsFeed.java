import java.util.ArrayList;

/**
 * NewsPostInterface
 * <p>
 * Defines the basic operations for managing news posts in the social media application.
 *
 * @version December 7, 2024
 * @author Nick Wu, Chris Brantley, Ramya Prasanna, and Divya Vemireddy
 */
public interface NewsFeed {

    void incrementUpvotes();

    void incrementDownvotes();

    String getAuthor();

    void setAuthor(String author);

    String getCaption();

    void setCaption(String caption);

    String getImagePath();

    void setImagePath(String imagePath);

    String getDate();

    void setDate(String date);

    int getUpvotes();

    void setUpvotes(int upvotes);

    int getDownvotes();

    void setDownvotes(int downvotes);

    ArrayList<NewsComment> getComments();

    void addComment(NewsComment comment);

    void setComments(ArrayList<NewsComment> comments);

    String toString();

    static void deletePost(String caption) {}

    static ArrayList<NewsComment> findComments(String captionOfPost) {
        return null;
    }

    static ArrayList<NewsComment> findCommentsForUser(String user1) {
        return null;
    }
}