import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class CreateNewUser extends UserProfile implements CreateNewUserInterface {
    private String username;
    private String password;
    private boolean alreadyRegistered;
    private static ArrayList<UserProfile> userProfiles = new ArrayList();
    private static final String filename = "users.txt";

    public CreateNewUser(String username, String password) {
        super(username, username + "@example.com", password);
        this.username = username;
        this.password = password;
        this.alreadyRegistered = this.checkIfUserExists(username);
        if (!this.alreadyRegistered) {
            UserProfile newUserProfile = new UserProfile(username, username + "@example.com", password);
            userProfiles.add(newUserProfile);
            this.saveUserToFile();
            System.out.println("User registered successfully!");
        } else {
            System.out.println("User already registered, cannot create profile.");
        }

    }

    public CreateNewUser() {
        this.username = null;
        this.password = null;
        this.alreadyRegistered = false;
        userProfiles = null;
    }

    public static ArrayList<UserProfile> getUserProfiles() {
        return userProfiles;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public boolean isAlreadyRegistered() {
        return this.alreadyRegistered;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean addFriend() {
        return false;
    }

    @Override
    public void unblockUser(String userToUnblock) {

    }

    public void setAlreadyRegistered(boolean alreadyRegistered) {
        this.alreadyRegistered = alreadyRegistered;
    }

    public UserProfile getUser() {
        if (!this.alreadyRegistered) {
            return (UserProfile)userProfiles.get(userProfiles.size() - 1);
        } else {
            System.out.println("User already registered, cannot create profile.");
            return null;
        }
    }

    private boolean checkIfUserExists(String username) {
        Iterator var2 = userProfiles.iterator();

        UserProfile profile;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            profile = (UserProfile)var2.next();
        } while(!profile.getUsername().equals(username));

        return true;
    }

    private void saveUserToFile() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt", true));

            try {
                writer.write(this.username + ":" + this.password);
                writer.newLine();
            } catch (Throwable var5) {
                try {
                    writer.close();
                } catch (Throwable var4) {
                    var5.addSuppressed(var4);
                }

                throw var5;
            }

            writer.close();
        } catch (IOException var6) {
            IOException e = var6;
            System.out.println("Error saving user to file: " + e.getMessage());
        }

    }

    public static void loadUsersFromFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("users.txt"));

            String line;
            try {
                while((line = reader.readLine()) != null) {
                    String[] parts = line.split(":");
                    if (parts.length == 2) {
                        String username = parts[0];
                        String password = parts[1];
                        UserProfile userProfile = new UserProfile(username, username + "@example.com", password);
                        userProfiles.add(userProfile);
                    }
                }
            } catch (Throwable var7) {
                try {
                    reader.close();
                } catch (Throwable var6) {
                    var7.addSuppressed(var6);
                }

                throw var7;
            }

            reader.close();
        } catch (IOException var8) {
            IOException e = var8;
            System.out.println("Error reading users from file: " + e.getMessage());
        }

    }

    public static void main(String[] args) {
        loadUsersFromFile();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter an email address: ");
        String username = scanner.nextLine().split("@")[0];
        System.out.println("Enter a password: ");
        String password = scanner.nextLine();
        CreateNewUser newUser = new CreateNewUser(username, password);
        if (newUser.alreadyRegistered) {
            System.out.println("User already registered!");
        } else {
            System.out.println("User registered successfully!");
            UserProfile profile = newUser.getUser();
            if (profile != null) {
                System.out.println("User profile created for " + profile.getUsername());
            }

            scanner.close();
        }

    }
}
