package Classes;

import java.time.LocalDateTime;

public class Friend {
    private final String userID;
    private final String friendID;
    private LocalDateTime dateTime;

    public Friend(String userID, String friendID) {

        if (userID.equals(friendID)) {
            throw new IllegalArgumentException("User cannot friend themselves");
        }

        this.userID = userID;
        this.friendID = friendID;
        this.dateTime = LocalDateTime.now();
    }

    // Getters
    public String getUserID() {
        return userID;
    }

    public String getFriendID() {
        return friendID;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    // Setters
    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}