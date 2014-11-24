import com.teacode.swing.component.FieldPanel;

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

import com.teacode.swing.component.FieldPanel;

import database.*;

public class UserPanel extends FieldPanel {
    protected Logger logger = Logger.getLogger("Certificate panel");

    // Поля ораганизациии
    protected String[] names = {"name", "department", "user", "email", "city", "region", "country"};
    protected String[] labels = {"Название", "Отделение", "Имя пользователя", "Email", "Город",
            "Регион", "Страна"};
    protected ArrayList<JTextField> controls = new ArrayList<JTextField>();

    public UserPanel (User user) {
        System.out.println("Users view panel");
        //FieldPanel panel = new FieldPanel();
        if (user instanceof Company) {
            this.setControls(user);
        } else {
            this.setControls();
        }
        //panel.addGlue();
        this.addGlue();
    }

    public void setControls() {
        //TODO throw info dialog
        final JLabel label = new JLabel();
        String companyLabel = "Новая организация";
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
    }


    protected void setControls(User user) {
        System.out.println("set controls with user object");
        String companyLabel = "Организация ООО \""+ user.getUsername() + "\"";
        this.addField(companyLabel, "label", new JPanel(), true);

        JTextField username = new JTextField(user.getUsername());
        username.setEditable(false);
        this.addField("Имя пользователя", "Имя пользователя/название компании", username, true);

        JTextField email = new JTextField(user.getEmail());
        email.setEditable(false);
        this.addField("Почта пользователя", "Почтовый адрес пользователя", email, true);

        JTextField status = new JTextField(user.getStatus()?"Active":"Not");
        status.setEditable(false);
        this.addField("Статус пользователя", "Статус пользователя", status, true);
    }
}