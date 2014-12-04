import com.teacode.swing.component.FieldPanel;
import com.teacode.swing.dialog.CloseButtonDialog;
import com.teacode.swing.dialog.OkCancelDialog;
import database.Certificate;
import database.Company;
import database.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;


/**
 * Created by new_name on 14.10.2014.
 */
public class MainFrame extends JFrame implements ActionListener, ItemListener{

    Dimension size = new Dimension(640,480);
    public User company;// = new Company();

    private JMenuBar menuBar;
    private JMenu menu, certificateMenu, companyMenu, userMenu, viewMenu, infoMenu;
    private JMenuItem menuItem;
    private MainPanel panel;

    public MainFrame() {
        super("Crypto App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //set icon
        ImageIcon img = new ImageIcon("icon.jpg");
        this.setIconImage(img.getImage());
    }

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
        //addMenuItem("Сертификаты", this, "cert", menu);
        //addMenuItem("Организация", this, "org", menu);
        //addMenuItem("Ключи", this,"key", menu);
        addMenuItem("Главная", this, "main", menu);
        addMenuItem("Сменить пользователя", this, "logout", menu);
        addMenuItem("Выход", this, "exit", menu);
        menuBar.add(menu);

        //Build the cert menu.
        certificateMenu = new JMenu("Сертификат организации");
        certificateMenu.setMnemonic(KeyEvent.VK_F3);
        certificateMenu.getAccessibleContext().setAccessibleDescription("");
        addMenuItem("Добавить", this, "certificate-add", certificateMenu);
        addMenuItem("Отозвать", this, "certificate-delete", certificateMenu);

        //Build the users menu.
        userMenu = new JMenu("Сотрудники");
        userMenu.setMnemonic(KeyEvent.VK_F4);
        userMenu.getAccessibleContext().setAccessibleDescription("");
        if (!(company instanceof Company)) {
            userMenu.setEnabled(false);
        }
        addMenuItem("Добавить", this, "user-add", userMenu);
        addMenuItem("Удалить", this, "user-delete", userMenu);

        //Build the company menu.
        companyMenu = new JMenu("Редактирование");
        companyMenu.setMnemonic(KeyEvent.VK_F2);
        companyMenu.getAccessibleContext().setAccessibleDescription("");
        addMenuItem("Информация о компании", this, "company-edit", companyMenu);
        companyMenu.add(certificateMenu);
        companyMenu.add(userMenu);
        //addMenuItem("Сертификат", this, "company-certificate", companyMenu);
        menuBar.add(companyMenu);

        //Build the view menu.
        viewMenu = new JMenu("Просмотр");
        viewMenu.setMnemonic(KeyEvent.VK_U);
        if (!(company instanceof Company)) {
            viewMenu.setEnabled(false);
        }
        addMenuItem("Информация о компании", this, "company-view", viewMenu);
        //addMenuItem("Ключ", this,"key", viewMenu);
        //addMenuItem("Хеш", this, "hash", viewMenu);
        addMenuItem("Сотрудники", this, "users", viewMenu);

        infoMenu = new JMenu("Помощь");
        infoMenu.setMnemonic(KeyEvent.VK_F5);
        infoMenu.setEnabled(false);
        addMenuItem("О компании",this, "company", infoMenu);
        addMenuItem("Справка", this, "help", infoMenu);
        menuBar.add(viewMenu);
        menuBar.add(infoMenu);
        setJMenuBar(menuBar);

    }

