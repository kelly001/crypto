import com.teacode.swing.component.FieldPanel;

/**
 * Created by new_name on 10.11.2014.
 */

import javax.swing.*;
import javax.swing.tree.ExpandVetoException;
import java.awt.*;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.teacode.swing.component.FieldPanel;

import com.teacode.swing.dialog.CloseButtonDialog;
import database.*;

public class UserPanel extends FieldPanel {
    protected Logger logger = Logger.getLogger("Certificate panel");
    JFrame frame;

    // Поля организациии
    protected String[] user_names = {"username", "email", "password"};
    protected String[] company_names = {"username", "email", "password", "department", "city", "region", "country"};
    protected String[] labels = {"Название/Имя пользователя", "Email", "Пароль", "Отделение","Город",
            "Регион", "Страна"};
    protected Map<String, JTextField> controls = new HashMap<String, JTextField>();

    public UserPanel (User user, JFrame frame) {
        System.out.println("Users view panel");
        this.frame = frame;
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
        CloseButtonDialog infodialog =
                new CloseButtonDialog(frame, "Приветствие",
                        new JLabel("Ораганизации с таким логином не найдено, создайте новую"));
        infodialog.setVisible(true);
        final JLabel label = new JLabel();
        String companyLabel = "Новая организация";
        this.addField(companyLabel, "label", label, true);
        for (int i=0; i < company_names.length; i++) {
            JTextField field = new JTextField();
            field.setName(company_names[i]);
            field.setColumns(20);
            field.setEditable(true);
            controls.put(company_names[i],field);
            this.addField(labels[i], company_names[i], field, true);
        }
        JButton saveCompanyButton = new JButton("Сохранить");
        saveCompanyButton.addActionListener(new saveCompanyAction());
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

    public class saveUserAction implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                User user = new User();
                user.setUsername(controls.get("username").getText());
                user.setEmail(controls.get("email").getText());
                user.setPassword(controls.get("password").getText());
                try {
                    if (User.newUser(user)){
                        CloseButtonDialog infodialog =
                                new CloseButtonDialog(frame, "Успех",
                                        new JLabel("Новая организация создана!"));
                        infodialog.setVisible(true);
                        String args[] = {controls.get("email").getText()};
                        frame.dispose();
                        MainFrame.main(args);
                    }

                } catch (Exception exc) {
                    System.out.println("saving user exception " + exc.getLocalizedMessage());
                }
            }
    }

    public class saveCompanyAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Company user = new Company();
            user.setUsername(controls.get("username").getText());
            user.setEmail(controls.get("email").getText());
            user.setPassword(controls.get("password").getText());
            user.setCountry(controls.get("country").getText());
            user.setRegion(controls.get("region").getText());
            user.setCity(controls.get("city").getText());
            user.setDepartment(controls.get("department").getText());
            try {
                if (Company.newUser(user)){
                    CloseButtonDialog infodialog =
                            new CloseButtonDialog(frame, "Успех",
                                    new JLabel("Новая организация создана!"));

                    String args[] = {controls.get("email").getText()};
                    frame.dispose();
                    MainFrame.main(args);
                }

            } catch (Exception exc) {
                System.out.println("saving user exception " + exc.getLocalizedMessage());
            }
        }
    }
}