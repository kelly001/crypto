package com.zpayment; /**
 * Created by new_name on 10.11.2014.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.teacode.swing.component.FieldPanel;

import database.Certificate;
import database.User;
import external.JLinkButton;

public class MainPanel extends JPanel{

    protected Logger logger = Logger.getLogger("Certificate panel");
    protected JFrame frame;

    public MainPanel (JFrame frame) {
        System.out.println("Main application panel");
        this.frame = frame;
    }

    public void setGUI(User user) {
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        FieldPanel companyPanel = new CompanyPanel(user, frame);
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 0;
        this.add(companyPanel,c);

        JLabel certLabel = new JLabel("Сертификаты пользователя:");
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 1;
        this.add(certLabel, c);

        if (user != null) {
            FieldPanel certPanel = this.certificatesGUI(user);
            c.gridwidth = 3;
            c.gridx = 0;
            c.gridy = 2;
            this.add(certPanel,c);

            JButton getCertificateButton = new JButton("Новый");
            getCertificateButton.addActionListener(new certAction(user));
            c.gridwidth = 1;
            c.gridx = 0;
            c.gridy = 3;
            this.add(getCertificateButton,c);
        }



        JLabel infoLabel = new JLabel("Сссылки на Z-Payment");
        c.gridwidth = 2;
        c.gridx = 3;
        c.gridy = 3;
        this.add(infoLabel,c);

        final JLinkButton btn = new JLinkButton("Z-Payment.com");
        btn.addActionListener(new URLAction("https://z-payment.com/"));
        c.gridwidth = 2;
        c.gridx = 3;
        c.gridy = 4;
        this.add(btn, c);
    }

    public FieldPanel certificatesGUI (User user) {
        //Certificates GUI
        FieldPanel certPanel = new FieldPanel();
        try {
            ArrayList<Certificate> certificates  = Certificate.loadByUser(user.getId());
            if (certificates.size() > 0)   {
                for (Certificate cert: certificates){
                    certPanel.addField(cert.getInfo(), "Инфо", new JLabel(), false);
                    JButton button = new JButton("Edit");
                    button.addActionListener(new certAction(cert, user));
                    certPanel.addField("", "Посмотреть сертификат сотрудника", button, false);

                    JButton cancelButton = new JButton("Отозвать");
                    cancelButton.addActionListener(new RemoveCertificateActionListener(cert));
                    certPanel.addField("", "Отозвать сертификат сотрудника", cancelButton, false);
                }

            } else {
                certPanel.addField("Нет сертификатов пользователя", "", new JLabel(), false);
            }
        }catch (Exception e){ //SQLException e) {
            System.out.println("Load certficates SQLException in User constructor " + e.getLocalizedMessage());}
        return certPanel;
    }

    public class certAction implements ActionListener
    {
        private final Certificate cert;
        private final User user;// = new Certificate();
        certAction(Certificate cert, User user) {
            super();
            this.cert = cert;
            this.user = user;
        }

        certAction(User user) {
            super();
            this.cert = new Certificate();
            this.user = user;
        }

        public void actionPerformed(ActionEvent e) {
            try {
                final CertificateDialog dialog =
                        new CertificateDialog(frame, cert, user);
                //dialog.setVisible(true);
            } catch (Exception e1) {
                System.out.println( "certAction actionPerfomed Exception: " + e1.getLocalizedMessage());
            }

        }
    }

    private class URLAction implements ActionListener {
        private String url;

        URLAction(String url) {
            this.url = url;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                Desktop.getDesktop().browse(new URL(url).toURI());
            } catch (Exception URIex) {
                System.out.println(URIex.getLocalizedMessage());
            }
        }
    }
}
