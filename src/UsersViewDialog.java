import com.teacode.swing.CommonRB;
import com.teacode.swing.component.FieldPanel;
import com.teacode.swing.dialog.CloseButtonDialog;
import database.Certificate;
import database.Company;
import database.Employer;
import database.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by new_name on 11.11.2014.
 */
public class UsersViewDialog extends CloseButtonDialog {

    static Logger logger = Logger.getLogger("Users View Dialog log");
    public static Dimension size = new Dimension(480,480);
    protected Frame frame;
    protected ArrayList<Employer> users;
    //protected FieldPanel panel = new FieldPanel();


    public UsersViewDialog(Frame parent, String title, FieldPanel panel) {
        super(parent, title, panel);
        System.out.println("constructor");
        //final CloseButtonDialog dialog = this;
    }

    public void setUsers(Long company_id) {
        System.out.println("Loading users by company " + company_id);
        try {
            users = Employer.loadByCompany(company_id);
            System.out.println("users size:" + users.size());
        } catch (Exception e) {
            System.out.println("Load users error: " + e.getLocalizedMessage());
        }
    }

    public void setControls(FieldPanel panel) {
        final JLabel label = new JLabel();
        panel.addField("Сотрудники", "label", label, true);
        if (users.size() != 0) {
            for (User user: users) {
                //final JLabel label = new JLabel();
                panel.addField(user.getUsername(), "Фамилия, Имя, Отчество сотрудника", new JLabel(), false);
                panel.addField("Сертификаты пользователя:", "label", new JLabel(), true);
                ArrayList<Certificate> certificates  = user.getCertificates();
                if (certificates.size() > 0)   {
                    for (Certificate cert: certificates){
                        JButton button = new JButton("Edit");
                        button.addActionListener(new certAction(cert, user));
                        panel.addField(cert.getInfo(), "Посмотреть сертификат сотрудника", button, true);
                    }

                } else {
                    JButton button = new JButton();
                    button.setText("Generate");
                    button.addActionListener(new certAction(user));
                    panel.addField("Нет сертификатов", "Посмотреть сертификат сотрудника", button, true);
                }

            }
        } else {
            panel.addField("Нет сотрудников", "Добавить нового сотрудника в компанию", new JButton("Добавить"), true);
        }


        panel.addGlue();
        this.getContentPane().add(panel);
        this.setSize(size);
        this.pack();
    }

    public class certAction implements ActionListener
    {
        private final Certificate cert;
        private final User user;// = new Certificate();
        certAction(Certificate cert, User user) {
            super();
            this.cert = cert;
            this.user = user;
        }

        certAction(User user) {
            super();
            this.cert = new Certificate();
            this.user = user;
        }

        public void actionPerformed(ActionEvent e) {
            try {
                final CertificateDialog dialog =
                        new CertificateDialog(frame,cert, user);
                //dialog.setVisible(true);
            } catch (Exception e1) {
                System.out.println( e1.getLocalizedMessage());
            }

        }

}}
