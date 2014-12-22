package com.zpayment;

import com.teacode.swing.component.FieldPanel;
import com.teacode.swing.dialog.CloseButtonDialog;
import database.Company;
import database.User;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

/**
 * Created by new_name on 20.11.2014.
 */
public class CompanyDialog extends CloseButtonDialog {
    static Logger logger = Logger.getLogger("Users View Dialog log");
    public static Dimension size = new Dimension(480,480);
    protected Frame frame;
    public Company company;
    //protected JPanel panel;

    public CompanyDialog(Frame parent, String title) {
        super(parent, title, new JPanel());
        //this.panel = panel;
        this.frame = parent;
    }

    public void setCompany(String name) {
        try {
            System.out.println(name);
            company = Company.loadByEmail(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setGUI() {
        //FieldPanel panel = new FieldPanel();
        this.getContentPane().setLayout(new FlowLayout());

        FieldPanel companyPanel = new CompanyPanel(company, this);
        this.getContentPane().add(companyPanel);
        this.pack();
        //this.setContentPane(companyPanel);
        //this.setSize(size);
    }

    public void createCompanyEditDialog() {
        this.getContentPane().setLayout(new FlowLayout());

        CompanyPanel companyPanel = new CompanyPanel(new User(), frame);
        companyPanel.setValues(company);
        this.getContentPane().add(companyPanel);
        this.pack();
        //this.setContentPane(panel);
        //this.setSize(size);
    }

    public void createUserEditDialog(User user) {
        this.getContentPane().setLayout(new FlowLayout());
        UserPanel panel = new UserPanel(this);
        panel.setControls(null);
        panel.setValues(user);
        this.getContentPane().add(panel);
        this.pack();
        //this.setContentPane(panel);
        //this.setSize(size);
    }

    public void createNewUserDialog(Company company) {
        this.getContentPane().setLayout(new FlowLayout());
        UserPanel panel = new UserPanel(this);
        panel.setControls(company);
        this.getContentPane().add(panel);
        this.pack();
        //this.setContentPane(panel);
        //this.setSize(size);
    }

}
