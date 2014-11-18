import com.teacode.swing.CommonRB;
import com.teacode.swing.component.FieldPanel;
import com.teacode.swing.dialog.CloseButtonDialog;
import database.Certificate;
import database.Company;
import database.Employer;
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
    protected ArrayList<Employer> users = new ArrayList<Employer>();
    //protected FieldPanel panel = new FieldPanel();


    public UsersViewDialog(Frame parent, String title, FieldPanel panel) {
        super(parent, title, panel);
        System.out.println("constructor");
        final CloseButtonDialog dialog = this;
        setControls(panel);
        dialog.getContentPane().add(panel);
        dialog.setSize(size);
        dialog.pack();
    }

    public void setUsers(Long company_id) {
        try {
            users = Employer.loadByCompany(company_id);
        } catch (Exception e) {
            System.out.println("Load users error: " + e.getLocalizedMessage());
        }
    }

    protected void setControls(FieldPanel panel) {
        final JLabel label = new JLabel();
        panel.addField("Сотрудники компании", "label", label, true);
        for (User user: users) {
            //final JLabel label = new JLabel();
            panel.addField(user.getUsername(), "Фамилия, Имя, Отчество сотрудника", label, true);
            JButton button = new JButton();
            ArrayList<Certificate> certificates  = new ArrayList<Certificate>();
            if (certificates.size() > 0)   {
                final JLabel cert_label = new JLabel();
                panel.addField("Сертификаты", "label", cert_label, true);
                for (Certificate cert: certificates){
                    button.setText("Edit");
                    button.addActionListener(new certAction(cert));
                }

            } else {
                button.setText("New");
                button.addActionListener(new certAction());
            }
            panel.addField("Сертификаты", "Посмотреть сертификат сотрудника", button, true);

        }

        panel.addGlue();
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
            dispose();
            try {
                final CertificateDialog dialog = new CertificateDialog(frame, "Save&Generate", cert);
                //System.out.println(dialog?"true":"false");
            } catch (Exception e1) {
                System.out.println( e1.getLocalizedMessage());
            }

        }
    }

}
