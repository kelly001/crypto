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
    protected Frame frame;
    //protected ArrayList<String> users = new ArrayList<String>();
    protected ArrayList<User> users = new ArrayList<User>();
    protected void createusers(ArrayList<User> list) {
        try {
            list = User.loadUsers();
        } catch (Exception e) {
            e.getLocalizedMessage();
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
        panel.addField(companyLabel, "label", label, true);

        for (User user: users) {
            //final JLabel label = new JLabel();
            panel.addField(user.getUsername(), "Фамилия, Имя, Отчество сотрудника", label, false);
            if (user.getCertificates().size() > 0)   {
                JButton button = new JButton("Сертификат");
                button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        //TODO start new dialog with certificates, load user cert
                    }
                });
                panel.addField("Сертификат", "Посмтреть сертификат сотрудника", button, true);
            }

        }

        panel.addGlue();
    }
}