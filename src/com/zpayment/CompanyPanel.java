package com.zpayment;

import com.teacode.swing.component.FieldPanel;
import com.teacode.swing.dialog.CloseButtonDialog;
import com.teacode.swing.dialog.OkCancelDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import database.*;

public class CompanyPanel extends FieldPanel {
    Component parent;

    // Поля организациии
    protected String[] names = {"username", "email", "department", "city", "region", "country"};
    protected String[] labels = {"Название/Имя пользователя", "Email", "Отделение","Город",
            "Регион", "Страна"};
    protected Map<String, JTextField> controls = new HashMap<String, JTextField>();
    JButton saveCompanyButton;
    ActionListener action1 = new saveCompanyAction();

    public CompanyPanel(User user, Component parent) {
        System.out.println("Company Panel constructor");
        this.parent = parent;
        //FieldPanel panel = new FieldPanel();
        if (user instanceof Company) {
            this.setControls((Company) user);
        } else {
            this.setControls();
        }
        //panel.addGlue();
        if (parent instanceof OkCancelDialog) {
            System.out.println("Parent - OkCancelDialog");
            OkCancelDialog dialog = (OkCancelDialog) parent;
            saveCompanyButton = dialog.getOkButton();
        }
        this.addGlue();
    }

    public void setControls() {
        System.out.println("Company Panel setControls()");
        final JLabel label = new JLabel();
        String companyLabel = "Новая организация";
        this.addField(companyLabel, "label", label, true);
        for (int i=0; i < names.length; i++) {
            JTextField field = new JTextField();
            field.setName(names[i]);
            field.setColumns(20);
            field.setEditable(true);
            controls.put(names[i],field);
            this.addField(labels[i], names[i], field, true);
        }

        // доступно только при создании нового
        JTextField field = new JTextField();
        this.addField("Введите пароль:", "Пароль для нового пользователя", field, true);
        controls.put("password", field );

      /*  saveCompanyButton = new JButton("Сохранить");
        this.addField("", "", saveCompanyButton, false);
        saveCompanyButton.addActionListener(action1);*/
    }


    protected void setControls(Company user) {
        System.out.println("CompanyPanel setControls(Company)");
        String companyLabel = "Организация \""+ user.getUsername() + "\"";
        this.addField(companyLabel, "label", new JPanel(), true);

        JTextField username = new JTextField(user.getUsername());
        username.setEditable(false);
        this.addField("Название компании:", "Название компании:", username, true);

        JTextField email = new JTextField(user.getEmail());
        email.setEditable(false);
        this.addField("Почта пользователя", "Почтовый адрес (Email)", email, true);

        JTextField status = new JTextField(user.getStatus()?"Active":"Not");
        status.setEditable(false);
        this.addField("Статус пользователя", "Статус", status, true);
    }

    public void setValues(Company user) {
        System.out.println("Company Panel setValues");
        System.out.println(user);
        if (user != null) {
            controls.get("username").setText(user.getUsername());
            controls.get("email").setText(user.getEmail());
            controls.get("city").setText(user.getCity());
            controls.get("region").setText(user.getRegion());
            controls.get("department").setText(user.getDepartment());
            controls.get("country").setText(user.getCountry());

            try {
                this.remove(controls.get("password"));
            } catch (Exception   e) {
                System.out.println("saving user exception " + e.getLocalizedMessage());
            }
            //saveCompanyButton.addActionListener(new updateCompanyAction(user));
            saveCompanyButton.removeActionListener(action1);

            this.addGlue();
            this.repaint();
        }
    }

    public Company getCompany () {
        System.out.println("Company Panel getCompany");
        Company user = new Company();
        user.setUsername(controls.get("username").getText());
        System.out.println(controls.get("username").getText());
        user.setEmail(controls.get("email").getText());
        user.setPassword(controls.get("password").getText());
        user.setCountry(controls.get("country").getText());
        user.setRegion(controls.get("region").getText());
        user.setCity(controls.get("city").getText());
        user.setDepartment(controls.get("department").getText());
        return user;
    }

    public Map<String, JTextField> getControls() {
        return this.controls;
    }

    public class saveCompanyAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Company Panel saveCompanyAction");
            Company user = new Company();
            user.setUsername(controls.get("username").getText());
            user.setEmail(controls.get("email").getText());
            user.setPassword(controls.get("password").getText());
            user.setCountry(controls.get("country").getText());
            user.setRegion(controls.get("region").getText());
            user.setCity(controls.get("city").getText());
            user.setDepartment(controls.get("department").getText());
            try {
                if (Company.newUser(user)){
                    CloseButtonDialog infodialog =
                            new CloseButtonDialog((JFrame) findWindow(parent), "Успех",
                                    new JLabel("Новая организация создана!"));
                    parent.setVisible(false);
                    String args[] = {controls.get("email").getText()};
                   /*if (parent instanceof CompanyDialog) parent.dispose();
/*
                    MainFrame.main(args);*/
                }
            } catch (Exception exc) {
                System.out.println("saving user exception " + exc.getLocalizedMessage());
            }
        }
    }

    public  class updateCompanyAction implements ActionListener {
        private Company user;
        updateCompanyAction(Company user) {
            this.user = user;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Company Panel updateCompanyAction");
            user.setUsername(controls.get("username").getText());
            user.setEmail(controls.get("email").getText());
            //user.setPassword(controls.get("password").getText());
            user.setCountry(controls.get("country").getText());
            user.setRegion(controls.get("region").getText());
            user.setCity(controls.get("city").getText());
            user.setDepartment(controls.get("department").getText());
            try {
                if (Company.updateUser(user)){
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