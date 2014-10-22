import java.awt.*;
import java.awt.event.*;
import java.security.*;
import javax.swing.*;

import javax.swing.JInternalFrame;

public class CertificateFrame extends JInternalFrame {
    static int openFrameCount = 0;
    static final int xOffset = 30, yOffset = 30;
    Dimension size = new Dimension(200,100);
    private JLabel lbUsername;
    private JTextField tfUsername;
    private JButton btnCert;

    public CertificateFrame(String name) {
        super(name,
                true, //resizable
                true, //closable
                true, //maximizable
                true);//iconifiable
        //...Create the GUI and put it in the window...
        //...Then set the window size or call pack...
        //Set the window's location.
        createGUI();
        //setLocation(xOffset*openFrameCount, yOffset*openFrameCount);
    }

    public void createGUI() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        /*JPanel panel = new JPanel(new FlowLayout());
        final JLabel infolabel = new JLabel("atata");
        panel.add(infolabel);
        panel.setSize(size);
        getContentPane().add(panel,  BorderLayout.CENTER);
        */
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();

        cs.fill = GridBagConstraints.HORIZONTAL;

        lbUsername = new JLabel("Имя: ");
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        panel.add(lbUsername, cs);

        tfUsername = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 1;
        panel.add(tfUsername, cs);

        btnCert = new JButton("Certificate");
        btnCert.setActionCommand("Cert");
        btnCert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = tfUsername.getText();
                System.out.println(name);
                Security certificate = new Security(name);
            }
        });
        cs.gridx = 1;
        cs.gridy = 1;
        panel.add(btnCert,cs);

        panel.setSize(size);
        getContentPane().add(panel,  BorderLayout.CENTER);
        pack();
    }

}
