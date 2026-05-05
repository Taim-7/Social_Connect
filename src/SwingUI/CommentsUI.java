package SwingUI;

import Classes.Comment;
import Classes.SocialMediaManager;
import Classes.User;

import javax.swing.*;
import java.util.List;

public class CommentsUI extends JDialog {
    private final SocialMediaManager manager;
    private final String postID;
    private JPanel commentsPanel;
    private JTextArea commentsDisplay;
    private JTextField newCommentTxt;
    private JButton addBtn;

    public CommentsUI(JFrame parent, SocialMediaManager manager, String postID) {
        super(parent, "Comments", true);
        this.manager = manager;
        this.postID = postID;

        setContentPane(commentsPanel);
        setSize(400, 400);
        setLocationRelativeTo(parent);

        commentsDisplay.setEditable(false);
        commentsDisplay.setLineWrap(true);
        commentsDisplay.setWrapStyleWord(true);

        refreshComments();

        addBtn.addActionListener(e -> {
            String text = newCommentTxt.getText().trim();
            if (!text.isEmpty()) {
                manager.addComment(postID, text);
                newCommentTxt.setText("");
                refreshComments();
            } else {
                JOptionPane.showMessageDialog(CommentsUI.this, "Comment cannot be empty!", "Error", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    private void refreshComments() {
        commentsDisplay.setText("");
        List<Comment> comments = manager.getCommentsForPost(postID);

        if (comments.isEmpty()) {
            commentsDisplay.append("No comments yet. Be the first!\n");
        } else {
            for (Comment c : comments) {
                User u = manager.getUserByID(c.getUserID());
                String author = (u != null) ? u.getFirstName() : "Unknown User";
                commentsDisplay.append(author + ": " + c.getText() + "\n");
                commentsDisplay.append("--------------------------------------------------\n");
            }
        }
    }
}