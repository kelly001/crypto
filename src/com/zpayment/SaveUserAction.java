package com.zpayment;

import com.teacode.swing.dialog.CloseButtonDialog;
import database.Company;
import database.Employer;
import database.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class SaveUserAction implements ActionListener{
    private Map<String, JTextField> controls = new HashMap<String, JTextField>();
    private Company company;

    SaveUserAction(Map<String, JTextField> controls, Company comp) {
        this.controls = controls;
        this.company = comp;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Employer user = new Employer();
        user.setUsername(controls.get("username").getText());
        user.setEmail(controls.get("email").getText());
        user.setPassword(controls.get("password").getText());
        user.setCompany(company);
        try {
            if (Employer.newUser(user)){
/*                CloseButtonDialog infodialog =
                        new CloseButtonDialog((JFrame) findWindow(parent), "Успех",
                                new JLabel("Новый пользователь создан!"));
                infodialog.setVisible(true);
                    *//* TODO просто гасить диалог, чтобы откурывалось главное окно с компанией
                    String args[] = {controls.get("email").getText()};
                    parent.setVisible(true);
                    com.zpayment.MainFrame.main(args);*//*
                parent.dispose();*/
                System.out.println("new user created");
            }

        } catch (Exception exc) {
            System.out.println("saving user exception " + exc.getLocalizedMessage());
        }
    }
}
