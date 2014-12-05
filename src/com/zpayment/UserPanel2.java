package com.zpayment;

import com.teacode.swing.component.FieldPanel;
import com.teacode.swing.dialog.CloseButtonDialog;
import database.Company;
import database.Employer;
import database.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class UserPanel2 extends FieldPanel {
    private User user;
    // Поля
    private JTextField username;
    private JTextField email;
    private JTextField status;

    public UserPanel2(User user) {
        System.out.println("Users view panel");
        this.user = user;
        setControls(user);
    }

    protected void setControls(User user) {
        System.out.println("set controls with user object");

        username = new JTextField(user.getUsername());  //todo JLabel ???
        username.setEditable(false);
        this.addField("Имя пользователя", "Имя пользователя", username, true);

        email = new JTextField(user.getEmail());
        email.setEditable(false);
        this.addField("Почта пользователя", "Почтовый адрес пользователя", email, true);

        status = new JTextField(user.getStatus()?"Active":"Not");
        status.setEditable(false);
        this.addField("Статус пользователя", "Статус пользователя", status, true);
        this.addGlue();
    }

    public void setValues(User user) {
        if (user != null) {
            username.setText(user.getUsername());
            username.setEditable(true);
            email.setText(user.getEmail());
            email.setEditable(true);
            this.addGlue();
            this.repaint();
        }
    }

    /*public void setCompany(Company company) {
        if (user instanceof Employer)
            user = new Employer();
            user.setCompany(company);
    }*/

    public void save(){
        user.setUsername(username.getText());
        user.setEmail(email.getText());
        //сделть отдельным окном смену пароля для пользователя с валидацией/подтверждением пароля
        //user.setPassword(password.getText());    todo where is password field????
        try {
            if (Employer.newUser(user) != null){
                CloseButtonDialog infodialog =
                        new CloseButtonDialog(null, "Успех",
                                new JLabel("Новый пользователь создан!"));
                infodialog.setVisible(true);
            }
        } catch (Exception exc) {
            System.out.println("saving user exception " + exc.getLocalizedMessage());
        }
    }
}
