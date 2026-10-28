package com.zpayment;

import com.zpayment.ui.FieldPanel;
import com.zpayment.ui.CloseButtonDialog;
import database.Certificate;
import database.Company;
import database.Employer;
import database.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class UsersViewDialog extends CloseButtonDialog {
    public static Dimension size = new Dimension(480,480);
    protected Frame frame;
    protected ArrayList<Employer> users;
    protected Company company;

    private static String title = " Сотрудники компании.";

    public UsersViewDialog(Frame parent, FieldPanel panel, Company company) {
        super(parent, title, panel);
        System.out.println("constructor");
        this.company = company;
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
        private final User user;

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
                new CertificateDialog(frame, cert, user);
            } catch (Exception e1) {
                System.out.println(e1.getLocalizedMessage());
            }
        }
    }
}
