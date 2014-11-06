import javax.swing.*;
import java.awt.*;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Time;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import com.teacode.swing.dialog
        .OkCancelDialog;
import com.teacode.swing.component.FieldPanel;
import com.teacode.swing.exception.WWRuntimeException;

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

    public CertificateDialog(Frame parent, String title) throws Exception{
        super(parent, title, title);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        succeeded = false;
        final OkCancelDialog dialog = this;
        final FieldPanel panel = new FieldPanel();

        System.out.println("constructor");
        this.addWindowListener(new WindowAdapter()
        {
            public void windowClosed(WindowEvent we)
            {
                if (isOkPressed())
                {
                        dialog.pressOK();
                        succeeded = true;
                        String name = tfName.getText();
                        Security certificate = new Security(name);
                        dispose();
                } else
                {
                    dispose();
                }
            }
        });

        createControls(panel);
        //getContentPane().add(panel);
        dialog.setMainPanel(panel);
        dialog.setSize(size);
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
                Security certificate = new Security(name);
            }
        });
        CertificateDialog dialog = new CertificateDialog(frame, title)
        {
            public boolean hasChanged()
            {
                //TODO haschanged nethod
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
        panel.addField("File Name", "Имя файла", tfName, false);
        panel.addField("Организация", "Оргранизация", tfOrganization,false);
        panel.addField("Отделение", "Подразделение организации", tfDepartment, false);
        panel.addField("Имя", "Имя владельца сертификата", tfUsername, false);
        panel.addField("Email", "Почта владельца ящика", tfEmail, false);
        panel.addField("City","Город или другой населенный пункт", tfLocalty, false);


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
