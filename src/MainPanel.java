/**
 * Created by new_name on 10.11.2014.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.teacode.swing.SmartTextArea;
import com.teacode.swing.component.FieldPanel;

import database.Certificate;
import database.Company;
import database.User;

public class MainPanel extends FieldPanel {

    protected Logger logger = Logger.getLogger("Certificate panel");
    protected Frame frame;

    // Поля ораганизациии
    protected String[] names = {"name", "department", "user", "email", "city", "region", "country"};
    protected String[] labels = {"Название", "Отделение", "Имя пользователя", "Email", "Город",
            "Регион", "Страна"};
    protected ArrayList<JTextField> controls = new ArrayList<JTextField>();

    public MainPanel (Frame frame) {
        this.frame = frame;
        System.out.println("Main application panel");
        //set icon
        ImageIcon img = new ImageIcon("icon.jpg");
        frame.setIconImage(img.getImage());
    }

    public void setControls() {
        //TODO throw info dialog
        final JLabel label = new JLabel();
        String companyLabel = "Новая организация/пользователь";
        this.addField(companyLabel, "label", label, true);

        for (int i=0; i < names.length; i++) {
            JTextField field = new JTextField(labels[i]);
            field.setName(names[i]);
            field.setColumns(20);
            field.setEditable(true);
            controls.add(field);
            this.addField(labels[i], names[i], field, true);
        }
        final JButton saveCompanyButton = new JButton("Сохранить");
        saveCompanyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        this.addField("", "", saveCompanyButton, false);

        final JButton getCertificateButton = new JButton("Получить сертификат");
        getCertificateButton.addActionListener(new certAction());
        this.addField("", "", getCertificateButton, false);

        final JLabel infoLabel = new JLabel();
        String infoLabelText = "Сссылки на Z-Payment";
        this.addField(infoLabelText, "label", infoLabel, true);
        this.addGlue();
    }

    public void setControls(User company) {
        System.out.println("set controls with user object");
        System.out.println(company);
        final JLabel label = new JLabel();
        String companyLabel = "Организация ООО \""+ company.getUsername() + "\"";
        this.addField(companyLabel, "label", label, true);

        JTextField username = new JTextField(company.getUsername());
        username.setEditable(false);
        this.addField("Имя пользователя", "Имя пользователя/название компании", username, true);

        JTextField email = new JTextField(company.getEmail());
        email.setEditable(false);
        this.addField("Почта пользователя", "Почтовый адрес пользователя", email, true);

        JTextField status = new JTextField(company.getStatus()?"Active":"Not");
        status.setEditable(false);
        this.addField("Статус пользователя", "Статус пользователя", status, true);

        this.addField("Сертификаты: ", "label", new JLabel(), true);
        ArrayList<Certificate> certificates  = company.getCertificates();
        if (certificates.size() > 0)   {
            for (Certificate cert: certificates){
                this.addField(cert.getUsername(), "label", new JLabel(), true);
            }

        }
        //getCertificate.setSize(50, 50);
        //panel.add(getCertificate, FlowLayout.LEFT);
        JButton getCertificateButton = new JButton("Получить");
        this.addField("", "", getCertificateButton, false);

        final JLabel infoLabel = new JLabel();
        String infoLabelText = "Сссылки на Z-Payment";
        JLinkButton btn = new JLinkButton("https://z-payment.com/");
        this.addField(infoLabelText, "label", btn, true);

        this.addGlue();
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
