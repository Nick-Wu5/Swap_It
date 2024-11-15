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
public class NewsPost implements NewsFeed, Serializable {
    private String author;  //author profile of post
    private String caption;  //caption of post
    private String imagePath;  //path to image of post
    private String date;  //date of published post
    private int upvotes;  //number of upvotes per post
    private int downvotes;  //number of downvotes per post
    private ArrayList<NewsComment> comments;  //number of comments per post

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

    public NewsPost() {}

    public static void deletePost(String caption) {
        try (PrintWriter tempWrite = new PrintWriter(new FileWriter("tempFile.txt"));
             PrintWriter writePost = new PrintWriter(new FileWriter("newsPost.txt", false));
             BufferedReader tempRead = new BufferedReader(new FileReader("tempFile.txt"));
             BufferedReader readPost = new BufferedReader(new FileReader("newsPost.txt"))) {
            String line;
            while ((line = readPost.readLine()) != null) {
                if (line.contains(caption)) {
                    continue;
                }
                tempWrite.println(line);
            }
            while ((line = tempRead.readLine()) != null) {
                writePost.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    public static ArrayList<NewsComment> findCommentsForUser(String username) {

        ArrayList<NewsComment> comments = new ArrayList<>();

        try (BufferedReader readComments = new BufferedReader(new FileReader("newsComments.txt"))) {
            String line;

            while ((line = readComments.readLine()) != null) {
                if (line.contains(username)) {

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

    public static void deleteComment(String title) {
        //IMPORTANT: add author and TITLE to comment info

    }

    public void incrementUpvotes() {
        upvotes++;
    }

    public void incrementDownvotes() {
        downvotes++;
    }

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

    @Override
    public String toString() {

        StringBuilder postInfo = new StringBuilder();

        postInfo.append("author: " + author + "\n");
        postInfo.append("caption: " + caption + "\n");
        postInfo.append("imagePath: " + imagePath + "\n");
        postInfo.append("date: " + date + "\n");
        postInfo.append("upvotes: " + upvotes + "\n");
        postInfo.append("downvotes: " + downvotes + "\n");

        postInfo.append("comments: ");
        for (NewsComment comment : comments) {
            postInfo.append(comment.toString() + "\n");
        }

        return postInfo.toString();
    }
}