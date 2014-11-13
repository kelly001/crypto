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

    public UserPanel (Frame frame) {
        this.frame = frame;
        System.out.println("Users view panel");
        try {
            users = User.loadUsers();
        } catch (Exception e) {
            System.out.println("Load users error: " + e.getLocalizedMessage());
        }
        setControls(this);
    }

    protected void setControls(FieldPanel panel) {
        final JLabel label = new JLabel();
        String companyLabel = "Сотрудники компании";
        panel.addField(companyLabel, "label", label, true);

        for (User user: users) {
            //final JLabel label = new JLabel();
            panel.addField(user.getUsername(), "Фамилия, Имя, Отчество сотрудника", label, true);
            JButton button;
            if (user.getCertificates().size() > 0)   {
                button = new JButton("посмотреть");
                button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        //TODO start new dialog with certificates, load user cert
                    }
                });
            } else {
                button = new JButton("создать");
                button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        //TODO start new dialog with certificates, load user cert
                    }
                });
            }
            panel.addField("Сертификаты", "Посмотреть сертификат сотрудника", button, true);

        }

        panel.addGlue();
    }
}