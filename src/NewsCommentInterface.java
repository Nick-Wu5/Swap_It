/**
 * NewsCommentInterface
 * <p>
 * Defines the basic operations for managing a news comment in the social media application.
 * This interface includes methods to retrieve and modify comment attributes and save the
 * comment to a file.
 *
 * @version December 7, 2024
 * @author Nick Wu, Chris Brantley, Ramya Prasanna, Divya Vemireddy
 */
public interface NewsCommentInterface {

    /**
     * Retrieves the number of upvotes for the comment.
     *
     * @return the number of upvotes
     */
    int getUpvotes();

    /**
     * Retrieves the number of downvotes for the comment.
     *
     * @return the number of downvotes
     */
    int getDownvotes();

    /**
     * Retrieves the text content of the comment.
     *
     * @return the content of the comment
     */
    String getContent();

    /**
     * Retrieves the author of the comment.
     *
     * @return the author of the comment
     */
    String getAuthor();

    /**
     * Increments the upvote count for the comment by 1.
     */
    void incrementUpvotes();

    /**
     * Increments the downvote count for the comment by 1.
     */
    void incrementDownvotes();

    /**
     * Saves the comment to a file in a persistent storage.
     */
    void saveToFile();
}