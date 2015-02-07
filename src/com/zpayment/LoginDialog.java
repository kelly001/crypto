package com.zpayment;

import external.JLinkButton;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by new_name on 17.10.2014.
 */
public class LoginDialog extends JDialog{
    private JTextField tfUsername;
    private JPasswordField pfPassword;
    private JLabel lbUsername;
    private JLabel lbPassword;
    private JButton btnLogin;
    private JButton btnCancel;
    private boolean succeeded = false;
    private String username = "";


    public LoginDialog(Frame parent) {
        super(parent, "Авторизация", true);
        succeeded = false;
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();

        cs.fill = GridBagConstraints.HORIZONTAL;

        lbUsername = new JLabel("Логин (Email): ");
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        panel.add(lbUsername, cs);

        tfUsername = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 2;
        panel.add(tfUsername, cs);

        lbPassword = new JLabel("Пароль: ");
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        panel.add(lbPassword, cs);

        pfPassword = new JPasswordField(20);
        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 2;
        panel.add(pfPassword, cs);
        panel.setBorder(new LineBorder(Color.GRAY));

        // Controls
        JPanel bp = new JPanel();
        btnLogin = new JButton("Войти");
        btnLogin.addActionListener(new AuthActionListener());

        btnCancel = new JButton("Отмена");
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        JLinkButton registrationLink = new JLinkButton("Регистрация");
        registrationLink.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    CompanyDialog RegistrationDialog = new CompanyDialog(null, "Регистрация");
                    RegistrationDialog.setGUI();
                    RegistrationDialog.setVisible(true);
                    //dispose();
                } catch (Exception exc) {
                    System.out.println( exc.getLocalizedMessage());
                }
            }
        });


        bp.add(btnLogin);
        bp.add(btnCancel);
        bp.add(registrationLink);
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.PAGE_END);

        pack();
        setResizable(false);
    }

    public class AuthActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e) {

            if (Login.authenticate(getUsername(), getPassword())) {
                /*JOptionPane.showMessageDialog(LoginDialog.this,
                        "Hi " + getUsername() + "! You have successfully logged in.",
                        "Авторизация",
                        JOptionPane.INFORMATION_MESSAGE);*/

                succeeded = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(LoginDialog.this,
                        "Invalid username or password",
                        "com.zpayment.Login",
                        JOptionPane.ERROR_MESSAGE);
                succeeded = false;

            }
            //dispose();
        }

    }


    public String getUsername() {
        if (username == null || username.equals("")) {
            if (!tfUsername.getText().trim().equals(""))
                username = tfUsername.getText().trim();

        }
        System.out.println("username="+username);
        return username;
    }

    public String getPassword() {
        return new String(pfPassword.getPassword());
    }

    public Boolean isSucceeded() {
        /*if (succeeded) return this.getUsername();
            else return null;*/
        return succeeded;
    }


    public void dispose() {
        getUsername();
        super.dispose();
    }

}
