package com.zpayment;

import com.teacode.swing.component.FieldPanel;
import com.teacode.swing.dialog.CloseButtonDialog;
import com.teacode.swing.dialog.OkCancelDialog;
import database.Company;
import database.Employer;
import database.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by new_name on 20.11.2014.
 */
public class CompanyDialog extends OkCancelDialog {
    static Logger logger = Logger.getLogger("Users View Dialog log");
    public static Dimension size = new Dimension(480,480);
    protected Frame frame;
    public Company company;
    //protected JPanel panel;

    public CompanyDialog(Frame parent, String title) {
        super(parent, title, "Сохранить");
        System.out.println("CompanyDialog constructor");
        //this.panel = panel;
        this.frame = parent;
        //this.setGUI();
    }

    public void setCompany(String name) {
        System.out.println("CompanyDialog setCompany");
        try {
            System.out.println(name);
            company = Company.loadByEmail(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setGUI() {
        //FieldPanel panel = new FieldPanel();
        //this.getContentPane().setLayout(new FlowLayout());
        System.out.println("CompanyDialog setGUI");
        FieldPanel companyPanel = new CompanyPanel(company, this);
        this.setMainPanel(companyPanel);
        //this.getContentPane().add(companyPanel);
        this.ok.addActionListener(new SaveCompanyAction(company));
        this.packAndCenter();
        //this.setContentPane(companyPanel);
        //this.setSize(size);
    }

    public void createCompanyEditDialog() {
        //this.getContentPane().setLayout(new FlowLayout());
        System.out.println("CompanyDialog createCompanyEditDialog");
        //CompanyPanel companyPanel = new CompanyPanel(new User(), frame);
        CompanyPanel companyPanel = new CompanyPanel(company, this);
        companyPanel.setValues(company);
        this.setMainPanel(companyPanel);
        //this.ok.addActionListener(new SaveCompanyAction(new Company()));
        //this.ok.addActionListener(new companyPanel.saveCompanyAction());
        this.pack();
        //this.setContentPane(panel);
        //this.setSize(size);
    }

    public void createUserEditDialog(User user) {
        System.out.println("CompanyDialog createUserEditDialog");
        //this.getContentPane().setLayout(new FlowLayout());
        UserPanel panel = new UserPanel(this);
        panel.setControls(null);
        panel.setValues(user);
        this.ok.addActionListener(new saveUserAction((Employer) user));
        this.getContentPane().add(panel);
        this.pack();
        //this.setContentPane(panel);
        //this.setSize(size);
    }

    public void createNewUserDialog(Company company) {
        System.out.println("CompanyDialog createNewUserDialog");
        //this.getContentPane().setLayout(new FlowLayout());
        UserPanel panel = new UserPanel(this);
        panel.setControls(company);
        this.ok.addActionListener(new SaveCompanyAction(company));
        this.getContentPane().add(panel);
        this.pack();
        //this.setContentPane(panel);
        //this.setSize(size);
    }

    public class saveUserAction implements ActionListener {
        private Employer user = new Employer();

        saveUserAction(Employer user) {
            this.user = user;
        }

        public void actionPerformed(ActionEvent e) {
            try {
                if (Employer.newUser(user)) {
                    // parent.dispose();
                    System.out.println("yeh");
                }

            } catch (Exception exc) {
                System.out.println("saving user exception " + exc.getLocalizedMessage());
            }
        }
    }
}