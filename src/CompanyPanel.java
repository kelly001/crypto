import com.teacode.swing.component.FieldPanel;
import com.teacode.swing.dialog.CloseButtonDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import database.*;

public class CompanyPanel extends FieldPanel {
    Component parent;

    // Поля организациии
    protected String[] names = {"username", "email", "password", "department", "city", "region", "country"};
    protected String[] labels = {"Название/Имя пользователя", "Email", "Пароль", "Отделение","Город",
            "Регион", "Страна"};
    protected Map<String, JTextField> controls = new HashMap<String, JTextField>();
    JButton saveCompanyButton;
    ActionListener action1 = new saveCompanyAction();

    public CompanyPanel(User user, Component parent) {
        System.out.println("Users view panel");
        this.parent = parent;
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
        final JLabel label = new JLabel();
        String companyLabel = "Новая организация";
        this.addField(companyLabel, "label", label, true);
        for (int i=0; i < names.length; i++) {
            JTextField field = new JTextField();
            field.setName(names[i]);
            field.setColumns(20);
            field.setEditable(true);
            controls.put(names[i],field);
            this.addField(labels[i], names[i], field, true);
        }
        saveCompanyButton = new JButton("Сохранить");
        saveCompanyButton.addActionListener(action1);
        this.addField("", "", saveCompanyButton, false);
    }


    protected void setControls(User user) {
        System.out.println("set controls with company object");
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

    public void setValues(Company user) {
        if (user != null) {
            controls.get("username").setText(user.getUsername());
            controls.get("email").setText(user.getEmail());
            controls.get("city").setText(user.getCity());
            controls.get("region").setText(user.getRegion());
            controls.get("department").setText(user.getDepartment());
            controls.get("country").setText(user.getCountry());
            saveCompanyButton.addActionListener(new updateCompanyAction(user));
            saveCompanyButton.removeActionListener(action1);
            this.addGlue();
            this.repaint();

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
                    String args[] = {controls.get("email").getText()};
                    parent.setVisible(false);
                    MainFrame.main(args);
                }
            } catch (Exception exc) {
                System.out.println("saving user exception " + exc.getLocalizedMessage());
            }
        }
    }

    public class updateCompanyAction implements ActionListener {
        private Company user;
        updateCompanyAction(Company user) {
            this.user = user;
        }
        @Override
        public void actionPerformed(ActionEvent e) {

            user.setUsername(controls.get("username").getText());
            user.setEmail(controls.get("email").getText());
            user.setPassword(controls.get("password").getText());
            user.setCountry(controls.get("country").getText());
            user.setRegion(controls.get("region").getText());
            user.setCity(controls.get("city").getText());
            user.setDepartment(controls.get("department").getText());
            try {
                if (Company.updateUser(user)){
                    CloseButtonDialog infodialog =
                            new CloseButtonDialog((JFrame)parent, "Успех",
                                    new JLabel("Новая организация создана!"));
                    parent.setVisible(false);
                    String args[] = {controls.get("email").getText()};
                    MainFrame.main(args);
                }
            } catch (Exception exc) {
                System.out.println("saving user exception " + exc.getLocalizedMessage());
            }
        }
    }
}