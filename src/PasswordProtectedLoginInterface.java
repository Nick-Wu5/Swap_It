import java.util.ArrayList;

public interface PasswordProtectedLoginInterface {

    boolean login();
    boolean authenticate(String username, String password);

}
