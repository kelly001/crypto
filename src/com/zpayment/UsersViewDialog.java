package com.zpayment;

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
import java.io.File;
import java.sql.SQLException;
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
    protected Company company;
    //protected FieldPanel panel = new FieldPanel();
    private static String title = " Сотрудники компании.";


    public UsersViewDialog(Frame parent, FieldPanel panel, Company company) {
        super(parent, title, panel);
        System.out.println("constructor");
        this.company = company;
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
                        panel.addField(cert.getInfo(), "Инфо", new JLabel(), false);

                        JButton button = new JButton("Edit");
                        button.addActionListener(new certAction(cert, user));
                        panel.addField("", "Редактировать сертификат сотрудника", button, false);

                        JButton cancelButton = new JButton("Отозвать");
                        cancelButton.addActionListener(new RemoveCertificateActionListener(cert));
                        panel.addField("", "Отозвать сертификат сотрудника", cancelButton, false);
                    }

                } else {
                    JButton button = new JButton();
                    button.setText("Generate");
                    button.addActionListener(new certAction(user));
                    panel.addField("Нет сертификатов", "Создать сертификат сотрудника", button, true);
                }

            }
        } else {
            JButton newEmp = new JButton("Добавить");
            newEmp.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        final CompanyDialog dialog = new CompanyDialog(frame, "Пользователи");
                        dialog.createNewUserDialog(company);
                        dialog.setVisible(true);
                    } catch (Exception userExp) {
                        System.out.println( userExp.getLocalizedMessage());
                    }
                }
            });
            panel.addField("Нет сотрудников", "Добавить нового сотрудника в компанию", newEmp, true);
        }


        panel.addGlue();
        this.getContentPane().add(panel);
        this.setSize(size);
        this.pack();
    }

    public class certAction implements ActionListener {
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
                        new CertificateDialog(frame, cert, user);
            } catch (Exception e1) {
                System.out.println(e1.getLocalizedMessage());
            }

        }
    }

    public class certificateDeleteAction implements ActionListener {
        private Certificate cert;
        private Boolean succeeded = false;
        certificateDeleteAction(Certificate cert) {
            this.cert = cert;
        }
        public void actionPerformed(ActionEvent e) {
            //com.zpayment.CertificateDialog.removeCertificate(this.cert);
            if (this.cert != null && this.cert.getStatus()) {
                String path = "files/" + this.cert.getFilename();
                try {
                    Certificate.cancel(this.cert.getId());

                    File certFile = new File(path);
                    if (certFile.exists() && certFile.isFile() && certFile.delete())
                        this.succeeded = true;
                } catch (SQLException sqle){
                    System.out.println("Update db exception catched: " + sqle.getLocalizedMessage());
                } catch (Exception x) {
                    System.err.println("deleting certificete files exception catched: " + x.getLocalizedMessage());
                }
            }
        }
        public boolean isSucceeded() {
            return this.succeeded;
        }
    }
}
