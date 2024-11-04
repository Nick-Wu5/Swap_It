public class NewsCommentTest {
    public static void main(String[] args) {
        // Assuming UserProfile class is already defined
        UserProfile commenter = new UserProfile("commenterUser");

        // Create a NewsComment instance
        NewsComment comment = new NewsComment("This is a test comment.", commenter);

        // Test comment details
        System.out.println("Comment Author: " + comment.getAuthor().getUsername());
        System.out.println("Comment Content: " + comment.getContent());
        System.out.println("Initial Upvotes: " + comment.getUpvotes()); // Expected: 0

        // Test upvoting and downvoting
        comment.incrementUpvotes();
        comment.incrementUpvotes();
        comment.incrementDownvotes();
        System.out.println("Upvotes after voting: " + comment.getUpvotes()); // Expected: 1 (2 upvotes - 1 downvote)

        // Add more votes to verify accuracy
        comment.incrementDownvotes();
        comment.incrementUpvotes();
        System.out.println("Final Upvotes after more voting: " + comment.getUpvotes()); // Expected: 1 (3 upvotes - 2 downvotes)
    }
}
