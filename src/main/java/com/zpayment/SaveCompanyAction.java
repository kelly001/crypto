package com.zpayment;

import database.Company;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SaveCompanyAction implements ActionListener {
    private final Company user;

    SaveCompanyAction(Company user) {this.user = user ;}

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            Company.newUser(user);
        } catch (Exception exc) {
            System.out.println("saving user exception " + exc.getLocalizedMessage());
        }
    }
}
