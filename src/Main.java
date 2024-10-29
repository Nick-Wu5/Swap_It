import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        ArrayList<String> nicksFriends = new ArrayList<String>();
        nicksFriends.add("Johnny93");
        nicksFriends.add("Isaac_Is_Cool_77");
        nicksFriends.add("Lebron-James");

        ArrayList<String> nicksBlockedFriends = new ArrayList<String>();

        UserProfile nicksAccount = new UserProfile("Nick_Wu_5", nicksFriends, nicksBlockedFriends
                , "nickwu2005@outlook.com", "tottenham123", "10/24/2024");

        System.out.println("Account Created: " + nicksAccount);
    }
}
