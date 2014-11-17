/**
 * Created by new_name on 10.11.2014.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.Frame;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.teacode.swing.SmartTextArea;
import com.teacode.swing.component.FieldPanel;

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
        setControls(this);
    }

    public MainPanel (Frame frame, User company) {
        this.frame = frame;
        System.out.println("Main application panel");
        //set icon
        ImageIcon img = new ImageIcon("icon.jpg");
        frame.setIconImage(img.getImage());
        setControls(this, company);
    }

    protected void setControls(FieldPanel panel) {
        final JLabel label = new JLabel();
        String companyLabel = "Организация ООО\"Название\"";
        panel.addField(companyLabel, "label", label, true);

        for (int i=0; i < names.length; i++) {
            JTextField field = new JTextField(labels[i]);
            field.setName(names[i]);
            field.setColumns(20);
            field.setEditable(false);
            controls.add(field);
            panel.addField(labels[i], names[i], field, true);
        }

        final JLabel labelCert = new JLabel();
        panel.addField("Сертификаты: ", "label", labelCert, true);

        final JButton getCertificate = new JButton("Получить");
        //getCertificate.setSize(50, 50);
        //panel.add(getCertificate, FlowLayout.LEFT);
        panel.addField("", "", getCertificate, false);

        final JLabel infoLabel = new JLabel();
        String infoLabelText = "Сссылки на Z-Payment";
        panel.addField(infoLabelText, "label", infoLabel, true);

        panel.addGlue();
    }

    protected void setControls(FieldPanel panel, User company) {
        System.out.println("set controls with user object");
        final JLabel label = new JLabel();
        String companyLabel = "Организация ООО\""+ company.getUsername() + "\"";
        panel.addField(companyLabel, "label", label, true);

        JTextField username = new JTextField(company.getUsername());
        username.setEditable(false);
        panel.addField("Имя пользователя", "Имя пользователя/название компании", username, true);

        JTextField email = new JTextField(company.getEmail());
        email.setEditable(false);
        panel.addField("Почта пользователя", "Почтовый адрес пользователя", email, true);

        JTextField status = new JTextField(company.getStatus()?"Active":"Not");
        email.setEditable(false);
        panel.addField("Статус пользователя", "Статус пользователя",status , true);

        final JLabel labelCert = new JLabel();
        panel.addField("Сертификаты: ", "label", labelCert, true);

        final JButton getCertificate = new JButton("Получить");
        //getCertificate.setSize(50, 50);
        //panel.add(getCertificate, FlowLayout.LEFT);
        panel.addField("", "", getCertificate, false);

        final JLabel infoLabel = new JLabel();
        String infoLabelText = "Сссылки на Z-Payment";
        panel.addField(infoLabelText, "label", infoLabel, true);

        panel.addGlue();
    }
}
