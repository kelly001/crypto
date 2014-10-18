import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * Created by new_name on 14.10.2014.
 */
public class MainFrame extends JFrame {
    private JMenuBar menuBar;
    private JMenu menu, submenu;
    private JMenuItem menuItem;

    private void addMenu(String name, ActionListener action) {

        //a group of JMenuItems
        menuItem = new JMenuItem(name,
                KeyEvent.VK_T);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_1, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "This doesn't really do anything");
        menuItem.addActionListener(action);
        menu.add(menuItem);
    }

    public void createGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setSize(350, 150);
        panel.setLayout(new FlowLayout());

        final JLabel infolabel = new JLabel("Информация");
        panel.add(infolabel);

        //MENU
        //Create the menu bar.
        menuBar = new JMenuBar();

        //Build the first menu.
        menu = new JMenu("Меню");
        menu.setMnemonic(KeyEvent.VK_T);
        menu.getAccessibleContext().setAccessibleDescription(
                "The only menu in this program that has menu items");
        menuBar.add(menu);


        addMenu("Сертификат", new MenuActionListener());
        addMenu("Организация", new MenuActionListener());
        addMenu("Ключи", new MenuActionListener());
        addMenu("Хеш", new MenuActionListener());
        setJMenuBar(menuBar);
        getContentPane().add(panel);
    }

    public MainFrame() {
        super("Crypto App");
        createGUI();
        setSize(640,480);
    }

    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame.setDefaultLookAndFeelDecorated(true);
                MainFrame mainFrame = new MainFrame();
                //mainFrame.pack();
                //mainFrame.setSize(640,480);
                mainFrame.setVisible(true);
            }
        });
    }

    public class MenuActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e) {
              System.out.println("Menu item action");
        }
    }
}
