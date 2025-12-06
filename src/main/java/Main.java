import view.LoginWindow;
import view.MainFrame;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        new LoginWindow().setVisible(true);
        // Устанавливаем Look and Feel
//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        // Запускаем главное окно
//        SwingUtilities.invokeLater(() -> {
//            MainFrame mainFrame = new MainFrame();
//            mainFrame.setVisible(true);
//        });
    }
}