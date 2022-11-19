
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * La classe <code>Main</code> est le point d'entrée du programme. Elle est responsable d'ouvrir la 
 * fenêtre et d'y afficher un menu.
 */
public class Main {
    public static void main(String[] args) {
        MainFrame frame = new MainFrame(MainFrame.DEFAULT_TITLE);

        JPanel menu = new MainMenuScreen(frame);

        frame.setScreen(menu);

        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}