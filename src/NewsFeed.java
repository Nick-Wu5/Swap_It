public interface NewsFeed {
    void incrementDownvotes();
    void incrementUpvotes();
    int getUpvotes();
    UserProfile getAuthor();
}
