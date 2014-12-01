import javax.swing.*;
import java.awt.*;
import java.awt.Frame;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.security.cert.X509Certificate;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import com.teacode.swing.dialog
        .OkCancelDialog;
import com.teacode.swing.component.FieldPanel;
import com.teacode.swing.exception.WWRuntimeException;
import database.Certificate;
import database.Company;
import database.User;

public class CertificateDialog extends OkCancelDialog {

    static Logger logger = Logger.getLogger("CertificateDialog log");
    public static Dimension size = new Dimension(500,500);
    public boolean succeeded;
    private Frame frame;
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
    private static String title = "Окно созднаия и редактирования сертификатов";
    private static String okTitle = "Сохранить";

    public CertificateDialog(Frame parent, final Certificate certificate, final User user) throws Exception{
        super(parent, title, okTitle);
        this.frame = parent;

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
                    Security security = new Security();
                    if (user instanceof Company) {
                        X509Certificate rootcert = security.generateRootCertificate();
                        panel.saveRoot(user.getId().toString());
                    } else {
                        //todo
                        try {
                            Certificate.loadByUser(user.getId());
                        } catch (Exception exc) {
                            System.out.print("Load root certificate by user error " + exc.getLocalizedMessage());
                        }
                        Map<String, String> values = new HashMap<String, String>();
                        values.put("username", user.getUsername());
                        values.put("organization", certificate.getOrganization());
                        security.generateUserCertificate(security.readPrivateKey(certificate.getFilename()), values);
                        //security.generateUserCertificate(rootcert);
                        panel.save(user.getId().toString());
                    }
                    dialog.dispose();
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

    /*public class windowListener extends WindowAdapter{
        public void windowClosed(WindowEvent we){
            if (isOkPressed())
            {
                this.pressOK();
                Security security = new Security();
                X509Certificate rootcert = security.generateRootCertificate();
                if (rootcert!=null) {
                    this.saveRoot(String.valueOf(user.getId()));
                    if (security.generateUserCertificate(rootcert))succeeded = true;
                    this.save("1");
                } else succeeded = false;
                //dispose();
                dialog.setVisible(false);
            } else
            {
                dispose();
            }
        }
    }*/

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
        CertificateDialog dialog = new CertificateDialog(frame, new Certificate(),new User())
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

    public boolean isSucceeded() {
        return succeeded;
    }
}
