package com.zpayment;

import com.teacode.swing.component.FieldPanel;
import com.teacode.swing.dialog.CloseButtonDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import database.*;

public class UserPanel extends FieldPanel {
    JDialog parent;

    // Поля
    protected String[] names = {"username", "email", "password"};
    protected String[] labels = {"Название/Имя пользователя", "Email", "Пароль"};
    protected Map<String, JTextField> controls = new HashMap<String, JTextField>();
    JButton saveCompanyButton;
    ActionListener action1;

    public UserPanel(Component parent) {
        System.out.println("Users view panel");
        this.parent = (JDialog) parent;
    }

    public void setControls(Company company) {
        final JLabel label = new JLabel();
        String companyLabel = "Новый пользователь";
        this.addField(companyLabel, "label", label, true);
        for (int i=0; i < names.length; i++) {
            JTextField field = new JTextField();
            field.setName(names[i]);
            field.setColumns(20);
            field.setEditable(true);
            controls.put(names[i],field);
            this.addField(labels[i], names[i], field, true);
        }

        saveCompanyButton = new JButton("Сохранить");
        action1 = new saveUserAction(company);
        saveCompanyButton.addActionListener(action1);
        this.addField("", "", saveCompanyButton, false);
        this.addGlue();
    }


    protected void setControls(User user) {
        System.out.println("set controls with user object");

        JTextField username = new JTextField(user.getUsername());
        username.setEditable(false);
        this.addField("Имя пользователя", "Имя пользователя", username, true);

        JTextField email = new JTextField(user.getEmail());
        email.setEditable(false);
        this.addField("Почта пользователя", "Почтовый адрес пользователя", email, true);

        JTextField status = new JTextField(user.getStatus()?"Active":"Not");
        status.setEditable(false);
        this.addField("Статус пользователя", "Статус пользователя", status, true);
        this.addGlue();
    }

    public void setValues(User user) {
        if (user != null) {
            controls.get("username").setText(user.getUsername());
            controls.get("email").setText(user.getEmail());
            saveCompanyButton.addActionListener(new updateUserAction(user));
            saveCompanyButton.removeActionListener(action1);
            this.addGlue();
            this.repaint();
        }
    }

    public Map<String, JTextField> getControls(){
        return this.controls;
    }

    public class saveUserAction implements ActionListener {
        private Employer user = new Employer();
        saveUserAction(Company company) {
            this.user.setCompany(company);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            //User user = new User();
            user.setUsername(controls.get("username").getText());
            user.setEmail(controls.get("email").getText());
            user.setPassword(controls.get("password").getText());
            try {
                if (Employer.newUser(user)){
                    CloseButtonDialog infodialog =
                            new CloseButtonDialog((JFrame) findWindow(parent), "Успех",
                                    new JLabel("Новый пользователь создан!"));
                    infodialog.setVisible(true);
                    /* TODO просто гасить диалог, чтобы откурывалось главное окно с компанией
                    String args[] = {controls.get("email").getText()};
                    parent.setVisible(true);
                    com.zpayment.MainFrame.main(args);*/
                    parent.dispose();
                }

            } catch (Exception exc) {
                System.out.println("saving user exception " + exc.getLocalizedMessage());
            }
        }
    }

    public class updateUserAction implements ActionListener {
        private User user;
        updateUserAction(User user) {
            this.user = user;
        }
        public void actionPerformed(ActionEvent e) {
            user.setUsername(controls.get("username").getText());
            user.setEmail(controls.get("email").getText());
            user.setPassword(controls.get("password").getText());
            try {
                if (User.saveUser(user)){
                    parent.setVisible(false);
                    String args[] = {controls.get("email").getText()};
                    MainFrame.main(args);
                }
            } catch (Exception exc) {
                System.out.println("saving user exception " + exc.getLocalizedMessage());
            }
        }
    }

    public static Window findWindow(Component c) {
        if (c instanceof JFrame) {
            return (Window) c;
        } else {
            Container parent = c.getParent();
            return parent == null ? null : findWindow(parent);
        }
    }
}
