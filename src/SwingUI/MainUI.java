package SwingUI;

import Classes.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MainUI extends JFrame {
    private final SocialMediaManager manager;
    private JPanel mainPanel;
    private JLabel welcomeLbl;
    private JButton addPostBtn;
    private JButton viewMyPostsBtn;
    private JButton viewPublicPostsBtn;
    private JButton friendsMenuBtn;
    private JButton ExitBtn;
    private JPanel feedPanel;
    private JButton helpBtn;
    private JButton LogoutBtn;

    public MainUI(SocialMediaManager manager) {
        this.manager = manager;

        setContentPane(mainPanel);
        setTitle("SocialConnect - Dashboard");
        setSize(800, 650);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        welcomeLbl.setText("Welcome back, " + manager.getCurrentUser().getFirstName() + "!");
        feedPanel.setLayout(new BoxLayout(feedPanel, BoxLayout.Y_AXIS));

        // Action Listeners
        addPostBtn.addActionListener(e -> new AddPostUI(manager, MainUI.this).setVisible(true));

        viewMyPostsBtn.addActionListener(e -> {

            feedPanel.removeAll(); // Clear current view

            JLabel header = new JLabel("=== My Posts ===", SwingConstants.CENTER);
            header.setFont(new Font("Segoe UI", Font.BOLD, 16));

            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.add(header, BorderLayout.CENTER);

            headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

            feedPanel.add(headerPanel);
            feedPanel.add(Box.createRigidArea(new Dimension(0, 10)));

            List<Posts> myPosts = manager.getMyPosts();

            if (myPosts.isEmpty()) {
                feedPanel.add(new JLabel("You haven't made any posts yet."));
            } else {
                for (Posts p : myPosts) {
                    feedPanel.add(createPostCard(p));
                    feedPanel.add(Box.createRigidArea(new Dimension(0, 15)));
                }
            }

            feedPanel.revalidate();
            feedPanel.repaint();
        });

        viewPublicPostsBtn.addActionListener(e -> {

            feedPanel.removeAll();

            JLabel header = new JLabel("=== Public Posts ===", SwingConstants.CENTER);
            header.setFont(new Font("Segoe UI", Font.BOLD, 16));

            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.add(header, BorderLayout.CENTER);

            headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

            feedPanel.add(headerPanel);
            feedPanel.add(Box.createRigidArea(new Dimension(0, 10)));

            List<Posts> publicPosts = manager.getPublicPosts();

            if (publicPosts.isEmpty()) {
                feedPanel.add(new JLabel("No public posts available to view."));
            } else {
                for (Posts p : publicPosts) {
                    feedPanel.add(createPostCard(p));
                    feedPanel.add(Box.createRigidArea(new Dimension(0, 15)));
                }
            }

            feedPanel.revalidate();
            feedPanel.repaint();
        });

        friendsMenuBtn.addActionListener(e -> {
            new FriendsMenuUI(manager).setVisible(true);
            MainUI.this.dispose();
        });

        LogoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    MainUI.this, "Are you sure you want to logout?", "Confirm Logout", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                manager.logout();
                new LoginUI(manager).setVisible(true);
                MainUI.this.dispose();
            }
        });

        ExitBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    MainUI.this, "Are you sure you want to exit?", "Exit Application", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        helpBtn.addActionListener(e -> {
            String helpText = """
                    System Help Guide:
                    
                    - Use the Main Menu to view Public posts or your own posts.
                    - Go to Friends Menu to search for users and add them.
                    - When adding a post, ensure text is under 300 characters.
                    - Click Exit to safely close the application.""";
            JOptionPane.showMessageDialog(null, helpText, "Help & Instructions", JOptionPane.INFORMATION_MESSAGE);
        });

        // hover colour
        LogoutBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                LogoutBtn.setBackground(new Color(0xc62222));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                LogoutBtn.setBackground(new Color(0xB91C1C));
            }
        });

        ExitBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                ExitBtn.setBackground(new Color(0x656c79));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ExitBtn.setBackground(new Color(0x4B5563));
            }
        });

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

    // Methods
    public void refreshFeed(java.util.List<Posts> posts) {
        feedPanel.removeAll();

        for (Posts p : posts) {
            feedPanel.add(createPostCard(p));
            feedPanel.add(Box.createRigidArea(new java.awt.Dimension(0, 15)));
        }

        feedPanel.revalidate();
        feedPanel.repaint();
    }

    private JPanel createPostCard(Posts p) {

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1), BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");
        card.add(new JLabel("Date: " + p.getDate().format(formatter)));
        card.add(new JLabel("Visibility: " + p.getPostSecurity()));
        card.add(Box.createRigidArea(new Dimension(0, 5)));

        if (p instanceof TextPost) {

            JTextArea text = new JTextArea(((TextPost) p).getText());
            text.setWrapStyleWord(true);
            text.setLineWrap(true);
            text.setEditable(false);
            text.setBackground(card.getBackground());
            card.add(text);
        } else if (p instanceof ImagePost ip) {

            card.add(new JLabel("Description: " + ip.getDescription()));
            card.add(Box.createRigidArea(new Dimension(0, 10)));

            JButton viewImageBtn = new JButton("View Full Image");

            viewImageBtn.addActionListener(e -> {
                String source = ip.getLocation();
                try {
                    BufferedImage original = null;
                    if (source.startsWith("http")) {
                        java.net.URL url = new java.net.URL(source);
                        java.net.HttpURLConnection connection = (java.net.HttpURLConnection) url.openConnection();
                        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
                        try (java.io.InputStream in = connection.getInputStream()) {
                            original = ImageIO.read(in);
                        }
                    } else {
                        File imgFile = new File(source);

                        if (!imgFile.exists()) {
                            JOptionPane.showMessageDialog(MainUI.this, "Image file not found!", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        original = ImageIO.read(imgFile);
                    }

                    if (original == null) {
                        JOptionPane.showMessageDialog(MainUI.this, "Failed to load image format!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    JDialog dialog = new JDialog(MainUI.this, ip.getName(), true);

                    ImagePanel panel = new ImagePanel(original);

                    JScrollPane scrollPane = new JScrollPane(panel);
                    scrollPane.getVerticalScrollBar().setUnitIncrement(16);
                    scrollPane.getHorizontalScrollBar().setUnitIncrement(16);

                    dialog.add(scrollPane);

                    dialog.setSize(600, 600);
                    dialog.setLocationRelativeTo(MainUI.this);
                    dialog.setVisible(true);

                } catch (Exception ex) {
                    System.out.println("error: " + ex.getMessage());

                    JOptionPane.showMessageDialog(MainUI.this, "Failed to load image!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            card.add(viewImageBtn);
        }

        card.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton commentsBtn = new JButton("View / Add Comments");

        commentsBtn.addActionListener(e -> new CommentsUI(MainUI.this, manager, p.getPostID()).setVisible(true));

        card.add(commentsBtn);

        return card;
    }
}