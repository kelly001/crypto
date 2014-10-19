import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by new_name on 14.10.2014.
 */
public class MainFrame extends JFrame implements ActionListener, ItemListener{
    private JMenuBar menuBar;
    private JMenu menu, submenu;
    private JMenuItem menuItem;

    private void addMenu(String name, ActionListener action, String id) {

        //a group of JMenuItems
        menuItem = new JMenuItem(name,
                KeyEvent.VK_T);
        //menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "This doesn't really do anything");

        menuItem.setActionCommand(id);
        menuItem.addActionListener(action);
        menuItem.addItemListener(this);
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


        addMenu("Сертификат", this, "sert");
        addMenu("Организация", this, "org");
        addMenu("Ключи", this,"key");
        addMenu("Хеш", this, "hash");
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
            System.out.println(menuBar.getComponent().getName());
        }
    }

    public void actionPerformed(ActionEvent e) {
        //...Get information from the action event...
        //...Display it in the text area...
        System.out.println("Menu item action default");
        System.out.println(e.getActionCommand());
    }

    //TODO
    public void itemStateChanged(ItemEvent e) {
        //...Get information from the item event...
        //...Display it in the text area...
        MenuSelectionManager msm = (MenuSelectionManager) e.getSource();
        MenuElement[] path = msm.getSelectedPath();
        if (path.length == 0) {
            System.out.println("No menus are opened or menu items selected");
        }
        for(MenuElement el: path) {
            Component c = el.getComponent();

            if (c instanceof JMenuItem) {
                JMenuItem mi = (JMenuItem) c;
                String label = mi.getText();
                System.out.println(label);
            }
        }
        MenuSelectionManager.defaultManager().clearSelectedPath();
    }
}
