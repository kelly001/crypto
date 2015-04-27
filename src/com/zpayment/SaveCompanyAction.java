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
    private Company user = new Company();;
    //SaveCompanyAction(Map<String, JTextField> controls) {this.controls = controls;}
    SaveCompanyAction(Company user) {this.user = user ;}

    @Override
    public void actionPerformed(ActionEvent e) {
        try {

            if (Company.newUser(user)){

                //parent.setVisible(false);
                String args[] = {controls.get("email").getText()};
            }
        } catch (Exception exc) {
            System.out.println("saving user exception " + exc.getLocalizedMessage());
        }
    }
}
