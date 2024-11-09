import java.net.*;
import java.io.*;
import java.time.LocalDate;

public class Server extends PasswordProtectedLogin implements Runnable {

    ServerSocket serverSocket = new ServerSocket(4242);

    public Server() throws IOException {
    }

    @Override
    public void run() {

        String username = "";
        String email = "";
        String password = "";
        boolean loggedIn = false;
        boolean alreadyRegistered = false;

        try (Socket socket = serverSocket.accept();
             BufferedReader read = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter write = new PrintWriter(socket.getOutputStream());
             ObjectOutputStream objectWrite = new ObjectOutputStream(socket.getOutputStream())){

            System.out.println("Server: Client connected");

            String loginOrRegister = read.readLine();

            if (loginOrRegister.equals("login")) {
                do {
                    while (username.isEmpty() && password.isEmpty()) {
                        if (read.readLine().contains("username")) {
                            username = read.readLine().substring(read.readLine().indexOf(":") + 1);
                        }

                        if (read.readLine().contains("password")) {
                            password = read.readLine().substring(read.readLine().indexOf(":") + 1);
                        }
                    }

                    loggedIn = authenticate(username, password);

                    if (!loggedIn) {
                        write.println("Incorrect username or password");
                    }

                } while (!loggedIn);

            } else if (loginOrRegister.equals("register")) {

                do {

                    while (username.isEmpty() && email.isEmpty() && password.isEmpty()) {

                        if (read.readLine().contains("username")) {
                            username = read.readLine().substring(read.readLine().indexOf(":") + 1);
                        }

                        if (read.readLine().contains("email")) {
                            email = read.readLine().substring(read.readLine().indexOf(":") + 1);
                        }

                        if (read.readLine().contains("password")) {
                            password = read.readLine().substring(read.readLine().indexOf(":") + 1);
                        }
                    }

                    CreateNewUser tempUser = new CreateNewUser(username, email, password);
                    alreadyRegistered = tempUser.isAlreadyRegistered();

                } while (alreadyRegistered);
            }

            while (loggedIn) {

                String menu = read.readLine();
                String prompt = "";

                switch (menu) {
                    case "search" -> {
                        prompt = read.readLine();
                        UserProfile searchedUser = UserSearch.findUserByUsername(prompt);
                        objectWrite.writeObject(searchedUser);
                        // deal with the null on the client side & resend menu
                    }
                    case "post" -> {
                        prompt = read.readLine();
                        if (prompt.equals("create")) {
                            String title = read.readLine();
                            String imagePath = read.readLine();
                            String date = String.valueOf(LocalDate.now());
                            new NewsPost(username, title, imagePath, date);
                            //make sure this saves to a file somehow
                        } else if (prompt.equals("delete")) {
                            //make an arraylist of all posts made by the user and dropdown of title
                            String title = read.readLine();
                            //needs to search through a file for a title and then delete the info of that post


                        }
                    }
                }

            }

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}
