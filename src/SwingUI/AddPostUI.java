package SwingUI;

import Classes.SocialMediaManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

public class AddPostUI extends JFrame {
    private JPanel mainPanel;
    private JComboBox<String> securityType;
    private JComboBox<String> postType;
    private JPanel textPanel;
    private JTextArea postTextArea;
    private JPanel imagePanel;
    private JTextField imageNameTxt;
    private JTextField imageDescTxt;
    private JTextField imageUrlTxt;
    private JButton uploadBtn;
    private JLabel imagePathLbl;
    private JLabel previewLabel;
    private JButton submitBtn;
    private JButton cancelBtn;
    private String selectedImagePath = "";

    private boolean isImageValid = false;

    public AddPostUI(SocialMediaManager manager, MainUI mainUI) {

        setContentPane(mainPanel);
        setTitle("Create Post");
        setSize(600, 600);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        securityType.addItem("Public");
        securityType.addItem("Private");

        postType.addItem("Text");
        postType.addItem("Image");

        // fallback value
        postType.setSelectedIndex(0);
        securityType.setSelectedIndex(0);

        // switch post type
        postType.addActionListener(e -> {
            boolean isText = postType.getSelectedItem().equals("Text");
            textPanel.setVisible(isText);
            imagePanel.setVisible(!isText);
        });

        // File Upload
        uploadBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {

                File file = chooser.getSelectedFile();

                selectedImagePath = file.getAbsolutePath();

                imageUrlTxt.setText("");

                imagePathLbl.setText(file.getName());

                showPreview(selectedImagePath);
            }
        });

        // timer so it dont spam thread
        Timer debounceTimer = new Timer(500, evt -> {
            String url = imageUrlTxt.getText().trim();

            if (!url.isEmpty()) {
                selectedImagePath = ""; // Clear local path if typing a URL
                if (imagePathLbl != null) imagePathLbl.setText("");
                showPreview(url);
            } else {
                // Only clear the UI if there isn't a local file
                if (selectedImagePath.isEmpty()) {
                    previewLabel.setIcon(null);
                    previewLabel.setText("");
                    isImageValid = false;
                }
            }
        });
        debounceTimer.setRepeats(false);

        // url Preview
        imageUrlTxt.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            public void removeUpdate(DocumentEvent e) {
                update();
            }

            public void changedUpdate(DocumentEvent e) {
                update();
            }

            void update() {
                debounceTimer.restart();
            }
        });

        // Submit
        submitBtn.addActionListener(e -> {
            String type = (String) postType.getSelectedItem();
            // Default to Public if no selection
            String security = (securityType.getSelectedItem() != null) ? securityType.getSelectedItem().toString() : "Public";

            switch (type) {

                // Text Post
                case "Text" -> {
                    String text = postTextArea.getText().trim();

                    if (text.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Text cannot be empty");
                        return;
                    }

                    if (text.length() > 300) {
                        JOptionPane.showMessageDialog(this, "Text too long (max 300)");
                        return;
                    }

                    manager.addTextPost(security, text);

                    mainUI.refreshFeed(manager.getPublicPosts());
                    JOptionPane.showMessageDialog(this, "Posted!");
                    dispose();
                }

                // Image Post
                case "Image" -> {

                    String name = imageNameTxt.getText().trim();
                    String desc = imageDescTxt.getText().trim();
                    String url = imageUrlTxt.getText().trim();

                    if (name.isEmpty() || desc.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Name/description cannot be empty");
                        return;
                    }

                    String location;

                    if (!selectedImagePath.isBlank()) {
                        location = selectedImagePath;
                    } else if (!url.isBlank()) {
                        location = url;
                    } else {
                        JOptionPane.showMessageDialog(this, "Please select an image or enter a URL");
                        return;
                    }

                    if (!isImageValid) {
                        JOptionPane.showMessageDialog(this, "Image failed to load");
                        return;
                    }

                    int result = manager.addImagePost(security, name, desc, location);

                    switch (result) {
                        case 0 -> {
                            mainUI.refreshFeed(manager.getPublicPosts());
                            JOptionPane.showMessageDialog(this, "Posted!");
                            dispose();
                        }
                        case 5 -> {
                            JOptionPane.showMessageDialog(this, "You are not logged in");
                        }
                        case 4 -> {
                            JOptionPane.showMessageDialog(this, "Missing required fields");
                        }
                        default -> {
                            JOptionPane.showMessageDialog(this, "Failed to create image post");
                        }
                    }
                }

                case null -> {
                }

                default -> throw new IllegalStateException("Unexpected value: " + type);
            }
        });

        cancelBtn.addActionListener(e -> dispose());
    }

    // Preview Image
    private void showPreview(String source) {
        String currentPreviewSource = source; // Record the latest requested source

        // Clear previous image/text instantly while loading
        previewLabel.setIcon(null);
        previewLabel.setText("Loading...");
        isImageValid = false;

        new Thread(() -> {
            try {
                BufferedImage img;

                if (source.startsWith("http")) {
                    img = ImageIO.read(new URL(source));
                } else {
                    img = ImageIO.read(new File(source));
                }

                if (img == null) {
                    SwingUtilities.invokeLater(() -> {
                        // Only update UI if this is still the most recently requested image
                        if (source.equals(currentPreviewSource)) {
                            previewLabel.setText("Invalid image");
                            isImageValid = false;
                        }
                    });
                    return;
                }

                Image scaled = img.getScaledInstance(
                        200, 200, Image.SCALE_SMOOTH);

                SwingUtilities.invokeLater(() -> {
                    if (source.equals(currentPreviewSource)) {
                        previewLabel.setText("");
                        previewLabel.setIcon(new ImageIcon(scaled));
                        isImageValid = true; // Image successfully loaded and displayed
                    }
                });

            } catch (Exception e) {
                System.out.println("error: " + e.getMessage());
                SwingUtilities.invokeLater(() -> {
                    if (source.equals(currentPreviewSource)) {
                        previewLabel.setText("Failed to load");
                        isImageValid = false;
                    }
                });
            }
        }).start();
    }
}