package SwingUI;

import Classes.SocialMediaManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginUI extends JFrame {
    private final SocialMediaManager manager;
    private JPanel loginPanel;
    private JTextField emailTxt;
    private JPasswordField passwordTxt;
    private JButton loginBtn;
    private JButton registerBtn;
    private JButton helpBtn;
    private JLabel titleLbl;
    private JLabel emailLbl;
    private JLabel passwordLbl;
    private JCheckBox showPasswordChk;

    public LoginUI(SocialMediaManager manager) {
        this.manager = manager;

        setContentPane(loginPanel);
        setTitle("Social Media - Login");
        setSize(550, 400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // show/hide password
        showPasswordChk.addActionListener(e -> {
            if (showPasswordChk.isSelected()) {
                passwordTxt.setEchoChar((char) 0);
            } else {
                passwordTxt.setEchoChar('*');
            }
        });

        loginBtn.addActionListener(e -> {
            String email = emailTxt.getText();

            // clear password array
            char[] passChars = passwordTxt.getPassword();
            String password = new String(passChars);
            java.util.Arrays.fill(passChars, '\0');

            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(LoginUI.this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int result = manager.login(email, password);
            switch (result) {
                case 1 -> {
                    JOptionPane.showMessageDialog(LoginUI.this, "Login Successful! Welcome " + manager.getCurrentUser().getFirstName());
                    new MainUI(manager).setVisible(true);
                    LoginUI.this.dispose();
                }
                case -1 -> {
                    JOptionPane.showMessageDialog(LoginUI.this, "Account is locked due to 5 failed attempts.", "Locked Out", JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                }
                case 0 -> {
                    JOptionPane.showMessageDialog(LoginUI.this, "Incorrect password. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                default -> {
                    JOptionPane.showMessageDialog(LoginUI.this, "User not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        registerBtn.addActionListener(e -> {
            new RegisterUI(manager).setVisible(true);
            LoginUI.this.dispose();
        });

        helpBtn.addActionListener(e -> {
            String helpText = """
                    System Help Guide:
                    
                    - Enter your registered email and password to log in.
                    - If you do not have an account, click Register.
                    - If you fail login 5 times, your account locks!""";
            JOptionPane.showMessageDialog(null, helpText, "Help & Instructions", JOptionPane.INFORMATION_MESSAGE);
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
}