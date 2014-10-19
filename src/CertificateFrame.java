import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import javax.swing.JInternalFrame;

public class CertificateFrame extends JInternalFrame {
    static int openFrameCount = 0;
    static final int xOffset = 30, yOffset = 30;
    private JLabel lbUsername;
    private JTextField tfUsername;

    public CertificateFrame() {
        super("Certificates",
                true, //resizable
                true, //closable
                true, //maximizable
                true);//iconifiable
        //...Create the GUI and put it in the window...
        //...Then set the window size or call pack...
        //Set the window's location.
        createGUI();
        setSize(640,480);
        setLocation(xOffset*openFrameCount, yOffset*openFrameCount);
    }

    public void createGUI() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();

        cs.fill = GridBagConstraints.HORIZONTAL;

        lbUsername = new JLabel("Имя пользователя: ");
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        panel.add(lbUsername, cs);

        tfUsername = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 2;
        panel.add(tfUsername, cs);

        getContentPane().add(panel,  BorderLayout.CENTER);
    }

}
