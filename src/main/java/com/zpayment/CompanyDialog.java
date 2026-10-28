package com.zpayment;

import com.zpayment.ui.FieldPanel;
import com.zpayment.ui.OkCancelDialog;
import database.Company;

import java.awt.*;

public class CompanyDialog extends OkCancelDialog {
    public Company company;

    public CompanyDialog(Frame parent, String title) {
        super(parent, title, "Сохранить");
        System.out.println("CompanyDialog constructor");
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
        System.out.println("CompanyDialog setGUI");
        FieldPanel companyPanel = new CompanyPanel(company, this);
        this.setMainPanel(companyPanel);

        this.ok.addActionListener(new SaveCompanyAction(company));
        this.packAndCenter();
    }

    public void createCompanyEditDialog() {
        System.out.println("CompanyDialog createCompanyEditDialog");

        CompanyPanel companyPanel = new CompanyPanel(company, this);
        companyPanel.setValues(company);
        this.setMainPanel(companyPanel);

        this.pack();
    }

    public void createNewUserDialog(Company company) {
        System.out.println("CompanyDialog createNewUserDialog");

        UserPanel panel = new UserPanel(this);
        panel.setControls(company);
        this.ok.addActionListener(new SaveCompanyAction(company));
        this.getContentPane().add(panel);
        this.pack();
    }
}