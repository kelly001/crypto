package com.zpayment;

import com.teacode.swing.component.FieldPanel;

import java.awt.event.*;
import javax.swing.*;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

public class LoginFrame extends JFrame{

    private JFrame frame;
    public LoginDialog dialog;
    public CompanyDialog RegistrationDialog;
    Dimension size = new Dimension(240, 160);

    public LoginFrame() {
        super("Добро пожаловать в Крипто!");
        this.frame = this;
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                System.out.println("WindowListener method called: windowClosed custom.");
                dispose();
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                System.out.println("WindowListener method called: windowActivated custom.");

                if (dialog.isSucceeded()) {
                    MainFrame new_frame = new MainFrame();
                    new_frame.setCompany(dialog.getUsername());

                    new_frame.setGUI();
                    new_frame.setVisible(true);
                    dispose();
                }
            }
        });

        setGUI();
        dialog = new LoginDialog(frame);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        JFrame frame = new LoginFrame();
        frame.setVisible(true);
    }

    private  void setGUI() {
        FieldPanel panel = new FieldPanel();
        JButton login = new JButton("Авторизация");
        login.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog = new LoginDialog(frame);
                dialog.setVisible(true);
            }
        });
        panel.addField("", "Авторизация",login, true);
        JButton registration = new JButton("Регистрация");
        registration.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    RegistrationDialog = new CompanyDialog(frame, "Регистрация новой компании");
                    RegistrationDialog.setGUI();
                    RegistrationDialog.setVisible(true);
                } catch (Exception exc) {
                    System.out.println( exc.getLocalizedMessage());
                }
            }
        });
        panel.addField("", "Регистрация",registration, true);
        panel.addGlue();
        this.setContentPane(panel);
        //this.setSize(size);
        this.pack();
    }


    public void windowActivated(WindowEvent e) {
        System.out.println("WindowListener method called: windowActivated.");
        System.out.println();
    }

    public void windowClosed(WindowEvent e) {
        //This will only be seen on standard output.
        System.out.println("WindowListener method called: windowClosed.");
    }

    public void windowOpened(WindowEvent e) {
        System.out.println("WindowListener method called: windowOpened.");
    }

}

