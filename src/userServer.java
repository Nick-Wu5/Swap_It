import java.util.List;

public interface userServer {
    void addUser(String username);
    void removeFriend(int userId, int friendId);
    void addFriend(int userId, int friendId);
    List<User>(int userId);

}
