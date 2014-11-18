import com.teacode.swing.component.FieldPanel;

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
    Dimension size = new Dimension(240, 160);

    public LoginFrame(String name) {
        super(name);
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
                    MainFrame new_frame = new MainFrame(dialog.getUsername());
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
        JFrame frame = new LoginFrame("Login Window");
        frame.setVisible(true);
    }

    private  void setGUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        infolabel = new JLabel("Добро пожаловать, гость!");
        infolabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(infolabel);
        JButton login = new JButton("Авторизация");
        login.setAlignmentX(Component.CENTER_ALIGNMENT);
        login.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog = new LoginDialog(frame);
                dialog.setVisible(true);
            }
        });
        panel.add(login);
        JButton registration = new JButton("Регистрация");
        registration.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(registration);
        this.setContentPane(panel);
        this.setSize(size);
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

