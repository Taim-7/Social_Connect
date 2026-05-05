package Classes;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class Posts {
    private final String postID;
    private final String userID;
    private String postSecurity; // "Public" or "Private"
    private LocalDateTime date;

    public Posts(String userID, String postSecurity) {
        this.postID = UUID.randomUUID().toString();
        this.userID = userID;
        this.postSecurity = postSecurity;
        this.date = LocalDateTime.now();
    }

    // Getters
    public String getPostID() {
        return postID;
    }

    public String getUserID() {
        return userID;
    }

    public String getPostSecurity() {
        return postSecurity;
    }

    // Setters
    public void setPostSecurity(String postSecurity) {
        this.postSecurity = postSecurity;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}