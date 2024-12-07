import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

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
public class UserProfile implements User, Serializable {

    private String username;  //unique name associated with user
    private ArrayList<String> friends;  //list of friends of user
    private ArrayList<String> blockedFriends;  //list of blocked friends of user
    private String email;  //email associated with user
    private String password;  //password associated with user

    public UserProfile(String username, String email, String password) {

        this.username = username;
        this.friends = new ArrayList<String>();
        this.blockedFriends = new ArrayList<String>();
        this.email = email;
        this.password = password;

    }

    public UserProfile() {
    }

    //Getters and Setters
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String newUsername) {
        this.username = newUsername;
    }

    public ArrayList<String> getFriends() {

        ArrayList<String> friendsList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] userDetails = line.split(",");
                if (userDetails[0].equals(this.username)) {
                    if (userDetails[3].equals("EmptyFriendsList")) {
                        break;
                    } else if (!(userDetails[3].equals("EmptyFriendsList")) && !(userDetails[3].contains(";"))) {
                        friendsList.add(userDetails[3]);
                    } else {
                        String[] friendsListArray = userDetails[3].split(";");
                        for (String friend : friendsListArray) {
                            friendsList.add(friend);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return friendsList;
    }

    public ArrayList<String> getFriendsList() {
        return this.friends;
    }

    public void setFriends(ArrayList<String> newFriends) {
        this.friends = newFriends;
    }

    public ArrayList<String> getBlockedFriends() {
        return this.blockedFriends;
    }

    public ArrayList<String> getBlockedFriendsFromFile() {

        ArrayList<String> blockedList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] userDetails = line.split(",");
                if (userDetails[0].equals(this.username)) {
                    if (userDetails[4].equals("EmptyBlockedList")) {
                        break;
                    } else if (!(userDetails[4].equals("EmptyBlockedList")) && !(userDetails[4].contains(";"))) {
                        blockedList.add(userDetails[4]);
                    } else {
                        String[] blockedListArray = userDetails[4].split(";");
                        for (String blockedProfile : blockedListArray) {
                            blockedList.add(blockedProfile);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return blockedList;
    }

    public void setBlockedFriends(ArrayList<String> newBlockedFriends) {
        this.blockedFriends = newBlockedFriends;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String newEmail) {
        this.email = newEmail;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    /**
     * Add Friend Method
     *
     * @param userToAdd - the user account to add to the friend list.
     * @return True if the user passed has been successfully added, false if otherwise.
     */
    public boolean addFriend(String userToAdd) {

        for (String blockedFriend : this.blockedFriends) {
            if (userToAdd.equals(blockedFriend)) {
                return false;
            }
        }

        this.friends.add(userToAdd);

        if (this.friends.contains("EmptyFriendsList") && !(userToAdd.equals("EmptyFriendsList"))) {
            this.removeFriend("EmptyFriendsList");
        }

        return true;
    }

    /**
     * Remove Friend Method
     *
     * @param userToRemove - the user account to add to the friend list.
     */
    public void removeFriend(String userToRemove) {
        this.friends.remove(userToRemove);

        if (this.friends.size() == 0) {
            this.addFriend("EmptyFriendsList");
        }
    }

    /**
     * Block User Method
     *
     * @param userToBlock - the user account to add to the friend list
     */
    public void blockUser(String userToBlock) {

        for (String friend : this.getFriendsList()) {
            if (userToBlock.equals(friend)) {
                this.removeFriend(friend);
                this.updateFriendsList();
                break;
            }
        }

        this.blockedFriends.add(userToBlock);

        if (this.blockedFriends.contains("EmptyBlockedList") && !(userToBlock.equals("EmptyBlockedList"))) {
            this.removeBlockedUser("EmptyBlockedList");
        }
    }

    public void removeBlockedUser(String userToRemoveBlock) {

        this.blockedFriends.remove(userToRemoveBlock);

        if (this.blockedFriends.size() == 0) {
            this.blockUser("EmptyBlockedList");
        }
    }

    /**
     * User File Formatting Method
     *
     * @return a string that contains all the user account information to be saved
     */
    public String toFileFormat() {

        String friendsList;
        String blockedList;

        // Convert friends and blockedFriends to strings
        if (friends.isEmpty()) {
            friendsList = "EmptyFriendsList";
        } else {
            friendsList = String.join(";", this.friends);
        }

        if (blockedFriends.isEmpty()) {
            blockedList = "EmptyBlockedList";
        } else {
            blockedList = String.join(";", this.blockedFriends);
        }

        // Return the formatted string with commas separating the main fields
        return this.username + "," + this.email + "," + this.password + "," + friendsList + "," + blockedList;
    }

    /**
     * Save To File Method - writes user account information to the users.txt file
     */
    public synchronized void saveToFile() {
        //use file writer to
        try (FileWriter fw = new FileWriter("users.txt", true); // 'true' for appending
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println(this.toFileFormat());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public StringBuilder getAccountInfo() {

        StringBuilder accountInfo = new StringBuilder();
        accountInfo.append(this.getUsername()).append(";");
        accountInfo.append(this.getEmail()).append(";");
        accountInfo.append(this.getPassword()).append(";");
        accountInfo.append(this.getFriends()).append(";");
        accountInfo.append(this.getBlockedFriends()).append(";");
        return accountInfo;
    }

    public ArrayList<NewsPost> getUserPosts() {

        String line;
        ArrayList<NewsPost> userPosts = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("newsPosts.txt"))) {
            while ((line = br.readLine()) != null) {

                String[] userDetails = line.split(",");

                if (userDetails[0].contains(this.getUsername())) {

                    NewsPost tempPost = new NewsPost();
                    tempPost.setAuthor(userDetails[0]);
                    tempPost.setCaption(userDetails[1]);
                    tempPost.setImagePath(userDetails[2]);
                    tempPost.setDate(userDetails[3]);
                    tempPost.setUpvotes(Integer.parseInt(userDetails[4]));
                    tempPost.setDownvotes(Integer.parseInt(userDetails[5]));
                    tempPost.setComments(NewsPost.findComments(userDetails[1]));

                    userPosts.add(tempPost);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return userPosts;
    }

    public void updateFriendsList() {
        try {
            // Read the file into a list of strings
            ArrayList<String> fileLines = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    fileLines.add(line);
                }
            }

            // Modify the relevant user's line
            boolean userFound = false;
            for (int i = 0; i < fileLines.size(); i++) {
                String[] parts = fileLines.get(i).split(",");
                if (parts[0].equals(username)) {

                    parts[3] = String.join(";", this.getFriendsList()); // Replace with the new friends list

                    fileLines.set(i, String.join(",", parts)); // Rebuild the line
                    userFound = true;
                    break;
                }
            }

            if (!userFound) {
                System.out.println("User not found: " + username);
                return;
            }

            // Write back the updated lines to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt"))) {
                for (String line : fileLines) {
                    writer.write(line);
                    writer.newLine();
                }
            }

            System.out.println("Friends list updated successfully for user: " + this.getUsername());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateBlockedList() {
        try {
            // Read the file into a list of strings
            ArrayList<String> fileLines = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    fileLines.add(line);
                }
            }

            // Modify the relevant user's line
            boolean userFound = false;
            for (int i = 0; i < fileLines.size(); i++) {
                String[] parts = fileLines.get(i).split(",");
                if (parts[0].equals(username)) {

                    parts[4] = String.join(";", this.getBlockedFriends()); // Replace with the new friends list

                    fileLines.set(i, String.join(",", parts)); // Rebuild the line
                    userFound = true;
                    break;
                }

            }

            if (!userFound) {
                System.out.println("User not found: " + username);
                return;
            }

            // Write back the updated lines to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt"))) {
                for (String line : fileLines) {
                    writer.write(line);
                    writer.newLine();
                }
            }

            System.out.println("Blocked list updated successfully for user: " + this.getUsername());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<NewsComment> findCommentsForUser() {

        ArrayList<NewsComment> comments = new ArrayList<>();

        try (BufferedReader readComments = new BufferedReader(new FileReader("newsComments.txt"))) {
            String line;

            while ((line = readComments.readLine()) != null) {
                if (line.split(",")[1].contains(this.getUsername())) {

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
}
