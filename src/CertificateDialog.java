import javax.swing.*;
import java.awt.*;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import com.teacode.swing.dialog.OkCancelDialog;
import com.teacode.swing.component.FieldPanel;
import com.teacode.swing.exception.WWRuntimeException;

public abstract class CertificateDialog extends OkCancelDialog {

    private Dimension size = new Dimension();
    protected CertificateDialog(Frame parent, String title) {
        super(parent, title, title);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        final OkCancelDialog dialog = this;
        addWindowListener(new WindowAdapter()
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
        });
    }

    public abstract boolean hasChanged();

    public static boolean showCertificateDialog(Frame frame, String title) throws Exception
    {
        final JPanel panel = new JPanel();
        CertificateDialog dialog = new CertificateDialog (frame, title);
        {
            public boolean hasChanged()
            {
                return panel.hasChanged();
            }
        };
        dialog.setMainPanel(panel);
        dialog.setSize(size);
        dialog.setVisible(true);
        if (dialog.isOkPressed())
        {
            panel.save();
            try
            {
                dialog.saveSize(prop);
            }
            catch (Exception ex)
            {
                //ex.printStackTrace();
            }
        }
        //logger.log(Level.FINE, String.valueOf(dialog.isOkPressed()));
        return dialog.isOkPressed();
    }
}
