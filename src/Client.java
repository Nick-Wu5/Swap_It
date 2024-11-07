import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client{

    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
        Scanner scan = new Scanner(System.in);

        Socket socket = new Socket("localhost", 4242);

        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(socket.getOutputStream());

        System.out.println("What do you want to send to the user?");
        String response = scan.nextLine();

        writer.write(response);
        writer.println();
        writer.flush();
        System.out.printf("Sent to user: \n%s\n", response);

        String s1 = reader.readLine();
        System.out.printf("Received from user: \n%s\n", s1);

        writer.close();
        reader.close();
    }
}