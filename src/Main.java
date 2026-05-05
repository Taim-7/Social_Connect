import Classes.SocialMediaManager;
import SwingUI.IntroScreenUI;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            SocialMediaManager manager = new SocialMediaManager();
            new IntroScreenUI(manager).setVisible(true);
        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
        }
    }
}