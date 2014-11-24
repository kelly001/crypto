/**
 * Created by new_name on 10.11.2014.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.teacode.swing.SmartTextArea;
import com.teacode.swing.component.FieldPanel;

import database.Certificate;
import database.Company;
import database.User;

public class MainPanel extends JPanel{

    protected Logger logger = Logger.getLogger("Certificate panel");
    protected Frame frame;

    public MainPanel (Frame frame) {
        System.out.println("Main application panel");
        this.frame = frame;
    }

    public void setGUI(User user) {
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        FieldPanel companyPanel = new UserPanel(user);
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 0;
        this.add(companyPanel,c);

        JLabel certLabel = new JLabel("Сертификаты пользователя:");
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 1;
        this.add(certLabel, c);

        if (user != null) {
            FieldPanel certPanel = this.certificatesGUI(user);
            c.gridwidth = 2;
            c.gridx = 0;
            c.gridy = 2;
            this.add(certPanel,c);
        }
        

        JButton getCertificateButton = new JButton("Получить сертификат");
        getCertificateButton.addActionListener(new certAction());
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 3;
        this.add(getCertificateButton,c);

        JLabel infoLabel = new JLabel("Сссылки на Z-Payment");
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 3;
        this.add(infoLabel,c);

        JLinkButton btn = new JLinkButton("https://z-payment.com/");
        c.gridwidth = 1;
        c.gridx = 1;
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
                    JButton button = new JButton("Edit");
                    button.addActionListener(new certAction(cert));
                    certPanel.addField(cert.getInfo(), "Посмотреть сертификат сотрудника", button, true);
                }

            } else {
                final JButton getCertificateButton = new JButton("Получить сертификат");
                getCertificateButton.addActionListener(new certAction());
                certPanel.addField("Нет сертификатов пользователя", "", new JLabel(), false);
            }
        }catch (SQLException e) {
            System.out.println("Load certficates SQLException in User constructor " + e.getLocalizedMessage());}
        return certPanel;
    }

    public class certAction implements ActionListener
    {
        private final Certificate cert;// = new Certificate();
        certAction(Certificate cert) {
            super();
            this.cert = cert;
        }

        certAction() {
            super();
            this.cert = new Certificate();
        }

        public void actionPerformed(ActionEvent e) {
            try {
                final CertificateDialog dialog = new CertificateDialog(frame, "Save&Generate", cert);
                //System.out.println(dialog?"true":"false");
            } catch (Exception e1) {
                System.out.println( e1.getLocalizedMessage());
            }

        }
    }
}
