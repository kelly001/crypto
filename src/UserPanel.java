import com.teacode.swing.component.FieldPanel;

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

import com.teacode.swing.component.FieldPanel;

import database.*;

public class UserPanel extends FieldPanel {
    protected Logger logger = Logger.getLogger("Certificate panel");
    protected Frame frame;
    protected ArrayList<String> users = new ArrayList<String>();
    protected ArrayList<User> users1 = new ArrayList<User>();
    //TODO load users from data
    protected void createusers(ArrayList<String> list) {
        for (int i=0; i < 10; i++){
               list.add("Пользователь" + i);
        }
    }

    public UserPanel (Frame frame) {
        this.frame = frame;
        System.out.println("Users view panel");
        createusers(users);
        setControls(this);
    }

    protected void setControls(FieldPanel panel) {
        final JLabel label = new JLabel();
        String companyLabel = "Сотрудники компании";
        panel.addField(companyLabel, "label", label, false);

        for (String user: users) {
            JButton field = new JButton("Сертификат");
            panel.addField(user, "Фамилия, Имя, Отчество сотрудника", field, true);
        }

        panel.addGlue();
    }
}