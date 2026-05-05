package Classes;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class SocialMediaManager {
    private static final String FILE_PATH = "src/Data/users.txt";
    private final ArrayList<Posts> posts = new ArrayList<>();
    private final ArrayList<Comment> comments = new ArrayList<>();
    private final ArrayList<Friend> friends = new ArrayList<>();
    private ArrayList<User> users = new ArrayList<>();
    private User currentUser = null;

    // 0 = success
    // 1 = already exists
    // 2 = invalid email
    // 3 = weak password
    // 4 = empty fields
    // 5 = not logged in
    // 6 = invalid input / unknown user

    public SocialMediaManager() {
        // load users upon start
        users = loadUsers();
        // example login cred
        users.add(new User("Taim", "Otah", LocalDate.of(2008, 4, 13), "t", "t"));
        users.add(new User("Joe", "Doe", LocalDate.of(2008, 4, 13), "j", "j"));
    }

    // Load all users from the file
    public static ArrayList<User> loadUsers() {
        ArrayList<User> users = new ArrayList<>();

        try {
            List<String> lines = Files.readAllLines(Paths.get(FILE_PATH));
            for (String line : lines) {
                String[] p = line.split(",");
                if (p.length == 5) {
                    users.add(new User(
                            new String(Base64.getDecoder().decode(p[0])),                    // firstName
                            new String(Base64.getDecoder().decode(p[1])),                    // surname
                            LocalDate.parse(new String(Base64.getDecoder().decode(p[2]))),   // dateOfBirth
                            new String(Base64.getDecoder().decode(p[3])),                    // email
                            new String(Base64.getDecoder().decode(p[4]))                     // password
                    ));
                }
            }

        } catch (IOException e) {
            System.out.println("Could not read file: " + e.getMessage());
        }

        return users;
    }

    // save a single user
    private static void saveUser(User user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(
                    // encode in base64 to make it unreadable
                    Base64.getEncoder().encodeToString(user.getFirstName().getBytes()) + "," +
                            Base64.getEncoder().encodeToString(user.getSurname().getBytes()) + "," +
                            Base64.getEncoder().encodeToString(user.getDateOfBirth().toString().getBytes()) + "," +
                            Base64.getEncoder().encodeToString(user.getEmail().getBytes()) + "," +
                            Base64.getEncoder().encodeToString(user.getPassword().getBytes()) + "\n"
            );
        } catch (IOException e) {
            System.out.println("Error writing user: " + e.getMessage());
        }
    }


    // Authentication Logic
    public int login(String email, String password) {
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                if (u.isLocked()) {
                    return -1; // locked
                }
                if (u.getPassword().equals(password)) {
                    u.resetFailedLogin();
                    u.setLastLogin(LocalDateTime.now());
                    currentUser = u;
                    return 1; // success
                } else {
                    u.recordFailedLogin();

                    if (u.isLocked()) {
                        return -1; // closes program
                    }

                    return 0;
                }
            }
        }
        return -2;
    }

    public void logout() {
        currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    // Registration Logic
    public int registerUser(String fName, String sName, LocalDate dob, String email, String password) {

        if (Validations.isEmpty(fName) || Validations.isEmpty(sName)) {
            return 4;
        }

        if (!Validations.isValidEmail(email)) {
            return 2;
        }

        if (Validations.getPasswordError(password, 6) != null) {
            return 3;
        }

        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return 1;
            }
        }

        users.add(new User(fName, sName, dob, email, password));
        saveUser(users.getLast());
        return 0;
    }

    // Post Management
    public void addTextPost(String security, String text) {
        if (currentUser != null) {
            posts.add(new TextPost(currentUser.getUserID(), security, text));
        }
    }

    public int addImagePost(String security, String name, String desc, String location) {

        if (currentUser == null) return 5;

        if (Validations.isEmpty(security) || Validations.isEmpty(name) || Validations.isEmpty(desc)) {
            return 4;
        }

        posts.add(new ImagePost(
                currentUser.getUserID(), security, name, desc, location));

        return 0;
    }

    public List<Posts> getMyPosts() {
        List<Posts> result = new ArrayList<>();

        if (currentUser == null) return result;

        for (Posts p : posts) {
            if (p.getUserID().equals(currentUser.getUserID())) {
                result.add(p);
            }
        }

        result.sort((p1, p2) -> p2.getDate().compareTo(p1.getDate()));
        return result;
    }

    public List<Posts> getPublicPosts() {
        List<Posts> result = new ArrayList<>();

        for (Posts p : posts) {
            if (p.getPostSecurity().equalsIgnoreCase("Public")) {
                result.add(p);
            }
        }

        result.sort((p1, p2) -> p2.getDate().compareTo(p1.getDate()));
        return result;
    }

    // Comments
    public void addComment(String postID, String text) {
        if (currentUser != null) {
            comments.add(new Comment(text, currentUser.getUserID(), postID));
        }
    }

    public List<Comment> getCommentsForPost(String postID) {
        List<Comment> result = new ArrayList<>();

        for (Comment c : comments) {
            if (c.getPostID().equals(postID)) {
                result.add(c);
            }
        }

        return result;
    }

    // Friend Management
    public List<User> getAllOtherUsers() {
        List<User> result = new ArrayList<>();

        if (currentUser == null) return result;

        for (User u : users) {
            if (u.getUserID().equals(currentUser.getUserID())) continue;

            boolean alreadyFriend = false;

            for (Friend f : friends) {
                if ((f.getUserID().equals(currentUser.getUserID()) && f.getFriendID().equals(u.getUserID())) || (f.getUserID().equals(u.getUserID()) && f.getFriendID().equals(currentUser.getUserID()))) {
                    alreadyFriend = true;
                    break;
                }
            }

            if (!alreadyFriend) {
                result.add(u);
            }
        }

        return result;
    }

    public int addFriend(String friendID) {

        if (currentUser == null) return 5;

        if (currentUser.getUserID().equals(friendID)) return 6;

        for (Friend f : friends) {
            if ((f.getUserID().equals(currentUser.getUserID()) && f.getFriendID().equals(friendID)) || (f.getUserID().equals(friendID) && f.getFriendID().equals(currentUser.getUserID()))) {
                return 1;
            }
        }

        friends.add(new Friend(currentUser.getUserID(), friendID));
        return 0;
    }

    public List<Posts> getFriendPosts(String friendID) {
        List<Posts> result = new ArrayList<>();

        if (currentUser == null) return result;

        boolean isFriend = false;

        for (Friend f : friends) {
            if ((f.getUserID().equals(currentUser.getUserID()) && f.getFriendID().equals(friendID)) || (f.getUserID().equals(friendID) && f.getFriendID().equals(currentUser.getUserID()))) {
                isFriend = true;
                break;
            }
        }

        if (!isFriend) return result;

        for (Posts p : posts) {
            if (p.getUserID().equals(friendID)) {
                result.add(p);
            }
        }

        result.sort((p1, p2) -> p2.getDate().compareTo(p1.getDate()));
        return result;
    }

    // Other
    public User getUserByID(String userID) {
        for (User u : users) {
            if (u.getUserID().equals(userID)) {
                return u;
            }
        }
        return null;
    }

    public List<User> getMyFriends() {
        if (currentUser == null) return new ArrayList<>();

        List<User> myFriends = new ArrayList<>();

        for (Friend f : friends) {
            if (f.getUserID().equals(currentUser.getUserID())) {
                for (User u : users) {
                    if (u.getUserID().equals(f.getFriendID())) {
                        myFriends.add(u);
                        break;
                    }
                }
            } else if (f.getFriendID().equals(currentUser.getUserID())) {
                for (User u : users) {
                    if (u.getUserID().equals(f.getUserID())) {
                        myFriends.add(u);
                        break;
                    }
                }
            }
        }

        return myFriends;
    }
}