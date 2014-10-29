import javax.swing.*;
import java.awt.*;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import com.teacode.swing.dialog.OkCancelDialog;
import com.teacode.swing.component.FieldPanel;
import com.teacode.swing.exception.WWRuntimeException;

public abstract class CertificateDialog extends OkCancelDialog {

    static Logger logger = Logger.getLogger("CertificateDialog log");
    public static Dimension size = new Dimension(240,240);
    public boolean succeeded;
    private JTextField tfName;
    private JButton btnCert;

    protected CertificateDialog(Frame parent, String title) throws Exception{
        super(parent, title, title);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        succeeded = false;
        final OkCancelDialog dialog = this;
        final FieldPanel panel = new FieldPanel();

        createControls(panel);
        dialog.setMainPanel(panel);
        dialog.setSize(size);
        dialog.setVisible(true);
        logger.log(Level.FINE, String.valueOf(dialog.isOkPressed()));
        /*addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent we)
            {
                if (hasChanged())
                {
                    int ret = JOptionPane.showConfirmDialog(dialog, "Есть несохраненные данные. Сохранить их?",
                            "Редактирование объекта", JOptionPane.YES_NO_CANCEL_OPTION);
                    if (ret == JOptionPane.YES_OPTION)
                    {
                        dialog.pressOK();
                        succeeded = true;
                        dispose();
                    } else if (ret == JOptionPane.NO_OPTION)
                    {
                        dialog.dispose();
                    }
                    //return; is unnecessary
                } else
                {
                    dialog.dispose();
                }
            }
        });*/

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

    protected void createControls(FieldPanel panel) throws Exception {
        tfName = new JTextField(20);
        panel.addField("File Name", "Имя файла", tfName, true);

        btnCert = new JButton("Generate");
        panel.addField("Genarete Keys", "Создание ключей, подпись файла", btnCert,false);
        btnCert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = tfName.getText();
                System.out.println(name);
                Security certificate = new Security(name);
            }
        });

        panel.addGlue();
    }

    public boolean isSucceeded() {
        return succeeded;
    }
}
