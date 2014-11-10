import com.teacode.swing.dialog.OkCancelDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


/**
 * Created by new_name on 14.10.2014.
 */
public class MainFrame extends JFrame implements ActionListener, ItemListener{

    static int openFrameCount = 0;
    static final int xOffset = 30, yOffset = 30;
    Dimension size = new Dimension(640,480);

    private JMenuBar menuBar;
    private JMenu menu, certificateMenu, companyMenu, userMenu;
    private JMenuItem menuItem;

    private void addMenuItem(String name, ActionListener action, String id, JMenu menu) {

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

    public void setMenu () {
        //MENU
        //Create the menu bar.
        menuBar = new JMenuBar();

        //Build the first menu.
        menu = new JMenu("Меню");
        menu.setMnemonic(KeyEvent.VK_F1);
        menu.getAccessibleContext().setAccessibleDescription(
                "The only menu in this program that has menu items");
        //addMenuItem("Сертификаты", this, "cert", menu);
        //addMenuItem("Организация", this, "org", menu);
        //addMenuItem("Ключи", this,"key", menu);
        //addMenuItem("Хеш", this, "hash", menu);
        addMenuItem("Сменить пользователя", this, "logout", menu);
        addMenuItem("Выход", this, "exit", menu);
        menuBar.add(menu);

        //Build the cert menu.
        certificateMenu = new JMenu("Сертификаты");
        certificateMenu.setMnemonic(KeyEvent.VK_F3);
        certificateMenu.getAccessibleContext().setAccessibleDescription(
                "The only menu in this program that has menu items");
        addMenuItem("Добавить", this, "certificate-add", certificateMenu);
        addMenuItem("Отозвать", this, "certificate-delete", certificateMenu);
        //menuBar.add(certificateMenu);
        //Build the company menu.
        companyMenu = new JMenu("Редактирование");
        companyMenu.setMnemonic(KeyEvent.VK_F2);
        companyMenu.getAccessibleContext().setAccessibleDescription(
                "The only menu in this program that has menu items");
        addMenuItem("Информация", this, "company-add", companyMenu);
        addMenuItem("Ключ", this,"key", companyMenu);
        addMenuItem("Хеш", this, "hash", companyMenu);
        companyMenu.add(certificateMenu);
        //addMenuItem("Сертификат", this, "company-certificate", companyMenu);
        menuBar.add(companyMenu);

        //Build the user menu.
        userMenu = new JMenu("Сотрудники");
        userMenu.setMnemonic(KeyEvent.VK_U);
        addMenuItem("Список", this, "users", userMenu);
        menuBar.add(userMenu);
        setJMenuBar(menuBar);

    }

    public void createGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        //panel.setSize(350, 150);
        panel.setLayout(new FlowLayout());

        final JLabel infolabel = new JLabel("Информация");
        panel.add(infolabel);

        //MENU
        //Create the menu bar.
        menuBar = new JMenuBar();

        //Build the first menu.
        menu = new JMenu("Меню");
        menu.setMnemonic(KeyEvent.VK_F1);
        menu.getAccessibleContext().setAccessibleDescription(
                "The only menu in this program that has menu items");
        //addMenuItem("Сертификаты", this, "cert", menu);
        //addMenuItem("Организация", this, "org", menu);
        //addMenuItem("Ключи", this,"key", menu);
        //addMenuItem("Хеш", this, "hash", menu);
        addMenuItem("Сменить пользователя", this, "logout", menu);
        addMenuItem("Выход", this, "exit", menu);
        menuBar.add(menu);

        //Build the cert menu.
        certificateMenu = new JMenu("Сертификаты");
        certificateMenu.setMnemonic(KeyEvent.VK_F3);
        certificateMenu.getAccessibleContext().setAccessibleDescription(
                "The only menu in this program that has menu items");
        addMenuItem("Добавить", this, "certificate-add", certificateMenu);
        addMenuItem("Отозвать", this, "certificate-delete", certificateMenu);
        //menuBar.add(certificateMenu);
        //Build the company menu.
        companyMenu = new JMenu("Организация");
        companyMenu.setMnemonic(KeyEvent.VK_F2);
        addMenuItem("Информация", this, "company-add", companyMenu);
        addMenuItem("Ключ", this,"key", companyMenu);
        addMenuItem("Хеш", this, "hash", companyMenu);
        companyMenu.add(certificateMenu);
        //addMenuItem("Сертификат", this, "company-certificate", companyMenu);
        menuBar.add(companyMenu);

        //Build the user menu.
        userMenu = new JMenu("Сотрудники");
        userMenu.setMnemonic(KeyEvent.VK_U);
        userMenu.setActionCommand("users");
        userMenu.addActionListener(this);
        userMenu.addItemListener(this);
        addMenuItem("Список", this, "users", userMenu);
        menuBar.add(userMenu);
        setJMenuBar(menuBar);
        //panel.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
        getContentPane().add(panel);
    }



