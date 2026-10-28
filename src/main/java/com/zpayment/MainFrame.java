package com.zpayment;

import com.zpayment.ui.FieldPanel;
import database.Certificate;
import database.Company;
import database.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.URI;
import java.net.URL;

public class MainFrame extends JFrame implements ActionListener, ItemListener{
    Dimension size = new Dimension(640,480);
    public User company;
    private MainFrame frame;
    public LoginDialog login;

    private JMenuBar menuBar;
    private JMenu menu, certificateMenu, companyMenu, userMenu, infoMenu;
    private JMenuItem menuItem;

    public MainFrame() {
        super("Добро пожаловать в Крипто!");
        this.frame = this;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageIcon img = new ImageIcon(MainFrame.class.getResource("/icon.jpg"));
        this.setIconImage(img.getImage());

        login = new LoginDialog(frame);
        login.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                System.out.println("!!!!");
                if (login.isSucceeded()) {
                    String user = login.getUsername();
                    frame.setCompany(user);

                    frame.setGUI();
                    frame.setVisible(true);
                }
            }
        });

        login.setVisible(true);
    }

    private void addMenuItem(String name, ActionListener action, String id, JMenu menu) {
        menuItem = new JMenuItem(name,
                KeyEvent.VK_T);

        menuItem.getAccessibleContext().setAccessibleDescription(
                "This doesn't really do anything");
        menuItem.setActionCommand(id);
        menuItem.addActionListener(action);
        menuItem.addItemListener(this);
        menu.add(menuItem);
    }

    public void setMenu () {
        menuBar = new JMenuBar();

        menu = new JMenu("Меню");
        menu.setMnemonic(KeyEvent.VK_F1);

        addMenuItem("Главная", this, "main", menu);
        addMenuItem("Сменить пользователя", this, "logout", menu);
        addMenuItem("Выход", this, "exit", menu);
        menuBar.add(menu);

        certificateMenu = new JMenu("Сертификат");
        certificateMenu.setMnemonic(KeyEvent.VK_F3);
        certificateMenu.getAccessibleContext().setAccessibleDescription("");
        addMenuItem("Добавить", this, "certificate-add", certificateMenu);
        addMenuItem("Отозвать", this, "certificate-delete", certificateMenu);

        JMenu infor1Menu = new JMenu("Информация");
        addMenuItem("Редактирование", this, "company-edit", infor1Menu);
        addMenuItem("Просмотр", this, "company-view", infor1Menu);

        companyMenu = new JMenu("Компания");
        companyMenu.setMnemonic(KeyEvent.VK_F2);
        companyMenu.getAccessibleContext().setAccessibleDescription("");
        companyMenu.add(infor1Menu);
        companyMenu.add(certificateMenu);

        menuBar.add(companyMenu);

        if (!(company instanceof Company)) companyMenu.setEnabled(false);

        userMenu = new JMenu("Пользователи");
        userMenu.setMnemonic(KeyEvent.VK_F4);
        userMenu.getAccessibleContext().setAccessibleDescription("");
        if (!(company instanceof Company)) {
            userMenu.add(certificateMenu);
            userMenu.add(infor1Menu);
        } else {
            addMenuItem("список сотрудников", this, "users", userMenu);
        }
        addMenuItem("Новый", this, "user-add", userMenu);

        menuBar.add(userMenu);

        infoMenu = new JMenu("Помощь");
        infoMenu.setMnemonic(KeyEvent.VK_F5);
        JMenuItem company = new JMenuItem("О компании");
        company.addActionListener(new URLAction("https://z-payment.com/"));
        infoMenu.add(company);
        JMenuItem help = new JMenuItem("Как пользоваться программой");
        help.addActionListener(new URLAction(new File("docs/index.html")));
        infoMenu.add(help);
        JMenuItem WinHelp = new JMenuItem("Установка сертификата Windows");
        WinHelp.addActionListener(new URLAction(new File("docs/windows.html")));
        infoMenu.add(WinHelp);
        JMenuItem bugs = new JMenuItem("Для разработчиков");
        bugs.addActionListener(new URLAction(new File("docs/Bugs.html")));
        infoMenu.add(bugs);
        addMenuItem("Установка сертификата в Chrome", new URLAction("http://russvet24.ru/services/2134/"), "browserhelp", infoMenu);
        addMenuItem("Установка сертификата в FireFox", new URLAction("http://russvet24.ru/services/2132/"), "browserhelp", infoMenu);
        menuBar.add(infoMenu);
        setJMenuBar(menuBar);
    }

    public  void setCompany(String name) {
        try {
            System.out.println("setCompany function, user = " + name);
            company = Company.loadByEmail(name);
            if (company == null) this.setUser(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUser(String name) {
        try {
            System.out.println("Set user function, user email = " + name);
            company = Company.loadByEmail(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setGUI() {
        MainPanel panel = new MainPanel(frame);
        panel.setGUI(company);
        setMenu();
        frame.setContentPane(panel);
        frame.setSize(size);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] arg) {
        com.zpayment.MainFrame new_frame = new com.zpayment.MainFrame();
        if (arg.length>0){
            new_frame.setCompany(arg[0]);
            new_frame.setGUI();
            new_frame.setVisible(true);
        }
    }

    public void actionPerformed(ActionEvent e) {
        System.out.println("Menu item action default");
        System.out.println(e.getActionCommand());

        if (e.getSource() instanceof Component) {
            if (e.getActionCommand().equals("main")) {
                frame.setGUI();
                frame.setVisible(true);
            } else if (e.getActionCommand().equals("exit")) {
                System.exit(1);
            } else if (e.getActionCommand().equals("logout")) {
                new MainFrame();

                findWindow((Component) e.getSource()).dispose();
            } else if (e.getActionCommand().contains("certificate")) {
                if (e.getActionCommand().contains("add") || e.getActionCommand().equals("certificate-update")) {
                    createCertificateDialog(frame);
                }
                if (e.getActionCommand().contains("delete")) {
                   if (company != null){
                       Certificate companyCE = company.getValidCertificate();
                       if (companyCE !=null)
                            new RemoveCertificateActionListener(company.getValidCertificate());
                   }
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
                    try {
                        new RemoveUserActionListener(Company.loadUsers().get(0));
                    } catch (Exception ULExc) {
                        System.out.println("Load company users exeption: " + ULExc.getLocalizedMessage());
                    }
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

    protected void createCertificateDialog(final MainFrame frame) {
        try {
            final CertificateDialog dialog =
                    new CertificateDialog(frame, new Certificate(), company);

            dialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    frame.setGUI();
                    frame.setVisible(true);
                }
            });
        } catch (Exception e) {
            System.out.println( "Create Certificate Dialog error: " + e.getLocalizedMessage());
        }
    }

    protected void createUsersDialog(JFrame frame) {
        System.out.println("create users dialog func");
        try {
            final FieldPanel panel = new FieldPanel();
            final UsersViewDialog dialog = new UsersViewDialog(frame, panel, (Company) company);
            dialog.setUsers(company.getId());
            dialog.setControls(panel);
            dialog.setVisible(true);
        } catch (Exception e) {
            System.out.println( e.getLocalizedMessage());
        }
    }

    protected void createCompanyDialog(JFrame frame) {
        try {
            final CompanyDialog dialog = new CompanyDialog(frame, "Информация о компании");
            dialog.setCompany(company.getEmail());
            dialog.setGUI();
            dialog.setVisible(true);
        } catch (Exception e) {
            System.out.println( e.getLocalizedMessage());
        }
    }

    protected void createEditCompanyDialog(JFrame frame) {
        try {
            final CompanyDialog dialog = new CompanyDialog(frame, "Информация о компании");
            dialog.setCompany(company.getEmail());
            dialog.createCompanyEditDialog();
            dialog.setVisible(true);
        } catch (Exception e) {
            System.out.println( e.getLocalizedMessage());
        }
    }

    protected void NewUserDialog(JFrame frame) {
        try {
            final CompanyDialog dialog = new CompanyDialog(frame, "Пользователи");
            dialog.createNewUserDialog((Company) company);
            dialog.setVisible(true);
        } catch (Exception e) {
            System.out.println( e.getLocalizedMessage());
        }
    }

    private class URLAction implements ActionListener {
        private URI url;

        URLAction(String url) {
            try {
                this.url = new URL(url).toURI();
            } catch (Exception e) {
                System.out.println("Open url exception: " + e.getLocalizedMessage());
            }
        }

        URLAction(File url) {
            this.url = url.toURI();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
                if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE))
                    desktop.browse(url);
            } catch (Exception URIex) {
                System.out.println("Open url exception: " + URIex.getLocalizedMessage());

                String msg = "This program just tried to open a webpage." + "\n"
                        + "The URL has been copied to your clipboard, simply paste into your browser to access."+
                        "Webpage: " + url;
                JOptionPane.showMessageDialog(null, msg, "URL Error", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}
