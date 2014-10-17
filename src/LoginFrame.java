import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

public class LoginFrame extends JFrame{
    public static void main(String[] args) {
        final JFrame frame = new JFrame("Login Window");
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        final JLabel infolabel = new JLabel("Hello");


        final LoginDialog loginDlg = new LoginDialog(frame);
        loginDlg.setVisible(true);

        // if logon successfully
        System.out.println(loginDlg.isSucceeded());
        if(loginDlg.isSucceeded()){
            infolabel.setText("Hi " + loginDlg.getUsername() + "!\n\r");
            frame.setVisible(false);
            frame.dispose();

            MainFrame new_frame = new MainFrame();
            new_frame.pack();
            new_frame.setVisible(true);
        }

        /*loginDlg.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                infolabel.setText("action");
                if(loginDlg.isSucceeded()){
                    infolabel.setText("Hi " + loginDlg.getUsername() + "!\n\r");
                    //frame.setVisible(false);
                    //frame.dispose();

                    /*MainFrame new_frame = new MainFrame();
                    new_frame.pack();
                    new_frame.setVisible(true);
                }
            }
        });*/
        frame.setSize(350, 150);
        frame.setLayout(new FlowLayout());
        frame.getContentPane().add(infolabel);
        frame.setVisible(true);

    }

}