    public MainFrame() {
        super("Crypto App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final MainPanel panel = new MainPanel(this);
        setMenu();
        this.getContentPane().add(panel);
        this.setSize(size);
        this.setVisible(true);
    }

    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame.setDefaultLookAndFeelDecorated(true);
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
            }
        });
    }

    public class MenuActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e) {
            System.out.println("Menu item action default custom");
            System.out.println(e.getActionCommand());
        }
    }

    public void actionPerformed(ActionEvent e) {
        //...Get information from the action event...
        //...Display it in the text area...
        System.out.println("Menu item action default");
        System.out.println(e.getActionCommand());

        //check menu id
        if (e.getActionCommand().equals("exit")) {
            System.exit(1);
        } else if (e.getActionCommand().equals("logout")) {
            // start login frame
            LoginFrame new_frame = new LoginFrame("Авторизация");
            new_frame.setVisible(true);
            //find main frame
            Object item = e.getSource();
            if (item instanceof Component) {
                Window w = findWindow((Component) item);
                w.dispose();
            } else {
                System.out.println("source is not a Component");
            }
        } else if (e.getActionCommand().contains("certificate")) {
            if (e.getActionCommand().contains("add") || e.getActionCommand().equals("certificate-update")) {
                //find main frame
                Object item = e.getSource();
                if (item instanceof Component) {
                    Window w = findWindow((Component) item);
                    createCertificateDialog((JFrame) w);
                } else {
                    System.out.println("source is not a Component");
                }
            } else if(e.getActionCommand().contains("delete")){
               System.out.println("delete");
            } else if (e.getActionCommand().equals("users")) {
                System.out.println("users menu");
                //set new FRAMe or DIALOG??
                JFrame usersFrame = new JFrame("Сотрудники");
                final UserPanel panel = new UserPanel(usersFrame);
                setMenu();
                usersFrame.getContentPane().add(panel);
                usersFrame.setSize(size);
                usersFrame.setVisible(true);
            }
        }
    }

    public static Window findWindow(Component c) {
        System.out.println(c.getClass().getName());
        if (c instanceof Window) {
            return (Window) c;
        } else if (c instanceof JPopupMenu) {
            JPopupMenu pop = (JPopupMenu) c;
            return findWindow(pop.getInvoker());
        } else {
            Container parent = c.getParent();
            return parent == null ? null : findWindow(parent);
        }
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

    protected static void createFrame(JFrame desktop) {
        CertificateFrame certificate_frame = new CertificateFrame("Frame #" + (openFrameCount+1));
        //certificate_frame.setSize(desktop.getSize());
        certificate_frame.setLocation(xOffset*openFrameCount, yOffset*openFrameCount);
        certificate_frame.setVisible(true);
        //desktop.getContentPane().add(frame);
        desktop.add(certificate_frame);
        try {
            certificate_frame.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
            System.out.println( e.getLocalizedMessage());
        }
        openFrameCount +=1;
    }

    protected void createCertificateDialog(JFrame frame) {
        try {
            final CertificateDialog dialog = new CertificateDialog(frame, "Save&Generate");
            //System.out.println(dialog?"true":"false");
        } catch (Exception e) {
            System.out.println( e.getLocalizedMessage());
        }
    }
}
