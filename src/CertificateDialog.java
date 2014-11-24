import javax.swing.*;
import java.awt.*;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.security.cert.X509Certificate;
import java.sql.Time;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import com.teacode.swing.dialog
        .OkCancelDialog;
import com.teacode.swing.component.FieldPanel;
import com.teacode.swing.exception.WWRuntimeException;
import database.Certificate;

public class CertificateDialog extends OkCancelDialog {

    static Logger logger = Logger.getLogger("CertificateDialog log");
    public static Dimension size = new Dimension(500,500);
    public boolean succeeded;
    private JTextField tfName;
    private JTextField tfOrganization;
    private JTextField tfDepartment;
    private JTextField tfUsername;
    private JTextField tfEmail;
    private JTextField tfType;
    private JTextField tfComment;
    private JTextField tfLocalty;
    private Time timestamp;
    private JButton btnCert;

    public CertificateDialog(Frame parent, String title, Certificate certificate) throws Exception{

        super(parent, title, title);
        System.out.println("constructor");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        succeeded = false;
        final OkCancelDialog dialog = this;
        final CertificatePanel panel = new CertificatePanel(parent);
        this.addWindowListener(new WindowAdapter()
        {
            public void windowClosed(WindowEvent we)
            {
                if (isOkPressed())
                {
                        dialog.pressOK();
                        //String name = tfName.getText();
                        panel.save();
                    Security security = new Security();
                    X509Certificate rootcert = security.generateRootCertificate();
                    if (rootcert!=null) {
                        if (security.generateUserCertificate(rootcert))succeeded = true;
                    } else succeeded = false;
                        //dispose();
                        dialog.setVisible(false);
                } else
                {
                    dispose();
                }
            }
        });

        //createControls(panel);
        getContentPane().add(panel);
        dialog.setSize(size);
        dialog.pack();
        dialog.setVisible(true);
        logger.log(Level.FINE, String.valueOf(dialog.isOkPressed()));
    }

    //public abstract boolean hasChanged();

    public static boolean showCertificateDialog(Frame frame, String title) throws Exception
    {
        final FieldPanel panel = new FieldPanel();
        final JTextField tfName = new JTextField(20);
        panel.addField("File Name", "Имя файла", tfName, true);

        JButton btnCert = new JButton("Generate");
        panel.addField("Genarete Keys", "Создание ключей, подпись файла", btnCert,false);
        btnCert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = tfName.getText();
                System.out.println(name);
                //Security certificate = new Security(name);

            }
        });
        CertificateDialog dialog = new CertificateDialog(frame, title, new Certificate())
        {
            public boolean hasChanged()
            {
                return tfName.getText().equals("")?false:true;
            }
        };
        dialog.setMainPanel(panel);
        dialog.setSize(size);
        dialog.setVisible(true);
        /*if (dialog.isOkPressed())
        {
            panel.save();
        }*/
        logger.log(Level.FINE, String.valueOf(dialog.isOkPressed()));
        return dialog.isOkPressed();
    }

    //create new certfificate manage dialog
    protected void createControls(FieldPanel panel) throws Exception {
        tfName = new JTextField(20);
        tfOrganization = new JTextField(20);
        tfDepartment =new JTextField(20);
        tfEmail = new JTextField(20);
        tfUsername = new JTextField(20);
        tfLocalty = new JTextField(20);
        panel.addField("File Name", "Имя файла", tfName, true);
        panel.addField("Организация", "Оргранизация", tfOrganization,true);
        panel.addField("Отделение", "Подразделение организации", tfDepartment, true);
        panel.addField("Имя", "Имя владельца сертификата", tfUsername, true);
        panel.addField("Email", "Почта владельца ящика", tfEmail, true);
        panel.addField("City","Город или другой населенный пункт", tfLocalty, true);


        /*btnCert = new JButton("Generate");
        panel.addField("Genarete Keys", "Создание ключей, подпись файла", btnCert,false);
        btnCert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = tfName.getText();
                System.out.println(name);
                Security certificate = new Security(name);
            }
        });*/

        logger.log(Level.FINE, "create Controls");
        panel.addGlue();
    }

    public boolean isSucceeded() {
        return succeeded;
    }
}
