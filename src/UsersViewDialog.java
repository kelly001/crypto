import com.teacode.swing.CommonRB;
import com.teacode.swing.component.FieldPanel;
import com.teacode.swing.dialog.CloseButtonDialog;
import database.Certificate;
import database.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by new_name on 11.11.2014.
 */
public class UsersViewDialog extends CloseButtonDialog {

    static Logger logger = Logger.getLogger("CertificateDialog log");
    public static Dimension size = new Dimension(500,500);
    protected Frame frame;
    protected ArrayList<User> users = new ArrayList<User>();
    //protected FieldPanel panel = new FieldPanel();


    public UsersViewDialog(Frame parent, String title, FieldPanel panel) {
        super(parent, title, panel);
        System.out.println("constructor");
        final CloseButtonDialog dialog = this;


        try {
            users = User.loadUsers();
        } catch (Exception e) {
            System.out.println("Load users error: " + e.getLocalizedMessage());
        }

        setControls(panel);
        dialog.getContentPane().add(panel);
        dialog.setSize(size);
        dialog.pack();
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
                        dispose();
                        //TODO start new dialog with certificates, load user cert
                    }
                });
            } else {
                button = new JButton("создать");
                button.addActionListener(new certActionListener());
            }
            panel.addField("Сертификаты", "Посмотреть сертификат сотрудника", button, true);

        }

        panel.addGlue();
    }

    public class certActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e) {
            dispose();
            //new certificate
            Certificate cert = new Certificate();
            try {
                final CertificateDialog dialog = new CertificateDialog(frame, "Save&Generate", cert);
                //System.out.println(dialog?"true":"false");
            } catch (Exception e1) {
                System.out.println( e1.getLocalizedMessage());
            }

        }
    }

}
