package SwingUI;

import Classes.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FriendsMenuUI extends JFrame {
    private final SocialMediaManager manager;
    private JPanel mainPanel;
    private JComboBox<User> addFriendCombo;
    private JButton addFriendBtn;
    private JComboBox<User> viewFriendCombo;
    private JButton viewPostsBtn;
    private JTextArea postsDisplayArea;
    private JButton backBtn;
    private JButton helpBtn;

    public FriendsMenuUI(SocialMediaManager manager) {
        this.manager = manager;

        setContentPane(mainPanel);
        setTitle("Friends Menu");
        setSize(600, 500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        refreshDropdowns();

        // Action Listeners
        addFriendBtn.addActionListener(e -> {
            User selectedUser = (User) addFriendCombo.getSelectedItem();
            if (selectedUser == null) {
                JOptionPane.showMessageDialog(FriendsMenuUI.this, "Please select a user to add.");
                return;
            }

            int result = manager.addFriend(selectedUser.getUserID());

            switch (result) {
                case 0 -> {
                    JOptionPane.showMessageDialog(FriendsMenuUI.this, "You are now friends with " + selectedUser.getFirstName() + "!");
                }
                case 1 -> {
                    JOptionPane.showMessageDialog(FriendsMenuUI.this, "You are already friends with this user.");
                }
                case 5 -> {
                    JOptionPane.showMessageDialog(FriendsMenuUI.this, "You are not logged in.");
                }
                case 6 -> {
                    JOptionPane.showMessageDialog(FriendsMenuUI.this, "You cannot add yourself as a friend.");
                }
                default -> {
                    JOptionPane.showMessageDialog(FriendsMenuUI.this, "Could not add friend.");
                }
            }

            refreshDropdowns();
        });

        viewPostsBtn.addActionListener(e -> {
            User selectedFriend = (User) viewFriendCombo.getSelectedItem();
            if (selectedFriend == null) {
                JOptionPane.showMessageDialog(FriendsMenuUI.this, "Please select a friend to view their posts.");
                return;
            }

            postsDisplayArea.setText("=== Posts by " + selectedFriend.getFirstName() + " ===\n\n");
            List<Posts> friendPosts = manager.getFriendPosts(selectedFriend.getUserID());

            if (friendPosts.isEmpty()) {
                postsDisplayArea.append("This friend hasn't posted anything yet.");
            } else {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");
                for (Posts p : friendPosts) {
                    postsDisplayArea.append("Date: " + p.getDate().format(formatter) + "\n");
                    postsDisplayArea.append("Visibility: " + p.getPostSecurity() + "\n");
                    if (p instanceof TextPost) {
                        postsDisplayArea.append("Text: " + ((TextPost) p).getText() + "\n");
                    } else if (p instanceof ImagePost ip) {
                        postsDisplayArea.append("Image: " + ip.getName() + " - " + ip.getDescription() + "\n");
                    }
                    postsDisplayArea.append("--------------------------------------------------\n\n");
                }
            }
        });

        backBtn.addActionListener(e -> {
            new MainUI(manager).setVisible(true);
            dispose();
        });

        // hover colour
        helpBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                helpBtn.setBackground(new Color(0x059669));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                helpBtn.setBackground(new Color(0x0a7f57));
            }
        });
    }

    private void refreshDropdowns() {
        addFriendCombo.removeAllItems();
        viewFriendCombo.removeAllItems();

        List<User> allOthers = manager.getAllOtherUsers();
        for (User u : allOthers) {
            addFriendCombo.addItem(u);
        }

        List<User> myFriends = manager.getMyFriends();
        for (User u : myFriends) {
            viewFriendCombo.addItem(u);
        }
    }
}