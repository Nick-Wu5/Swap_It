public class NewsFeedTest {
    public static void main(String[] args) {
        // Create author profiles
        UserProfile author1 = new UserProfile("author1");
        UserProfile author2 = new UserProfile("author2");
        UserProfile commenter = new UserProfile("commenter");

        // Create a NewsPost
        NewsPost post = new NewsPost(author1, "Java Basics", "path/to/image.jpg", "2024-11-03");

        // Print post details
        System.out.println("Post Author: " + post.getAuthor().getUsername());
        System.out.println("Post Title: " + post.getTitle());
        System.out.println("Post Date: " + post.getDate());
        System.out.println("Post Upvotes: " + post.getUpvotes());

        // Upvote and downvote the post
        post.incrementUpvotes();
        post.incrementDownvotes();
        post.incrementUpvotes(); // Upvote twice
        System.out.println("Post Upvotes after voting: " + post.getUpvotes());

        // Create a comment
        NewsComment comment1 = new NewsComment("Great post on Java!", commenter);

        // Add the comment to the post
        post.addComment(comment1);

        // Print comment details
        System.out.println("Comment Author: " + comment1.getAuthor().getUsername());
        System.out.println("Comment Content: " + comment1.getContent());
        System.out.println("Comment Upvotes: " + comment1.getUpvotes());

        // Upvote and downvote the comment
        comment1.incrementUpvotes();
        comment1.incrementUpvotes();
        comment1.incrementDownvotes();
        System.out.println("Comment Upvotes after voting: " + comment1.getUpvotes());

        // Print number of comments on the post
        System.out.println("Number of comments on post: " + post.getComments().size());

        // Add another comment
        NewsComment comment2 = new NewsComment("Thanks for the insights!", commenter);
        post.addComment(comment2);

        // Verify the comments added to the post
        System.out.println("Number of comments on post after adding another comment: " + post.getComments().size());
    }
}
