package SwingUI;

import Classes.SocialMediaManager;
import Classes.Validations;

import javax.swing.*;
import java.time.LocalDate;

public class RegisterUI extends JFrame {
    private JPanel registerPanel;
    private JTextField fNameTxt;
    private JTextField sNameTxt;
    private JTextField dobTxt;
    private JTextField emailTxt;
    private JPasswordField passwordTxt;
    private JButton registerBtn;
    private JButton backBtn;
    private JCheckBox showPasswordChk;

    public RegisterUI(SocialMediaManager manager) {

        setContentPane(registerPanel);
        setTitle("Register Account");
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

        registerBtn.addActionListener(e -> {
            String fName = fNameTxt.getText().trim();
            String sName = sNameTxt.getText().trim();
            String email = emailTxt.getText().trim();

            // clear password array
            char[] passChars = passwordTxt.getPassword();
            String password = new String(passChars);
            java.util.Arrays.fill(passChars, '\0');

            String dobString = dobTxt.getText().trim();

            if (Validations.isEmpty(fName) || Validations.isEmpty(sName)) {
                JOptionPane.showMessageDialog(RegisterUI.this, "Name fields cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!Validations.isValidEmail(email)) {
                JOptionPane.showMessageDialog(RegisterUI.this, "Please enter a valid email address.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String passError = Validations.getPasswordError(password, 8);
            if (passError != null) {
                JOptionPane.showMessageDialog(RegisterUI.this, passError, "Weak Password", JOptionPane.WARNING_MESSAGE);
                return;
            }

            LocalDate dob = Validations.parseAndValidateDate(dobString);
            if (dob == null) {
                JOptionPane.showMessageDialog(RegisterUI.this, "Invalid Date format or Date is in the future.\n\nPlease strictly use the format: YYYY-MM-DD\nExample: 1995-10-25", "Date Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String summary = String.format("Please confirm your details:\nName: %s %s\nDOB: %s\nEmail: %s", fName, sName, dob, email);
            int confirm = JOptionPane.showConfirmDialog(RegisterUI.this, summary, "Confirm Registration", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                int result = manager.registerUser(fName, sName, dob, email, password);

                switch (result) {
                    case 0 -> {
                        JOptionPane.showMessageDialog(RegisterUI.this, "Registration Successful! Please login.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        new LoginUI(manager).setVisible(true);
                        RegisterUI.this.dispose();
                    }
                    case 1 -> {
                        JOptionPane.showMessageDialog(RegisterUI.this, "Email is already registered.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    case 2 -> {
                        JOptionPane.showMessageDialog(RegisterUI.this, "Invalid email format.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    case 3 -> {
                        JOptionPane.showMessageDialog(RegisterUI.this, "Password is too weak.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    case 4 -> {
                        JOptionPane.showMessageDialog(RegisterUI.this, "Name fields cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    default -> {
                        JOptionPane.showMessageDialog(RegisterUI.this, "Registration failed.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        backBtn.addActionListener(e -> {
            new LoginUI(manager).setVisible(true);
            RegisterUI.this.dispose();
        });
    }
}