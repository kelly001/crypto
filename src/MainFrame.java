import javax.swing.*;
import java.awt.*;

/**
 * Created by new_name on 14.10.2014.
 */
public class MainFrame extends JFrame {

    public void createGUI() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setSize(350, 150);
        panel.setLayout(new FlowLayout());

        final JLabel infolabel = new JLabel("Информация");
        panel.add(infolabel);

        getContentPane().add(panel);
    }

    public MainFrame() {
        super("Crypto App");
        createGUI();
    }

    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame.setDefaultLookAndFeelDecorated(true);
                MainFrame mainFrame = new MainFrame();
                mainFrame.pack();
                mainFrame.setVisible(true);
            }
        });
    }
}
