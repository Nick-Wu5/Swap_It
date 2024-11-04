import java.util.ArrayList;

public interface PasswordProtectedLoginInterface {

    ArrayList<String> getUsers();
    ArrayList<String> getPasses();
    boolean login();
    boolean authenticate(String username, String password);

}
