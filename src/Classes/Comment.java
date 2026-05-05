package Classes;

import java.util.UUID;

public class Comment {
    private final String commentID;
    private final String postID;
    private final String userID;
    private String text;

    public Comment(String text, String userID, String postID) {

        if (Validations.isEmpty(text)) {
            throw new IllegalArgumentException("Comment cannot be empty");
        }

        this.commentID = UUID.randomUUID().toString();
        this.userID = userID;
        this.postID = postID;
        this.text = text;
    }

    // Getters
    public String getCommentID() {
        return commentID;
    }

    public String getPostID() {
        return postID;
    }

    public String getUserID() {
        return userID;
    }

    public String getText() {
        return text;
    }

    // Setters
    public void setText(String text) {

        if (Validations.isEmpty(text)) {
            throw new IllegalArgumentException("Comment cannot be empty");
        }

        this.text = text;
    }
}