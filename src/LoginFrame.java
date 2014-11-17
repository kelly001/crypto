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

    private JFrame frame;
    private JLabel infolabel;
    public LoginDialog dialog;

    public LoginFrame(String name) {
        super(name);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

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

                if(dialog.isSucceeded()) {
                    infolabel.setText("Hi " + dialog.getUsername() + "!\n\r");
                    MainFrame new_frame = new MainFrame(dialog.getUsername());
                    new_frame.setVisible(true);
                    dispose();
                }
            }
        });

        infolabel = new JLabel("Hello");

        dialog = new LoginDialog(frame);
        dialog.setVisible(true);

        getContentPane().add(infolabel);
        setSize(350, 150);
    }

    public static void main(String[] args) {

        JFrame frame = new LoginFrame("Login Window");
        frame.setVisible(true);
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