    public void setCompany(String name) {
        try {
            System.out.println(name);
            company = Company.loadByEmail(name);
            if (company == null) this.setUser(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUser(String name) {
        try {
            System.out.println(name);
            company = Company.loadByEmail(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setGUI() {
        MainPanel panel = new MainPanel(this);
        panel.setGUI(company);
        setMenu();
        this.setContentPane(panel);
        this.setSize(size);
        this.pack();
        this.setVisible(true);
    }



    //main func, first argument - company email
    public static void main(String[] arg) {
        MainFrame new_frame = new MainFrame();
        if (arg.length>0) new_frame.setCompany(arg[0]);
        new_frame.setGUI();
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
        if (e.getSource() instanceof Component) {
            if (e.getActionCommand().equals("main")) {
                String args[] = {company.getEmail()};
                MainFrame.main(args);
                Window w = findWindow((Component)e.getSource());
                w.dispose();
            } else if (e.getActionCommand().equals("exit")) {
                System.exit(1);
            } else if (e.getActionCommand().equals("logout")) {
                // start login frame
                LoginFrame new_frame = new LoginFrame("Авторизация");
                new_frame.setVisible(true);
                findWindow((Component) e.getSource()).dispose();
            } else if (e.getActionCommand().contains("certificate")) {
                if (e.getActionCommand().contains("add") || e.getActionCommand().equals("certificate-update")) {
                    //find main
                    Window w = findWindow((Component) e.getSource());
                    // TODO сделать передачу сертификата, id сертификата лучше
                    createCertificateDialog((JFrame) w);
                }
            } else if (e.getActionCommand().equals("users")) {
                    System.out.println("users menu");
                    Window w = findWindow((Component) e.getSource());
                    createUsersDialog((JFrame) w);
            } else if (e.getActionCommand().contains("company")) {
                if (e.getActionCommand().contains("view")) {
                        Window w = findWindow((Component) e.getSource());
                        createCompanyDialog((JFrame) w);
                } else if (e.getActionCommand().contains("edit")) {
                        Window w = findWindow((Component) e.getSource());
                        createEditCompanyDialog((JFrame) w);
                }
            } else if (e.getActionCommand().contains("user")) {
                if (e.getActionCommand().contains("add")){
                    Window w = findWindow((Component) e.getSource());
                    NewUserDialog((JFrame) w);
                }
                if (e.getActionCommand().contains("delete")) {
                    //TODO get user's id
                }
            }
        }else {
            System.out.println("source is not a Component");
        }
    }

    public static Window findWindow(Component c) {
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

    protected void createCertificateDialog(JFrame frame) {
        try {
            final CertificateDialog dialog =
                    new CertificateDialog(frame, new Certificate(), company);
            //System.out.println(dialog?"true":"false");
        } catch (Exception e) {
            System.out.println( "Create Certificate Dialog error: " + e.getLocalizedMessage());
        }
    }

    protected void createUsersDialog(JFrame frame) {
        System.out.println("create users dialog func");
        try {
            final FieldPanel panel = new FieldPanel();
            final UsersViewDialog dialog = new UsersViewDialog(frame, "Сотрудники", panel);
            dialog.setUsers(company.getId());
            dialog.setControls(panel);
            dialog.setVisible(true);
        } catch (Exception e) {
            System.out.println( e.getLocalizedMessage());
        }
    }

    protected void createCompanyDialog(JFrame frame) {
        try {
            final CompanyDialog dialog = new CompanyDialog(frame, "Информация о компании", new JPanel());
            dialog.setCompany(company.getEmail());
            dialog.setGUI();
            dialog.setVisible(true);
        } catch (Exception e) {
            System.out.println( e.getLocalizedMessage());
        }
    }

    protected void createEditCompanyDialog(JFrame frame) {
        try {
            final CompanyDialog dialog = new CompanyDialog(frame, "Информация о компании", new JPanel());
            dialog.setCompany(company.getEmail());
            dialog.createCompanyEditDialog();
            dialog.setVisible(true);
        } catch (Exception e) {
            System.out.println( e.getLocalizedMessage());
        }
    }

    protected void EditUserDialog(JFrame frame) {
        try {
            final CompanyDialog dialog = new CompanyDialog(frame, "Пользователи", new JPanel());
            dialog.createUserEditDialog(company);
            dialog.setVisible(true);
        } catch (Exception e) {
            System.out.println( e.getLocalizedMessage());
        }
    }

    protected void NewUserDialog(JFrame frame) {
        try {
            final CompanyDialog dialog = new CompanyDialog(frame, "Пользователи", new JPanel());
            dialog.createNewUserDialog((Company) company);
            dialog.setVisible(true);
        } catch (Exception e) {
            System.out.println( e.getLocalizedMessage());
        }
    }
}
