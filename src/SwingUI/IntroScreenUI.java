package SwingUI;

import Classes.SocialMediaManager;

import javax.swing.*;

public class IntroScreenUI extends JWindow {
    private JPanel introPanel;
    private JLabel titleLbl;
    private JLabel loadingLbl;

    public IntroScreenUI(SocialMediaManager manager) {
        setContentPane(introPanel);
        setSize(400, 300);
        setLocationRelativeTo(null);

        Timer timer = new Timer(5000, e -> {
            dispose();
            new LoginUI(manager).setVisible(true);
        });
        timer.setRepeats(false);
        timer.start();
    }
}