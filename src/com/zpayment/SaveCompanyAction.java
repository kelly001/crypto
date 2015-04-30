package com.zpayment;

import com.teacode.swing.dialog.CloseButtonDialog;
import database.Company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class SaveCompanyAction implements ActionListener {
    private Map<String, JTextField> controls = new HashMap<String, JTextField>();
    SaveCompanyAction(Map<String, JTextField> controls) {this.controls = controls;}
   //private Company user;
   //SaveCompanyAction(Company user) {this.user = user ;}


    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            Company user = new Company();
            user.setUsername(controls.get("username").getText());
            System.out.println(controls.get("username").getText());
            user.setEmail(controls.get("email").getText());
            user.setPassword(controls.get("password").getText());
            user.setCountry(controls.get("country").getText());
            user.setRegion(controls.get("region").getText());
            user.setCity(controls.get("city").getText());
            user.setDepartment(controls.get("department").getText());
            Company.newUser(user);

        } catch (Exception exc) {
            System.out.println("saving user exception " + exc.getLocalizedMessage());
        }
    }
}
