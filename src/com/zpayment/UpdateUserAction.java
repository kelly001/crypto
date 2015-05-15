package com.zpayment;

import database.Company;
import database.Employer;
import database.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kira_Svetlova on 06.05.2015.
 */
public class UpdateUserAction implements ActionListener{
    private Map<String, JTextField> controls = new HashMap<String, JTextField>();
    private User user;
    UpdateUserAction(Map<String, JTextField> controls, User user) {
        this.user = user;
        this.controls = controls;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("UpdateUserAction");
        user.setUsername(controls.get("username").getText());
        user.setEmail(controls.get("email").getText());
        System.out.println(controls.get("password").getText());
        user.setPassword(controls.get("password").getText());
        /*
        user.setCompany(company);
        user.setCountry(controls.get("country").getText());
        user.setRegion(controls.get("region").getText());
        user.setCity(controls.get("city").getText());
        user.setDepartment(controls.get("department").getText());*/
        try {
            User.saveUser(user);

        } catch (Exception exc) {
            System.out.println("saving user exception " + exc.getLocalizedMessage());
        }
    }
}
